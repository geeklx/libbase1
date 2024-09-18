package com.yanbo.libscreen.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.yanbo.libscreen.VApplication;
import com.yanbo.libscreen.callback.ContentBrowseCallback;
import com.yanbo.libscreen.entity.RemoteItem1;
import com.yanbo.libscreen.event.DIDLEvent;
import com.yanbo.libscreen.listener.ClingRegistryListener;
import com.yanbo.libscreen.service.ClingService;
import com.yanbo.libscreen.service.SystemService;
import com.yanbo.libscreen.utils.LogUtils;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.item.Item;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by lzan13 on 2018/3/6.
 */
public class ClingManager {
    private final String TAG = this.getClass().getSimpleName();
    public static final ServiceType CONTENT_DIRECTORY = new UDAServiceType("ContentDirectory");

    private static ClingManager instance;
    private Context context;

    private ClingRegistryListener clingRegistryListener;
    private ServiceConnection clingServiceConnection;
    private ClingService clingService;

    private ServiceConnection systemServiceConnection;
    private SystemService systemService;

    private Item localItem;
    private RemoteItem1 remoteItem;

    /**
     * 私有构造方法
     */
    private ClingManager() {
        context = VApplication.getContext();
    }

    public static ClingManager getInstance() {
        if (instance == null) {
            instance = new ClingManager();
        }
        return instance;
    }

    /**
     * 获取 UPnP 堆栈核心，注册跟组设备和资源
     */
    public Registry getRegistry() {
        return clingService.getRegistry();
    }

    /**
     * 获取控制点
     */
    public ControlPoint getControlPoint() {
        return clingService.getControlPoint();
    }

    /**
     * 搜索设备
     */
    public void searchDevices() {
        getControlPoint().search();
    }

    /**
     * 设置 ClingService
     */
    public void setClingService(ClingService service) {
        clingService = service;
    }

    public void setSystemService(SystemService service) {
        systemService = service;
    }

    public void setLocalItem(Item item) {
        localItem = item;
        remoteItem = null;
        ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
    }

    public Item getLocalItem() {
        return localItem;
    }

    public RemoteItem1 getRemoteItem() {
        return remoteItem;
    }

    public void setRemoteItem(RemoteItem1 remoteItem) {
        this.remoteItem = remoteItem;
        this.localItem = null;
        ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
    }

    public void startClingService() {
        bindService();
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        clingServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtils.i("onServiceConnected - %s", name);
                ClingService.LocalBinder binder = (ClingService.LocalBinder) service;
                ClingService clingService = binder.getService();

                setClingService(clingService);

                clingRegistryListener = new ClingRegistryListener();
                getRegistry().addListener(clingRegistryListener);

                searchDevices();
                searchLocalContent("0");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtils.e("onServiceDisconnected - %s", name);
                setClingService(null);
            }
        };
        Intent clingServiceIntent = new Intent(context, ClingService.class);
        context.bindService(clingServiceIntent, clingServiceConnection, Context.BIND_AUTO_CREATE);


        systemServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtils.i("onServiceConnected - %s", name);
                SystemService.LocalBinder binder = (SystemService.LocalBinder) service;
                setSystemService(binder.getService());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtils.e("onServiceDisconnected - %s", name);
                setSystemService(null);
            }
        };
        Intent systemServiceIntent = new Intent(context, SystemService.class);
        context.bindService(systemServiceIntent, systemServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopClingService() {
        unbindService();
    }

    /**
     * 取消服务绑定
     */
    private void unbindService() {
        if (clingServiceConnection != null) {
            context.unbindService(clingServiceConnection);
            clingServiceConnection = null;
        }
        if (systemServiceConnection != null) {
            context.unbindService(systemServiceConnection);
            systemServiceConnection = null;
        }
        if (clingService != null) {
            clingService.onDestroy();
            clingService = null;
        }
        if (systemService != null) {
            systemService.onDestroy();
            systemService = null;
        }
        clingRegistryListener = null;
    }

    public void searchLocalContent(String containerId) {
        Device localDevice = clingService.getLocalDevice();
        Service service = localDevice.findService(CONTENT_DIRECTORY);
        ControlPoint controlPoint = clingService.getControlPoint();

        controlPoint.execute(new ContentBrowseCallback(service, containerId) {
            @Override
            public void received(ActionInvocation actionInvocation, DIDLContent didl) {
                LogUtils.e("Load local content! containers:%d, items:%d", didl.getContainers().size() + "    " +
                        didl.getItems().size() + "  ");
                DIDLEvent event = new DIDLEvent();
                event.content = didl;
                EventBus.getDefault().post(event);
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("Load local content failure %s", msg);
            }
        });
    }

    public void destroy() {
        stopClingService();
        instance = null;
    }

}
