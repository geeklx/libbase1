package com.geek.libprogressbar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libprogressbar.numberprogressbar.CircleProgressBar;

public class NumberActivity1 extends AppCompatActivity {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number1);
        final EnhancedProgressBar progress_barss1 = (EnhancedProgressBar) findViewById(R.id.epb1);
        final EnhancedProgressBar progress_barss2 = (EnhancedProgressBar) findViewById(R.id.epb2);
        final EnhancedProgressBar progress_barss3 = (EnhancedProgressBar) findViewById(R.id.epb3);
        final EnhancedProgressBar progress_barss4 = (EnhancedProgressBar) findViewById(R.id.epb4);
        final EnhancedProgressBar progress_barss5 = (EnhancedProgressBar) findViewById(R.id.epb5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 100) {
                    if (count == 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count += 2;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_barss1.setProgress(count);
                            progress_barss2.setProgress(count);
                            progress_barss3.setProgress(count);
                            progress_barss4.setProgress(count);
                            progress_barss5.setProgress(count);
                        }
                    });
                }
            }
        }).start();
    }
}
