//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

import androidx.annotation.NonNull;

public class Face3DAngle {
    float pitch;
    float roll;
    float yaw;

    public Face3DAngle() {
        this.yaw = 0.0F;
        this.roll = 0.0F;
        this.pitch = 0.0F;
    }

    public Face3DAngle(Face3DAngle obj) {
        if (obj == null) {
            this.yaw = 0.0F;
            this.roll = 0.0F;
            this.pitch = 0.0F;
        } else {
            this.yaw = obj.getYaw();
            this.roll = obj.getRoll();
            this.pitch = obj.getPitch();
        }
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getRoll() {
        return this.roll;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override
    @NonNull
    public Face3DAngle clone() {
        return new Face3DAngle(this);
    }

    @Override
    @NonNull
    public String toString() {
        return "Face3DAngle{yaw=" + this.yaw + ", roll=" + this.roll + ", pitch=" + this.pitch + '}';
    }
}
