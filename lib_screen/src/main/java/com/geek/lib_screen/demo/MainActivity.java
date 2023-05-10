package com.geek.lib_screen.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.lib_screen.R;
import com.geek.lib_screen.entity.RemoteItem;
import com.geek.lib_screen.manager.ClingManager;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button button1;
    RecyclerView recyclerView;
    //    String  url1="http://hc.yinyuetai.com/uploads/videos/common/44E4016521C693F23F7E9344AEBF5AF0.mp4?sc=5c4d956adf76a722&br=781&vid=3266995&aid=35&area=ML&vst=0";
//    String  url1="https://v.dtdjzx.gov.cn/dyjy/video/normal/course/3294447508898817.mp4";
//    String  url1="https://v.dyjyzyk.dtdjzx.gov.cn/resource-oss/transcode/20230410/3610116072822287628/hls/1500/3610116782196538001.m3u8";
    String url1 = "https://v.dtdjzx.gov.cn/dyjy/video/normal/course/3324516597728256.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        recyclerView = findViewById(R.id.recycler_view);
        ClingManager.getInstance().startClingService();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceListActivity1.startSelf(MainActivity.this);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteItem itemurl1 = new RemoteItem("一路之下", "425703", "张杰",
                        107362668, "01:20:50", "1920x1068", url1);
//                        107362668, "01:20:50", "1280x720", url1);
                ClingManager.getInstance().setRemoteItem(itemurl1);
                MediaPlayActivity1.startSelf(MainActivity.this);
            }
        });
    }
}
