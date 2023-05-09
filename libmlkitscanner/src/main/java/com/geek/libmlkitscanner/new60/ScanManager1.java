package com.geek.libmlkitscanner.new60;

import android.app.Activity;
import android.bluetooth.le.ScanCallback;
import android.content.Intent;

import com.geek.libmlkitscanner.R;
import com.geek.libmlkitscanner.callback.act.ActResultRequest;
import com.geek.libmlkitscanner.callback.act.MNScanCallback;

public class ScanManager1 {

    //常量
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_CANCLE = 2;
    public static final String INTENT_KEY_RESULT_SUCCESS = "INTENT_KEY_RESULT_SUCCESS";
    public static final String INTENT_KEY_RESULT_ERROR = "INTENT_KEY_RESULT_ERROR";
    //是否是调试模式
    public static final boolean isDebugMode = false;


    //跳转传入的数据
    public static final String INTENT_KEY_CONFIG_MODEL = "INTENT_KEY_CONFIG_MODEL";


//    public static void startScan(Activity activity, ScanCallback scanCallback) {
//        startScan(activity, null, scanCallback);
//    }

    public static void startScan(Activity activity, ScanCallback1 scanCallback) {
        Intent intent = new Intent(activity.getApplicationContext(), QRCodeAct1.class);
//        intent.putExtra(ScanManager.INTENT_KEY_CONFIG_MODEL, mnScanConfig);
        ActResultRequest1 actResultRequest = new ActResultRequest1(activity);
        actResultRequest.startForResult(intent, scanCallback);
        activity.overridePendingTransition(R.anim.anim_bottom_in, android.R.anim.fade_out);
    }

    /**
     * 关闭当前页面
     */
    public static void closeScanPage() {
        QRCodeAct1.closeScanPage();
    }

    /**
     * 打开相册扫描图片
     */
    public static void openAlbumPage() {
        QRCodeAct1.startPickPhoto();
    }

    /**
     * 手电筒
     */
    public static void ScanLight() {
        QRCodeAct1.onClickFlashlight();
    }


}
