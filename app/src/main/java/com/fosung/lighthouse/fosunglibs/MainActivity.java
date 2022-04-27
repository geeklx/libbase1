package com.fosung.lighthouse.fosunglibs;

import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent3 = new Intent(this, ShadowMainActivity.class);
//        startActivity(intent3);
//        new PgyerSDKManager.Init()
//                .setContext(getApplicationContext()) //设置上下问对象
//                .start();
//        startActivity(new Intent(MainActivity.this, ScanAct2.class));
        tv1 = findViewById(R.id.tv1);
        String strBase64 = Base64.encodeToString("梁肖".getBytes(), Base64.DEFAULT);// 编码
//        tv1.setText(EncodeUtils.base64Encode2String("梁肖".getBytes()));
        String str2 = new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));// 解码
        tv1.setText(strBase64 + "," + str2);

    }


}