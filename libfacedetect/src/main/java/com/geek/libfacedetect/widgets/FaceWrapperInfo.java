package com.geek.libfacedetect.widgets;

import android.graphics.Rect;
import android.hardware.camera2.params.Face;

/**
 * Created on 2020-04-28.
 */
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
