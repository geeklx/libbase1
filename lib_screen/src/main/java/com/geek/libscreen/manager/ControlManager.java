package com.geek.libscreen.manager;


import com.alibaba.fastjson.JSON;
import com.geek.libscreen.VError;
import com.geek.libscreen.callback.AVTransportCallback;
import com.geek.libscreen.callback.ControlCallback;
import com.geek.libscreen.callback.RenderingControlCallback;
import com.geek.libscreen.entity.AVTransportInfo;
import com.geek.libscreen.entity.ClingDevice;
import com.geek.libscreen.entity.ProjectionState;
import com.geek.libscreen.entity.RemoteItem1;
import com.geek.libscreen.entity.RenderingControlInfo;
import com.geek.libscreen.event.ControlEvent;
import com.geek.libscreen.mmkv.MmkvUtils;
import com.geek.libscreen.utils.ClingUtil;
import com.geek.libscreen.utils.LogUtils;
import com.geek.libscreen.utils.VMDate;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.callback.GetPositionInfo;
import org.fourthline.cling.support.avtransport.callback.GetTransportInfo;
import org.fourthline.cling.support.avtransport.callback.Pause;
import org.fourthline.cling.support.avtransport.callback.Play;
import org.fourthline.cling.support.avtransport.callback.Seek;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportState;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.renderingcontrol.callback.GetVolume;
import org.fourthline.cling.support.renderingcontrol.callback.SetMute;
import org.fourthline.cling.support.renderingcontrol.callback.SetVolume;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by lzan13 on 2018/3/10.
 * 控制点管理器
 */
public class ControlManager {

    // 视频传输服务
    public static final String AV_TRANSPORT = "AVTransport";
    // DMR 设备的控制服务
    public static final String RENDERING_CONTROL = "RenderingControl";


    private static ControlManager instance;
    private Service avtService;
    private Service rcService;
    private UnsignedIntegerFourBytes instanceId;

    private AVTransportCallback avtCallback;
    private RenderingControlCallback rcCallback;
    private boolean isScreenCast = false;
    private String absTimeStr;
    private long absTime;
    private String trackDurationStr;
    private long trackDuration;

    private CastState state = CastState.STOPED;
    private boolean isMute = false;

    private ControlManager() {
        avtService = findServiceFromDevice(AV_TRANSPORT);
        rcService = findServiceFromDevice(RENDERING_CONTROL);
        instanceId = new UnsignedIntegerFourBytes("0");
    }

    public static ControlManager getInstance() {
        if (instance == null) {
            instance = new ControlManager();
        }
        return instance;
    }

    /**
     * 开始新的投屏播放，需要先停止上一次的投屏
     *
     * @param item 需要投屏播放的本地资源对象
     */
    public void newPlayCast(final Item item,final String definitiontype, final ControlCallback callback) {
        stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                setAVTransportURI(item,definitiontype, new ControlCallback() {
                    @Override
                    public void onSuccess() {
                        playCast(callback);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        callback.onError(code, msg);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                callback.onError(code, msg);
            }
        });
    }

    /**
     * 开始投屏，需要先停止上一个投屏
     *
     * @param item 需要投屏的远程网络资源对象
     */
    public void newPlayCast(final RemoteItem1 item,final String definitiontype, final ControlCallback callback) {
        stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                setAVTransportURI(item,definitiontype, new ControlCallback() {
                    @Override
                    public void onSuccess() {
                        playCast(callback);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        callback.onError(code, msg);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                callback.onError(code, msg);
            }
        });
    }

