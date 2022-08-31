package com.geek.libfacedetect.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import androidx.annotation.RequiresApi;

public class CameraTextureView extends TextureView {

    private static final String TAG = "CameraTextureView";
    private CameraProxy mCameraProxy;
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    private float mOldDistance;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraTextureView(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setSurfaceTextureListener(mSurfaceTextureListener);
        mCameraProxy = new CameraProxy((Activity) context);

    }

    public Matrix calculateSurfaceHolderTransform() {
        // 预览 View 的大小，比如 SurfaceView
        int viewHeight = getHeight();
        int viewWidth = getWidth();
        // 相机选择的预览尺寸
        int cameraHeight = mCameraProxy.getPreviewHeight();
        int cameraWidth = mCameraProxy.getPreviewWidth();
        // 计算出将相机的尺寸 => View 的尺寸需要的缩放倍数
        float ratioPreview = (float) cameraWidth / cameraHeight;
        float ratioView = (float) viewWidth / viewHeight;
        float scaleX, scaleY;
        if (ratioView < ratioPreview) {
            scaleX = ratioPreview / ratioView;
            scaleY = 1;
        } else {
            scaleX = 1;
            scaleY = ratioView / ratioPreview;
        }
        // 计算出 View 的偏移量
        float scaledWidth = viewWidth * scaleX;
        float scaledHeight = viewHeight * scaleY;
        float dx = (viewWidth - scaledWidth) / 2;
        float dy = (viewHeight - scaledHeight) / 2;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        matrix.postTranslate(dx, dy);

        return matrix;
    }

    private SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.v(TAG, "onSurfaceTextureAvailable. width: " + width + ", height: " + height);
            mCameraProxy.openCamera();
            int previewWidth = mCameraProxy.getPreviewWidth();
            int previewHeight = mCameraProxy.getPreviewHeight();

            if (width > height) {
                setAspectRatio(previewWidth, previewHeight);
            } else {
                setAspectRatio(previewHeight, previewWidth);
            }
//            Matrix matrix = calculateSurfaceHolderTransform();
//            setTransform(matrix);
            mCameraProxy.startPreview(surface);
//            mCameraProxy.getCamera().cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
            mCameraProxy.doAutoFocus1();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.v(TAG, "onSurfaceTextureSizeChanged. width: " + width + ", height: " + height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.v(TAG, "onSurfaceTextureDestroyed");
            mCameraProxy.releaseCamera();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    public CameraProxy getCameraProxy() {
        return mCameraProxy;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            // 点击聚焦
            mCameraProxy.focusOnPoint((int) event.getX(), (int) event.getY(), getWidth(), getHeight());
            Log.e("CameraProxy","点击聚焦");
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDistance = getFingerSpacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float newDistance = getFingerSpacing(event);
                if (newDistance > mOldDistance) {
                    mCameraProxy.handleZoom(true);
                } else if (newDistance < mOldDistance) {
                    mCameraProxy.handleZoom(false);
                }
                mOldDistance = newDistance;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
