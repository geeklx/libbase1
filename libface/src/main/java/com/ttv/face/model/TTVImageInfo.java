//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face.model;

public class TTVImageInfo {
    private int height;
    private int imageFormat;
    private byte[][] planes;
    private int[] strides;
    private int width;

    public TTVImageInfo(int width2, int height2, int imageFormat2) {
        this.width = width2;
        this.height = height2;
        this.imageFormat = imageFormat2;
    }

    public TTVImageInfo(int width2, int height2, int imageFormat2, byte[][] planes2, int[] strides2) {
        this.width = width2;
        this.height = height2;
        this.imageFormat = imageFormat2;
        this.planes = planes2;
        this.strides = strides2;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public int getImageFormat() {
        return this.imageFormat;
    }

    public void setImageFormat(int imageFormat2) {
        this.imageFormat = imageFormat2;
    }

    public byte[][] getPlanes() {
        return this.planes;
    }

    public void setPlanes(byte[][] planes2) {
        this.planes = planes2;
    }

    public int[] getStrides() {
        return this.strides;
    }

    public void setStrides(int[] strides2) {
        this.strides = strides2;
    }
}
