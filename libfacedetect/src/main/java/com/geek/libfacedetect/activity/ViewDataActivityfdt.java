package com.geek.libfacedetect.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.adapter.UserAdapter;
import com.geek.libfacedetect.db.DatabaseHelper;
import com.geek.libfacedetect.db.UserInfo;

import java.util.List;

import me.jessyan.autosize.AutoSizeCompat;

public class ViewDataActivityfdt extends AppCompatActivity {

    @Override
    public Resources getResources() {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
        if (Looper.myLooper()==Looper.getMainLooper()){
            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
            AutoSizeCompat.autoConvertDensity((super.getResources()), 667, false);//如果有自定义需求就用这个方法
        }
        return super.getResources();
    }

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
