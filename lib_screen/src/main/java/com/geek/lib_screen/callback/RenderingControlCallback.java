<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/callback/RenderingControlCallback.java
package com.yanbo.lib_screen.callback;


import com.yanbo.lib_screen.entity.RenderingControlInfo;
import com.yanbo.lib_screen.utils.LogUtils;
========
package com.geek.lib_screen.callback;


import com.geek.lib_screen.entity.RenderingControlInfo;
import com.geek.lib_screen.utils.LogUtils;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/callback/RenderingControlCallback.java

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.lastchange.LastChangeParser;
import org.fourthline.cling.support.model.Channel;
import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelMute;
import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelVolume;
import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;

import java.util.List;

/**
 * Created by lzan13 on 2018/3/5.
 * 投屏播放控制相关回调，这里主要会回调音量和是否静音
 */
public abstract class RenderingControlCallback extends BaseSubscriptionCallback {
    private final String TAG = this.getClass().getSimpleName();

    protected RenderingControlCallback(Service service) {
        super(service);
    }

    @Override
    protected LastChangeParser getLastChangeParser() {
        return new RenderingControlLastChangeParser();
    }

    @Override
    protected void onReceived(List<EventedValue> values) {
        RenderingControlInfo info = new RenderingControlInfo();
        for (EventedValue entry : values) {
            if ("Mute".equals(entry.getName())) {
                Object obj = entry.getValue();
                if (obj instanceof ChannelMute) {
                    ChannelMute cm = (ChannelMute) obj;
                    if (Channel.Master.equals(cm.getChannel())) {
                        info.setMute(cm.getMute());
                    }
                }
            }
            if ("Volume".equals(entry.getName())) {
                Object obj = entry.getValue();
                if (obj instanceof ChannelVolume) {
                    ChannelVolume cv = (ChannelVolume) obj;
                    if (Channel.Master.equals(cv.getChannel())) {
                        info.setVolume(cv.getVolume());
                    }
                }
            }
            if ("PresetNameList".equals(entry.getName())) {
                Object obj = entry.getValue();
                info.setPresetNameList(obj.toString());
            }
        }
        LogUtils.d("RenderingControlCallback onReceived:", "  info.isMute===  "+info.isMute() +"  info.getVolume===  "+info.getVolume());
        received(info);
    }

    protected abstract void received(RenderingControlInfo info);

}
