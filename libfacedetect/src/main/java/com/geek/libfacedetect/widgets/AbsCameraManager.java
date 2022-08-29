package com.geek.libfacedetect.widgets;

import android.view.SurfaceView;
import android.view.TextureView;

import java.util.ArrayList;


public abstract class AbsCameraManager {

    private static AbsCameraManager manager;
    public static final int PWIDTH = 640;
    public static final int PHEIGHT = 480;
    /**
     * 预览view
     **/
    public SurfaceView surfaceView;

    /**
     * 人脸框view
     **/
    public TextureView rView;

    public synchronized static AbsCameraManager getInstance() {
        manager = Camera2ManagerImpl.getInstance();
        return manager;
    }

    public interface CaptureListener {
        void onCaptureResult(byte[] data, ArrayList<FaceWrapperInfo> arrayList);
    }

    public abstract void setCaptureListener(CaptureListener listener);


    public boolean isUseCamera2() {
        return manager instanceof Camera2ManagerImpl;
    }

    public void setPreviewView(SurfaceView sv) {
        this.surfaceView = sv;

    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public TextureView getrView() {
        return rView;
    }

    public void setrView(TextureView rView) {
        this.rView = rView;
    }

    public abstract void startCapture();

    public abstract void stopCapture();

    public void release() {
        rView = null;
        surfaceView = null;
        if (manager != null) {
            manager.destory();
        }
    }

    public abstract void destory();

}
