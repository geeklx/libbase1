package com.ttv.face.imageutil;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class TTVImageFormatIdx {
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

