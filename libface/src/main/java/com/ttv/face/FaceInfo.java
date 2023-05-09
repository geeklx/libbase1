//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

import android.graphics.Rect;

import androidx.annotation.NonNull;

public class FaceInfo {
    public static final int FACE_DATA_SIZE = 5000;
    Face3DAngle face3DAngle;
    FaceAttributeInfo faceAttributeInfo;
    byte[] faceData;
    int faceId;
    Rect foreheadRect;
    int isWithinBoundary;
    int orient;
    Rect rect;

    public FaceInfo(Rect rect2, int orient2, int faceId2, byte[] faceData2, int isWithinBoundary2, Rect foreheadRect2, FaceAttributeInfo faceAttributeInfo2, Face3DAngle face3DAngle2) {
        this.rect = new Rect(rect2);
        this.orient = orient2;
        this.faceId = faceId2;
        this.faceData = faceData2;
        this.isWithinBoundary = isWithinBoundary2;
        this.foreheadRect = new Rect(foreheadRect2);
        this.faceAttributeInfo = new FaceAttributeInfo(faceAttributeInfo2);
        this.face3DAngle = new Face3DAngle(face3DAngle2);
    }

    public FaceInfo(FaceInfo obj) {
        if (obj == null) {
            this.rect = new Rect();
            this.orient = 0;
            this.faceId = 0;
            this.faceData = new byte[5000];
            this.isWithinBoundary = 0;
            this.foreheadRect = new Rect();
            this.faceAttributeInfo = new FaceAttributeInfo();
            this.face3DAngle = new Face3DAngle();
        } else {
            this.rect = new Rect(obj.rect);
            this.orient = obj.orient;
            this.faceId = obj.faceId;
            this.faceData = (byte[])obj.getFaceData().clone();
            this.isWithinBoundary = obj.isWithinBoundary;
            this.foreheadRect = new Rect(obj.foreheadRect);
            this.faceAttributeInfo = new FaceAttributeInfo(obj.faceAttributeInfo);
            this.face3DAngle = new Face3DAngle(obj.face3DAngle);
        }
    }

    public FaceInfo() {
        this.rect = new Rect();
        this.orient = 0;
        this.faceId = 0;
        this.faceData = new byte[5000];
        this.isWithinBoundary = 0;
        this.foreheadRect = new Rect();
        this.faceAttributeInfo = new FaceAttributeInfo();
        this.face3DAngle = new Face3DAngle();
    }

    public Rect getRect() {
        return this.rect;
    }

    public void setRect(Rect rect2) {
        this.rect = rect2;
    }

    public int getOrient() {
        return this.orient;
    }

    public void setOrient(int orient2) {
        this.orient = orient2;
    }

    public int getFaceId() {
        return this.faceId;
    }

    public void setFaceId(int faceId2) {
        this.faceId = faceId2;
    }

    public byte[] getFaceData() {
        return this.faceData;
    }

    public void setFaceData(byte[] faceData2) {
        this.faceData = faceData2;
    }

    public int getIsWithinBoundary() {
        return this.isWithinBoundary;
    }

    public void setIsWithinBoundary(int isWithinBoundary2) {
        this.isWithinBoundary = isWithinBoundary2;
    }

    public Rect getForeheadRect() {
        return this.foreheadRect;
    }

    public void setForeheadRect(Rect foreheadRect2) {
        this.foreheadRect = foreheadRect2;
    }

    public FaceAttributeInfo getFaceAttributeInfo() {
        return new FaceAttributeInfo(this.faceAttributeInfo);
    }

    public void setFaceAttributeInfo(FaceAttributeInfo faceAttributeInfo2) {
        this.faceAttributeInfo = faceAttributeInfo2;
    }

    public Face3DAngle getFace3DAngle() {
        return new Face3DAngle(this.face3DAngle);
    }

    public void setFace3DAngle(Face3DAngle face3DAngle2) {
        this.face3DAngle = face3DAngle2;
    }

    @Override
    @NonNull
    public String toString() {
        return "FaceInfo{faceRect=" + this.rect.toString() + ", orient=" + this.orient + ", faceId=" + this.faceId + ", isWithinBoundary=" + this.isWithinBoundary + "\nforeheadRect=" + this.foreheadRect.toString() + ", " + this.face3DAngle.toString() + "\n" + this.faceAttributeInfo.toString() + '}';
    }

    @Override
    @NonNull
    public FaceInfo clone() {
        return new FaceInfo(this);
    }
}
