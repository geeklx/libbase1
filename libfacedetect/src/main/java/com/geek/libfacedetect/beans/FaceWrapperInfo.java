package com.geek.libfacedetect.beans;

import android.graphics.Rect;
import android.hardware.camera2.params.Face;

public class FaceWrapperInfo {


    private Face face;

    private Rect rect;

    public FaceWrapperInfo(Face face, Rect rect) {
        this.face = face;
        this.rect = rect;
    }

    public FaceWrapperInfo(){}

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
