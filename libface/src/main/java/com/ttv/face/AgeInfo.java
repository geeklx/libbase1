package com.ttv.face;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import androidx.annotation.NonNull;

public class AgeInfo {
    public static final int UNKNOWN_AGE = 0;
    int age;

    public AgeInfo() {
        this.age = 0;
    }

    public AgeInfo(AgeInfo obj) {
        if (obj == null) {
            this.age = 0;
        } else {
            this.age = obj.getAge();
        }

    }

    public int getAge() {
        return this.age;
    }

    @Override
    @NonNull
    public AgeInfo clone() {
        return new AgeInfo(this);
    }
}
