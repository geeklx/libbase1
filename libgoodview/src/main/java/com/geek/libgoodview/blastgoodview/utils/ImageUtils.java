package com.geek.libgoodview.blastgoodview.utils;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.geek.libgoodview.blastgoodview.Size;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午4:00
 * @description:类作用
 */
public class ImageUtils {
    /**
     * 获取图片宽高（未兼容旋转角度）
     *
     * @return
     */
    public static Size getImageSize(Context context, int resId) {
        Size size = new Size();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(context.getResources(), resId, options);

        size.setWidth(options.outWidth);
        size.setHeight(options.outHeight);

        return size;
    }

}
