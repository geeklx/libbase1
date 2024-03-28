package coms.geek.libcamera1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import coms.luck.lib.camerax.BaseApp7;
import coms.luck.picture.lib.app.IApp;
import coms.luck.picture.lib.app.PictureAppMaster;
import coms.luck.picture.lib.basic.IBridgePictureBehavior;
import coms.luck.picture.lib.basic.PictureCommonFragment;
import coms.luck.picture.lib.basic.PictureSelectionCameraModel;
import coms.luck.picture.lib.basic.PictureSelector;
import coms.luck.picture.lib.config.PictureConfig;
import coms.luck.picture.lib.config.PictureMimeType;
import coms.luck.picture.lib.engine.ImageEngine;
import coms.luck.picture.lib.engine.PictureSelectorEngine;
import coms.luck.picture.lib.entity.LocalMedia;
import coms.luck.picture.lib.entity.MediaExtraInfo;
import coms.luck.picture.lib.interfaces.OnResultCallbackListener;
import coms.luck.picture.lib.style.PictureSelectorStyle;
import coms.luck.picture.lib.utils.MediaUtils;
import coms.luck.picture.lib.utils.PictureFileUtils;

/**
 * @author：luck
 * @data：2019/12/20 晚上 23:12
 * @描述: Demo
 */

public class SmCameraAct1 extends AppCompatActivity implements IBridgePictureBehavior {
    private final static int ACTIVITY_RESULT = 1;
    private final static int CALLBACK_RESULT = 2;
    private final static int LAUNCHER_RESULT = 3;

