/*
 * Copyright (C) Jenly, MLKit Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.geek.libmlkitscanner.new60;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.geek.libmlkitscanner.R;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.king.mlkit.vision.barcode.BarcodeDecoder;
import com.king.mlkit.vision.barcode.ViewfinderView;
import com.king.mlkit.vision.barcode.analyze.BarcodeScanningAnalyzer;
import com.king.mlkit.vision.barcode.utils.PointUtils;
import com.king.mlkit.vision.camera.AnalyzeResult;
import com.king.mlkit.vision.camera.CameraScan;
import com.king.mlkit.vision.camera.analyze.Analyzer;
import com.king.mlkit.vision.camera.config.ResolutionCameraConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class QRCodeCameraScanActivity1 extends BaseCameraScanActivity1<List<Barcode>> {

    private static WeakReference<QRCodeCameraScanActivity1> sActivityRef;

    public static final int REQUEST_CODE_PHOTO = 11111;
    public static final int REQUEST_CODE_REQUEST_EXTERNAL_STORAGE = 22222;
    public static final int REQUEST_CODE_SCAN_CODE = 33333;

    protected ViewfinderView viewfinderView;
    protected View fakeStatusBar;
    protected View ivFlashlight;
    protected TextView ivFlashlight2;
    protected ImageView ivResult;
    protected LinearLayout btn_close;
    protected LinearLayout btn_photo;
    protected LinearLayout ll_scan_history;


    public int getLayoutId() {
        return R.layout.qrcode_scan_activity;
    }

    @Override
    public void onBackPressed() {
        if (viewfinderView.isShowPoints()) {//如果是结果点显示时，用户点击了返回键，则认为是取消选择当前结果，重新开始扫码
            ivResult.setImageResource(0);
            viewfinderView.showScanner();
            getCameraScan().setAnalyzeImage(true);
            return;
        }
        super.onBackPressed();

    }

    @Override
    public void initUI() {
        //
        sActivityRef = new WeakReference<>(this);
        //
        viewfinderView = findViewById(R.id.viewfinderView);
        fakeStatusBar = findViewById(R.id.fakeStatusBar);
        ivFlashlight = findViewById(R.id.ivFlashlight);
        ivFlashlight2 = findViewById(R.id.tv_scan_light);
        ivResult = findViewById(R.id.ivResult);
        btn_close = findViewById(R.id.btn_close);
        btn_photo = findViewById(R.id.btn_photo);
        ll_scan_history = findViewById(R.id.ll_scan_history);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeScanPage();
            }
        });
//        btn_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startPickPhoto();
//            }
//        });
        ivFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFlashlight();
            }
        });
        super.initUI();
        initStatusBar();
    }


    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtil1.setTransparentForWindow(this);
            int statusBarHeight = StatusBarUtil1.getStatusBarHeight(sActivityRef.get());
            Log.e("======", "statusBarHeight--" + statusBarHeight);
            ViewGroup.LayoutParams fakeStatusBarLayoutParams = fakeStatusBar.getLayoutParams();
            fakeStatusBarLayoutParams.height = statusBarHeight;
            fakeStatusBar.setLayoutParams(fakeStatusBarLayoutParams);
            //状态栏文字颜色
            if (false) {
                StatusBarUtil1.setDarkMode(this);
            }
            //状态栏颜色
            String statusBarColor = "#00000000";
            fakeStatusBar.setBackgroundColor(Color.parseColor(statusBarColor));
        } else {
            ViewGroup.LayoutParams fakeStatusBarLayoutParams = fakeStatusBar.getLayoutParams();
            fakeStatusBarLayoutParams.height = 0;
            fakeStatusBar.setLayoutParams(fakeStatusBarLayoutParams);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PHOTO) {
                processPhoto(data);
            }
            if (requestCode == REQUEST_CODE_SCAN_CODE) {
                processScanResult(data);
            }
        }
    }

    private void finishSuccess(String result) {
        ArrayList<String> results = new ArrayList<>();
        results.add(result);
        finishSuccess(results);
    }

    private void finishSuccess(ArrayList<String> results) {
        Intent intent = new Intent();
        intent.putExtra(ScanManager1.INTENT_KEY_RESULT_SUCCESS, results);
        setResult(ScanManager1.RESULT_SUCCESS, intent);
        finishFinal();
    }

    private void processScanResult(Intent data) {
        String text = CameraScan.parseScanResult(data);
//        Toast.makeText(QRCodeCameraScanActivity1.this, text, Toast.LENGTH_SHORT).show();
        finishSuccess(text);
    }

    private void setresultdata(List<Barcode> result) {
        ArrayList<String> results1 = new ArrayList<>();
        for (Barcode barcode : result) {
            String value = barcode.getRawValue();
            Log.e("======QRCodeAct1", "value:" + value);
            results1.add(value);
        }
        finishSuccess(results1);
    }

    private void processPhoto(Intent data) {
        try {
            Bitmap src = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            BarcodeDecoder.process(src, new Analyzer.OnAnalyzeListener<List<Barcode>>() {
                @Override
                public void onSuccess(@NonNull List<Barcode> result) {
                    if (result != null && result.size() > 0) {
//                        String aaa = result.get(0).getDisplayValue();
//                        Bitmap src2 = src;
                        //
//                        Toast.makeText(QRCodeCameraScanActivity1.this, aaa, Toast.LENGTH_LONG).show();
                        setresultdata(result);
                    } else {
                        Log.d("processPhoto", "result is null");
                        Toast.makeText(QRCodeCameraScanActivity1.this, "result is null", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure() {
                    Log.d("processPhoto", "onFailure");
                    Toast.makeText(QRCodeCameraScanActivity1.this, "onFailure", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(QRCodeCameraScanActivity1.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void getImageFromAlbum() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, REQUEST_CODE_PHOTO);
    }

    public static void startPickPhoto() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            sActivityRef.get().getImageFromAlbum();
        }
    }

    /**
     * 点击手电筒
     */
    public static void onClickFlashlight() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            sActivityRef.get().toggleTorchState();
        }
    }

    /**
     * 切换闪光灯状态（开启/关闭）
     */
    protected void toggleTorchState() {
        if (getCameraScan() != null) {
            boolean isTorch = getCameraScan().isTorchEnabled();
            getCameraScan().enableTorch(!isTorch);
            if (ivFlashlight != null) {
                ivFlashlight.setSelected(!isTorch);
                if (isTorch) {
                    ivFlashlight2.setText("打开手电筒");
                } else {
                    ivFlashlight2.setText("关闭手电筒");
                }
            }
        }
    }

    /**
     * 关闭当前Activity
     */
    public static void closeScanPage() {
        if (sActivityRef != null && sActivityRef.get() != null) {
            sActivityRef.get().finishCancle();
        }
    }

    private void finishCancle() {
        setResult(ScanManager1.RESULT_CANCLE, new Intent());
        finishFinal();
    }

    private void finishFinal() {
        sActivityRef = null;
        finish();
        overridePendingTransition(0, R.anim.anim_bottom_out);
    }

    @Override
    public void onScanResultCallback(@NonNull AnalyzeResult<List<Barcode>> result) {
        getCameraScan().setAnalyzeImage(false);
        List<Barcode> results = result.getResult();
        //取预览当前帧图片并显示，为结果点提供参照
        ivResult.setImageBitmap(previewView.getBitmap());
        List<Point> points = new ArrayList<Point>();
        for (Barcode barcode : results) {
            //将实际的结果中心点坐标转换成界面预览的坐标
            Point point = PointUtils.transform(barcode.getBoundingBox().centerX(), barcode.getBoundingBox().centerY(), result.getBitmap().getWidth(), result.getBitmap().getHeight(), viewfinderView.getWidth(), viewfinderView.getHeight());
            points.add(point);
        }
        //设置Item点击监听
        viewfinderView.setOnItemClickListener(new ViewfinderView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //显示点击Item将所在位置扫码识别的结果返回
//                Intent intent = new Intent();
//                intent.putExtra(CameraScan.SCAN_RESULT, results.get(position).getDisplayValue());
//                setResult(RESULT_OK, intent);
//                finish();
                List<Barcode> results11 = new ArrayList<>();
                results11.add(results.get(position));
                setresultdata(results11);

            /*
                显示结果后，如果需要继续扫码，则可以继续分析图像
             */
//            ivResult.setImageResource(0)
//            viewfinderView.showScanner()
//            cameraScan.setAnalyzeImage(true)
            }
        });
        //显示结果点信息
        viewfinderView.showResultPoints(points);

        if (results.size() == 1) {//只有一个结果直接返回
//            Intent intent = new Intent();
//            intent.putExtra(CameraScan.SCAN_RESULT, results.get(0).getDisplayValue());
//            setResult(RESULT_OK, intent);
//            finish();
            setresultdata(results);
        }

    }


    @Override
    public void onScanResultFailure() {
        super.onScanResultFailure();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);

    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();
        getCameraScan().setPlayBeep(true).setVibrate(true).setCameraConfig(new ResolutionCameraConfig1(this));//设置CameraConfig
    }


    /**
     * 创建分析器，默认分析所有条码格式
     *
     * @return
     */
    @Nullable
    @Override
    public Analyzer<List<Barcode>> createAnalyzer() {
        return new BarcodeScanningAnalyzer(Barcode.FORMAT_ALL_FORMATS);
    }
}
