package coms.luck.lib.camerax;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import coms.geek.libcamera1.BjyyBeanYewu3;
import coms.geek.libcamera1.CameraUtils1;
import coms.geek.libcamera1.GlideEngine1;
import coms.luck.lib.camerax.listener.CameraListener;
import coms.luck.lib.camerax.listener.ClickListener;
import coms.luck.lib.camerax.listener.IObtainCameraView;
import coms.luck.lib.camerax.listener.ImageCallbackListener;
import coms.luck.lib.camerax.permissions.PermissionChecker;
import coms.luck.lib.camerax.permissions.PermissionResultCallback;
import coms.luck.lib.camerax.utils.SimpleXSpUtils;
import coms.luck.picture.lib.entity.LocalMedia;
import coms.luck.picture.lib.style.PictureSelectorStyle;
import coms.luck.picture.lib.interfaces.OnResultCallbackListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * @author：luck
 * @date：2021/11/29 7:50 下午
 * @describe：PictureCameraActivity
 */
public class PictureCameraActivity extends AppCompatActivity implements IObtainCameraView {
    /**
     * PermissionResultCallback
     */
    private PermissionResultCallback mPermissionResultCallback;

    private CustomCameraView mCameraView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mCameraView = new CustomCameraView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mCameraView.setLayoutParams(layoutParams);
        setContentView(mCameraView);
        mCameraView.post(new Runnable() {
            @Override
            public void run() {
                mCameraView.setCameraConfig(getIntent());
            }
        });
        mCameraView.setImageCallbackListener(new ImageCallbackListener() {
            @Override
            public void onLoadImage(String url, ImageView imageView) {
                if (CustomCameraConfig.imageEngine != null) {
                    CustomCameraConfig.imageEngine.loadImage(imageView.getContext(), url, imageView);
                }
                // 拿到拍照的图片了


            }
        });
        mCameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureSuccess(@NonNull String url) {
                handleCameraSuccess();
            }

            @Override
            public void onRecordSuccess(@NonNull String url) {
                handleCameraSuccess();
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message,
                                @Nullable Throwable cause) {
                Toast.makeText(PictureCameraActivity.this.getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        });

        mCameraView.setOnCancelClickListener(new ClickListener() {
            @Override
            public void onClick() {
                handleCameraCancel();
            }
        });

    }


    private void handleImgSuccess() {
//        CameraUtils1.getInstance(PictureCameraActivity.this).handleImgSuccess(new PictureSelectorStyle())
//                .forSystemResultActivity(new OnResultCallbackListener<LocalMedia>() {
//                    @Override
//                    public void onResult(ArrayList<LocalMedia> result) {
//                        // 相册返回结果
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
        CameraUtils1.getInstance(PictureCameraActivity.this).handleImgSuccess2(GlideEngine1.createGlideEngine(), new PictureSelectorStyle())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        // 相册返回结果
                        String url = result.get(0).getCutPath();
                        if (TextUtils.isEmpty(url)) {
                            return;
                        }
                        EventBus.getDefault().post(new BjyyBeanYewu3("相册", url + ""));
                        onBackPressed();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    private void handleCameraSuccess() {
        Uri uri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        Intent intent = new Intent();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        setResult(RESULT_OK, getIntent());
        onBackPressed();
    }

    private void handleCameraCancel() {
        setResult(RESULT_CANCELED);
        onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            mCameraView.onCancelMedia();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mCameraView.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        CustomCameraConfig.destroy();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CustomCameraConfig.explainListener != null) {
            CustomCameraConfig.explainListener.onDismiss(mCameraView);
        }
        if (requestCode == PermissionChecker.PERMISSION_SETTING_CODE) {
            if (PermissionChecker.checkSelfPermission(this, new String[]{Manifest.permission.CAMERA})) {
                mCameraView.buildUseCameraCases();
            } else {
                SimpleXSpUtils.putBoolean(this, Manifest.permission.CAMERA, true);
                handleCameraCancel();
            }
        } else if (requestCode == PermissionChecker.PERMISSION_RECORD_AUDIO_SETTING_CODE) {
            if (!PermissionChecker.checkSelfPermission(this, new String[]{Manifest.permission.RECORD_AUDIO})) {
                SimpleXSpUtils.putBoolean(this, Manifest.permission.RECORD_AUDIO, true);
                Toast.makeText(getApplicationContext(), "Missing recording permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Set PermissionResultCallback
     *
     * @param callback
     */
    public void setPermissionsResultAction(PermissionResultCallback callback) {
        mPermissionResultCallback = callback;
    }

    @Override
    protected void onDestroy() {
        mCameraView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionResultCallback != null) {
            PermissionChecker.getInstance()
                    .onRequestPermissionsResult(grantResults, mPermissionResultCallback);
            mPermissionResultCallback = null;
        }
    }

    @Override
    public ViewGroup getCustomCameraView() {
        return mCameraView;
    }
}
