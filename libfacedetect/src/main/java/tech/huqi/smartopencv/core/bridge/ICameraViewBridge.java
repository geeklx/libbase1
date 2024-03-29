package tech.huqi.smartopencv.core.bridge;

import org.opencv.android.CameraBridgeViewBase3;

/**
 * Created by hzhuqi on 2019/9/6
 */

/**
 * SmartOpenCV mock OpenCV CameraBridgeViewBase3类的功能接口
 * 1）定义需要改写的OpenCV的关键同名接口
 * 2）功能增强的新增接口
 */
public interface ICameraViewBridge {
    void setCvCameraViewListener(CameraBridgeViewBase3.CvCameraViewListener2 listener);

    void setCameraIndex(int cameraIndex);

    void setUseFrontCamera(boolean isUseFrontCamera);

    void disableView();

    void AllocateCache(int frameWidth, int frameHeight);

    void deliverAndDrawFrame(CameraBridgeViewBase3.CvCameraViewFrame frame);
}
