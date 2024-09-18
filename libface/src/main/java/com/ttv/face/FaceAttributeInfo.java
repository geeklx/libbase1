//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

import androidx.annotation.NonNull;

public class FaceAttributeInfo {
    int leftEyeOpen;
    int mouthClose;
    int rightEyeOpen;
    int wearGlasses;

    public FaceAttributeInfo() {
        this.wearGlasses = -1;
        this.leftEyeOpen = -1;
        this.rightEyeOpen = -1;
        this.mouthClose = -1;
    }

    public FaceAttributeInfo(FaceAttributeInfo obj) {
        if (obj == null) {
            this.wearGlasses = -1;
            this.leftEyeOpen = -1;
            this.rightEyeOpen = -1;
            this.mouthClose = -1;
        } else {
            this.wearGlasses = obj.getWearGlasses();
            this.leftEyeOpen = obj.getLeftEyeOpen();
            this.rightEyeOpen = obj.getRightEyeOpen();
            this.mouthClose = obj.getMouthClose();
        }
    }

    public int getWearGlasses() {
        return this.wearGlasses;
    }

    public int getLeftEyeOpen() {
        return this.leftEyeOpen;
    }

    public int getRightEyeOpen() {
        return this.rightEyeOpen;
    }

    public int getMouthClose() {
        return this.mouthClose;
    }

    @Override
    @NonNull
    public FaceAttributeInfo clone() {
        return new FaceAttributeInfo(this);
    }

    @Override
    @NonNull
    public String toString() {
        return "FaceAttributeInfo{wearGlasses=" + this.wearGlasses + ", leftEyeOpen=" + this.leftEyeOpen + ", rightEyeOpen=" + this.rightEyeOpen + ", mouthClose=" + this.mouthClose + '}';
    }
}
