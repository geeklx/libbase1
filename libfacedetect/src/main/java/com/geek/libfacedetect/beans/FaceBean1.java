package com.geek.libfacedetect.beans;

import java.util.List;

public class FaceBean1 {
    private List<FaceBean2> data;

    public FaceBean1() {
    }

    public FaceBean1(List<FaceBean2> data) {
        this.data = data;
    }

    public List<FaceBean2> getData() {
        return data;
    }

    public void setData(List<FaceBean2> data) {
        this.data = data;
    }
}
