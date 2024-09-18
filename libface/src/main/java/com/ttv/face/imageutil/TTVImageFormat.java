package com.ttv.face.imageutil;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


public enum TTVImageFormat {
    BGR24(513),
    YUYV(1281),
    I420(1537),
    YV12(1541),
    GRAY(1793),
    NV12(2049),
    NV21(2050);

    int format;

    private TTVImageFormat(int format2) {
        this.format = format2;
    }

    public static TTVImageFormat valueOf(int formatValue) throws IllegalArgumentException {
        TTVImageFormat[] values = values();
        TTVImageFormat[] var2 = values;
        int var3 = values.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            TTVImageFormat value = var2[var4];
            if (value.format == formatValue) {
                return value;
            }
        }

        throw new IllegalArgumentException("formatValue '" + formatValue + "' does not match any format");
    }
}
