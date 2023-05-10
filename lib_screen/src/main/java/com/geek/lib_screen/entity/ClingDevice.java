<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/entity/ClingDevice.java
package com.yanbo.lib_screen.entity;
========
package com.geek.lib_screen.entity;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/entity/ClingDevice.java

import org.fourthline.cling.model.meta.Device;

/**
 * Created by lzan13 on 2018/3/5.
 */
public class ClingDevice {
    private Device device;
    private boolean isSelected = false;

    public ClingDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
