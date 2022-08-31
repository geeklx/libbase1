package com.geek.libfacedetect.beans;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class FaceBean2 {
    private String img;
    private String name;

    public FaceBean2(String img, String name) {
        this.img = img;
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
