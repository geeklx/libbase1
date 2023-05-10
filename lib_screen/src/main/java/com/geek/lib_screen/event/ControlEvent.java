<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/event/ControlEvent.java
package com.yanbo.lib_screen.event;


import com.yanbo.lib_screen.entity.AVTransportInfo;
import com.yanbo.lib_screen.entity.RenderingControlInfo;
========
package com.geek.lib_screen.event;


import com.geek.lib_screen.entity.AVTransportInfo;
import com.geek.lib_screen.entity.RenderingControlInfo;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/event/ControlEvent.java

/**
 * Created by lzan13 on 2018/4/12.
 * 投屏控制相关事件类
 */
public class ControlEvent {
    private AVTransportInfo avtInfo;
    private RenderingControlInfo rcInfo;

    public AVTransportInfo getAvtInfo() {
        return avtInfo;
    }

    public void setAvtInfo(AVTransportInfo avtInfo) {
        this.avtInfo = avtInfo;
    }

    public RenderingControlInfo getRcInfo() {
        return rcInfo;
    }

    public void setRcInfo(RenderingControlInfo rcInfo) {
        this.rcInfo = rcInfo;
    }
}