    /**
     * 播放投屏
     */
    public void playCast(final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "电视服务为空");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Play(instanceId, avtService) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.i("","播放成功");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("播放错误 %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 暂停投屏
     */
    public void pauseCast(final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "电视服务为空");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Pause(instanceId, avtService) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.i("","暂停成功");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("暂停错误 %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 停止投屏
     */
    public void stopCast(final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "电视服务为空");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Stop(instanceId, avtService) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.i("","停止投屏 success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("停止投屏 error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置投屏进度
     *
     * @param target 目标进度
     */
    public void seekCast(final String target, final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "电视服务为空");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new Seek(instanceId, avtService, target) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.d("投屏进度 success - %s", target);
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("投屏进度 error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * -------------- RCService 相关操作 --------------
     * 设置投屏音量
     */
    public void setVolumeCast(int volume, final ControlCallback callback) {
        if (checkRCService()) {
            callback.onError(VError.SERVICE_IS_NULL, "RC 服务为空");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetVolume(instanceId, rcService, volume) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.d(" ","设置音量 success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("设置音量 error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 静音投屏
     */
    public void muteCast(boolean mute, final ControlCallback callback) {
        if (checkRCService()) {
            callback.onError(VError.SERVICE_IS_NULL, "RC 服务为空");
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetMute(instanceId, rcService, mute) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.d("","静音 success");
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("静音 error %s", msg);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置本地资源音视频传输 URI
     *
     * @param item 需要投屏的资源
     */
    public void setAVTransportURI(Item item,final String definitiontype, final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "服务为空");
            return;
        }
        final String uri = item.getFirstResource().getValue();
        DIDLContent content = new DIDLContent();
        content.addItem(item);
        String metadata = "";
        try {
            metadata = new DIDLParser().generate(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d("metadata: %s", metadata);
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new SetAVTransportURI(instanceId, avtService, uri, metadata) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.i("setAVTransportURI success %s", uri);
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("setAVTransportURI - error %s url:%s", msg+"   URL   "+uri);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 设置远程资源音视频传输 URI
     */
    public void setAVTransportURI(RemoteItem1 item,final String definitiontype, final ControlCallback callback) {
        if (checkAVTService()) {
            callback.onError(VError.SERVICE_IS_NULL, "service is null");
            return;
        }
        String metadata = ClingUtil.getItemMetadata(item);
        LogUtils.i("metadata: " , metadata);
        String uri = null;
        if (definitiontype.equals("gq")){
            uri=item.getHighurl();
        }else if(definitiontype.equals("zgq")){
            uri=item.getQuasihighurl();
        }else if(definitiontype.equals("bq")){
            uri=item.getStandardurl();
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        final String finalUri = uri;
        ProjectionState projectionState=new ProjectionState(finalUri,definitiontype,true);
        MmkvUtils.getInstance().set_common_json("projectionState", JSON.toJSONString(projectionState),ProjectionState.class);
        controlPoint.execute(new SetAVTransportURI(instanceId, avtService, finalUri, metadata) {
            @Override
            public void success(ActionInvocation invocation) {
                LogUtils.i("setAVTransportURI success url:%s", finalUri);
                callback.onSuccess();
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("setAVTransportURI - error %s url:%s", msg+"   URL   "+ finalUri);
                callback.onError(VError.UNKNOWN, msg);
            }
        });
    }

    /**
     * 初始化投屏相关回调
     */
    public void initScreenCastCallback() {
        unInitScreenCastCallback();
        isScreenCast = true;
        LogUtils.d("","initScreenCastCallback");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isScreenCast) {
                    try {
                        getPositionInfo();
                        getTransportInfo();
                        getVolume();
                        // 如果是暂停状态就睡眠5秒
                        if (state == CastState.PAUSED) {
                            Thread.sleep(2000);
                        } else {
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        // 设置投屏传输相关回调
        avtCallback = new AVTransportCallback(avtService) {
            @Override
            protected void received(AVTransportInfo info) {
                ControlEvent event = new ControlEvent();
                event.setAvtInfo(info);
                EventBus.getDefault().post(event);
            }
        };
        ClingManager.getInstance().getControlPoint().execute(avtCallback);

        // 设置播放控制相关回调，这个其实在大部分设备上都无效
        rcCallback = new RenderingControlCallback(rcService) {
            @Override
            protected void received(RenderingControlInfo info) {
                LogUtils.d("RenderingControlCallback received: mute:%b, volume:%d", info.isMute()+"   volume  "+info
                        .getVolume());
                ControlEvent event = new ControlEvent();
                event.setRcInfo(info);
                EventBus.getDefault().post(event);
            }
        };
        ClingManager.getInstance().getControlPoint().execute(rcCallback);
    }

    /**
     * 取消初始化投屏相关回调
     */
    public void unInitScreenCastCallback() {
        LogUtils.d("","unInitScreenCastCallback");
        absTimeStr = "00:00:00";
        absTime = 0;
        trackDurationStr = "00:00:00";
        trackDuration = 0;

        isScreenCast = false;
        avtCallback = null;
        rcCallback = null;
    }

    /**
     * 获取投屏设备端播放进度信息
     */
    public void getPositionInfo() {
        if (checkAVTService()) {
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new GetPositionInfo(instanceId, avtService) {
            @Override
            public void received(ActionInvocation invocation, PositionInfo positionInfo) {
                if (positionInfo != null) {
                    AVTransportInfo info = new AVTransportInfo();
                    info.setTimePosition(positionInfo.getAbsTime());
                    info.setMediaDuration(positionInfo.getTrackDuration());
                    ControlEvent event = new ControlEvent();
                    event.setAvtInfo(info);
                    EventBus.getDefault().post(event);

                    absTimeStr = positionInfo.getAbsTime();
                    absTime = VMDate.fromTimeString(absTimeStr);
                    trackDurationStr = positionInfo.getTrackDuration();
                    trackDuration = VMDate.fromTimeString(trackDurationStr);
                    if (absTimeStr.equals(trackDurationStr) && absTime != 0 && trackDuration != 0) {
                        unInitScreenCastCallback();
                    }
                }
                LogUtils.d("getPositionInfo success positionInfo:" , positionInfo.toString());
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("E","getPositionInfo failed");
            }
        });
    }

    /**
     * 获取投屏设备播放端数据传输状态信息
     */
    public void getTransportInfo() {
        if (checkAVTService()) {
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new GetTransportInfo(instanceId, avtService) {

            @Override
            public void received(ActionInvocation invocation, TransportInfo transportInfo) {
                TransportState ts = transportInfo.getCurrentTransportState();
                AVTransportInfo info = new AVTransportInfo();
                if (TransportState.TRANSITIONING == ts) {
                    info.setState(AVTransportInfo.TRANSITIONING);
                } else if (TransportState.PLAYING == ts) {
                    info.setState(AVTransportInfo.PLAYING);
                } else if (TransportState.PAUSED_PLAYBACK == ts) {
                    info.setState(AVTransportInfo.PAUSED_PLAYBACK);
                } else if (TransportState.STOPPED == ts) {
                    info.setState(AVTransportInfo.STOPPED);
                    if (absTime != 0 && trackDuration != 0) {
                        unInitScreenCastCallback();
                    }
                } else {
                    info.setState(AVTransportInfo.STOPPED);
                    if (absTime != 0 && trackDuration != 0) {
                        unInitScreenCastCallback();
                    }
                }
                ControlEvent event = new ControlEvent();
                event.setAvtInfo(info);
                EventBus.getDefault().post(event);
                LogUtils.d("getTransportInfo success transportInfo:" , ts.getValue());
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("E","getTransportInfo failed");
            }
        });
    }

    /**
     * 获取投屏音量
     */
    public void getVolume() {
        if (checkRCService()) {
            return;
        }
        ControlPoint controlPoint = ClingManager.getInstance().getControlPoint();
        controlPoint.execute(new GetVolume(instanceId, rcService) {
            @Override
            public void received(ActionInvocation actionInvocation, int currentVolume) {
                RenderingControlInfo info = new RenderingControlInfo();
                info.setVolume(currentVolume);
                info.setMute(false);
                ControlEvent event = new ControlEvent();
                event.setRcInfo(info);
                EventBus.getDefault().post(event);
                LogUtils.d("getVolume success volume:" , currentVolume);
            }

            @Override
            public void failure(ActionInvocation invocation, UpnpResponse operation, String msg) {
                LogUtils.e("getVolume error %s", msg);
            }
        });
    }

    /**
     * 检查视频传输服务是否存在
     */
    private boolean checkAVTService() {
        if (avtService == null) {
            avtService = findServiceFromDevice(AV_TRANSPORT);
        }
        return avtService == null;
    }

    /**
     * 检查视频播放控制服务是否存在
     */
    private boolean checkRCService() {
        if (rcService == null) {
            rcService = findServiceFromDevice(RENDERING_CONTROL);
        }
        return rcService == null;
    }

    /**
     * 通过指定服务类型，搜索当前选择的设备的服务
     *
     * @param type 需要的服务类型
     */
    public Service findServiceFromDevice(String type) {
        UDAServiceType serviceType = new UDAServiceType(type);
        ClingDevice device = DeviceManager.getInstance().getCurrClingDevice();
        if (device == null) {
            return null;
        }
        return device.getDevice().findService(serviceType);
    }

    public CastState getState() {
        return state;
    }

    public void setState(CastState state) {
        this.state = state;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    /**
     * 销毁，释放资源
     */
    public void destroy() {
        instance = null;
        avtService = null;
        rcService = null;
    }

    public enum CastState {
        TRANSITIONING,
        PLAYING,
        PAUSED,
        STOPED
    }

}
