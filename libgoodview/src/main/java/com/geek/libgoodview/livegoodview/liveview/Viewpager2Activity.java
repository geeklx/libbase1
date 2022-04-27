package com.geek.libgoodview.livegoodview.liveview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.geek.libgoodview.R;
import com.geek.libgoodview.livegoodview.liveview.adapter.Viewpager2Adapter;

/**
 * @ClassName: Viewpager2Activity
 * @Author: KaiSenGao
 * @CreateDate: 8/5/21 10:25 PM
 * @Description: Viewpager2 列表模式
 */
public class Viewpager2Activity extends AppCompatActivity {

    private ViewPager2 mViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_viewpager2);
        // InitView
        this.initView();
    }

    /**
     * InitView
     */
    private void initView() {
        this.mViewPager2 = findViewById(R.id.viewPager2);
        // Init Adapter
        this.initAdapter();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        Viewpager2Adapter adapter = new Viewpager2Adapter();
        // ViewPager
        this.mViewPager2.setAdapter(adapter);
    }
}