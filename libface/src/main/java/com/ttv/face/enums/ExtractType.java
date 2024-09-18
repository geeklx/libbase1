//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face.enums;

public enum ExtractType {
    REGISTER(1),
    RECOGNIZE(0);

    int extractType;

    private ExtractType(int extractType2) {
        this.extractType = extractType2;
    }

    public int getExtractType() {
        return this.extractType;
    }
}
