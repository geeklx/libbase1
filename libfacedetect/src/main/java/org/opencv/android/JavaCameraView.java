package org.opencv.android;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * This class is an implementation of the Bridge View between OpenCV and Java Camera.
 * This class relays on the functionality available in base class and only implements
 * required functions:
 * connectCamera - opens Java camera and sets the PreviewCallback to be delivered.
 * disconnectCamera - closes the camera and stops preview.
 * When frame is delivered via callback from Camera - it processed via OpenCV to be
 * converted to RGBA32 and then passed to the external callback for modifications if required.
 */
public class JavaCameraView extends CameraBridgeViewBase implements PreviewCallback {

    private static final int MAGIC_TEXTURE_ID = 10;
    private static final String TAG = "JavaCameraView";

    private byte mBuffer[];
    private Mat[] mFrameChain;
    private int mChainIdx = 0;
    private Thread mThread;
    private boolean mStopThread;

    protected Camera mCamera;
    protected JavaCameraFrame[] mCameraFrame;
    private SurfaceTexture mSurfaceTexture;
    private int mPreviewFormat = ImageFormat.NV21;

    public static class JavaCameraSizeAccessor implements ListItemAccessor {

        @Override
        public int getWidth(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.width;
        }

        @Override
        public int getHeight(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.height;
        }
    }

