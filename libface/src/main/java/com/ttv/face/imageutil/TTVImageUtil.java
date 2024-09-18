package com.ttv.face.imageutil;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;

import com.ttv.face.imageutil.TTVImageFormat;
import com.ttv.face.imageutil.TTVMirrorOrient;
import com.ttv.face.imageutil.TTVRotateDegree;

public final class TTVImageUtil {
    public TTVImageUtil() {
    }

    private static native void nativeAlignBitmap(Bitmap var0, Bitmap var1);

    private static native int nativeBitmapToImageData(Bitmap var0, byte[] var1, int var2);

    private static native int nativeCropImage(byte[] var0, byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    private static native int nativeImageDataToBitmap(byte[] var0, Bitmap var1, int var2);

    private static native int nativeImageFormatTransform(byte[] var0, byte[] var1, int var2, int var3, int var4, int var5);

    private static native int nativeMirrorImage(byte[] var0, byte[] var1, int var2, int var3, boolean var4, int var5);

    private static native int nativeRotateImage(byte[] var0, byte[] var1, int var2, int var3, int var4, int var5);

    public static Bitmap getAlignedBitmap(Bitmap bitmap, boolean crop) {
        if (bitmap == null) {
            return null;
        } else if (bitmap.getConfig() != Config.ARGB_8888 && bitmap.getConfig() != Config.RGB_565) {
            return null;
        } else if ((bitmap.getWidth() & 3) == 0 && (bitmap.getHeight() & 3) == 0) {
            return bitmap;
        } else {
            Bitmap newBmp = Bitmap.createBitmap(crop ? bitmap.getWidth() & -4 : bitmap.getWidth() + (4 - (bitmap.getWidth() & 3)) % 4, crop ? bitmap.getHeight() & -4 : bitmap.getHeight() + (4 - (bitmap.getHeight() & 3)) % 4, bitmap.getConfig());
            nativeAlignBitmap(bitmap, newBmp);
            return newBmp;
        }
    }

    public static byte[] createImageData(int width, int height, TTVImageFormat ttvImageFormat) {
        return new byte[getDataLengthBySizeAndFormat(width, height, ttvImageFormat)];
    }

    private static int getDataLengthBySizeAndFormat(int width, int height, TTVImageFormat ttvImageFormat) {
        if (ttvImageFormat == null) {
            throw new NullPointerException("ttvImageFormat not specified");
        } else if ((width & 3) == 0 && (height & 3) == 0) {
            switch(ttvImageFormat) {
                case NV21:
                case NV12:
                case I420:
                case YV12:
                    return width * height * 3 / 2;
                case BGR24:
                    return width * height * 3;
                case YUYV:
                    return width * height * 2;
                case GRAY:
                    return width * height;
                default:
                    throw new IllegalArgumentException("invalid ttvImageFormat '" + ttvImageFormat + "'");
            }
        } else {
            throw new IllegalArgumentException("invalid width or height, width and height must be a multiple of 4");
        }
    }

    public static int bitmapToImageData(Bitmap bitmap, byte[] data, TTVImageFormat ttvImageFormat) {
        if (ttvImageFormat != null && data != null && bitmap != null) {
            return data.length != getDataLengthBySizeAndFormat(bitmap.getWidth(), bitmap.getHeight(), ttvImageFormat) ? 1 : nativeBitmapToImageData(bitmap, data, ttvImageFormat.format);
        } else {
            return 4;
        }
    }

    public static int imageDataToBitmap(byte[] data, Bitmap bitmap, TTVImageFormat ttvImageFormat) {
        if (ttvImageFormat != null && data != null && bitmap != null) {
            return data.length != getDataLengthBySizeAndFormat(bitmap.getWidth(), bitmap.getHeight(), ttvImageFormat) ? 1 : nativeImageDataToBitmap(data, bitmap, ttvImageFormat.format);
        } else {
            return 4;
        }
    }

    public static int cropImage(byte[] originImageData, byte[] cropImageData, int originWidth, int originHeight, Rect rect, TTVImageFormat ttvImageFormat) {
        return rect != null && ttvImageFormat != null ? nativeCropImage(originImageData, cropImageData, originWidth, originHeight, rect.left, rect.top, rect.right, rect.bottom, ttvImageFormat.format) : 4;
    }

    public static int cropImage(byte[] originImageData, byte[] cropImageData, int originWidth, int originHeight, int left, int top, int right, int bottom, TTVImageFormat ttvImageFormat) {
        return ttvImageFormat == null ? 4 : nativeCropImage(originImageData, cropImageData, originWidth, originHeight, left, top, right, bottom, ttvImageFormat.format);
    }

    public static int cropImage(byte[] originImageData, byte[] cropImageData, int originWidth, int originHeight, Point leftTop, Point rightBottom, TTVImageFormat ttvImageFormat) {
        return ttvImageFormat == null ? 4 : nativeCropImage(originImageData, cropImageData, originWidth, originHeight, leftTop.x, leftTop.y, rightBottom.x, rightBottom.y, ttvImageFormat.format);
    }

    public static int rotateImage(byte[] originImageData, byte[] rotateImageData, int originWidth, int originHeight, TTVRotateDegree degree, TTVImageFormat ttvImageFormat) {
        return originImageData != null && rotateImageData != null && ttvImageFormat != null && degree != null ? nativeRotateImage(originImageData, rotateImageData, originWidth, originHeight, degree.degree, ttvImageFormat.format) : 4;
    }

    public static int mirrorImage(byte[] originImageData, byte[] mirrorImageData, int width, int height, TTVMirrorOrient ttvMirrorOrient, TTVImageFormat ttvImageFormat) {
        return ttvMirrorOrient != null && ttvImageFormat != null ? nativeMirrorImage(originImageData, mirrorImageData, width, height, ttvMirrorOrient.horizontal, ttvImageFormat.format) : 4;
    }

    public static int transformImage(byte[] originImageData, byte[] targetImageData, int width, int height, TTVImageFormat originFormat, TTVImageFormat targetFormat) {
        if (originFormat != null && targetFormat != null && originImageData != null && targetImageData != null) {
            if (originFormat != targetFormat) {
                return nativeImageFormatTransform(originImageData, targetImageData, width, height, originFormat.format, targetFormat.format);
            } else if (originImageData.length == targetImageData.length && originImageData.length == getDataLengthBySizeAndFormat(width, height, originFormat)) {
                if (originImageData == targetImageData) {
                    return 5;
                } else {
                    System.arraycopy(originImageData, 0, targetImageData, 0, originImageData.length);
                    return 0;
                }
            } else {
                return 1;
            }
        } else {
            return 4;
        }
    }

    static {
        System.loadLibrary("ttvimage");
    }

    public static class TTVImageFormatIdx {
        static final int[] imageFormats = new int[TTVImageFormat.values().length];

        public TTVImageFormatIdx() {
        }

        static {
            try {
                imageFormats[TTVImageFormat.NV21.ordinal()] = 1;
            } catch (NoSuchFieldError var7) {
            }

            try {
                imageFormats[TTVImageFormat.NV12.ordinal()] = 2;
            } catch (NoSuchFieldError var6) {
            }

            try {
                imageFormats[TTVImageFormat.I420.ordinal()] = 3;
            } catch (NoSuchFieldError var5) {
            }

            try {
                imageFormats[TTVImageFormat.YV12.ordinal()] = 4;
            } catch (NoSuchFieldError var4) {
            }

            try {
                imageFormats[TTVImageFormat.BGR24.ordinal()] = 5;
            } catch (NoSuchFieldError var3) {
            }

            try {
                imageFormats[TTVImageFormat.YUYV.ordinal()] = 6;
            } catch (NoSuchFieldError var2) {
            }

            try {
                imageFormats[TTVImageFormat.GRAY.ordinal()] = 7;
            } catch (NoSuchFieldError var1) {
            }

        }
    }
}
