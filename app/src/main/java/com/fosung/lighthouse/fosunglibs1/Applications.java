package com.fosung.lighthouse.fosunglibs1;

import android.app.Application;

//import com.geek.libnsfw.ktl.NSFWHelper;


public class Applications extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        //开启日志输出，可选
//        NSFWHelper.INSTANCE.openDebugLog();
//        //扫描前必须初始化
//        NSFWHelper.INSTANCE.initHelper(this, null, true, 4);
    }
}
