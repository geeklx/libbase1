package com.geek.libfacerecognition;

import android.graphics.Bitmap;

public class FaceEntity {

    public String userName;
    public Bitmap headImg;
    public byte[] feature;
    public int user_id;

    public FaceEntity() {

    }

    public FaceEntity(int user_id, String userName, Bitmap headImg, byte[] feature) {
        this.user_id = user_id;
        this.userName = userName;
        this.headImg = headImg;
        this.feature = feature;
    }
}
