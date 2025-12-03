package com.fosung.lighthouse.fosunglibs1.flexbox;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fosung.lighthouse.fosunglibs1.R;
import com.google.android.flexbox.FlexboxLayout;

public class Actflexbox1 extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingeek2);
        flexboxLayout = findViewById(R.id.flexbox_layout);
        displayMetrics = getResources().getDisplayMetrics();
        // 延迟执行数据加载（确保布局完成）
        flexboxLayout.post(() -> {
            // 模拟数据
            String[] dataList = {"卖点卖点", "卖点卖点", "卖点卖点", "卖点", "卖点卖点卖点卖点", "卖点卖点", "卖点卖点卖点卖点卖点卖点卖点卖点"};
            // 逐条添加并检测宽度
            for (int i = 0; i < dataList.length; i++) {
                TextView textView = FlexUtil1.getInstance(this).createTextView(i, dataList[i]);
                flexboxLayout.addView(textView);
                if (FlexUtil1.getInstance(this).isOverWidth(flexboxLayout)) {
                    FlexUtil1.getInstance(this).adjustLastItem(flexboxLayout); // 调整最后一项
//                    flexboxLayout.removeView(textView); // 移除超限项
                    break;
                }
            }
        });

    }


}