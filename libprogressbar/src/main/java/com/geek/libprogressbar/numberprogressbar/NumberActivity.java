package com.geek.libprogressbar.numberprogressbar;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libprogressbar.R;

public class NumberActivity extends AppCompatActivity {

    private int count = 0;
    private ProgressBar progressBar;
    private UpperNumberProgressBar number_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        final NumberProgressBar progress_barss = (NumberProgressBar) findViewById(R.id.progress_barss);
        final NumberHorizontalProgressBar num_pb = (NumberHorizontalProgressBar) findViewById(R.id.num_pb);
        final NumberHorizontalProgressBar num_pb2 = (NumberHorizontalProgressBar) findViewById(R.id.num_pb2);
        final NumberHorizontalProgressBar num_pb3 = (NumberHorizontalProgressBar) findViewById(R.id.num_pb3);
        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        number_progress = (UpperNumberProgressBar) findViewById(R.id.number_progress);

        final CircleProgressBar cpb = (CircleProgressBar) findViewById(R.id.cpb);
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
                            progress_barss.setProgress(count);
                            num_pb.setProgress(count);
                            num_pb2.setProgress(count);
                            num_pb3.setProgress(count);
                            progressBar.setProgress(count);
                            number_progress.setProgress(count);
                            cpb.setProgress(count);
                        }
                    });
                }
            }
        }).start();
    }
}
