package com.geek.libfacedetect.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import tech.huqi.smartopencv.SmartOpenCV;
import tech.huqi.smartopencv.core.preview.CameraConfiguration;

public class DetectActivity22 extends AppCompatActivity implements
        CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {
    public final static int FLAG_REGISTER = 1;
    public final static int FLAG_VERIFY = 2;
    private CameraBridgeViewBase cameraView;
    private CascadeClassifier classifier;
    private CascadeClassifier mJavaDetector, mNoseDetector;
    private Mat mGray;
    private Mat mRgba;
    private int mAbsoluteFaceSize = 0;
    private boolean isFrontCamera = true;
    //
    List<UserInfo> userList;
    private Bitmap mDetectedFace;
    private FaceMatcher matcher;
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
                            intent = new Intent(DetectActivity22.this,
                                    RegisterActivityfdt.class);
                            intent.putExtra("Face", "bitmap");
                            BitmapUtil.saveBitmap2file(mDetectedFace, "bitmap");
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
                        //
                        Mat testMat = new Mat();
                        Utils.bitmapToMat(mDetectedFace, testMat);
                        Log.e("sssssssssssss-人脸矩阵传值前", testMat.width() + "," + testMat.height());
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

    // 手动装载openCV库文件，以保证手机无需安装OpenCV Manager
    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindowSettings();
        setContentView(R.layout.activty_detectfacedetector22);
        cameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        cameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        cameraView.setCvCameraViewListener(this); // 设置相机监听
        initClassifier();
        cameraView.enableView();
        Button switchCamera = (Button) findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this); // 切换相机镜头，默认后置
        //
        SmartOpenCV.getInstance().init(cameraView, new CameraConfiguration.Builder()
                .debug(true)
                .cameraIndex(1)      // 设置摄像头索引,主要用于多摄像头设备，优先级低于frontCamera
                .keepScreenOn(false) // 是否保持屏幕常亮
                .frontCamera(true)   // 是否使用前置摄像头
                .openCvDefaultDrawStrategy(false)      // 是否使用OpenCV默认的预览图像绘制策略
                .openCvDefaultPreviewCalculator(false) // 是否使用OpenCV默认的预览帧大小计算策略
                .landscape(false)     // 是否横屏显示
                .enableFpsMeter(false) // 开启预览帧率的显示
                .usbCamera(false)     // 是否使用USB摄像头，当设备接入的是USB摄像头时将其设置为true
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置预览帧图像格式
                .maxFrameSize(400, 320)     // 设置预览帧的最大大小
                .cvCameraViewListener(this) // 设置OpenCV回调监听器
//                .previewSizeCalculator(new IPreviewSizeCalculator() { // 自定义预览帧大小计算策略
//                    @Override
//                    public Size calculateCameraFrameSize(List<Size> supportedSizes, int surfaceWidth, int surfaceHeight) {
//                        // 若需要根据自己的具体业务场景改写览帧大小，覆写该方法逻辑
//                        return new Size(1080, 1920);
//                    }
//                })
//                .drawStrategy(new IDrawStrategy() { // 自定义绘制策略
//                    @Override
//                    public void drawBitmap(Canvas canvas, Bitmap frameBitmap, int surfaceWidth, int surfaceHeight) {
//                        // 若需根据自己的具体业务场景绘制预览帧图像，覆写该方法逻辑
//
//                    }
//                })
                .build());
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
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
//            classifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
            //
            classifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (classifier.empty()) {
                classifier = null;
            }
            cascadeDir.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        cameraView.enableView();
        DatabaseHelper helper = new DatabaseHelper(DetectActivity22.this);
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
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
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
//            Log.e("sssssssssssss", faceRect.height + "");
            if (faceRect.height > 500 && faceRect.height < 600) {
                // 获取并利用message传递当前检测的人脸
                Imgproc.rectangle(mRgba, faceRect.tl(), faceRect.br(), FACE_RECT_COLOR2, 3);
                Mat faceMat = new Mat(mRgba, faceRect);
                Log.e("sssssssssssss-获取人脸矩阵", faceMat.width() + "," + faceMat.height());
                Imgproc.resize(faceMat, faceMat, new Size(matcher.width, matcher.height));
//                Imgproc.resize(faceMat, faceMat, new Size(faceMat.width(), faceMat.height()));
                Bitmap bitmap = Bitmap.createBitmap(faceMat.width(),
                        faceMat.height(), Bitmap.Config.ARGB_8888);
//                Log.e("ssssssssssssbitmap", bitmap.getByteCount() + "");
                Utils.matToBitmap(faceMat, bitmap);
                Message message = Message.obtain();
                message.what = getIntent().getIntExtra("flag", 0);
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }
        //new2
//        mRgba = inputFrame.rgba(); //RGBA
//        mGray = inputFrame.gray(); //单通道灰度图
//        //检测并显示
//        MatOfRect frontalFaces = new MatOfRect();
//        MatOfRect profileFaces = new MatOfRect();
//
//        if (classifier != null) {//这里2个 Size 是用于检测人脸的，越小，检测距离越远，1.1, 5, 2, m65Size, mDefault着四个参数可以提高检测的准确率，5表示确认五次，具体百度 detectMultiScale 这个方法
//            classifier.detectMultiScale(mGray, frontalFaces, 1.1, 5, 2, m65Size, mDefault);
//            mFrontalFacesArray = frontalFaces.toArray();
//            mFronFacesSize = mFrontalFacesArray.length;
//            //如果正脸数大于0就绘制框框
//            if (mFronFacesSize > 0) {
//                Log.i("TAG", "正脸人数为 : " + mFrontalFacesArray.length);
//                for (int i = 0; i < mFrontalFacesArray.length; i++) {    //用框标记
//                    Imgproc.rectangle(mRgba, mFrontalFacesArray[i].tl(), mFrontalFacesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
//                    Log.d("caifeng", "绘制正脸框框");
//                }
//            }
//        }


        return mRgba;
    }


    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    private static final Scalar FACE_RECT_COLOR2 = new Scalar(255, 255, 255, 255);


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
