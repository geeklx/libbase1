package com.geek.libgoodview.blastgoodview.utils;

import android.content.Context;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:46
 * @description:类作用
 */
public class DeviceUtils {
    /**
     * @return int 实际转换后的px值
     * @description: 单位转换：dp转px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
