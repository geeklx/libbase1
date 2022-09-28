package com.geek.libzxinglite.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.geek.libzxinglite.CaptureHelper;
import com.geek.libzxinglite.CaptureHelper1;
import com.geek.libzxinglite.OnCaptureCallback;
import com.geek.libzxinglite.R;
import com.geek.libzxinglite.ViewfinderView;
import com.geek.libzxinglite.ViewfinderViews;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义扫码：当直接使用CaptureActivity
 * 自定义扫码，切记自定义扫码需在{@link Activity}或者{@link Fragment}相对应的生命周期里面调用{@link #mCaptureHelper}对应的生命周期
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class CustomActivity1 extends AppCompatActivity implements OnCaptureCallback {

    private boolean isContinuousScan;

    private CaptureHelper1 mCaptureHelper;

    private SurfaceView surfaceView;

    private ViewfinderViews viewfinderView;

    private View ivTorch;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.custom1_activity);

        initUI();
    }

    private void initUI() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        StatusBarUtils.immersiveStatusBar(this, toolbar, 0.2f);
//        TextView tvTitle = findViewById(R.id.tvTitle);
//        tvTitle.setText(getIntent().getStringExtra(ZxingMainActivity.KEY_TITLE));


        surfaceView = findViewById(R.id.surfaceView);
        viewfinderView = findViewById(R.id.viewfinderView);
        ivTorch = findViewById(R.id.ivFlash);
        ivTorch.setVisibility(View.INVISIBLE);

        isContinuousScan = getIntent().getBooleanExtra(ZxingMainActivity.KEY_IS_CONTINUOUS, false);

        mCaptureHelper = new CaptureHelper1(this, surfaceView, viewfinderView, ivTorch);
        mCaptureHelper.setOnCaptureCallback(this);
        mCaptureHelper.onCreate();
        mCaptureHelper.playBeep(true)//是否开启音效
                .vibrate(true)//是否震动
                .fullScreenScan(true)//全屏扫码
//                .supportAutoZoom(false)//设置支持缩放

                .supportZoom(false)//设置支持缩放
                .supportVerticalCode(true)//支持扫垂直条码，建议有此需求时才使用。
                .supportLuminanceInvert(true)//是否支持识别反色码（黑白反色的码），增加识别率
                .continuousScan(isContinuousScan);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    /**
     * 扫码结果回调
     *
     * @param result 扫码结果
     * @return
     */
    @Override
    public boolean onResultCallback(String result) {
        if (isContinuousScan) {
            if (!TextUtils.isEmpty(result)) {
                String[] infoStr = result.split(",");
                if (infoStr.length > 4) {
                    List<String> wordList = Arrays.asList(infoStr);
                    String card = wordList.get(2);
                    String phone = wordList.get(3);
                    if (card != null && !"".equals(card)) {
                        Toast.makeText(this, card + "---" + phone, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "未识别到结果", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "未识别到结果", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "未识别到结果", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


    public void onClick(View v) {
        if (v.getId() == R.id.ivLeft) {
            onBackPressed();
        }
    }
}