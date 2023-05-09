package com.ttv.face;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class FaceAttributeParam {
    private float eyeopenThreshold;
    private float mouthcloseThreshold;
    private float wearGlassesThreshold;

    public FaceAttributeParam(float eyeopenThreshold2, float mouthcloseThreshold2, float wearGlassesThreshold2) {
        this.eyeopenThreshold = eyeopenThreshold2;
        this.mouthcloseThreshold = mouthcloseThreshold2;
        this.wearGlassesThreshold = wearGlassesThreshold2;
    }

    public float getEyeopenThreshold() {
        return this.eyeopenThreshold;
    }

    public void setEyeopenThreshold(float eyeopenThreshold2) {
        this.eyeopenThreshold = eyeopenThreshold2;
    }

    public float getMouthcloseThreshold() {
        return this.mouthcloseThreshold;
    }

    public void setMouthcloseThreshold(float mouthcloseThreshold2) {
        this.mouthcloseThreshold = mouthcloseThreshold2;
    }

    public float getWearGlassesThreshold() {
        return this.wearGlassesThreshold;
    }

    public void setWearGlassesThreshold(float wearGlassesThreshold2) {
        this.wearGlassesThreshold = wearGlassesThreshold2;
    }
}
