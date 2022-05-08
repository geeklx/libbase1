package com.fosung.lighthouse.fosunglibs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.hubert.guide.newbieguide.FirstActivity;
import com.example.slbyanzheng.ZhiwenActtivity;
import com.geek.libnsfw.NsfwAct;
import com.geek.libocr.ScanAct1;
import com.geek.libpicturecompressor.PictureCompressorActivity;
import com.geek.libshadowlayout.ShadowMainActivity;
import com.vincent.videocompressor.activity.VideoComPressorActivity;

/**
 * @author houjie
 * @date 2022/4/13
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button, button1, button2, button3, button4, button5, button6, button7, button8, button9,
            button10, button11, button12, button13,button14;

    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.button14);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
//        new PgyerSDKManager.Init()
//                .setContext(getApplicationContext()) //设置上下问对象
//                .start();
//        startActivity(new Intent(MainActivity.this, ScanAct2.class));
//        String strBase64 = Base64.encodeToString("梁肖".getBytes(), Base64.DEFAULT);// 编码
////        tv1.setText(EncodeUtils.base64Encode2String("梁肖".getBytes()));
//        String str2 = new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));// 解码
//        tv1.setText(strBase64 + "," + str2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                /*黄图识别*/
                Intent intent = new Intent(this, NsfwAct.class);
                startActivity(intent);
                break;
            case R.id.button1:
                /*ocr识别*/
                Intent intent1 = new Intent(this, ScanAct1.class);
                startActivity(intent1);
                break;
            case R.id.button2:
                /*新手引导页*/
                Intent intent2 = new Intent(this, FirstActivity.class);
                startActivity(intent2);
                break;
            case R.id.button3:
                /*视频压缩*/
                Intent intent3 = new Intent(this, VideoComPressorActivity.class);
                startActivity(intent3);
                break;
            case R.id.button4:
                /*shadow阴影的各项使用*/
                Intent intent4 = new Intent(this, ShadowMainActivity.class);
                startActivity(intent4);
                break;
            case R.id.button5:
                /*手势指纹验证库*/
                Intent intent5 = new Intent(this, ZhiwenActtivity.class);
                startActivity(intent5);
                break;
            case R.id.button6:
                /*图片压缩库*/
                Intent intent6 = new Intent(MainActivity.this, PictureCompressorActivity.class);
                startActivity(intent6);
                break;
            case R.id.button7:
                /*通用自定义textview*/
//                Intent intent7 = new Intent(this, SuperTextviewActivity.class);
//                startActivity(intent7);
                break;
            case R.id.button8:
                /*Android点赞+1效果*/
//                Intent intent8 = new Intent(this, GoodViewActivity.class);
//                startActivity(intent8);
                break;
            case R.id.button9:
                /*Android仿头条点赞效果*/
//                Intent intent9 = new Intent(this, BlastActivity.class);
//                startActivity(intent9);
                break;
            case R.id.button10:
                /*直播点赞效果*/
//                Intent intent10 = new Intent(this, LiveActivity.class);
//                startActivity(intent10);
            case R.id.button11:
                /*人脸识别*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.MainActivityfdt"));
                break;
            case R.id.button12:
                /*Viewpager+Bottomsheet功能*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.MainActivitySheet"));
                break;
            case R.id.button13:
                /*多种进度条样式功能*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.ProgressMainActivity"));
                break;
            case R.id.button14:
                /*图片识别文字*/
                startActivity(new Intent(getPackageName() + ".hs.act.slbapp.PicturecognitionTextActivity"));
                break;
            default:
                break;
        }
    }


}