package com.ttv.face;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class LivenessParam {
    private float fqThreshold;
    private float irThreshold;
    private float rgbThreshold;

    public LivenessParam(float rgbThreshold2, float irThreshold2, float fqThreshold2) {
        this.rgbThreshold = rgbThreshold2;
        this.irThreshold = irThreshold2;
        this.fqThreshold = fqThreshold2;
    }

    public float getRgbThreshold() {
        return this.rgbThreshold;
    }

    public void setRgbThreshold(float rgbThreshold2) {
        this.rgbThreshold = rgbThreshold2;
    }

    public float getIrThreshold() {
        return this.irThreshold;
    }

    public void setIrThreshold(float irThreshold2) {
        this.irThreshold = irThreshold2;
    }

    public float getFqThreshold() {
        return this.fqThreshold;
    }

    public void setFqThreshold(float fqThreshold2) {
        this.fqThreshold = fqThreshold2;
    }
}
