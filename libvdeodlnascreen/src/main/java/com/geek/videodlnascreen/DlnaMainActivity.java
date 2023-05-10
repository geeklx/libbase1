package com.geek.videodlnascreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.yanbo.lib_screen.callback.ControlCallback;
import com.yanbo.lib_screen.manager.ClingManager;
import com.yanbo.lib_screen.manager.ControlManager;

public class DlnaMainActivity extends AppCompatActivity {
    Button button;
    Button button1;
    Button button2;
    RecyclerView recyclerView;
    //    String  url1="http://hc.yinyuetai.com/uploads/videos/common/44E4016521C693F23F7E9344AEBF5AF0.mp4?sc=5c4d956adf76a722&br=781&vid=3266995&aid=35&area=ML&vst=0";
    String  url2="https://v.dtdjzx.gov.cn/dyjy/video/normal/course/3294447508898817.mp4";
    String  url3="https://v.dyjyzyk.dtdjzx.gov.cn/resource-oss/transcode/20230410/3610116072822287628/hls/1500/3610116782196538001.m3u8";
    String url1 = "https://v.dtdjzx.gov.cn/dyjy/video/normal/course/3324516597728256.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dlna);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        recyclerView = findViewById(R.id.recycler_view);
        ClingManager.getInstance().startClingService();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DlnaMainActivity.this, DeviceListActivity12.class));
//                DeviceListActivity1.startSelf(DlnaMainActivity.this);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RemoteItem1 itemurl1 = new RemoteItem1("一路之下", "425703", "张杰",
//                        107362668, "01:20:50", "1920x1068", url1,url2,url3);
////                        107362668, "01:20:50", "1280x720", url1);
//                ClingManager.getInstance().setRemoteItem(itemurl1);
                startActivity(new Intent(DlnaMainActivity.this, MediaPlayActivity1.class));
//                MediaPlayActivity1.startSelf(DlnaMainActivity.this);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

    }
    private void stop() {
        ControlManager.getInstance().unInitScreenCastCallback();
        stopCast();
    }
    private  void stopCast() {
        ControlManager.getInstance().stopCast(new ControlCallback() {
            @Override
            public void onSuccess() {
                ControlManager.getInstance().setState(ControlManager.CastState.STOPED);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
//                showToast(String.format("停止播放失败 %s", msg));
            }
        });
    }
}
