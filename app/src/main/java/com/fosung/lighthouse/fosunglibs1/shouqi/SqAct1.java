package com.fosung.lighthouse.fosunglibs1.shouqi;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fosung.lighthouse.fosunglibs1.R;

public class SqAct1 extends AppCompatActivity {

    private ImageView ivzbn3;
    private TextView tvzbn2;
    private LinearLayout llzbn2;
    private RelativeLayout rlzbn1;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingeek3);
        ivzbn3 = findViewById(R.id.ivzbn3);
        tvzbn2 = findViewById(R.id.tvzbn2);
        llzbn2 = findViewById(R.id.llzbn2);
        rlzbn1 = findViewById(R.id.rlzbn1);
        scrollView = findViewById(R.id.scrollView);
        //
        scrollView.setVisibility(View.GONE);
        llzbn2.setVisibility(View.GONE);
        rlzbn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.VISIBLE);
                llzbn2.setVisibility(View.VISIBLE);
                ivzbn3.setVisibility(View.GONE);

            }
        });
        llzbn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);
                llzbn2.setVisibility(View.GONE);
                ivzbn3.setVisibility(View.VISIBLE);

            }
        });
        // 监听文本变化
//        tvzbn2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                checkAndAdjustScroll();
//            }
//        });

        // 监听布局变化（如字体大小调整）
        tvzbn2.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            checkAndAdjustScroll();
        });
        tvzbn2.setText("内容");
        tvzbn2.setText("内容内容内容内容内容内容内容内容内容内内容内容内容内容内容内内容");
        tvzbn2.setText("内容内容内容内容内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内内容内容内容内容内容内");
    }

    // 动态调整 ScrollView 滚动权限
    private void checkAndAdjustScroll() {
        int contentHeight = tvzbn2.getHeight();  // 获取 TextView 实际高度

        if (contentHeight > dpToPx(150)) {
            // 内容超限，允许滚动
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = dpToPx(150);
            scrollView.setLayoutParams(params);
            scrollView.setOnTouchListener(null);  // 移除拦截
        } else {
            // 内容未超限，禁止滚动
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(params);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });  // 拦截所有触摸事件
        }
    }

    // dp 转 px 工具方法
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}