package com.geek.libfacedetect.util;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;

import java.nio.ByteBuffer;

public class ImageUtil {
    private static final String TAG = "ImageUtil";
    public static final int YUV420P = 0;
    public static final int YUV420SP = 1;
    public static final int NV21 = 2;

    /***
     * 此方法内注释以640*480为例
     * 未考虑CropRect的
     */
    public static byte[] getBytesFromImageAsType(Image image, int type) {
        try {
            //获取源数据，如果是YUV格式的数据planes.length = 3
            //plane[i]里面的实际数据可能存在byte[].length <= capacity (缓冲区总大小)
            final Image.Plane[] planes = image.getPlanes();

            //数据有效宽度，一般的，图片width <= rowStride，这也是导致byte[].length <= capacity的原因
            // 所以我们只取width部分
            int width = image.getWidth();
            int height = image.getHeight();

            //此处用来装填最终的YUV数据，需要1.5倍的图片大小，因为Y U V 比例为 4:1:1
            byte[] yuvBytes = new byte[width * height * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8];
            //目标数组的装填到的位置
            int dstIndex = 0;

            //临时存储uv数据的
            byte uBytes[] = new byte[width * height / 4];
            byte vBytes[] = new byte[width * height / 4];
            int uIndex = 0;
            int vIndex = 0;

            int pixelsStride, rowStride;
            for (int i = 0; i < planes.length; i++) {
                pixelsStride = planes[i].getPixelStride();
                rowStride = planes[i].getRowStride();

                ByteBuffer buffer = planes[i].getBuffer();

                //如果pixelsStride==2，一般的Y的buffer长度=640*480，UV的长度=640*480/2-1
                //源数据的索引，y的数据是byte中连续的，u的数据是v向左移以为生成的，两者都是偶数位为有效数据
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);

                int srcIndex = 0;
                if (i == 0) {
                    //直接取出来所有Y的有效区域，也可以存储成一个临时的bytes，到下一步再copy
                    for (int j = 0; j < height; j++) {
                        System.arraycopy(bytes, srcIndex, yuvBytes, dstIndex, width);
                        srcIndex += rowStride;
                        dstIndex += width;
                    }
                } else if (i == 1) {
                    //根据pixelsStride取相应的数据
                    for (int j = 0; j < height / 2; j++) {
                        for (int k = 0; k < width / 2; k++) {
                            uBytes[uIndex++] = bytes[srcIndex];
                            srcIndex += pixelsStride;
                        }
                        if (pixelsStride == 2) {
                            srcIndex += rowStride - width;
                        } else if (pixelsStride == 1) {
                            srcIndex += rowStride - width / 2;
                        }
                    }
                } else if (i == 2) {
                    //根据pixelsStride取相应的数据
                    for (int j = 0; j < height / 2; j++) {
                        for (int k = 0; k < width / 2; k++) {
                            vBytes[vIndex++] = bytes[srcIndex];
                            srcIndex += pixelsStride;
                        }
                        if (pixelsStride == 2) {
                            srcIndex += rowStride - width;
                        } else if (pixelsStride == 1) {
                            srcIndex += rowStride - width / 2;
                        }
                    }
                }
            }

            //   image.close();

            //根据要求的结果类型进行填充
            switch (type) {
                case YUV420P:
                    System.arraycopy(uBytes, 0, yuvBytes, dstIndex, uBytes.length);
                    System.arraycopy(vBytes, 0, yuvBytes, dstIndex + uBytes.length, vBytes.length);
                    break;
                case YUV420SP:
                    for (int i = 0; i < vBytes.length; i++) {
                        yuvBytes[dstIndex++] = uBytes[i];
                        yuvBytes[dstIndex++] = vBytes[i];
                    }
                    break;
                case NV21:
                    for (int i = 0; i < vBytes.length; i++) {
                        yuvBytes[dstIndex++] = vBytes[i];
                        yuvBytes[dstIndex++] = uBytes[i];
                    }
                    break;
                default:
                    break;
            }
            return yuvBytes;
        } catch (final Exception e) {
//                if (image != null) {
//                    image.close();
//                }
//                Log.i(TAG, e.toString());
        }
        return null;
    }

    public static void NV21ToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        if (nv21 == null || nv12 == null) {
            return;
        }
        int framesize = width * height;
        int i = 0, j = 0;
        System.arraycopy(nv21, 0, nv12, 0, framesize);
        for (i = 0; i < framesize; i++) {
            nv12[i] = nv21[i];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j - 1] = nv21[j + framesize];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j] = nv21[j + framesize - 1];
        }
    }


