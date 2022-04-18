package com.geek.libfacedetect.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.adapter.UserAdapter;
import com.geek.libfacedetect.db.DatabaseHelper;
import com.geek.libfacedetect.db.UserInfo;

import java.util.List;

public class ViewDataActivityfdt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_datafacedetector);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DatabaseHelper helper = new DatabaseHelper(this);
        List<UserInfo> users = helper.query();
        helper.close();
        recyclerView.setAdapter(new UserAdapter(users));
    }
}
