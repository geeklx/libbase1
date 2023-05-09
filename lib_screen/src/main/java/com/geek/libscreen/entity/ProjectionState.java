package com.geek.libscreen.entity;

import java.io.Serializable;

public class ProjectionState implements Serializable {
    //
    private static final long serialVersionUID = 1L;
    private String playback;//播放地址
    private String definition;//清晰度管理
    private boolean screenstatus;//投屏状态

    public ProjectionState(){

    }
    public ProjectionState(String playback, String definition, boolean screenstatus) {
        this.playback=playback;
        this.definition=definition;
        this.screenstatus=screenstatus;
    }

    public String getPlayback() {
        return playback;
    }

    public void setPlayback(String playback) {
        this.playback = playback;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean getScreenstatus() {
        return screenstatus;
    }

    public void setScreenstatus(boolean screenstatus) {
        this.screenstatus = screenstatus;
    }
}
