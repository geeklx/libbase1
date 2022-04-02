package com.geek.libshadowlayout;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libshadowlayout.widgets.ShadowLayouts;

/**
 * shape功能的各项使用
 */
public class ShapeActivity extends AppCompatActivity {
    private ShadowLayouts ShadowLayout_image;
    private ShadowLayouts shadowLayout_bar_left;
    private ShadowLayouts shadowLayout_select;
    private ShadowLayouts shadowLayout_bindView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);
        ShadowLayout_image = findViewById(R.id.ShadowLayout_image);
        shadowLayout_bar_left = findViewById(R.id.shadowLayout_bar_left);
        shadowLayout_select = findViewById(R.id.shadowLayout_select);
        shadowLayout_bindView = findViewById(R.id.shadowLayout_bindView);

        ShadowLayout_image.setSelected(true);
        ShadowLayout_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShadowLayout_image.setSelected(ShadowLayout_image.isSelected());
            }
        });
        shadowLayout_bar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        shadowLayout_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shadowLayout_select.setSelected(shadowLayout_select.isSelected());
            }
        });
        shadowLayout_bindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shadowLayout_bindView.setSelected(shadowLayout_bindView.isSelected());
            }
        });
    }
}
