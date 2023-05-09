package com.ttv.face;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class MaskInfo {
    public static final int NOT_WORN = 0;
    public static final int UNKNOWN = -1;
    public static final int WORN = 1;
    private int mask;

    public int getMask() {
        return this.mask;
    }

    public MaskInfo() {
        this.mask = -1;
    }

    public MaskInfo(MaskInfo maskInfo) {
        if (maskInfo == null) {
            this.mask = -1;
        } else {
            this.mask = maskInfo.mask;
        }

    }
}
