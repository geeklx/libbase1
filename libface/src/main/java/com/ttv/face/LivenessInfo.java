//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

public class LivenessInfo {
    public static final int NOT_ALIVE = 0;
    public static final int ALIVE = 1;
    public static final int UNKNOWN = -1;
    public static final int FACE_NUM_MORE_THAN_ONE = -2;
    public static final int FACE_TOO_SMALL = -3;
    public static final int FACE_ANGLE_TOO_LARGE = -4;
    public static final int FACE_BEYOND_BOUNDARY = -5;
    public static final int ERROR_DEPTH = -6;
    public static final int TOO_BRIGHT_IR_IMAGE = -7;
    public static final int TOO_DARK_IR_IMAGE = -8;
    public static final int ERROR_FQ = -100;
    int liveness;

    public LivenessInfo() {
        this.liveness = -1;
    }

    public LivenessInfo(LivenessInfo livenessInfo) {
        if (livenessInfo != null) {
            this.liveness = livenessInfo.getLiveness();
        } else {
            this.liveness = -1;
        }

    }

    public int getLiveness() {
        return this.liveness;
    }

    public LivenessInfo clone() {
        return new LivenessInfo(this);
    }
}
