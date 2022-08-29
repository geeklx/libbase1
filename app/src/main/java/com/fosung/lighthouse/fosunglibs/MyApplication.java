package com.fosung.lighthouse.fosunglibs;

import android.app.Application;

import com.blankj.utilcode.util.SPUtils;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;


/**
 * @author:wangshouxue
 * @date:2022/3/28 下午4:09
 * @description:类作用
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        ApplicationUtils.init(this);
        //
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setPrivateFontScale(SPUtils.getInstance().getFloat("textSizef", 0f))
                .setExcludeFontScale(true)
                .getUnitsManager()
                .setSupportDP(true)
                .setSupportSubunits(Subunits.MM);
    }
}
