package com.ttv.face;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class FaceFeatureInfo {
    String faceTag;
    byte[] featureData;
    int searchId;

    public FaceFeatureInfo() {
    }

    public FaceFeatureInfo(int searchId2, byte[] featureData2) {
        this.searchId = searchId2;
        this.featureData = featureData2;
    }

    public FaceFeatureInfo(int searchId2, byte[] featureData2, String faceTag2) {
        this.searchId = searchId2;
        this.featureData = featureData2;
        this.faceTag = faceTag2;
    }

    public int getSearchId() {
        return this.searchId;
    }

    public String getFaceTag() {
        return this.faceTag;
    }

    public byte[] getFeatureData() {
        return this.featureData;
    }
}
