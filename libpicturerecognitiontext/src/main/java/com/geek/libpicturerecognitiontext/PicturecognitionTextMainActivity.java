package com.geek.libpicturerecognitiontext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 源库地址 implementation 'com.rmtheis:tess-two:9.1.0'
 */
public class PicturecognitionTextMainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button, button1;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picturecognition_text_main);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            startActivity(new Intent(getPackageName() + ".hs.act.slbapp.PicturecognitionTextActivity"));
        } else if (id == R.id.button1) {
//            startActivity(new Intent(getPackageName() + ".hs.act.slbapp.PicturecognitionTextActivity1"));
        }
    }
}
