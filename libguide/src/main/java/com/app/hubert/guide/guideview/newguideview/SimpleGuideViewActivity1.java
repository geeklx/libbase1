package com.app.hubert.guide.guideview.newguideview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.hubert.guide.R;
import com.app.hubert.guide.guideview.Component;
import com.app.hubert.guide.guideview.Guide;
import com.app.hubert.guide.guideview.GuideBuilder;
import com.app.hubert.guide.guideview.newguideview.component.LottieComponent;
import com.app.hubert.guide.guideview.newguideview.component.MutiComponent;
import com.app.hubert.guide.guideview.newguideview.component.SimpleComponent;

public class SimpleGuideViewActivity1 extends Activity {

    private Button header_imgbtn;
    private LinearLayout ll_nearby, ll_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_guide_view1);
        header_imgbtn = (Button) findViewById(R.id.header_imgbtn);
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
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(header_imgbtn)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                showGuideView2();
            }
        });

        builder.addComponent(new SimpleComponent());
        Guide guide = builder.createGuide();
        guide.show(SimpleGuideViewActivity1.this);
    }

    public void showGuideView2() {
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(ll_nearby)
                .setAlpha(150)
                .setHighTargetGraphStyle(Component.CIRCLE);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                showGuideView3();
            }
        });

        builder1.addComponent(new MutiComponent());
        Guide guide = builder1.createGuide();
        guide.show(SimpleGuideViewActivity1.this);
    }

    @SuppressLint("ResourceType")
    public void showGuideView3() {
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(ll_video)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setExitAnimationId(android.R.anim.fade_out);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
            }
        });

        builder1.addComponent(new LottieComponent());
        Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(SimpleGuideViewActivity1.this);
    }
}
