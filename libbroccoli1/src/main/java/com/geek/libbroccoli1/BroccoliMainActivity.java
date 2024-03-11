package com.geek.libbroccoli1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BroccoliMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_broccoli);
    }

    public void startLayoutSample(View view) {
        startActivity(new Intent(this, LayoutSimpleSampleActivity.class));
    }

    public void startRecyclerViewSample(View view) {
        startActivity(new Intent(this, RecyclerViewSampleActivity.class));
    }

    public void startAdvancedLayoutSample(View view) {
        startActivity(new Intent(this, LayoutAdvancedSampleActivity.class));
    }

    public void startDingDingSample(View view) {
        startActivity(new Intent(this, DingDingSampleActivity.class));
    }
}