//    I420: YYYYYYYY UU VV    =>YUV420P
//    YV12: YYYYYYYY VV UU    =>YUV420P
//    NV12: YYYYYYYY UVUV     =>YUV420SP
//    NV21: YYYYYYYY VUVU     =>YUV420SP

    public static void YV420toNV12(final byte[] input, final byte[] output, final int width, final int height) {
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;
        final int tempFrameSize = frameSize * 5 / 4;
        System.arraycopy(input, 0, output, 0, frameSize); // Y
        for (int i = 0; i < qFrameSize; i++) {
            output[frameSize + i * 2] = input[frameSize + i]; // Cb (U)
            output[frameSize + i * 2 + 1] = input[tempFrameSize + i]; // Cr (V)
        }
    }


    /***
     * YUV420 转化成 RGB
     */
    public static int[] decodeYUV420SP(byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        int rgb[] = new int[frameSize];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) {
                    y = 0;
                }
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0) {
                    r = 0;
                } else if (r > 262143) {
                    r = 262143;
                }
                if (g < 0) {
                    g = 0;
                } else if (g > 262143) {
                    g = 262143;
                }
                if (b < 0) {
                    b = 0;
                } else if (b > 262143) {
                    b = 262143;
                }
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }

    /**
     * I420P数据转成NV21数据
     *
     * @param i420bytes I420P源数据
     * @param width     宽
     * @param height    高
     * @return nv21数据
     */
    public static byte[] I420Tonv21(byte[] i420bytes, int width, int height) {
        byte[] nv21bytes = new byte[i420bytes.length];
        int total = width * height; //Y数据的长度
        int nLen = total / 4;  //U、V数据的长度
        System.arraycopy(i420bytes, 0, nv21bytes, 0, total);

        for (int i = 0; i < nLen; i++) {
            byte u = i420bytes[total + i];
            byte v = i420bytes[total + nLen + i];

            nv21bytes[total + i * 2] = v;
            nv21bytes[total + i * 2 + 1] = u;
        }
        return nv21bytes;
    }

    public static Bitmap imageCrop(Bitmap bitmap, Rect rect) {
        if (bitmap == null || rect == null || rect.isEmpty() || bitmap.getWidth() < rect.right || bitmap.getHeight() < rect.bottom) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), null, false);
    }

    /**
     * Bitmap转化为ARGB数据，再转化为NV21数据
     *
     * @param src    传入的Bitmap，格式为{@link Bitmap.Config#ARGB_8888}
     * @param width  NV21图像的宽度
     * @param height NV21图像的高度
     * @return nv21数据
     */
    public static byte[] bitmapToNv21(Bitmap src, int width, int height) {
        if (src != null && src.getWidth() >= width && src.getHeight() >= height) {
            int[] argb = new int[width * height];
            src.getPixels(argb, 0, width, 0, 0, width, height);
            return argbToNv21(argb, width, height);
        } else {
            return null;
        }
    }


    /**
     * ARGB数据转化为NV21数据
     *
     * @param argb   argb数据
     * @param width  宽度
     * @param height 高度
     * @return nv21数据
     */
    private static byte[] argbToNv21(int[] argb, int width, int height) {
        int frameSize = width * height;
        int yIndex = 0;
        int uvIndex = frameSize;
        int index = 0;
        byte[] nv21 = new byte[width * height * 3 / 2];
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                int R = (argb[index] & 0xFF0000) >> 16;
                int G = (argb[index] & 0x00FF00) >> 8;
                int B = argb[index] & 0x0000FF;
                int Y = (66 * R + 129 * G + 25 * B + 128 >> 8) + 16;
                int U = (-38 * R - 74 * G + 112 * B + 128 >> 8) + 128;
                int V = (112 * R - 94 * G - 18 * B + 128 >> 8) + 128;
                nv21[yIndex++] = (byte) (Y < 0 ? 0 : (Y > 255 ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0 && uvIndex < nv21.length - 2) {
                    nv21[uvIndex++] = (byte) (V < 0 ? 0 : (V > 255 ? 255 : V));
                    nv21[uvIndex++] = (byte) (U < 0 ? 0 : (U > 255 ? 255 : U));
                }

                ++index;
            }
        }
        return nv21;
    }


    // 此方法有问题，排列错误
//    public static byte[] I420Tonv21(byte[] data, int width, int height) {
//        byte[] ret = new byte[data.length];
//        int total = width * height;
//
//        ByteBuffer bufferY = ByteBuffer.wrap(ret, 0, total);
//        ByteBuffer bufferV = ByteBuffer.wrap(ret, total, total / 4);
//        ByteBuffer bufferU = ByteBuffer.wrap(ret, total + total / 4, total / 4);
//
//        bufferY.put(data, 0, total);
//        for (int i = 0; i < total / 4; i += 1) {
//            bufferV.put(data[total + i]);
//            bufferU.put(data[i + total + total / 4]);
//        }
//        return ret;
//    }
}