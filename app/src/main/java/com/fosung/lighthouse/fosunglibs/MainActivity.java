package com.fosung.lighthouse.fosunglibs;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libshadowlayout.ShadowMainActivity;
import com.vincent.videocompressor.activity.VideoComPressorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent3 = new Intent(this, VideoComPressorActivity.class);
        startActivity(intent3);
//        Intent intent3 = new Intent(this, ShadowMainActivity.class);
//        startActivity(intent3);
//        new PgyerSDKManager.Init()
//                .setContext(getApplicationContext()) //设置上下问对象
//                .start();
//        startActivity(new Intent(MainActivity.this, ScanAct2.class));


    }
}