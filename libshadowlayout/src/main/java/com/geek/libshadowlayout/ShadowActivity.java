package com.geek.libshadowlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libshadowlayout.widgets.ShadowLayouts;


/**
 * Created by leo
 * on 2020/10/27.
 * shadow阴影的各项使用
 */
public class ShadowActivity extends AppCompatActivity {
    private ShadowLayouts ShadowLayoutIntent;
    private ShadowLayouts shadowLayout_bar_left;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        shadowLayout_bar_left = findViewById(R.id.shadowLayout_bar_left);
        ShadowLayoutIntent = findViewById(R.id.ShadowLayoutIntent);
        shadowLayout_bar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ShadowLayoutIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShadowActivity.this, StarShowActivity.class));
            }
        });
    }
}
