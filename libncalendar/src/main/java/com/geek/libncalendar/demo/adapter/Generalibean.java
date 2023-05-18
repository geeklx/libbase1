package com.geek.libncalendar.demo.adapter;

public class Generalibean {
    private String title;
    private String time;
    private String cjren;
    private String type;

    public Generalibean(String title, String time, String cjren, String type) {
        this.title = title;
        this.time = time;
        this.cjren = cjren;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCjren() {
        return cjren;
    }

    public void setCjren(String cjren) {
        this.cjren = cjren;
    }
}
