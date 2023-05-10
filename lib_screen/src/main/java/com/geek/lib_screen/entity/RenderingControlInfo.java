<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/entity/RenderingControlInfo.java
package com.yanbo.lib_screen.entity;
========
package com.geek.lib_screen.entity;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/entity/RenderingControlInfo.java

/**
 * Created by lzan13 on 2018/4/11.
 */
public class RenderingControlInfo {
    private boolean isMute;
    private int volume;
    private String presetNameList;

    public RenderingControlInfo() {}

    public RenderingControlInfo(boolean mute, int volume) {
        this.isMute = mute;
        this.volume = volume;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getPresetNameList() {
        return presetNameList;
    }

    public void setPresetNameList(String presetNameList) {
        this.presetNameList = presetNameList;
    }
}