    private PictureSelectorStyle selectorStyle;
    private List<LocalMedia> mData = new ArrayList<>();
    private ActivityResultLauncher<Intent> launcherResult;
    private int resultMode = LAUNCHER_RESULT;
    private ImageEngine imageEngine;
    //
    private TextView tvpz1;
    private ImageView iv1111;
//    private List<LocalMedia> result1;


    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mData.size() > 0) {
            outState.putParcelableArrayList("selectorList", (ArrayList<? extends Parcelable>) mData);

        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventBus(BjyyBeanYewu3 event) {
        if (event.getId().equals("重拍")) {
            CameraUtils1.getInstance(SmCameraAct1.this).handleCameraSuccess(imageEngine, selectorStyle)
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(ArrayList<LocalMedia> result) {
                            mData = result;
                            String url = mData.get(0).getCutPath();
                            SPUtils.getInstance().put("camera_url", url);
                            Glide.with(SmCameraAct1.this).load(url).into(iv1111);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
            //

        }
        if (event.getId().equals("相册")) {
            if (TextUtils.isEmpty(event.getName())) {
                return;
            }
            Glide.with(SmCameraAct1.this).load(event.getName()).into(iv1111);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
//        ImmersionBar.with(this).fullScreen(false).statusBarColor(R.color.ucrop_color_black)
//                .navigationBarColor(R.color.ucrop_color_black).statusBarDarkFont(false).init();
        setContentView(R.layout.gactivity_camera111);
        //
        PictureAppMaster.getInstance().setApp(new IApp() {
            @Override
            public Context getAppContext() {
                return BaseApp7.get();
            }

            @Override
            public PictureSelectorEngine getPictureSelectorEngine() {
                return new PictureSelectorEngineImp();
            }
        });
        //
        selectorStyle = new PictureSelectorStyle();
        // 注册需要写在onCreate或Fragment onAttach里，否则会报java.lang.IllegalStateException异常
        launcherResult = createActivityResultLauncher();
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mData.clear();
            mData.addAll(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        imageEngine = GlideEngine1.createGlideEngine();
        // 清除缓存
//        clearCache();
        //
        tvpz1 = findViewById(R.id.tvpz1);
        iv1111 = findViewById(R.id.iv1111);
        tvpz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 拍照
//                result1 = new ArrayList<>();
//                        .setSelectedData(mData);
//                CameraUtils1.getInstance(SmCameraAct1.this).handleCameraSuccess(imageEngine, selectorStyle)
//                        .forResult(new OnResultCallbackListener<LocalMedia>() {
//                            @Override
//                            public void onResult(ArrayList<LocalMedia> result) {
//                                mData = result;
//                                String url = mData.get(0).getCutPath();
//                                SPUtils.getInstance().put("camera_url", url);
//                                if (TextUtils.isEmpty(url)) {
//                                    return;
//                                }
//                                Glide.with(SmCameraAct1.this).load(url).into(iv1111);
//                            }
//
//                            @Override
//                            public void onCancel() {
//
//                            }
//                        });
                //
                CameraUtils1.getInstance(SmCameraAct1.this).handleCameraSuccess(imageEngine, selectorStyle)
                        .forResultActivity(launcherResult);
            }
        });
    }

    private void forOnlyCameraResult(PictureSelectionCameraModel model) {
//        if (cb_attach_camera_mode.isChecked()) {
//            switch (resultMode) {
//                case ACTIVITY_RESULT:
//                    model.forResultActivity(PictureConfig.REQUEST_CAMERA);
//                    break;
//                case CALLBACK_RESULT:
//                    model.forResultActivity(new MainActivity.MeOnResultCallbackListener());
//                    break;
//                default:
//                    model.forResultActivity(launcherResult);
//                    break;
//            }
//        } else {
//            if (resultMode == CALLBACK_RESULT) {
//                model.forResult(new MainActivity.MeOnResultCallbackListener());
//            } else {
//                model.forResult();
//            }
//        }
    }


    @Override
    public void onSelectFinish(@Nullable PictureCommonFragment.SelectorResult result) {
        if (result == null) {
            return;
        }
        if (result.mResultCode == RESULT_OK) {
            ArrayList<LocalMedia> selectorResult = PictureSelector.obtainSelectorList(result.mResultData);
            analyticalSelectResults(selectorResult);
        } else if (result.mResultCode == RESULT_CANCELED) {
            Log.i("TAG", "onSelectFinish PictureSelector Cancel");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                analyticalSelectResults(result);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i("TAG", "onActivityResult PictureSelector Cancel");
        }
    }

    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    private ActivityResultLauncher<Intent> createActivityResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            ArrayList<LocalMedia> selectList = PictureSelector.obtainSelectorList(result.getData());
                            analyticalSelectResults(selectList);
                            //
                            mData = selectList;
                            String url = mData.get(0).getCutPath();
                            SPUtils.getInstance().put("camera_url", url);
                            if (TextUtils.isEmpty(url)) {
                                return;
                            }
                            Glide.with(SmCameraAct1.this).load(url).into(iv1111);
                        } else if (resultCode == RESULT_CANCELED) {
                            Log.i("TAG", "onActivityResult PictureSelector Cancel");
                        }
                    }
                });
    }


    /**
     * 处理选择结果
     *
     * @param result
     */
    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(SmCameraAct1.this, media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(SmCameraAct1.this, media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            Log.i("TAG", "文件名: " + media.getFileName());
            Log.i("TAG", "是否压缩:" + media.isCompressed());
            Log.i("TAG", "压缩:" + media.getCompressPath());
            Log.i("TAG", "初始路径:" + media.getPath());
            Log.i("TAG", "绝对路径:" + media.getRealPath());
            Log.i("TAG", "是否裁剪:" + media.isCut());
            Log.i("TAG", "裁剪路径:" + media.getCutPath());
            Log.i("TAG", "是否开启原图:" + media.isOriginal());
            Log.i("TAG", "原图路径:" + media.getOriginalPath());
            Log.i("TAG", "沙盒路径:" + media.getSandboxPath());
            Log.i("TAG", "水印路径:" + media.getWatermarkPath());
            Log.i("TAG", "视频缩略图:" + media.getVideoThumbnailPath());
            Log.i("TAG", "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i("TAG", "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i("TAG", "文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.getSize()));
            Log.i("TAG", "文件时长: " + media.getDuration());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                boolean isMaxSize = result.size() == mAdapter.getSelectMax();
//                int oldSize = mAdapter.getData().size();
//                mAdapter.notifyItemRangeRemoved(0, isMaxSize ? oldSize + 1 : oldSize);
//                mAdapter.getData().clear();
//
//                mAdapter.getData().addAll(result);
//                mAdapter.notifyItemRangeInserted(0, result.size());
                //
                mData.clear();
                mData.addAll(result);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
