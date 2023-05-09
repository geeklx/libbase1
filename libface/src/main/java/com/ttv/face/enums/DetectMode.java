//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face.enums;

public enum DetectMode {
    TTV_DETECT_MODE_VIDEO(0L),
    TTV_DETECT_MODE_IMAGE(4294967295L);

    private long mode;

    private DetectMode(long mode2) {
        this.mode = mode2;
    }

    public long getMode() {
        return this.mode;
    }
}
