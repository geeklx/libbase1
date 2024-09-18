package com.app.hubert.guide.listener;

import android.view.View;

import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.model.GuidePage;

/**
 * Created by hubert on 2018/2/12.
 * <p>
 * 用于引导层布局初始化
 */

public interface OnLayoutInflatedListener {

    /**
     * @param view       {@link GuidePage#setLayoutRes(int, int...)}方法传入的layoutRes填充后的view
     * @param controller {@link Controller}
     */
    void onLayoutInflated(View view, Controller controller);
}
