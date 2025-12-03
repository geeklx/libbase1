package com.fosung.lighthouse.fosunglibs1.flexbox;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fosung.lighthouse.fosunglibs1.R;
import com.geek.libutils.app.MyLogUtil;
import com.google.android.flexbox.FlexboxLayout;

public class Actflexbox12 extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingeek2);

        flexboxLayout = findViewById(R.id.flexbox_layout);
        displayMetrics = getResources().getDisplayMetrics();

        // 模拟数据
        String[] dataList = {
                "卖点卖点", "卖点卖点", "卖点卖点", "卖点",
                "卖点卖点卖点卖点", "卖点卖点卖点卖点卖点卖点卖点卖点"
        };

        for (String text : dataList) {
            TextView textView = createTextView(text);
            flexboxLayout.addView(textView);
        }
        MyLogUtil.e("flexboxLayout", flexboxLayout.getWidth() + "");
        adjustLastItemIfOverflow(); // 初始调整
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FF5200"));
        textView.setTextSize(20f);
        int padding = 10;
        textView.setPadding(padding, 0, padding, 0);
        textView.setBackgroundResource(R.drawable.shape_remark23);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(padding, 0, 0, 0);
        params.setFlexShrink(0f); // 默认禁止收缩
        textView.setLayoutParams(params);

        // 关键：初始配置单行+末尾省略（后续仅最后一项收缩时生效）
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    private void adjustLastItemIfOverflow() {
        flexboxLayout.post(() -> {
            // 计算FlexboxLayout可用宽度（总宽 - 左右内边距）
            int parentAvailableWidth = flexboxLayout.getWidth()
                    - flexboxLayout.getPaddingLeft()
                    - flexboxLayout.getPaddingRight();
            MyLogUtil.e("flexboxLayout", parentAvailableWidth + "");
            int totalWidth = 0;

            for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
                View child = flexboxLayout.getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                // 获取子View布局参数（需强转Flexbox的LayoutParams）
                FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) child.getLayoutParams();

                // 计算子View总占用宽度（自身宽度 + 左右边距）
                int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

                if (totalWidth + childWidth > parentAvailableWidth) {
                    // 仅处理TextView类型（确保安全）
                    if (child instanceof TextView) {
                        TextView lastTv = (TextView) child;
                        FlexboxLayout.LayoutParams tvLp = (FlexboxLayout.LayoutParams) lastTv.getLayoutParams();

                        tvLp.setFlexShrink(1f); // 允许收缩（父容器宽度不足时自动压缩）
                        lastTv.setLayoutParams(tvLp);

                        // 确保省略规则生效（虽初始已设置，此处再次确认）
                        lastTv.setEllipsize(TextUtils.TruncateAt.END);
                        lastTv.requestLayout(); // 触发布局更新，让收缩和省略生效
                    }
                    break; // 只处理第一个溢出的项（本题需求仅需处理最后一项）
                }
                totalWidth += childWidth;
            }
        });
    }
}