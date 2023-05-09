//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

import androidx.annotation.NonNull;

public class FaceFeature {
    public static final int FEATURE_SIZE = 2056;
    byte[] featureData;

    public FaceFeature(FaceFeature obj) {
        if (obj == null) {
            this.featureData = new byte[2056];
        } else {
            this.featureData = (byte[])obj.getFeatureData().clone();
        }

    }

    public FaceFeature() {
        this.featureData = new byte[2056];
    }

    public FaceFeature(byte[] data) {
        this.featureData = data;
    }

    public byte[] getFeatureData() {
        return this.featureData;
    }

    public void setFeatureData(byte[] data) {
        this.featureData = data;
    }

    @Override
    @NonNull
    public FaceFeature clone() {
        return new FaceFeature(this);
    }
}
