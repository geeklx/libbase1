package tech.huqi.smartopencv.core.bridge;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;

import org.opencv.android.CameraBridgeViewBase3;

import static org.opencv.android.CameraBridgeViewBase3.CAMERA_ID_BACK;
import static org.opencv.android.CameraBridgeViewBase3.CAMERA_ID_FRONT;

/**
 * Created by hzhuqi on 2019/9/6
 */
public class CameraViewBridgeImpl extends CameraBridgeViewWrapper {
    public CameraViewBridgeImpl(CameraBridgeViewBase3 base, SurfaceHolder holder) {
        super(base, holder);
    }

    @Override
    public void setUseFrontCamera(boolean isUseFrontCamera) {
        this.isUseFrontCamera = isUseFrontCamera;
        if (isUseFrontCamera) {
            mBase.setCameraIndex(CAMERA_ID_FRONT);
        } else {
            mBase.setCameraIndex(CAMERA_ID_BACK);
        }
    }

    public void setLandscape(boolean isLandscape) {
        this.isSetLandscape = isLandscape;
    }

    public void setIsUsbCamera() {
        this.isUsbCamera = true;
    }

    public void setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.mBitmapConfig = bitmapConfig;
    }
}
