<<<<<<<< HEAD:lib_screen/src/main/java/com/yanbo/lib_screen/listener/ClingRegistryListener.java
package com.yanbo.lib_screen.listener;


import com.yanbo.lib_screen.manager.DeviceManager;
import com.yanbo.lib_screen.utils.LogUtils;
========
package com.geek.lib_screen.listener;


import com.geek.lib_screen.manager.DeviceManager;
import com.geek.lib_screen.utils.LogUtils;
>>>>>>>> 50f42b5b88681741d4c35c65f2b3458cb68b4a82:lib_screen/src/main/java/com/geek/lib_screen/listener/ClingRegistryListener.java

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

/**
 * Created by lzan13 on 2018/3/9.
 * 监听当前局域网设备变化
 */
public class ClingRegistryListener extends DefaultRegistryListener {
    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        LogUtils.d("remoteDeviceDiscoveryStarted %s", device.getDisplayString());
//        onDeviceAdded(device);
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
        LogUtils.e("remoteDeviceDiscoveryFailed %s - %s", device.getDisplayString()+"---"+ ex.toString());
//        onDeviceRemoved(device);
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        LogUtils.i("remoteDeviceAdded %s", device.getDisplayString());
        onDeviceAdded(device);
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        LogUtils.e("remoteDeviceRemoved %s", device.getDisplayString());
        onDeviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        LogUtils.d("localDeviceAdded %s", device.getDisplayString());
//        onDeviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        LogUtils.d("localDeviceRemoved %s", device.getDisplayString());
//        onDeviceRemoved(device);
    }

    /**
     * 新增 DLNA 设备
     */
    public void onDeviceAdded(Device device) {
        DeviceManager.getInstance().addDevice(device);
    }

    /**
     * 移除 DLNA 设备
     */
    public void onDeviceRemoved(Device device) {
        DeviceManager.getInstance().removeDevice(device);
    }
}
