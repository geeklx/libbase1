package com.geek.libnsfw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libnsfw.javas.NsfwAct2;
import com.geek.libnsfw.ktl.NsfwMainActivity;

/**
 * 黄图识别
 * com.github.geeklx.libbase1:libnsfw:2.0.6
 */
public class NsfwAct extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsfw);
        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NsfwAct.this, NsfwAct2.class));
            }
        });
        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NsfwAct.this, NsfwMainActivity.class));
            }
        });
    }
}
