package com.geek.libsupertextview.supertextview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libsupertextview.CommonTextView;
import com.geek.libsupertextview.R;

/**
 * Created by allen on 2016/11/22.
 */

public class CommonTextViewActivity extends AppCompatActivity {
    CommonTextView commonTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_textview_layout);

        commonTextView = (CommonTextView) findViewById(R.id.common_tv);
        commonTextView.setOnCommonTextViewClickListener(new CommonTextView.OnCommonTextViewClickListener() {
            @Override
            public void onCommonTextViewClick() {
                super.onCommonTextViewClick();
                Toast.makeText(CommonTextViewActivity.this, "onCommonTextViewClick", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLeftViewClick() {
                super.onLeftViewClick();
                Toast.makeText(CommonTextViewActivity.this, "onLeftViewClick", Toast.LENGTH_LONG).show();
            }

        });

//        Glide.with(this).load("url").placeholder(R.drawable.head_default)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        commonTextView.setLeftDrawableLeft(resource);
//                    }
//                });
    }
}