    public JavaCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    public JavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //传入的是JavaCameraView的width,height
    protected boolean initializeCamera(int width, int height) {
        Log.d(TAG, "Initialize java camera");
        boolean result = true;
        synchronized (this) {
            mCamera = null;
            //mCameraIndex是指定相机的类型,是应用里的设置不是指相机ID
            //相机ID需根据类型找
            //继承父类CameraBridgeViewBase
            //初值为CAMERA_ID_ANY
            if (mCameraIndex == CAMERA_ID_ANY) {
                Log.d(TAG, "Trying to open camera with old open()");
                try {
                    //先尝试不指定相机类型启动相机
                    mCamera = Camera.open();
                } catch (Exception e) {
                    Log.e(TAG, "Camera is not available (in use or does not exist): " + e.getLocalizedMessage());
                }

                if (mCamera == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    boolean connected = false;
                    for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                        Log.d(TAG, "Trying to open camera with new open(" + Integer.valueOf(camIdx) + ")");
                        try {
                            //若不指定相机类型启动相机失败则遍历所有相机ID一个个尝试启动,一旦成功
                            //就选择当前成功启动的相机
                            mCamera = Camera.open(camIdx);
                            connected = true;
                        } catch (RuntimeException e) {
                            Log.e(TAG, "Camera #" + camIdx + "failed to open: " + e.getLocalizedMessage());
                        }
                        if (connected) {
                            break;
                        }
                    }
                }
            } else {
                //这里是指定相机类型的情况
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    int localCameraIndex = mCameraIndex;
                    if (mCameraIndex == CAMERA_ID_BACK) {
                        Log.i(TAG, "Trying to open back camera");
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        //根据相机类型找此类型对应的相机ID
                        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                            Camera.getCameraInfo(camIdx, cameraInfo);
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                                localCameraIndex = camIdx;
                                break;
                            }
                        }
                    } else if (mCameraIndex == CAMERA_ID_FRONT) {
                        Log.i(TAG, "Trying to open front camera");
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                            Camera.getCameraInfo(camIdx, cameraInfo);
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                localCameraIndex = camIdx;
                                break;
                            }
                        }
                    }
                    if (localCameraIndex == CAMERA_ID_BACK) {
                        //localCameraIndex初赋值为CAMERA_ID_BACK类型,指定要启动背面相机
                        //若有背面相机此处localCameraIndex值已经被赋值为背面相机的相机ID了
                        Log.e(TAG, "Back camera not found!");
                    } else if (localCameraIndex == CAMERA_ID_FRONT) {
                        Log.e(TAG, "Front camera not found!");
                    } else {
                        Log.d(TAG, "Trying to open camera with new open(" + Integer.valueOf(localCameraIndex) + ")");
                        try {
                            //根据找到的相机ID启动相机
                            mCamera = Camera.open(localCameraIndex);
                        } catch (RuntimeException e) {
                            Log.e(TAG, "Camera #" + localCameraIndex + "failed to open: " + e.getLocalizedMessage());
                        }
                    }
                }
            }

            //若启动相机失败则返回false
            if (mCamera == null) {
                return false;
            }

            /* Now set camera parameters */
            try {
                Camera.Parameters params = mCamera.getParameters();
                Log.d(TAG, "getSupportedPreviewSizes()");
                List<android.hardware.Camera.Size> sizes = params.getSupportedPreviewSizes();

                if (sizes != null) {
                    //选择预览size
                    /* Select the size that fits surface considering maximum size allowed */
                    Size frameSize = calculateCameraFrameSize(sizes, new JavaCameraSizeAccessor(), width, height);
                    //这里width,height是connectCamera(getWidth(), getHeight())传进来的
                    //是surfaceView的大小也是surface的大小
                    //Log相机frame大小和surface大小
                    Log.d("FunnyAR", "surface width: " + width + " surface height: " + height +
                            "frameSize: " + frameSize.toString());

                    //选择预览格式
                    /* Image format NV21 causes issues in the Android emulators */
                    if (Build.FINGERPRINT.startsWith("generic")
                            || Build.FINGERPRINT.startsWith("unknown")
                            || Build.MODEL.contains("google_sdk")
                            || Build.MODEL.contains("Emulator")
                            || Build.MODEL.contains("Android SDK built for x86")
                            || Build.MANUFACTURER.contains("Genymotion")
                            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                            || "google_sdk".equals(Build.PRODUCT)) {
                        params.setPreviewFormat(ImageFormat.YV12);  // "generic" or "android" = android emulator
                    } else {
                        params.setPreviewFormat(ImageFormat.NV21);
                    }

                    //预览格式记录到成员变量里
                    mPreviewFormat = params.getPreviewFormat();

                    Log.d(TAG, "Set preview size to " + Integer.valueOf((int) frameSize.width) + "x" + Integer.valueOf((int) frameSize.height));
                    params.setPreviewSize((int) frameSize.width, (int) frameSize.height);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !android.os.Build.MODEL.equals("GT-I9100")) {
                        params.setRecordingHint(true);
                    }

                    //JavaCameraView的聚焦模式也是写定下的
                    List<String> FocusModes = params.getSupportedFocusModes();
                    if (FocusModes != null && FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    }

                    mCamera.setParameters(params);
                    params = mCamera.getParameters();

                    //设置frame大小
                    mFrameWidth = params.getPreviewSize().width;
                    mFrameHeight = params.getPreviewSize().height;

                    //这里涉及到缩放
                    /*
                        #Modified portrait step1
                        为了在deliverAndDrawFrame里往画布上画时应用缩放
                        <JavaCameraView>里
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        若又想指定缩放后的大小可将<JavaCameraView>放在一个有大小的
                        LinearLayout里
                        且当方向是portrait时比率是
                        surface的width/相机frame的mFrameHeight
                        surface的height/相机frame的mFrameWidth
                        若不想设置<JavaCameraView>则这里直接去掉if语句应该也可
                     */
                    if ((getLayoutParams().width == LayoutParams.MATCH_PARENT) && (getLayoutParams().height == LayoutParams.MATCH_PARENT))
                    //mScale = Math.min(((float)height)/mFrameHeight, ((float)width)/mFrameWidth);
                    {
                        mScale = Math.min(((float) width) / mFrameHeight, ((float) height) / mFrameWidth);
                    } else {
                        mScale = 0;
                    }

                    //Log缩放和相机Frame大小
                    Log.d("FunnyAR", "mScale: " + mScale + " mFrameWidth: " + mFrameWidth +
                            " mFrameHeight: " + mFrameHeight);

                    if (mFpsMeter != null) {
                        mFpsMeter.setResolution(mFrameWidth, mFrameHeight);
                    }

                    //算frame的字节大小,设置相应大小的缓冲区接收数据
                    //像素个数
                    int size = mFrameWidth * mFrameHeight;
                    //像素个数x当前格式每个像素所需bit个数/一个字节8bit==frame所需byte数
                    size = size * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
                    mBuffer = new byte[size];

                    /*
                    Adds a pre-allocated buffer to the preview callback buffer queue.
                    Applications can add one or more buffers to the queue.
                    When a preview frame arrives and there is still at least
                    one available buffer, the buffer will be used and removed from the queue.
                    Then preview callback is invoked with the buffer.
                    If a frame arrives and there is no buffer left, the frame is discarded.
                    Applications should add buffers back when they finish processing the data
                     in them.
                     */
                    /*
                    This method is only necessary when setPreviewCallbackWithBuffer(PreviewCallback)
                     is used. When setPreviewCallback(PreviewCallback) or
                     setOneShotPreviewCallback(PreviewCallback) are used,
                     buffers are automatically allocated.
                     When a supplied buffer is too small to hold the preview frame data,
                     preview callback will return null and the buffer will be removed from the
                     buffer queue.
                     */
                    mCamera.addCallbackBuffer(mBuffer);
                    /*
                    Installs a callback to be invoked for every preview frame,
                    using buffers supplied with addCallbackBuffer(byte[]),
                    in addition to displaying them on the screen.
                    he callback will be repeatedly called for as long as preview is active
                    and buffers are available. Any other preview callbacks are overridden.
                     */
                    mCamera.setPreviewCallbackWithBuffer(this);

                    //一个Mat数组
                    //注意Yuv420sp格式
                    mFrameChain = new Mat[2];
                    mFrameChain[0] = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
                    mFrameChain[1] = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);

                    //继承的方法为继承的Bitmap mCacheBitmap初始化内存
                    AllocateCache();

                    //JavaCameraFrame内部有对Mat的引用
                    //mCameraFrame[0].mYuvFrameData就是Mat mFrameChain[0]
                    mCameraFrame = new JavaCameraFrame[2];
                    mCameraFrame[0] = new JavaCameraFrame(mFrameChain[0], mFrameWidth, mFrameHeight);
                    mCameraFrame[1] = new JavaCameraFrame(mFrameChain[1], mFrameWidth, mFrameHeight);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mSurfaceTexture = new SurfaceTexture(MAGIC_TEXTURE_ID);
                        mCamera.setPreviewTexture(mSurfaceTexture);
                    } else {
                        mCamera.setPreviewDisplay(null);
                    }

                    /* Finally we are ready to start the preview */
                    Log.d(TAG, "startPreview");
                    mCamera.startPreview();
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void releaseCamera() {
        synchronized (this) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);

                mCamera.release();
            }
            mCamera = null;
            if (mFrameChain != null) {
                mFrameChain[0].release();
                mFrameChain[1].release();
            }
            if (mCameraFrame != null) {
                mCameraFrame[0].release();
                mCameraFrame[1].release();
            }
        }
    }

    private boolean mCameraFrameReady = false;

    //重载父类的抽象方法,负责启动相机
    @Override
    protected boolean connectCamera(int width, int height) {

        /* 1. We need to instantiate camera
         * 2. We need to start thread which will be getting frames
         */
        /* First step - initialize camera connection */
        Log.d(TAG, "Connecting to camera");
        //用initializeCamera函数实现初始化相机连接
        if (!initializeCamera(width, height)) {
            return false;
        }

        mCameraFrameReady = false;

        /* now we can start update thread */
        Log.d(TAG, "Starting processing thread");
        mStopThread = false;
        mThread = new Thread(new CameraWorker());
        mThread.start();

        return true;
    }

    @Override
    protected void disconnectCamera() {
        /* 1. We need to stop thread which updating the frames
         * 2. Stop camera and release it
         */
        Log.d(TAG, "Disconnecting from camera");
        try {
            mStopThread = true;
            Log.d(TAG, "Notify thread");
            synchronized (this) {
                this.notify();
            }
            Log.d(TAG, "Waiting for thread");
            if (mThread != null) {
                mThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mThread = null;
        }

        /* Now release camera */
        releaseCamera();

        mCameraFrameReady = false;
    }

    /*
    重载Camera.PreviewCallback onPreviewFrame方法
    这里onPreviewFrame时在UI线程里被处理的,因为相机时在主线程里被启动的
    但是数据将被另一个线程取走处理
     */
    /*
    Callback interface used to deliver copies of preview frames as they are displayed.
    Called as preview frames are displayed. This callback is invoked
     on the event thread Camera.open(int) was called from.
     */
    @Override
    public void onPreviewFrame(byte[] frame, Camera arg1) {
        if (true) {
            Log.d(TAG, "Preview Frame received. Frame size: " + frame.length);
        }
        synchronized (this) {
            //mChainIdx在0,1间切换,由另一个线程负责管理
            //OpenCV Java层特有的方法
            //mFrameChain[mChainIdx]的大小是1.5height x 1.0width,将数据存进去
            mFrameChain[mChainIdx].put(0, 0, frame);
            //设置标志表示数据存好了
            mCameraFrameReady = true;
            //唤醒一个等待当前JavaCameraView.this的线程

            this.notify();
        }
        /*
        onPreviewFrame处理数据时addCallbackBuffer()的buffer将出队列被处理
        处理完后为了下次onPreviewFrame需再次将buffer给回调
         */
        if (mCamera != null) {
            mCamera.addCallbackBuffer(mBuffer);
        }
    }

    /*
    JavaCameraFrame实现CvCameraViewFrame的rgba(),gray()方法
    这个类型将通过deliverAndDrawFrame()里的mListener.onCameraFrame(frame)传给用户处理
    在JavaCameraFrame的接口里实现Mat的旋转是最好的时机了
    如此client通过gray(),rgba()获得的Mat就是方向portrait的了
    #Modified portrait step3
     */
    private class JavaCameraFrame implements CvCameraViewFrame {
        @Override
        public Mat gray() {
            //返回Mat里的选定区域,这跟Yuv420sp格式紧密相关
//            return mYuvFrameData.submat(0, mHeight, 0, mWidth);
            //#Modified step3.1
//            Core.flip(portrait_gray, portrait_gray, -1);
            Core.rotate(mYuvFrameData.submat(0, mHeight, 0, mWidth),
                    portrait_gray, Core.ROTATE_90_COUNTERCLOCKWISE);
            return portrait_gray;
        }

        @Override
        public Mat rgba() {
            if (mPreviewFormat == ImageFormat.NV21) {
                Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);
            } else if (mPreviewFormat == ImageFormat.YV12) {
                Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGB_I420, 4);  // COLOR_YUV2RGBA_YV12 produces inverted colors
            } else {
                throw new IllegalArgumentException("Preview Format can be NV21 or YV12");
            }

            //#Modified step3.2
//            Core.flip(mRgba, mRgba, -1);
            Core.rotate(mRgba, portrait_rgba, Core.ROTATE_90_COUNTERCLOCKWISE);
            return portrait_rgba;
//            return mRgba;
        }

        public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
            super();
            mWidth = width;
            mHeight = height;
            //#Modified
            portrait_mHeight = mWidth;
            portrait_mWidth = mHeight;
            portrait_gray = new Mat(portrait_mHeight, portrait_mWidth, CvType.CV_8UC1);
            portrait_rgba = new Mat(portrait_mHeight, portrait_mWidth, CvType.CV_8UC4);
            mYuvFrameData = Yuv420sp;
            mRgba = new Mat();
        }

        public void release() {
            mRgba.release();
        }

        private Mat mYuvFrameData;
        private Mat mRgba;
        private int mWidth;
        private int mHeight;
        //#Modified
        private int portrait_mHeight;
        private int portrait_mWidth;
        private Mat portrait_gray;
        private Mat portrait_rgba;
    }

    ;

    private class CameraWorker implements Runnable {

        @Override
        public void run() {
            do {
                boolean hasFrame = false;
                synchronized (JavaCameraView.this) {
                    try {
                        //onPreviewFrame里frame准备好了会设置mCameraFrameReady为true然后唤醒此线程
                        //只要相机启动着mStopThread就为false
                        //当相机启动着且onPreviewFrame里frame没准备好时线程就等待
                        //等待语句放在while里防止条件没满足时线程被唤醒
                        while (!mCameraFrameReady && !mStopThread) {
                            JavaCameraView.this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //线程被唤醒是因为onPreviewFrame里frame准备好了
                    if (mCameraFrameReady) {
                        //mChainIdx在0,1之间切换表示mCameraFrame当前的缓冲区
                        mChainIdx = 1 - mChainIdx;
                        //设置mCameraFrameReady为false用来等下次onPreviewFrame里frame准备好
                        mCameraFrameReady = false;
                        //表示当前有frame可用
                        hasFrame = true;
                    }
                }

                //线程没停止且有frame可用
                if (!mStopThread && hasFrame) {
                    //当前的缓冲区不为空则处理它
                    //mChainIdx初值为0,mChainIdx = 1 - mChainIdx设置其为1
                    //这里1 - mChainIdx为0
                    //之后mChainIdx值为1,mChainIdx = 1 - mChainIdx设置其为0
                    //这里1 - mChainIdx为1
                    //如此循环
                    //mCameraFrame[1 - mChainIdx].mYuvFrameData就是对mFrameChain[1 - mChainIdx]
                    //的引用,即JavaCameraFrame类里有对Mat的引用
                    if (!mFrameChain[1 - mChainIdx].empty()) {
                        deliverAndDrawFrame(mCameraFrame[1 - mChainIdx]);
                    }
                }
            } while (!mStopThread);
            Log.d(TAG, "Finish processing thread");
        }
    }
}