package com.geek.libcamera1;

import java.io.Serializable;

public class BjyyBeanYewu3 implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    public BjyyBeanYewu3() {
    }

    public BjyyBeanYewu3(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}