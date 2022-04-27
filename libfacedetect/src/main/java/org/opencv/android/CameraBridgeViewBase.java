package org.opencv.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.geek.libfacedetect.R;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.List;

/**
 * This is a basic class, implementing the interaction with Camera and OpenCV library.
 * The main responsibility of it - is to control when camera can be enabled, process the frame,
 * call external listener to make any adjustments to the frame and then draw the resulting
 * frame to the screen.
 * The clients shall implement CvCameraViewListener.
 */
public abstract class CameraBridgeViewBase extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraBridge";
    private static final int MAX_UNSPECIFIED = -1;
    private static final int STOPPED = 0;
    private static final int STARTED = 1;

    private int mState = STOPPED;
    private Bitmap mCacheBitmap;
    private CvCameraViewListener2 mListener;
    private boolean mSurfaceExist;
    private final Object mSyncObject = new Object();

    protected int mFrameWidth;
    protected int mFrameHeight;
    protected int mMaxHeight;
    protected int mMaxWidth;
    protected float mScale = 0;
    protected int mPreviewFormat = RGBA;
    protected int mCameraIndex = CAMERA_ID_ANY;
    protected boolean mEnabled;
    protected FpsMeter mFpsMeter = null;

    public static final int CAMERA_ID_ANY = -1;
    public static final int CAMERA_ID_BACK = 99;
    public static final int CAMERA_ID_FRONT = 98;
    public static final int RGBA = 1;
    public static final int GRAY = 2;

    public CameraBridgeViewBase(Context context, int cameraId) {
        super(context);
        mCameraIndex = cameraId;
        getHolder().addCallback(this);
        mMaxWidth = MAX_UNSPECIFIED;
        mMaxHeight = MAX_UNSPECIFIED;
    }

    public CameraBridgeViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        int count = attrs.getAttributeCount();
        Log.d(TAG, "Attr count: " + Integer.valueOf(count));

        TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CameraBridgeViewBase);
        if (styledAttrs.getBoolean(R.styleable.CameraBridgeViewBase_show_fps, false)) {
            enableFpsMeter();
        }

        mCameraIndex = styledAttrs.getInt(R.styleable.CameraBridgeViewBase_camera_id, -1);

        getHolder().addCallback(this);
        mMaxWidth = MAX_UNSPECIFIED;
        mMaxHeight = MAX_UNSPECIFIED;
        styledAttrs.recycle();
    }

    /**
     * Sets the camera index
     *
     * @param cameraIndex new camera index
     */
    public void setCameraIndex(int cameraIndex) {
        this.mCameraIndex = cameraIndex;
    }

    public interface CvCameraViewListener {
        /**
         * This method is invoked when camera preview has started. After this method is invoked
         * the frames will start to be delivered to client via the onCameraFrame() callback.
         *
         * @param width  -  the width of the frames that will be delivered
         * @param height - the height of the frames that will be delivered
         */
        public void onCameraViewStarted(int width, int height);

        /**
         * This method is invoked when camera preview has been stopped for some reason.
         * No frames will be delivered via onCameraFrame() callback after this method is called.
         */
        public void onCameraViewStopped();

        /**
         * This method is invoked when delivery of the frame needs to be done.
         * The returned values - is a modified frame which needs to be displayed on the screen.
         * TODO: pass the parameters specifying the format of the frame (BPP, YUV or RGB and etc)
         */
        public Mat onCameraFrame(Mat inputFrame);
    }

    public interface CvCameraViewListener2 {
        /**
         * This method is invoked when camera preview has started. After this method is invoked
         * the frames will start to be delivered to client via the onCameraFrame() callback.
         *
         * @param width  -  the width of the frames that will be delivered
         * @param height - the height of the frames that will be delivered
         */
        public void onCameraViewStarted(int width, int height);

        /**
         * This method is invoked when camera preview has been stopped for some reason.
         * No frames will be delivered via onCameraFrame() callback after this method is called.
         */
        public void onCameraViewStopped();

        /**
         * This method is invoked when delivery of the frame needs to be done.
         * The returned values - is a modified frame which needs to be displayed on the screen.
         * TODO: pass the parameters specifying the format of the frame (BPP, YUV or RGB and etc)
         */
        public Mat onCameraFrame(CvCameraViewFrame inputFrame);
    }

    ;

    private Mat cacheMat;

    protected class CvCameraViewListenerAdapter implements CvCameraViewListener2 {
        public CvCameraViewListenerAdapter(CvCameraViewListener oldStypeListener) {
            mOldStyleListener = oldStypeListener;
        }

        @Override
        public void onCameraViewStarted(int width, int height) {
            mOldStyleListener.onCameraViewStarted(width, height);
        }

        @Override
        public void onCameraViewStopped() {
            mOldStyleListener.onCameraViewStopped();
        }

        @Override
        public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
            Mat result = null;
            switch (mPreviewFormat) {
                case RGBA:
                    Mat rgbCache = inputFrame.rgba();
                    if (mCameraIndex == 1) {
                        if (cacheMat == null) {
                            cacheMat = new Mat();
                        }
                        Core.rotate(rgbCache, cacheMat, Core.ROTATE_180);
                        result = mOldStyleListener.onCameraFrame(cacheMat);
                        break;
                    }
                    result = mOldStyleListener.onCameraFrame(rgbCache);
                    break;
                case GRAY:
                    Mat grayCache = inputFrame.gray();
                    if (mCameraIndex == 1) {
                        if (cacheMat == null) {
                            cacheMat = new Mat();
                        }
                        Core.rotate(grayCache, cacheMat, Core.ROTATE_180);
                        result = mOldStyleListener.onCameraFrame(cacheMat);
                        break;
                    }
                    result = mOldStyleListener.onCameraFrame(grayCache);
                    break;
                default:
                    Log.e(TAG, "Invalid frame format! Only RGBA and Gray Scale are supported!");
            }
//            ;
//            if (mCameraIndex == 1) {
//                //前置摄像头 翻转图片
//                try {
//                    cacheMat = new Mat();
//                    Core.rotate(result, cacheMat, Core.ROTATE_180);
//                    return cacheMat;
//                } catch (Exception e) {
//
//                }
//            }
            return result;
        }

        public void setFrameFormat(int format) {
            mPreviewFormat = format;
        }

        private int mPreviewFormat = RGBA;
        private CvCameraViewListener mOldStyleListener;
    }

    ;

    /**
     * This class interface is abstract representation of single frame from camera for onCameraFrame callback
     * Attention: Do not use objects, that represents this interface out of onCameraFrame callback!
     */
    public interface CvCameraViewFrame {

        /**
         * This method returns RGBA Mat with frame
         */
        public Mat rgba();

        /**
         * This method returns single channel gray scale Mat with frame
         */
        public Mat gray();
    }

    ;

    /*
    重载SurfaceHolder.Callback的方法
     */
    /*
    Access to the underlying surface is provided via the SurfaceHolder interface,
    which can be retrieved by calling getHolder().
    The Surface will be created for you while the SurfaceView's window is visible;
    you should implement SurfaceHolder.Callback.surfaceCreated(SurfaceHolder)
    and SurfaceHolder.Callback.surfaceDestroyed(SurfaceHolder) to discover when the
    Surface is created and destroyed as the window is shown and hidden.
    One of the purposes of this class is to provide a surface in which a secondary
    thread can render into the screen. If you are going to use it this way,
    you need to be aware of some threading semantics:
    All SurfaceView and SurfaceHolder.Callback methods will be called from
    the thread running the SurfaceView's window (typically the main thread of the application). They thus need to correctly synchronize with any state that is also touched by the drawing thread.
    You must ensure that the drawing thread only touches the underlying Surface
    while it is valid -- between SurfaceHolder.Callback.surfaceCreated()
    and SurfaceHolder.Callback.surfaceDestroyed().
     */
    /*
    This is called immediately after any structural changes (format or size)
    have been made to the surface. You should at this point update the imagery
    in the surface. This method is always called at least once,
    after surfaceCreated(SurfaceHolder).
     */
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d(TAG, "call surfaceChanged event");
        synchronized (mSyncObject) {
            if (!mSurfaceExist) {
                mSurfaceExist = true;
                checkCurrentState();
            } else {
                /** Surface changed. We need to stop camera and restart with new parameters */
                /* Pretend that old surface has been destroyed */
                mSurfaceExist = false;
                checkCurrentState();
                /* Now use new surface. Say we have it now */
                mSurfaceExist = true;
                checkCurrentState();
            }
        }
    }

    /*
    This is called immediately after the surface is first created.
    Implementations of this should start up whatever rendering code they desire.
    Note that only one thread can ever draw into a Surface,
    so you should not draw into the Surface here if your normal rendering
    will be in another thread.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        /* Do nothing. Wait until surfaceChanged delivered */
    }

    /*
    This is called immediately before a surface is being destroyed.
    After returning from this call, you should no longer try to access this surface.
    If you have a rendering thread that directly accesses the surface,
    you must ensure that thread is no longer touching the Surface before returning
    from this function.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (mSyncObject) {
            mSurfaceExist = false;
            checkCurrentState();
        }
    }

    /**
     * This method is provided for clients, so they can enable the camera connection.
     * The actual onCameraViewStarted callback will be delivered only after both this method is called and surface is available
     */
    public void enableView() {
        synchronized (mSyncObject) {
            mEnabled = true;
            checkCurrentState();
        }
    }

    /**
     * This method is provided for clients, so they can disable camera connection and stop
     * the delivery of frames even though the surface view itself is not destroyed and still stays on the scren
     */
    public void disableView() {
        synchronized (mSyncObject) {
            mEnabled = false;
            checkCurrentState();
        }
    }

    /**
     * This method enables label with fps value on the screen
     */
    public void enableFpsMeter() {
        if (mFpsMeter == null) {
            mFpsMeter = new FpsMeter();
            mFpsMeter.setResolution(mFrameWidth, mFrameHeight);
        }
    }

    public void disableFpsMeter() {
        mFpsMeter = null;
    }

    /**
     * @param listener
     */

    public void setCvCameraViewListener(CvCameraViewListener2 listener) {
        mListener = listener;
    }

    public void setCvCameraViewListener(CvCameraViewListener listener) {
        CvCameraViewListenerAdapter adapter = new CvCameraViewListenerAdapter(listener);
        adapter.setFrameFormat(mPreviewFormat);
        mListener = adapter;
    }

    /**
     * This method sets the maximum size that camera frame is allowed to be. When selecting
     * size - the biggest size which less or equal the size set will be selected.
     * As an example - we set setMaxFrameSize(200,200) and we have 176x152 and 320x240 sizes. The
     * preview frame will be selected with 176x152 size.
     * This method is useful when need to restrict the size of preview frame for some reason (for example for video recording)
     *
     * @param maxWidth  - the maximum width allowed for camera frame.
     * @param maxHeight - the maximum height allowed for camera frame
     */
    public void setMaxFrameSize(int maxWidth, int maxHeight) {
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
    }

    public void SetCaptureFormat(int format) {
        mPreviewFormat = format;
        if (mListener instanceof CvCameraViewListenerAdapter) {
            CvCameraViewListenerAdapter adapter = (CvCameraViewListenerAdapter) mListener;
            adapter.setFrameFormat(mPreviewFormat);
        }
    }

    /**
     * Called when mSyncObject lock is held
     */
    private void checkCurrentState() {
        Log.d(TAG, "call checkCurrentState");
        int targetState;
        //enableView()将设置mEnabled为true,surfaceChanged()将设置mSurfaceExist
        //getVisibility() == VISIBLE似乎总是成立的
        //当surface准备好了且client设置enableView()时设置targetState为STARTED
        if (mEnabled && mSurfaceExist && getVisibility() == VISIBLE) {
            targetState = STARTED;
        } else {
            targetState = STOPPED;
        }

        //mState初始值是STOPPED
        //若目标状态与当前状态不同则退出当前状态进入目标状态
        if (targetState != mState) {
            /* The state change detected. Need to exit the current state and enter target state */
            processExitState(mState);
            mState = targetState;
            processEnterState(mState);
        }
    }

    private void processEnterState(int state) {
        Log.d(TAG, "call processEnterState: " + state);
        switch (state) {
            case STARTED:
                //真正启动相机的地方
                onEnterStartedState();
                if (mListener != null) {
                    //进入STARTED状态后若CameraBridgeViewBase类的成员CvCameraViewListener2 mListener
                    //不为null则调用其onCameraViewStarted方法,通知mListener相机启动了
                    mListener.onCameraViewStarted(mFrameWidth, mFrameHeight);
                }
                break;
            case STOPPED:
                onEnterStoppedState();
                if (mListener != null) {
                    //进入STOPPED状态后若CameraBridgeViewBase类的成员CvCameraViewListener2 mListener
                    //不为null则调用其onCameraViewStopped方法,通知mListener相机停止了
                    mListener.onCameraViewStopped();
                }
                break;
        }
        ;
    }

    private void processExitState(int state) {
        Log.d(TAG, "call processExitState: " + state);
        switch (state) {
            case STARTED:
                onExitStartedState();
                break;
            case STOPPED:
                onExitStoppedState();
                break;
        }
        ;
    }

    private void onEnterStoppedState() {
        /* nothing to do */
    }

    private void onExitStoppedState() {
        /* nothing to do */
    }

    // NOTE: The order of bitmap constructor and camera connection is important for android 4.1.x
    // Bitmap must be constructed before surface
    private void onEnterStartedState() {
        Log.d(TAG, "call onEnterStartedState");
        /* Connect camera */
        //connectCamera的参数是CameraBridgeViewBase的width,height
        if (!connectCamera(getWidth(), getHeight())) {
            AlertDialog ad = new AlertDialog.Builder(getContext()).create();
            ad.setCancelable(false); // This blocks the 'BACK' button
            ad.setMessage("It seems that you device does not support camera (or it is locked). Application will be closed.");
            ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((Activity) getContext()).finish();
                }
            });
            ad.show();

        }
    }

    private void onExitStartedState() {
        disconnectCamera();
        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
        }
    }

    /*
        onPreviewFrame在UI线程被调用,它存好数据后通知另一个线程处理
        另一个线程就调用这个方法处理数据
        onPreviewFrame相当于生产者,另一个线程相当于消费者
        当使用JavaCameraView类时frame是JavaCameraFrame类型
        其通过OpenCV实现了接口
     */

    /**
     * This method shall be called by the subclasses when they have valid
     * object and want it to be delivered to external client (via callback) and
     * then displayed on the screen.
     *
     * @param frame - the current frame to be delivered
     */
    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
        Mat modified;

        if (mListener != null) {
            //CvCameraViewListener2 mListener是client指定的
            //这里调用客户重载的接口方法且接收返回值
            //这里都是在数据处理线程里执行的
            modified = mListener.onCameraFrame(frame);
        } else {
            //若client没指定CvCameraViewListener2 mListener即client不准备处理preview数据
            //则modified设置为
            //onPreviewFrame传回的数据转换成的rgba Mat
            modified = frame.rgba();
        }

        //Log Mat的大小和Bitmap的大小
        Log.d("FunnyAR", "mScale: " + mScale + " modified.rows: " + modified.rows()
                + " modified.cols: " + modified.cols() + " mCacheBitmap.getWidth(): " +
                mCacheBitmap.getWidth() + " mCacheBitmap.getHeight() " +
                mCacheBitmap.getHeight());

        //标志modified转Bitmap是否成功
        boolean bmpValid = true;
        //若确实有modified则将其转为Bitmap
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, mCacheBitmap);
            } catch (Exception e) {
                Log.e(TAG, "Mat type: " + modified);
                Log.e(TAG, "Bitmap type: " + mCacheBitmap.getWidth() + "*" + mCacheBitmap.getHeight());
                Log.e(TAG, "Utils.matToBitmap() throws an exception: " + e.getMessage());
                bmpValid = false;
            }
        }
        //转换成功通过画布画到surface里
        if (bmpValid && mCacheBitmap != null) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                if (true) {
                    Log.d(TAG, "mStretch value: " + mScale);
                }

                if (mScale != 0) {
                    canvas.drawBitmap(mCacheBitmap, new Rect(0, 0, mCacheBitmap.getWidth(), mCacheBitmap.getHeight()),
                            new Rect((int) ((canvas.getWidth() - mScale * mCacheBitmap.getWidth()) / 2),
                                    (int) ((canvas.getHeight() - mScale * mCacheBitmap.getHeight()) / 2),
                                    (int) ((canvas.getWidth() - mScale * mCacheBitmap.getWidth()) / 2 + mScale * mCacheBitmap.getWidth()),
                                    (int) ((canvas.getHeight() - mScale * mCacheBitmap.getHeight()) / 2 + mScale * mCacheBitmap.getHeight())), null);
                } else {
                    canvas.drawBitmap(mCacheBitmap, new Rect(0, 0, mCacheBitmap.getWidth(), mCacheBitmap.getHeight()),
                            new Rect((canvas.getWidth() - mCacheBitmap.getWidth()) / 2,
                                    (canvas.getHeight() - mCacheBitmap.getHeight()) / 2,
                                    (canvas.getWidth() - mCacheBitmap.getWidth()) / 2 + mCacheBitmap.getWidth(),
                                    (canvas.getHeight() - mCacheBitmap.getHeight()) / 2 + mCacheBitmap.getHeight()), null);
                }

                if (mFpsMeter != null) {
                    mFpsMeter.measure();
                    mFpsMeter.draw(canvas, 20, 30);
                }
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * This method is invoked shall perform concrete operation to initialize the camera.
     * CONTRACT: as a result of this method variables mFrameWidth and mFrameHeight MUST be
     * initialized with the size of the Camera frames that will be delivered to external processor.
     *
     * @param width  - the width of this SurfaceView
     * @param height - the height of this SurfaceView
     */
    //具体启动相机的过程由子类实现
    protected abstract boolean connectCamera(int width, int height);

    /**
     * Disconnects and release the particular camera object being connected to this surface view.
     * Called when syncObject lock is held
     */
    protected abstract void disconnectCamera();

    // NOTE: On Android 4.1.x the function must be called before SurfaceTexture constructor!
    protected void AllocateCache() {
        //mCacheBitmap = Bitmap.createBitmap(mFrameWidth, mFrameHeight, Bitmap.Config.ARGB_8888);
        //#Modified portrait step2
        //为了方向正确mCacheBitmap存储的时相机frame旋转90度之后的数据
        //旋转90度后mFrameWidth,mFrameHeight互换
        int portraitWidth = mFrameHeight;
        int portraitHeight = mFrameWidth;
        mCacheBitmap = Bitmap.createBitmap(portraitWidth, portraitHeight, Bitmap.Config.ARGB_8888);
    }

    public interface ListItemAccessor {
        public int getWidth(Object obj);

        public int getHeight(Object obj);
    }

    ;

    /**
     * This helper method can be called by subclasses to select camera preview size.
     * It goes over the list of the supported preview sizes and selects the maximum one which
     * fits both values set via setMaxFrameSize() and surface frame allocated for this view
     *
     * @param supportedSizes
     * @param surfaceWidth
     * @param surfaceHeight
     * @return optimal frame size
     */
    protected Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor, int surfaceWidth, int surfaceHeight) {
        //选择一个相机frame大小
        int calcWidth = 0;
        int calcHeight = 0;

        //允许的最大width和height
        //#Modified step4
        //相机Frame的mMaxWidth应该与surface的surfaceHeight比
        //相机Frame的mMaxHeight应该与surface的surfaceWidth比
        //int maxAllowedWidth = (mMaxWidth != MAX_UNSPECIFIED && mMaxWidth < surfaceWidth)? mMaxWidth : surfaceWidth;
        //int maxAllowedHeight = (mMaxHeight != MAX_UNSPECIFIED && mMaxHeight < surfaceHeight)? mMaxHeight : surfaceHeight;
        int maxAllowedWidth = (mMaxWidth != MAX_UNSPECIFIED && mMaxWidth < surfaceHeight) ? mMaxWidth : surfaceHeight;
        int maxAllowedHeight = (mMaxHeight != MAX_UNSPECIFIED && mMaxHeight < surfaceWidth) ? mMaxHeight : surfaceWidth;

        for (Object size : supportedSizes) {
            int width = accessor.getWidth(size);
            int height = accessor.getHeight(size);

            //在允许的范围内选择最大的size
            //client是可通过设置小的mMaxWidth,mMaxHeight来选择低分辨率frame的
            if (width <= maxAllowedWidth && height <= maxAllowedHeight) {
                if (width >= calcWidth && height >= calcHeight) {
                    calcWidth = (int) width;
                    calcHeight = (int) height;
                }
            }
        }

        return new Size(calcWidth, calcHeight);
    }
}