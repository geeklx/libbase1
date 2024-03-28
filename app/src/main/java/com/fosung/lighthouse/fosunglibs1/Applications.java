package com.fosung.lighthouse.fosunglibs1;

import android.app.Application;
import android.content.Context;

import coms.geek.libcamera1.PictureSelectorEngineImp;
import coms.luck.lib.camerax.BaseApp7;
import coms.luck.picture.lib.app.IApp;
import coms.luck.picture.lib.app.PictureAppMaster;
import coms.luck.picture.lib.engine.PictureSelectorEngine;
//import com.geek.libnsfw.ktl.NSFWHelper;


public class Applications extends Application {
//public class Applications extends HarmonyApplication {


    @Override
    public void onCreate() {
        super.onCreate();
//        //开启日志输出，可选
//        NSFWHelper.INSTANCE.openDebugLog();
//        //扫描前必须初始化
//        NSFWHelper.INSTANCE.initHelper(this, null, true, 4);

    }
}
