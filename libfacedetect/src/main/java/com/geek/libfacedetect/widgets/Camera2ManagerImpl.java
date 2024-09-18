package com.geek.libfacedetect.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.geek.libfacedetect.util.BaseAppCamera;
import com.geek.libfacedetect.util.ImageUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 2020-04-24.
 */
public class Camera2ManagerImpl extends AbsCameraManager {


    private static final String TAG = "ECDeviceSDK.Camera2ManagerImpl";

    private static Camera2ManagerImpl camera2Manager = null;

    HandlerThread cHandlerThread;//相机处理线程
    Handler cHandler;//相机处理
    CameraDevice cDevice;
    CameraCaptureSession cSession;
    boolean isFront;

    Size cPixelSize;//相机成像尺寸
    int cOrientation;

    private int position = 0;
    Size captureSize;

    Paint pb;

    int[] faceDetectModes;

    private Range<Integer>[] fpsRange;

    CaptureRequest.Builder previewRequestBuilder;//预览请求构建
    CaptureRequest previewRequest;//预览请求
    CameraCaptureSession.CaptureCallback previewCallback;//预览回调

    CameraDevice.StateCallback cDeviceOpenCallback = null;//相机开启回调

    private ImageReader cImageReader;


    public synchronized static Camera2ManagerImpl getInstance() {
        if (camera2Manager == null) {
            camera2Manager = new Camera2ManagerImpl();
        }
        return camera2Manager;

    }

    private CaptureListener captureListener;

    @Override
    public void setCaptureListener(CaptureListener listener) {
        this.captureListener = listener;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void startCapture() {
        CameraManager cManager = (CameraManager) BaseAppCamera.get().getSystemService(Context.CAMERA_SERVICE);

        try {
            String[] cameraList = cManager.getCameraIdList();
            if (cameraList == null || cameraList.length == 0) {
                Log.e(TAG, "no camera found");
                return;
            }
            if (cameraList.length == 1) {
                openCamera(cameraList[0].equalsIgnoreCase("1"));
            } else {
                openCamera(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stopCapture() {

        closeCamera();

    }

    @Override
    public void destory() {
        closeCamera();


    }

    private boolean isCaptured = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"MissingPermission", "LongLogTag"})
    private void openCamera(boolean isFront) {
        Log.d(TAG, "openCamera = " + isFront);
        isCaptured = true;
        this.isFront = isFront;
        String cId = null;
        if (isFront) {
            cId = CameraCharacteristics.LENS_FACING_BACK + "";
        } else {
            cId = CameraCharacteristics.LENS_FACING_FRONT + "";
        }

        CameraManager cManager = (CameraManager) BaseAppCamera.get().getSystemService(Context.CAMERA_SERVICE);
        try {
            //获取开启相机的相关参数
            CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cId);
            //

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] previewSizes = map.getOutputSizes(SurfaceTexture.class);//获取预览尺寸
            Size[] captureSizes = map.getOutputSizes(ImageFormat.JPEG);//获取拍照尺寸 3120 4160
            //调整预览画面显示方向
//            configureTextureViewTransform(getrView(), getrView().getWidth(), getrView().getHeight(), previewSizes);
            cOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);//获取相机角度
            Log.e("sssssssss",cOrientation+"");
//            getrView().setRotation(90);
            Log.e("sssssssss2",cOrientation+"");
            Rect cRect = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);//获取成像区域
            cPixelSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);//获取成像尺寸，同上


