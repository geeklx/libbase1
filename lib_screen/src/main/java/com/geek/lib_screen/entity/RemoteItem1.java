package com.geek.lib_screen.entity;

/**
 * Created by lzan13 on 2018/3/24.
 * 网络资源实体类
 */
public class RemoteItem1 {
    private String title;
    private String id;
    private String creator;
    private long size;
    private String duration;
    private String resolution;
    private String standardurl;//标清
    private String highurl;//高清
    private String quasihighurl;//准高清



    public RemoteItem1(String title, String id, String creator, long size, String duration,
                       String resolution, String gqurl, String zgqurl, String bqurl) {
        setTitle(title);
        setId(id);
        setCreator(creator);
        setSize(size);
        setDuration(duration);
        setResolution(resolution);
        setHighurl(gqurl);
        setQuasihighurl(zgqurl);
        setStandardurl(bqurl);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    public String getStandardurl() {
        return standardurl;
    }

    public void setStandardurl(String standardurl) {
        this.standardurl = standardurl;
    }

    public String getHighurl() {
        return highurl;
    }

    public void setHighurl(String highurl) {
        this.highurl = highurl;
    }

    public String getQuasihighurl() {
        return quasihighurl;
    }

    public void setQuasihighurl(String quasihighurl) {
        this.quasihighurl = quasihighurl;
    }
}
