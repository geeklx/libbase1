package com.geek.libprogressbar.CircleProgress;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.geek.libprogressbar.R;
import com.white.progressview.CircleProgressView;

public class CircularProgressActivity extends AppCompatActivity {


    private CircularProgressBar progress, progress2, progress3, progress4;
    private int num = 0;
    private ProgressYuan progressYuan;
    private CircleProgressView circle_progress_normal;
    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_progress);

        progressYuan = (ProgressYuan) findViewById(R.id.view1);
        circle_progress_normal = (CircleProgressView) findViewById(R.id.circle_progress_normal);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        progress = (CircularProgressBar) findViewById(R.id.progress);
        progress.setProgress(30);
        progress.setText("进度");
        progress.setStartColor(getResources().getColor(R.color.colorAccent3));
        progress.setMidColor(getResources().getColor(R.color.colorAccent2));
        progress.setEndColor(getResources().getColor(R.color.colorAccent1));

        progress2 = (CircularProgressBar) findViewById(R.id.progress2);
        progress2.setProgress(40);
        progress2.setText("进度");

        progress3 = (CircularProgressBar) findViewById(R.id.progress3);
        progress3.setProgress(80);
        progress3.setText("警告" + progress3.getProgress() + "%");

        progress4 = (CircularProgressBar) findViewById(R.id.progress4);
        progress4.setProgress(80);
        progress4.setText("渐变" + progress4.getProgress() + "%");

        new MyThread().start();
        progress2();
    }

    private void progress2() {
        progressYuan.playMusic();
        int color = ContextCompat.getColor(this, R.color.blue);
        circle_progress_normal.setReachBarColor(color);
        circle_progress_normal.setNormalBarColor(adjustAlpha(color, 0.3f));
        circle_progress_normal.runProgressAnim(20000);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                num++;
                Message msg = new Message();
                msg.what = 0;
                msg.arg1 = num;
                mHandler.sendMessage(msg);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    progress2.setProgress(msg.arg1);
                    progress2.setText(msg.arg1 + "%");
                    if (msg.arg1 >= 100) {
                        num = 0;
                    }
                    break;
            }
        }
    };

    public void btn1(View view) {
        progressYuan.playMusic();
        if (btn1.getText().equals("开始")) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    btn1.setText("暂停");
                }
            });

        }
        if (btn1.getText().equals("暂停")) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    btn1.setText("开始");

                }
            });

        }
    }

    public void btn2(View view) {
        progressYuan.stopMusic();
    }
}