            fpsRange = characteristics.get(
                    CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            if (fpsRange != null && fpsRange.length > 0) {

                for (int i = 0; i < fpsRange.length; i++) {
                    if (fpsRange[i].getLower() == 15 && fpsRange[i].getUpper() == 15) {
                        position = i;
                    }
                }

            }

            //可用于判断是否支持人脸检测，以及支持到哪种程度
            faceDetectModes = characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);//支持的人脸检测模式
            int maxFaceCount = characteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT);//支持的最大检测人脸数量

            try {
                Log.d(TAG, "openCamera openCamera openCamera");
                cManager.openCamera(cId, getCDeviceOpenCallback(), getCHandler());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureTextureViewTransform(TextureView mTextureView, int viewWidth, int viewHeight, Size[] mPreviewSize) {
        if (null == mTextureView) {
            return;
        }
        int rotation = 0;/*activity.getWindowManager().getDefaultDisplay().getRotation();*/
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize[0].getHeight(), mPreviewSize[0].getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize[0].getHeight(),
                    (float) viewWidth / mPreviewSize[0].getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private CameraCaptureSession.CaptureCallback getPreviewCallback() {
        if (previewCallback == null) {
            previewCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    onCameraImagePreviewed(result);
                }
            };
        }
        return previewCallback;
    }

    private ArrayList arrayList = new ArrayList();

    @SuppressLint("LongLogTag")
    private void onCameraImagePreviewed(CaptureResult result) {


        if (getrView() == null) {
            return;
        }
        if (result == null) {
            return;
        }
        Canvas canvas = null;
        try {
            Face[] faces = result.get(CaptureResult.STATISTICS_FACES);
            if (faces == null) {
                return;
            }


            try {
                canvas = getrView().lockCanvas();
                if (canvas == null || getrView() == null) {
                    return;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            } catch (Exception e) {
                Log.e(TAG, e + "get Exception on lockcanvas");
            }
            if (arrayList != null) {
                arrayList.clear();
            }

            for (int i = 0; i < faces.length; i++) {
                Rect fRect = faces[i].getBounds();

                float scaleWidth = canvas.getWidth() * 1.0f / cPixelSize.getWidth();//1912
                float scaleHeight = canvas.getHeight() * 1.0f / cPixelSize.getHeight();//1000

                float scaleWidth2 = getSurfaceView().getWidth() * 1.0f / canvas.getWidth();//1912
                float scaleHeight2 = getSurfaceView().getHeight() * 1.0f / canvas.getHeight();//1000

                int l = (int) (fRect.left * scaleWidth * scaleWidth2);
                int t = (int) (fRect.top * scaleHeight * scaleHeight2);
                int r = (int) (fRect.right * scaleWidth * scaleWidth2);
                int b = (int) (fRect.bottom * scaleHeight * scaleHeight2);

                Rect rect = new Rect(l, t, r, b);

                FaceWrapperInfo faceWrapper = new FaceWrapperInfo(faces[i], rect);
                arrayList.add(faceWrapper);
                if (captureListener != null && faces.length >= 1) {
                    captureListener.onCaptureResult(mData, arrayList);
                }
                if (isFront) {
                    canvas.drawRect(canvas.getWidth() - b, canvas.getHeight() - r,
                            canvas.getWidth() - t, canvas.getHeight() - l, getPaint());
//                    canvas.drawRect(l, t, r, b, getPaint());
                } else {
                    canvas.drawRect(l, t, r, b, getPaint());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                getrView().unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化画笔
     */
    private Paint getPaint() {
        if (pb == null) {
            pb = new Paint();
            pb.setColor(Color.WHITE);
            pb.setStrokeWidth(4);
            pb.setStyle(Paint.Style.STROKE);//使绘制的矩形中空
        }
        return pb;
    }

    private CaptureRequest getPreviewRequest() {
        previewRequest = getPreviewRequestBuilder().build();
        return previewRequest;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private CameraDevice.StateCallback getCDeviceOpenCallback() {
        if (cDeviceOpenCallback == null) {
            cDeviceOpenCallback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    cDevice = camera;
                    try {
                        //创建Session，需先完成画面呈现目标（此处为预览和拍照Surface）的初始化
                        camera.createCaptureSession(Arrays.asList(getPreviewSurface(), getCaptureSurface()), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession session) {
                                cSession = session;
                                previewRequest = getPreviewRequest();
                                if (previewRequest != null) {
                                    try {
                                        session.setRepeatingRequest(previewRequest, getPreviewCallback(), getCHandler());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //构建预览请求，并发起请求

                            }

                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClosed(CameraCaptureSession session) {
                                super.onClosed(session);
                                Log.e(TAG, "camera2 onClosed");
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession session) {
                                if (session != null) {
                                    session.close();
                                }
                            }
                        }, getCHandler());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    camera.close();
                }
            };
        }
        return cDeviceOpenCallback;
    }

    @SuppressLint("LongLogTag")
    private Surface getPreviewSurface() {

        Surface surface = getSurfaceView().getHolder().getSurface();
        Log.e(TAG, "getPreviewSurface ==" + surface);
        return surface;
    }

    private Handler getCHandler() {
        if (cHandler == null) {
            //单独开一个线程给相机使用
            cHandlerThread = new HandlerThread("cHandlerThread");
            cHandlerThread.start();
            cHandler = new Handler(cHandlerThread.getLooper());
        }
        return cHandler;
    }

    private int getFaceDetectMode() {
        return CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL;
    }


    private Surface captureSurface;

    private Surface getCaptureSurface() {
        if (cImageReader == null) {
            cImageReader = ImageReader.newInstance(PWIDTH, PHEIGHT, ImageFormat.YUV_420_888, 2);
            cImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    onCaptureFinished(reader);
                }
            }, getCHandler());
            captureSurface = cImageReader.getSurface();
        }
        return captureSurface;
    }

    private byte[] mData;

    private void onCaptureFinished(ImageReader reader) {
        Image image = null;
        image = reader.acquireLatestImage();
        if (image == null) {
            return;
        }
        mData = ImageUtil.getBytesFromImageAsType(image, 2);
        image.close();
    }


    private CaptureRequest.Builder getPreviewRequestBuilder() {
        if (cSession == null || cSession.getDevice() == null) {
            return null;
        }
        try {

            previewRequestBuilder = cSession.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(getPreviewSurface());
            previewRequestBuilder.addTarget(getCaptureSurface());
//                previewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, 30);
            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fpsRange[position]);
            previewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);//自动曝光、白平衡、对焦
            previewRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, getFaceDetectMode());//设置人脸检测级别
        } catch (Exception e) {
        }

        return previewRequestBuilder;
    }


    /**
     * 关闭相机
     */
    private void closeCamera() {
        try {
            if (cSession != null) {
                cSession.close();
                cSession = null;
            }
            if (cDevice != null) {
                cDevice.close();
                cDevice = null;
            }

            try {
                if (cImageReader != null) {
                    cImageReader.close();
                    cImageReader = null;
                }
            } catch (Exception e) {
            }
            if (cHandlerThread != null) {
                cHandlerThread.quitSafely();
                try {
                    cHandlerThread.join();
                    cHandlerThread = null;
                    cHandler = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
