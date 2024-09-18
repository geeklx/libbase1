package com.ttv.face;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class SearchResult {
    FaceFeatureInfo faceFeatureInfo;
    float maxSimilar;

    public SearchResult(float maxSimilar2, FaceFeatureInfo faceFeatureInfo2) {
        this.maxSimilar = maxSimilar2;
        this.faceFeatureInfo = faceFeatureInfo2;
    }

    public float getMaxSimilar() {
        return this.maxSimilar;
    }

    public FaceFeatureInfo getFaceFeatureInfo() {
        return this.faceFeatureInfo;
    }
}
