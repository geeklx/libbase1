//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import java.nio.ByteBuffer;

public class ImageUtils {
    public ImageUtils() {
    }

    public static byte[] bitmapToBgr24(Bitmap image) {
        if (image == null) {
            return null;
        } else {
            if (image.getConfig() != Config.ARGB_8888) {
                image = image.copy(Config.ARGB_8888, true);
            }

            ByteBuffer buffer = ByteBuffer.allocate(image.getByteCount());
            image.copyPixelsToBuffer(buffer);
            byte[] rgbaData = buffer.array();
            int pixelCount = rgbaData.length / 4;
            byte[] bgrData = new byte[pixelCount * 3];
            int rgbaIndex = 0;
            int bgrIndex = 0;

            for(int i = 0; i < pixelCount; ++i) {
                bgrData[bgrIndex] = rgbaData[rgbaIndex + 2];
                bgrData[bgrIndex + 1] = rgbaData[rgbaIndex + 1];
                bgrData[bgrIndex + 2] = rgbaData[rgbaIndex];
                bgrIndex += 3;
                rgbaIndex += 4;
            }

            return bgrData;
        }
    }

    public static Bitmap bgrToBitmap(byte[] bgr, int width, int height) {
        int[] colors = convertBgrToColor(bgr);
        return colors == null ? null : Bitmap.createBitmap(colors, 0, width, width, height, Config.ARGB_8888);
    }

    private static int[] convertBgrToColor(byte[] bgr) {
        int[] color = null;
        int size = bgr.length;
        if (size != 0 && size % 3 == 0) {
            color = new int[size / 3];
            int index = 0;

            for(int i = 0; i < color.length; ++i) {
                color[i] = (bgr[index + 2] & 255) << 16 | (bgr[index + 1] & 255) << 8 | bgr[index] & 255 | -16777216;
                index += 3;
            }
        }

        return color;
    }

    public static Bitmap alignBitmapForBgr24(Bitmap bitmap) {
        if (bitmap != null && bitmap.getWidth() >= 4) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            return (width & 3) != 0 ? cropImage(bitmap, new Rect(0, 0, width & -4, height)) : bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap cropImage(Bitmap bitmap, Rect rect) {
        return bitmap != null && rect != null && !rect.isEmpty() && bitmap.getWidth() >= rect.right && bitmap.getHeight() >= rect.bottom ? Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), (Matrix)null, false) : null;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float rotateDegree) {
        if (bitmap == null) {
            return null;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateDegree);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        }
    }

    public static byte[] cropNv21(byte[] nv21, int width, int height, Rect rect) {
        if (nv21 != null && nv21.length != 0 && width * height * 3 / 2 == nv21.length && rect != null) {
            if (rect.left >= 0 && rect.top >= 0 && rect.right <= width && rect.bottom <= height) {
                if (rect.right > rect.left && rect.bottom > rect.top) {
                    if ((rect.right - rect.left & 1) != 1 && (rect.bottom - rect.top & 1) != 1) {
                        int cropImageWidth = rect.right - rect.left;
                        int cropImageHeight = rect.bottom - rect.top;
                        byte[] cropNv21 = new byte[cropImageWidth * cropImageHeight * 3 / 2];
                        int originalYLineStart = rect.top * width;
                        int targetYIndex = 0;
                        int originalUVLineStart = width * height + rect.top * width / 2;
                        int targetUVIndex = cropImageWidth * cropImageHeight;

                        for(int i = rect.top; i < rect.bottom; ++i) {
                            System.arraycopy(nv21, rect.left + originalYLineStart, cropNv21, targetYIndex, cropImageWidth);
                            originalYLineStart += width;
                            targetYIndex += cropImageWidth;
                            if ((i & 1) == 0) {
                                System.arraycopy(nv21, rect.left + originalUVLineStart, cropNv21, targetUVIndex, cropImageWidth);
                                originalUVLineStart += width;
                                targetUVIndex += cropImageWidth;
                            }
                        }

                        return cropNv21;
                    } else {
                        throw new IllegalArgumentException("nv21 width and height must be even!");
                    }
                } else {
                    throw new IllegalArgumentException("invalid rect!");
                }
            } else {
                throw new IllegalArgumentException("rect out of bounds!");
            }
        } else {
            throw new IllegalArgumentException("invalid image params!");
        }
    }

    public static byte[] cropBgr24(byte[] bgr24, int width, int height, Rect rect) {
        if (bgr24 != null && bgr24.length != 0 && width * height * 3 == bgr24.length && rect != null) {
            if (rect.left >= 0 && rect.top >= 0 && rect.right <= width && rect.bottom <= height) {
                if (rect.right > rect.left && rect.bottom > rect.top) {
                    int cropImageWidth = rect.right - rect.left;
                    byte[] cropBgr24 = new byte[cropImageWidth * (rect.bottom - rect.top) * 3];
                    int originalLineStart = rect.top * width * 3;
                    int targetIndex = 0;

                    for(int i = rect.top; i < rect.bottom; ++i) {
                        System.arraycopy(bgr24, rect.left * 3 + originalLineStart, cropBgr24, targetIndex, cropImageWidth * 3);
                        originalLineStart += width * 3;
                        targetIndex += cropImageWidth * 3;
                    }

                    return cropBgr24;
                } else {
                    throw new IllegalArgumentException("invalid rect!");
                }
            } else {
                throw new IllegalArgumentException("rect out of bounds!");
            }
        } else {
            throw new IllegalArgumentException("invalid image params!");
        }
    }
}
