package com.geek.libgoodview.livegoodview.liveview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.libgoodview.R;
import com.geek.libgoodview.livegoodview.liveview.adapter.RecyclerAdapter;

/**
 * @ClassName: ListActivity
 * @Author: KaiSenGao
 * @CreateDate: 8/5/21 11:06 PM
 * @Description: 列表模式
 */
public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_recycler);
        // InitView
        this.initView();
    }

    /**
     * InitView
     */
    private void initView() {
        this.mRecyclerView = findViewById(R.id.recycler);
        // Init Adapter
        this.initAdapter();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        RecyclerAdapter adapter = new RecyclerAdapter();
        // Recycler
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setAdapter(adapter);
    }
}