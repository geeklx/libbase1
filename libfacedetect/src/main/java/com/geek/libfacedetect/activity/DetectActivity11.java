package com.geek.libfacedetect.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.db.DatabaseHelper;
import com.geek.libfacedetect.db.UserInfo;
import com.geek.libfacedetect.util.BitmapUtil;
import com.geek.libfacedetect.util.FaceMatcher;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase2;
import org.opencv.android.JavaCameraView2;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import me.jessyan.autosize.AutoSizeCompat;

public class DetectActivity11 extends AppCompatActivity implements
        JavaCameraView2.CvCameraViewListener2, View.OnClickListener {

    private Bitmap mDetectedFace;
    private FaceMatcher matcher;
    public final static int FLAG_REGISTER = 1;
    public final static int FLAG_VERIFY = 2;
    private JavaCameraView2 cameraView;
    private CascadeClassifier classifier;
    private Mat mGray;
    private Mat mRgba;
    private int mAbsoluteFaceSize = 0;
    private boolean isFrontCamera;
    private static final Scalar FACE_RECT_COLOR2 = new Scalar(255, 255, 255, 255);
    private List<UserInfo> userList;


    // 手动装载openCV库文件，以保证手机无需安装OpenCV Manager
    static {
        System.loadLibrary("opencv_java3");
    }

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
        initWindowSettings();
        setContentView(R.layout.activty_detectfacedetector11);
        cameraView = findViewById(R.id.camera_view);
        cameraView.setCvCameraViewListener(this); // 设置相机监听
        initClassifier();
        cameraView.enableView();
        Button switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this); // 切换相机镜头，默认后置
        //
        cameraView.disableView();
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        isFrontCamera = true;
        cameraView.enableView();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.switch_camera) {
            cameraView.disableView();
            if (isFrontCamera) {
                cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
                isFrontCamera = false;
            } else {
                cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
                isFrontCamera = true;
            }
            cameraView.enableView();
        }
    }

    // 初始化窗口设置, 包括全屏、横屏、常亮
    private void initWindowSettings() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    // 初始化人脸级联分类器，必须先初始化
    private void initClassifier() {
        try {
            InputStream is = getResources()
                    .openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            classifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatabaseHelper helper = new DatabaseHelper(DetectActivity11.this);
        userList = helper.query();
        matcher = new FaceMatcher(userList);
        helper.close();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }


    @Override
    // 这里执行人脸检测的逻辑, 根据OpenCV提供的例子实现(face-detection)
    public Mat onCameraFrame(CameraBridgeViewBase2.CvCameraViewFrame2 inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        // 翻转矩阵以适配前后置摄像头
        if (isFrontCamera) {
            Core.flip(mRgba, mRgba, 1);
            Core.flip(mGray, mGray, 1);
        } else {
            Core.flip(mRgba, mRgba, -1);
            Core.flip(mGray, mGray, -1);
        }
        float mRelativeFaceSize = 0.2f;
        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }
        MatOfRect faces = new MatOfRect();
        if (classifier != null) {
            classifier.detectMultiScale(mGray, faces, 1.1, 2, 2,
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        // new1
        Rect[] facesArray = faces.toArray();
//        Scalar faceRectColor = new Scalar(0, 255, 0, 255);
        for (Rect faceRect : facesArray) {
            Imgproc.rectangle(mRgba, faceRect.tl(), faceRect.br(), FACE_RECT_COLOR, 3);
//            Log.e("ssssssssssss", faceRect.height + "");
            if (faceRect.height > 200 && faceRect.height < 1024) {
                // 获取并利用message传递当前检测的人脸
//                Imgproc.rectangle(mRgba, faceRect.tl(), faceRect.br(), FACE_RECT_COLOR2, 3);
                Mat faceMat = new Mat(mRgba, faceRect);
                Log.e("ssssssssssss-change", faceMat.width() + "," + faceMat.height());
                try {
//                    Imgproc.resize(faceMat, faceMat, new Size(faceMat.width(), faceMat.height()));
                    Imgproc.resize(faceMat, faceMat, new Size(matcher.width, matcher.height));
                    Bitmap bitmap = Bitmap.createBitmap(faceMat.width(), faceMat.height(), Bitmap.Config.ARGB_8888);
//                    Log.e("ssssssssssss-bitmap", bitmap.getByteCount() + "");
                    Utils.matToBitmap(faceMat, bitmap);
                    Message message = Message.obtain();
                    message.what = getIntent().getIntExtra("flag", 0);
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Log.e("ssssssssssss-error", e.toString() + "");
                    e.printStackTrace();
                }
            }
        }
        return mRgba;
    }

    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case FLAG_REGISTER:
                    if (mDetectedFace == null) {
                        mDetectedFace = (Bitmap) msg.obj;
                        int result = matcher.histogramMatch(mDetectedFace);
                        if (result == matcher.UNFINISHED) {
                            mDetectedFace = null;
                        } else if (result == matcher.NO_MATCHER) {
//                            Log.e("ssssssssssssmDetectedFace", mDetectedFace.getByteCount() + "");
                            intent = new Intent(DetectActivity11.this,
                                    RegisterActivityfdt.class);
                            intent.putExtra("Face", "bitmap");
                            BitmapUtil.saveBitmap2file(mDetectedFace,"bitmap");
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    break;
                case FLAG_VERIFY:
                    if (mDetectedFace == null) {
                        mDetectedFace = (Bitmap) msg.obj;
                        int result = matcher.histogramMatch(mDetectedFace);
                        if (result == matcher.UNFINISHED) {
                            mDetectedFace = null;
                        } else if (result == matcher.NO_MATCHER) {
                            intent = new Intent();
                            setResult(RESULT_CANCELED, intent);
                            finish();
                        } else {
                            intent = new Intent();
                            intent.putExtra("USER_ID", result);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.disableView();
    }
}