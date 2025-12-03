package com.fosung.lighthouse.fosunglibs1.flexbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fosung.lighthouse.fosunglibs1.R;
import com.google.android.flexbox.FlexboxLayout;


public class FlexUtil1 {

    private static volatile FlexUtil1 instance;
    private Context mContext;

    public FlexUtil1(Context context) {
        mContext = context;
    }

    public static FlexUtil1 getInstance(Context context) {
        if (instance == null) {
            synchronized (FlexUtil1.class) {
                instance = new FlexUtil1(context);
            }
        }
        return instance;
    }


    // 处理超限项（允许收缩 + 省略号）
    public void adjustLastItem(FlexboxLayout flexboxLayout) {
        if (flexboxLayout.getChildCount() == 0) {
            return;
        }

        View lastChild = flexboxLayout.getChildAt(flexboxLayout.getChildCount() - 1);
        if (lastChild instanceof TextView) {
            TextView lastTv = (TextView) lastChild;
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) lastTv.getLayoutParams();
            lp.setFlexShrink(1f); // 允许收缩
            lastTv.setLayoutParams(lp);
            lastTv.setEllipsize(TextUtils.TruncateAt.END);
            lastTv.requestLayout();
        }
    }

    // 检测 FlexboxLayout 是否超宽
    public boolean isOverWidth(FlexboxLayout flexboxLayout) {
        int parentAvailableWidth = flexboxLayout.getWidth() - flexboxLayout.getPaddingLeft() - flexboxLayout.getPaddingRight();
        if (parentAvailableWidth <= 0) {
            return false; // 防止宽度未测量
        }

        int totalWidth = 0;
        for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
            View child = flexboxLayout.getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            // 强制测量子项宽度（确保准确）
            child.measure(View.MeasureSpec.makeMeasureSpec(flexboxLayout.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) child.getLayoutParams();
            totalWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        }
        return totalWidth > parentAvailableWidth;
    }

    public TextView createTextView(int pos, String text) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FF5200"));
        textView.setTextSize(9f);
        int padding = dp2px(mContext, 4f);
        textView.setPadding(padding, 0, padding, 0);
        textView.setBackgroundResource(R.drawable.shape_remark23);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (pos == 0) {
            params.setMargins(0, 0, 0, 0);
        } else {
            params.setMargins(padding, 0, 0, 0);
        }
        params.setFlexShrink(0f); // 默认禁止收缩
        textView.setLayoutParams(params);

        // 关键：初始配置单行+末尾省略（后续仅最后一项收缩时生效）
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    // dp 转 px 工具方法
    public int dp2px(Context context, float dipValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (dipValue * scale + 0.5f);
    }
}

