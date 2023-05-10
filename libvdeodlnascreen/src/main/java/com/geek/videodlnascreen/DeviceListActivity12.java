package com.geek.videodlnascreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yanbo.lib_screen.entity.ClingDevice;
import com.yanbo.lib_screen.entity.ProjectionState;
import com.yanbo.lib_screen.entity.RemoteItem1;
import com.yanbo.lib_screen.event.DeviceEvent;
import com.yanbo.lib_screen.listener.ItemClickListener;
import com.yanbo.lib_screen.manager.ClingManager;
import com.yanbo.lib_screen.manager.DeviceManager;
import com.yanbo.lib_screen.mmkv.MmkvUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 描述：
 *
 * @author Yanbo
 * @date 2018/11/6
 */
public class DeviceListActivity12 extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    TextView tvSearch;
    LinearLayout llJingzhitoup;
    private LinearLayoutManager layoutManager;
    private ClingDeviceAdapter adapter;
    private List<ClingDevice> clingDevices;

    public static void startSelf(Activity context) {
        Intent intent = new Intent(context, DeviceListActivity12.class);
        context.startActivity(intent);
    }

    String url2 = "https://v.dtdjzx.gov.cn/dyjy/video/normal/course/3294447508898817.mp4";
    String url3 = "https://v.dyjyzyk.dtdjzx.gov.cn/resource-oss/transcode/20230410/3610116072822287628/hls/1500/3610116782196538001.m3u8";
    String url1 = "https://v.dtdjzx.gov.cn/dyjy/video/normal/course/3324516597728256.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_device_list);
        recyclerView = findViewById(R.id.recycler_view);
        tvSearch = findViewById(R.id.tv_search);
        llJingzhitoup = findViewById(R.id.ll_jingzhitoup);
        imageView = findViewById(R.id.imageView);
        showloading();
        layoutManager = new LinearLayoutManager(this);
        adapter = new ClingDeviceAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        llJingzhitoup.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemAction(int action, Object object) {
                ClingDevice device = clingDevices.get(action);
                DeviceManager.getInstance().setCurrClingDevice(device);
//                Toast.makeText(getBaseContext(), "选择了设备 " + device.getDevice().getDetails().getFriendlyName(), Toast.LENGTH_LONG).show();
                RemoteItem1 itemurl1 = new RemoteItem1("山东革命老区山东临沂山东革命老区山东临沂", "425703", "张杰",
                        107362668, "01:20:50", "1920x1068", url1, url2, url3);
                ClingManager.getInstance().setRemoteItem(itemurl1);
                ProjectionState projectionState = new ProjectionState(url1, "gq", false);
                MmkvUtils.getInstance().set_common_json("projectionState", JSON.toJSONString(projectionState), ProjectionState.class);
                MediaPlayActivity1.startSelf(DeviceListActivity12.this);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showloading();
                tvSearch.setText("正在搜索设备...");

            }
        });
    }

    public void showloading() {
        //加载动画
        loading();
        imageView.startAnimation(animationSet);

        //结束动画
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.clearAnimation();
                refresh();
            }
        }, 2000);
    }

    private AnimationSet animationSet;

    public void loading() {
        animationSet = new AnimationSet(true);
        RotateAnimation animation_rotate = new RotateAnimation(0, +359,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //第一个参数fromDegrees为动画起始时的旋转角度 //第二个参数toDegrees为动画旋转到的角度
        //第三个参数pivotXType为动画在X轴相对于物件位置类型 //第四个参数pivotXValue为动画相对于物件的X坐标的开始位置
        //第五个参数pivotXType为动画在Y轴相对于物件位置类型 //第六个参数pivotYValue为动画相对于物件的Y坐标的开始位置
        animation_rotate.setRepeatCount(-1);
        animation_rotate.setStartOffset(0);
        animation_rotate.setDuration(1000);
        LinearInterpolator lir = new LinearInterpolator();
        animationSet.setInterpolator(lir);
        animationSet.addAnimation(animation_rotate);
    }

    public void refresh() {
        if (adapter == null) {
            return;
        }
        clingDevices = DeviceManager.getInstance().getClingDeviceList();
        if (clingDevices != null && clingDevices.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            llJingzhitoup.setVisibility(View.GONE);
            adapter.setNewdata(clingDevices);
//            adapter.setNewData(clingDevices);
            tvSearch.setText("请选择投屏设备");
        } else {
            recyclerView.setVisibility(View.GONE);
            llJingzhitoup.setVisibility(View.VISIBLE);
            tvSearch.setText("未找到设备");
            adapter.refresh();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(DeviceEvent event) {
        refresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
