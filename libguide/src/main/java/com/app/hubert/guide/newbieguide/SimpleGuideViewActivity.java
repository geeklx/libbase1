package com.app.hubert.guide.newbieguide;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.R;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;

public class SimpleGuideViewActivity extends Activity {

    private Button header_imgbtn;
    private LinearLayout ll_nearby, ll_video;
    private ImageView iv_nearby;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_guide_view);
        mActivity = SimpleGuideViewActivity.this;
        header_imgbtn = (Button) findViewById(R.id.header_imgbtn);
        iv_nearby = (ImageView) findViewById(R.id.iv_nearby);
        ll_nearby = (LinearLayout) findViewById(R.id.ll_nearby);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);
        header_imgbtn.post(new Runnable() {
            @Override
            public void run() {
                showGuideView();
            }
        });
    }

    public void showGuideView() {
        NewbieGuide.with(mActivity)
                .setLabel("guide30")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e("aaaaaa", "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e("aaaaaa", "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                        showGuideView2();
                    }
                })
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(header_imgbtn, HighLight.Shape.RECTANGLE, 20)
                        .setLayoutRes(R.layout.layer_frends1)).show();
    }

    public void showGuideView2() {
        NewbieGuide.with(mActivity)
                .setLabel("guide31")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e("aaaaaa", "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e("aaaaaa", "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                        showGuideView3();
                    }
                })
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(iv_nearby, HighLight.Shape.CIRCLE, 20)
                        .setLayoutRes(R.layout.layer_frends_muti)).show();
    }

    //
    public void showGuideView3() {
        NewbieGuide.with(mActivity)
                .setLabel("guide32")
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e("aaaaaa", "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e("aaaaaa", "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                        showGuideView();
                    }
                })
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(ll_video, HighLight.Shape.ROUND_RECTANGLE, 10)
                        .setLayoutRes(R.layout.layer_lottie1)).show();
    }
}
