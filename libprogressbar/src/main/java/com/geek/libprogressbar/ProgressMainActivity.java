package com.geek.libprogressbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libprogressbar.CircleProgress.CircularProgressActivity;
import com.geek.libprogressbar.numberprogressbar.NumberActivity;

public class ProgressMainActivity extends AppCompatActivity {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_main);
        findViewById(R.id.tv_jindutiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProgressMainActivity.this, NumberActivity.class));
            }
        });
        findViewById(R.id.tv_yuanxingjindutiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProgressMainActivity.this, CircularProgressActivity.class));
            }
        });
        findViewById(R.id.tv_jindutiao1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProgressMainActivity.this, NumberActivity1.class));
            }
        });

    }
}
