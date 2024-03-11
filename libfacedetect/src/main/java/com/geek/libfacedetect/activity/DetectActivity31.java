package com.geek.libfacedetect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.geek.libfacedetect.R;
import com.geek.libfacedetect.db.DatabaseHelper;
import com.geek.libfacedetect.db.UserInfo;
import com.geek.libfacedetect.util.FaceMatcher;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase3;
import org.opencv.android.CameraBridgeViewBase3.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase3.CvCameraViewListener2;
import org.opencv.android.Utils;
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
import java.util.Collections;
import java.util.List;

import tech.huqi.smartopencv.SmartOpenCV;
import tech.huqi.smartopencv.core.preview.CameraConfiguration;

public class DetectActivity31 extends CameraActivity implements CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    public static final int JAVA_DETECTOR = 0;
    public static final int NATIVE_DETECTOR = 1;

    private MenuItem mItemFace50;
    private MenuItem mItemFace40;
    private MenuItem mItemFace30;
    private MenuItem mItemFace20;
    private MenuItem mItemType;

    private Mat mRgba;
    private Mat mGray;
    private File mCascadeFile;
    private CascadeClassifier classifier;
    private DetectionBasedTracker2 mNativeDetector;

    private int mDetectorType = NATIVE_DETECTOR;
    private String[] mDetectorName;

    private float mRelativeFaceSize = 0.2f;
    private int mAbsoluteFaceSize = 0;
    public final static int FLAG_REGISTER = 1;
    public final static int FLAG_VERIFY = 2;

    private Bitmap mDetectedFace;
    List<UserInfo> userList;
    private FaceMatcher matcher;

    private CameraBridgeViewBase3 mOpenCvCameraView;
//    private JavaCameraView mOpenCvCameraView;


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case FLAG_REGISTER:
                    if (mDetectedFace == null) {
                        mDetectedFace = (Bitmap) msg.obj;
                        int result = matcher.histogramMatch(mDetectedFace);
                        Log.e(TAG, mDetectedFace + "mHandler: " + result);
                        if (result == matcher.UNFINISHED) {
                            mDetectedFace = null;
                        } else if (result == matcher.NO_MATCHER) {
                            intent = new Intent(DetectActivity31.this,
                                    RegisterActivityfdt.class);
                            intent.putExtra("Face", mDetectedFace);
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
                        }
//                        else if (result == matcher.NO_MATCHER) {
//                            intent = new Intent();
//                            setResult(RESULT_CANCELED, intent);
//                            finish();
//                            ToastUtil.showToast(DetectActivity31.this, "验证失败，请对准识别框", 1);
//                        }
                        else {
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


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activty_detectfacedetector31);

//        mOpenCvCameraView = (CameraBridgeViewBase3) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView = findViewById(R.id.fd_activity_surface_view);
        initClassifier();
        SmartOpenCV.getInstance().init(mOpenCvCameraView, new CameraConfiguration.Builder()
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

        DatabaseHelper helper = new DatabaseHelper(DetectActivity31.this);
        userList = helper.query();
        matcher = new FaceMatcher(userList);
        helper.close();
        //
        mOpenCvCameraView.disableView();
        mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase3.CAMERA_ID_FRONT);
        mOpenCvCameraView.enableView();
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
        DatabaseHelper helper = new DatabaseHelper(DetectActivity31.this);
        userList = helper.query();
        matcher = new FaceMatcher(userList);
        helper.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected List<? extends CameraBridgeViewBase3> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
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
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        // 翻转矩阵以适配前后置摄像头
//        if (true) {
//            Core.flip(mRgba, mRgba, 1);
//            Core.flip(mGray, mGray, 1);
//        } else {
//            Core.flip(mRgba, mRgba, -1);
//            Core.flip(mGray, mGray, -1);
//        }
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

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
//            Log.e(TAG, "onCameraFrame1: " + facesArray[i]);
//            Point point = new Point(facesArray[i].x + 320, facesArray[i].y + 320);
//            facesArray[i] = new Rect(point, facesArray[i].size());

//            if (facesArray[i].height > 480 && facesArray[i].height < 640) {
            if (facesArray[i].height > 200 && facesArray[i].height < 1024) {
                Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(),
                        FACE_RECT_COLOR, 3);
                Log.e(TAG, "onCameraFrame3: " + facesArray[i].height);
                // 获取并利用message传递当前检测的人脸
                Mat faceMat = new Mat(mRgba, facesArray[i]);
//                Imgproc.resize(faceMat, faceMat, new Size(320, 320));
                Imgproc.resize(faceMat, faceMat, new Size(matcher.width, matcher.height));
                Bitmap bitmap = Bitmap.createBitmap(faceMat.width(),
                        faceMat.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(faceMat, bitmap);
                Message message = Message.obtain();
                message.what = getIntent().getIntExtra("flag", 0);
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }

        }

        return mRgba;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemFace50 = menu.add("Face size 50%");
        mItemFace40 = menu.add("Face size 40%");
        mItemFace30 = menu.add("Face size 30%");
        mItemFace20 = menu.add("Face size 20%");
        mItemType = menu.add(mDetectorName[mDetectorType]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemFace50) {
            setMinFaceSize(0.5f);
        } else if (item == mItemFace40) {
            setMinFaceSize(0.4f);
        } else if (item == mItemFace30) {
            setMinFaceSize(0.3f);
        } else if (item == mItemFace20) {
            setMinFaceSize(0.2f);
        } else if (item == mItemType) {
            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[tmpDetectorType]);
            setDetectorType(tmpDetectorType);
        }
        return true;
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }
}