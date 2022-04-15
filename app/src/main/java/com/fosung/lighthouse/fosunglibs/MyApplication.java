package com.fosung.lighthouse.fosunglibs;

import android.app.Application;

import com.geek.libgoodview.blastgoodview.utils.ApplicationUtils;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午4:09
 * @description:类作用
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtils.init(this);
    }
}
