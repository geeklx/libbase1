//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face.enums;

public enum DetectFaceOrientPriority {
    TTV_OP_0_ONLY(1),
    TTV_OP_90_ONLY(2),
    TTV_OP_270_ONLY(3),
    TTV_OP_180_ONLY(4),
    TTV_OP_ALL_OUT(5);

    private int priority;

    private DetectFaceOrientPriority(int priority2) {
        this.priority = priority2;
    }

    public int getPriority() {
        return this.priority;
    }
}
