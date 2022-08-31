package com.geek.libfacedetect.activity;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.adapter.FaceComparisonAdapter;
import com.geek.libfacedetect.util.ToastUtil;
import com.geek.libfacedetect.widgets.AbsCameraManager;
import com.geek.libfacedetect.widgets.FaceWrapperInfo;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 人脸验证
 */

public class FaceComparisonActivity extends AppCompatActivity {

    //人脸相关
    private TextView mIvBack, personal;
    private FrameLayout mFrameLayout;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;

    private TextureView rView;//用于标注人脸
    private TextView mTvInfo;
    private LinearLayout rootView;


    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean isStart = true;
    private String deviceCode;

    private Snackbar snackbar;

    private RecyclerView recyclerView;
    private FaceComparisonAdapter adapter;

//    @Override
//    public Resources getResources() {
//        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
//            AutoSizeCompat.autoConvertDensity((super.getResources()), 1080, false);//如果有自定义需求就用这个方法
//        }
//        return super.getResources();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_comparison);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initView();
        initListener();
    }

    private void initView() {
//        //方案2
//        if (CameraUtil.Companion.hasBackFacingCamera()) {
//            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
//        } else if (CameraUtil.Companion.hasFrontFacingCamera()) {
//            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
//        } else {
//            ToastUtil.toastLong("无摄像头");
//            finish();
//        }
        mFrameLayout = findViewById(R.id.navigation);
        recyclerView = findViewById(R.id.listView);
        rView = findViewById(R.id.rView);
        mPreview = findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                AbsCameraManager.getInstance().setPreviewView(mPreview);
                AbsCameraManager.getInstance().setrView(rView);
                AbsCameraManager.getInstance().startCapture();

                AbsCameraManager.getInstance().setCaptureListener(new AbsCameraManager.CaptureListener() {
                    @Override
                    public void onCaptureResult(byte[] data, ArrayList<FaceWrapperInfo> arrayList) {
                        if (!isStart) {
                            return;
                        }
                        Bitmap bitmap2 = nv21ToBitmap(data, AbsCameraManager.PWIDTH, AbsCameraManager.PHEIGHT);
                        Bitmap newBitmap = zoomImg(bitmap2, AbsCameraManager.PWIDTH, AbsCameraManager.PHEIGHT);
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/face.jpeg";
                        isStart = false;
                        try {
                            saveBitmapToImage(newBitmap, 50, Bitmap.CompressFormat.JPEG, path, true);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updaloadPic(path);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtil.showToast(FaceComparisonActivity.this, "图片存储失败", 3);
                            isStart = true;
                        }
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                AbsCameraManager.getInstance().stopCapture();
            }
        });
//        // FrameLayout
//        ViewGroup.LayoutParams framelayout_params =
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//        mFrameLayout.setLayoutParams(framelayout_params);
//        mHolder.setFixedSize(1440, 1080);
//        mHolder.setFixedSize(mFrameLayout.getWidth(), mFrameLayout.getHeight());
        //
        rView.setAlpha(0.9f);
        rootView = findViewById(R.id.rootview);
        snackbar = Snackbar.make(rootView, "错误", LENGTH_LONG);
        mIvBack = findViewById(R.id.detail_back);
        mTvInfo = findViewById(R.id.tv_info);
        personal = findViewById(R.id.personal);
        adapter = new FaceComparisonAdapter();
//        adapter.getDatas().add(new LifeFaceLogin.Data());
//        adapter.getDatas().add(new LifeFaceLogin.Data());
//        adapter.getDatas().add(new LifeFaceLogin.Data());
//        adapter.getDatas().add(new LifeFaceLogin.Data());
//        adapter.getDatas().add(new LifeFaceLogin.Data());
        recyclerView.setAdapter(adapter);
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AbsCameraManager.getInstance().startCapture();
                    }
                });
            }
        }, 500);
    }

    private void initListener() {
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    isStart = true;
                    AbsCameraManager.getInstance().startCapture();
                    mTvInfo.setVisibility(View.GONE);
                } catch (Exception e) {

                }
            }
        });
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
////        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
////        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        AbsCameraManager.getInstance().stopCapture();
//    }

    private Bitmap nv21ToBitmap(byte[] nv21, int width, int height) {
        Bitmap bitmap = null;
        try {
            YuvImage image = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 100, stream);
            bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public void saveBitmapToImage(Bitmap srcBip, int quality, Bitmap.CompressFormat format
            , String outPath, boolean recycle) throws IOException {
        if (TextUtils.isEmpty(outPath)) {
            throw new IOException("saveBitmapToImage pathName null or nil");
        }

        File file = new File(outPath);
        file.createNewFile();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            srcBip.compress(format, quality, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (recycle) {
                srcBip.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传图片去接口识别bufen
    private void updaloadPic(String path) {
//        HashMap<String, String> map = new HashMap();
//        HashMap<String, File> fileHashMap = new HashMap<>();
//        fileHashMap.put("file", new File(path));
//        ZHttp.uploadFile(Face_Upload, fileHashMap, new ZResponse<UploadPic>(UploadPic.class) {
//            @Override
//            public void onSuccess(Response response, UploadPic resObj) {
//                File file = new File(path);
//                file.deleteOnExit();
//                face_search(resObj.data.url);
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                super.onError(code, error);
//                isStart = true;
//
//            }
//        });
    }

    // 人脸对比接口
    private void face_search(String image) {
//        String authorizationValue = UtilSignature.buildRequestAuthorization(HttpUrlMaster.AC_KEY, HttpUrlMaster.AC_SIGNATURE,
//                "post", "/cloud_meeting/meeting/out/checkLoginFaceUrl");
//        LinkedHashMap headerParams = new LinkedHashMap<String, String>();
//        headerParams.put("Authorization", authorizationValue);
//        Values bean = new Values();
//        bean.imageUrl = image;
//        bean.orgCode = deviceCode;
//        bean.appType = "ai";
//
//        String json = GsonUtil.beanToString(bean);
//        ZHttp.postJsonWithHeader(HttpUrlMaster.FACESEARCH, headerParams, json, new ZResponse<LifeFaceLogin>(LifeFaceLogin.class) {
//            @Override
//            public void onSuccess(Response response, LifeFaceLogin resObj) {
////                mTvInfo.setText("");
////                String info = "";
////                info = info + "姓名：" + resObj.data.userName + "\r\n"
////                        + "组织名称：" + resObj.data.orgName + "\r\n";
////                if (TextUtils.isEmpty(info)) {
////                    if (mTvInfo.getText().toString().equals("未找到相关人员信息")) {
////
////                    } else {
////                        mTvInfo.setText("未找到相关人员信息");
////                    }
////                    isStart = true;
////                } else {
////                    mTvInfo.setText(info);
////                    AbsCameraManager.getInstance().stopCapture();
////                }
//
////                mTvInfo.setVisibility(View.VISIBLE);
//                if (resObj.data != null || adapter.getDatas().size() > 0) {
//                    recyclerView.setVisibility(View.VISIBLE);
//                }
//                boolean isHave = false;
//                for (LifeFaceLogin.Data p : adapter.getDatas()) {
//                    if (p.userId.equals(resObj.data.userId)) {
//                        isHave = true;
//                    }
//                }
//                if (!isHave) adapter.getDatas().add(0, resObj.data);
//                adapter.notifyDataSetChanged();
//                isStart = true;
//            }
//
//            @Override
//            public void onError(int code, String error) {
////              super.onError(code, error);
//                snackbar.setText(error);
//                snackbar.show();
//                isStart = true;
//
//            }
//        });
    }

    private class Values {
        public String imageUrl;
        public String orgCode;
        public String appType;
    }

}
