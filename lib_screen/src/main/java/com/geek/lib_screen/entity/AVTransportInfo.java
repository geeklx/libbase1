<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/entity/AVTransportInfo.java
package com.yanbo.lib_screen.entity;
========
package com.geek.lib_screen.entity;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/entity/AVTransportInfo.java

/**
 * Created by lzan13 on 2018/4/11.
 */
public class AVTransportInfo {

    public static String TRANSITIONING = "TRANSITIONING";
    public static String PLAYING = "PLAYING";
    public static String PAUSED_PLAYBACK = "PAUSED_PLAYBACK";
    public static String STOPPED = "STOPPED";

    private String state;
    private String mediaDuration;
    private String timePosition;

    public AVTransportInfo() {}

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    public String getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(String timePosition) {
        this.timePosition = timePosition;
    }
}
