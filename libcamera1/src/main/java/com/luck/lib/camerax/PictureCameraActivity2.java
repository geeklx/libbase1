package com.luck.lib.camerax;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.geek.libcamera1.BjyyBeanYewu3;
import com.geek.libcamera1.CameraUtils1;
import com.geek.libcamera1.GlideEngine1;
import com.geek.libcamera1.R;
import com.luck.lib.camerax.listener.CameraListener;
import com.luck.lib.camerax.listener.ClickListener;
import com.luck.lib.camerax.listener.IObtainCameraView;
import com.luck.lib.camerax.listener.ImageCallbackListener;
import com.luck.lib.camerax.listener.ImgClickListener;
import com.luck.lib.camerax.permissions.PermissionChecker;
import com.luck.lib.camerax.permissions.PermissionResultCallback;
import com.luck.lib.camerax.utils.SimpleXSpUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectorConfig;
import com.luck.picture.lib.config.SelectorProviders;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.luck.picture.lib.utils.DateUtils;
import com.luck.picture.lib.utils.FileDirMap;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.FileUtils;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author：luck
 * @date：2021/11/29 7:50 下午
 * @describe：PictureCameraActivity
 */
public class PictureCameraActivity2 extends AppCompatActivity implements IObtainCameraView {
    /**
     * PermissionResultCallback
     */
    private PermissionResultCallback mPermissionResultCallback;

    private RelativeLayout rl1;
    private CustomCameraView2 mCameraView;
    private ImageView ivtj1;
    private ImageView ivtj2;

    @SuppressLint("WrongConstant")
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
        setContentView(R.layout.ucrop_activity_photobox2);
        // 截图bufen
        rl1 = findViewById(R.id.rl1cv);
        mCameraView = findViewById(R.id.customCameraView1);
        ivtj1 = findViewById(R.id.ivtj1);
        ivtj2 = findViewById(R.id.ivtj2);
        //
        mCameraView.post(new Runnable() {
            @Override
            public void run() {
                mCameraView.setCameraConfig(getIntent());
            }
        });
        // 相册按钮回调bufen
        mCameraView.setImgClickListener(new ImgClickListener() {
            @Override
            public void onClick() {
                CameraUtils1.getInstance(PictureCameraActivity2.this).handleImgSuccess2(GlideEngine1.createGlideEngine(), new PictureSelectorStyle()).forResult(new OnResultCallbackListener<LocalMedia>() {
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
        });
        // 相机预览页面回调
        mCameraView.setImageCallbackListener(new ImageCallbackListener() {
            @Override
            public void onLoadImage(String url, ImageView imageView) {
                if (CustomCameraConfig.imageEngine != null) {
                    CustomCameraConfig.imageEngine.loadImage(imageView.getContext(), url, imageView);
                }

            }
        });
        // 获取相机预览后回调到剪切操作页面回调
        mCameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureSuccess(@NonNull String url) {
//                handleCameraSuccess();
                // 打开裁剪bufen
                if (!isfistin) {
                    isfistin = true;
                    setupViews(getIntent());
                } else {

                }
                SelectorConfig selectorConfig = SelectorProviders.getInstance().getSelectorConfig();
//                setImageData(getIntent(), selectorConfig.getSelectedResult());
                // 相机事件回调处理
                dispatchHandleCamera(getIntent(), selectorConfig);
                setInitialState();
                addBlockingView();

            }

            @Override
            public void onRecordSuccess(@NonNull String url) {
                handleCameraSuccess();
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                Toast.makeText(PictureCameraActivity2.this.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
        // 关闭页面回调
        mCameraView.setOnCancelClickListener(new ClickListener() {
            @Override
            public void onClick() {
                handleCameraCancel();
            }
        });
        // 裁剪bufen
        ivtj2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 提交裁剪后的图片
                cropAndSaveImage();

            }
        });
        // 重拍bufen
        ivtj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 返回
//                onBackPressed();
                if (mBlockingView != null) {
                    ((RelativeLayout) findViewById(R.id.ucrop_photobox)).removeView(mBlockingView);
                }
                rl1.setVisibility(View.GONE);
                mCameraView.setVisibility(View.VISIBLE);
                mCameraView.onCancelMedia();
            }
        });


    }

    private boolean isfistin;


    /**
     * 相机事件回调处理
     */
    private void dispatchHandleCamera(Intent intent, SelectorConfig selectorConfig) {
//        SelectorConfig selectorConfig = SelectorProviders.getInstance().getSelectorConfig();
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<LocalMedia>() {

            @Override
            public LocalMedia doInBackground() {
                String outputPath = getOutputPath(intent, selectorConfig);
                if (!TextUtils.isEmpty(outputPath)) {
                    selectorConfig.cameraPath = outputPath;
                }
                if (TextUtils.isEmpty(selectorConfig.cameraPath)) {
                    return null;
                }
                if (selectorConfig.chooseMode == SelectMimeType.ofAudio()) {
                    CameraUtils1.getInstance(PictureCameraActivity2.this).copyOutputAudioToDir(selectorConfig);
                }
                LocalMedia media = mCameraView.buildLocalMedia(selectorConfig.cameraPath, selectorConfig);
                media.setCameraSource(true);
                return media;
            }

            @Override
            public void onSuccess(LocalMedia result) {
                PictureThreadUtils.cancel(this);
                if (result != null) {
                    CameraUtils1.getInstance(PictureCameraActivity2.this).onScannerScanFile(PictureCameraActivity2.this, result);
                    //
                    List<LocalMedia> selectedResult = selectorConfig.getSelectedResult();
                    selectedResult.clear();
                    selectedResult.add(result);
                    ArrayList<LocalMedia> result1 = new ArrayList<>(selectedResult);
                    srcUri = null;
                    destinationUri = null;
                    onCrop(result1, intent);
                }
                selectorConfig.cameraPath = "";
            }
        });
    }

    Uri srcUri = null;
    Uri destinationUri = null;

    public void onCrop(ArrayList<LocalMedia> result, Intent intent) {
//        Uri srcUri = null;
//        Uri destinationUri = null;
        ArrayList<String> dataCropSource = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            LocalMedia media = result.get(i);
            dataCropSource.add(media.getAvailablePath());
            if (srcUri == null && PictureMimeType.isHasImage(media.getMimeType())) {
                String currentCropPath = media.getAvailablePath();
                if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(currentCropPath)) {
                    srcUri = Uri.parse(currentCropPath);
                } else {
                    srcUri = Uri.fromFile(new File(currentCropPath));
                }
                String fileName = DateUtils.getCreateFileName("CROP_") + ".jpg";
                File externalFilesDir = new File(FileDirMap.getFileDirPath(BaseApp7.get(), SelectMimeType.TYPE_IMAGE));
                File outputFile = new File(externalFilesDir.getAbsolutePath(), fileName);
                destinationUri = Uri.fromFile(outputFile);
            }
        }
//        selectorConfig.cropFileEngine.onStartCrop(this, srcUri, destinationUri, dataCropSource, Crop.REQUEST_CROP);
        //
        Uri inputUri = srcUri;
        Uri outputUri = destinationUri;
        processOptions(intent);

        if (inputUri != null && outputUri != null) {
            try {
                String outputDir = getIntent().getStringExtra(UCrop.Options.EXTRA_CROP_OUTPUT_DIR);
                if (!TextUtils.isEmpty(outputDir) && "file".equalsIgnoreCase(outputUri.getScheme())) {
                    File originalFile = new File(outputUri.getPath());
                    outputUri = Uri.fromFile(new File(outputDir, originalFile.getName()));
                }
                outputUri = FileUtils.replaceOutputUri(PictureCameraActivity2.this, isForbidCropGifWebp, inputUri, outputUri);
                mGestureCropImageView.setImageUri(inputUri, outputUri, isUseCustomBitmap);
            } catch (Exception e) {
                setResultError(e);
                finish();
            }
        } else {
            setResultError(new NullPointerException(getString(R.string.ucrop_error_input_data_is_absent)));
            finish();
        }
    }


    /**
     * 尝试匹配查找自定义相机返回的路径
     *
     * @param data
     * @return
     */
    protected String getOutputPath(Intent data, SelectorConfig selectorConfig) {
        if (data == null) {
            return null;
        }
        Uri outPutUri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        String cameraPath = selectorConfig.cameraPath;
        boolean isCameraFileExists = TextUtils.isEmpty(cameraPath) || PictureMimeType.isContent(cameraPath) || new File(cameraPath).exists();
        if ((selectorConfig.chooseMode == SelectMimeType.ofAudio() || !isCameraFileExists) && outPutUri == null) {
            outPutUri = data.getData();
        }
        if (outPutUri == null) {
            return null;
        }
        return PictureMimeType.isContent(outPutUri.toString()) ? outPutUri.toString() : outPutUri.getPath();
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
            PermissionChecker.getInstance().onRequestPermissionsResult(grantResults, mPermissionResultCallback);
            mPermissionResultCallback = null;
        }
    }

    @Override
    public ViewGroup getCustomCameraView() {
        return mCameraView;
    }


    /**
     * 裁剪bufen
     */

    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;

    @IntDef({NONE, SCALE, ROTATE, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {

    }

    private static final String TAG = "UCropActivity";
    private static final long CONTROLS_ANIMATION_DURATION = 50;
    private static final int TABS_COUNT = 3;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;

    private String mToolbarTitle;
    private int mToolbarTitleSize;
    private boolean isUseCustomBitmap;

    // Enables dynamic coloring
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mActiveControlsWidgetColor;
    private int mToolbarWidgetColor;
    @ColorInt
    private int mRootViewBackgroundColor;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    private int mLogoColor;

    private boolean mShowBottomControls;
    private boolean mShowLoader = true;
    private boolean isForbidCropGifWebp;

    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private ViewGroup mWrapperStateAspectRatio, mWrapperStateRotate, mWrapperStateScale;
    private ViewGroup mLayoutAspectRatio, mLayoutRotate, mLayoutScale;
    private List<ViewGroup> mCropAspectRatioViews = new ArrayList<>();
    private TextView mTextViewRotateAngle, mTextViewScalePercent;
    private View mBlockingView;

    private Transition mControlsTransition;

    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = DEFAULT_COMPRESS_QUALITY;
    private int[] mAllowedGestures = new int[]{SCALE, ROTATE, ALL};

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    protected void cropAndSaveImage() {
        mBlockingView.setClickable(true);
        mShowLoader = true;
        supportInvalidateOptionsMenu();

        mGestureCropImageView.cropAndSaveImage(mCompressFormat, mCompressQuality, new BitmapCropCallback() {

            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                setResultUri(resultUri, mGestureCropImageView.getTargetAspectRatio(), offsetX, offsetY, imageWidth, imageHeight);
                finish();
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                setResultError(t);
                finish();
            }
        });
    }


    private void setupViews(@NonNull Intent intent) {
        isForbidCropGifWebp = intent.getBooleanExtra(UCrop.Options.EXTRA_CROP_FORBID_GIF_WEBP, false);
        mStatusBarColor = intent.getIntExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_statusbar));
        mToolbarColor = intent.getIntExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar));
        mActiveControlsWidgetColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_COLOR_CONTROLS_WIDGET_ACTIVE, ContextCompat.getColor(this, R.color.ucrop_color_active_controls_color));

        mToolbarWidgetColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget));
        mToolbarCancelDrawable = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, R.drawable.ucrop_ic_cross);
        mToolbarCropDrawable = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, R.drawable.ucrop_ic_done);
        mToolbarTitle = intent.getStringExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitleSize = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_SIZE_TOOLBAR, 18);
        mToolbarTitle = mToolbarTitle != null ? mToolbarTitle : getResources().getString(R.string.ucrop_label_edit_photo);
        mLogoColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_LOGO_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_default_logo));
        mShowBottomControls = !intent.getBooleanExtra(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, false);
        mRootViewBackgroundColor = intent.getIntExtra(UCrop.Options.EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_crop_background));

        setupAppBar();
        initiateRootViews();
        // 剪切操作栏bufen
        if (mShowBottomControls) {

            ViewGroup viewGroup = findViewById(R.id.ucrop_photobox);
            ViewGroup wrapper = viewGroup.findViewById(R.id.controls_wrapper);
            wrapper.setVisibility(View.VISIBLE);
            LayoutInflater.from(this).inflate(R.layout.ucrop_controls, wrapper, true);

            mControlsTransition = new AutoTransition();
            mControlsTransition.setDuration(CONTROLS_ANIMATION_DURATION);

            mWrapperStateAspectRatio = findViewById(R.id.state_aspect_ratio);
            mWrapperStateAspectRatio.setOnClickListener(mStateClickListener);
            mWrapperStateRotate = findViewById(R.id.state_rotate);
            mWrapperStateRotate.setOnClickListener(mStateClickListener);
            mWrapperStateScale = findViewById(R.id.state_scale);
            mWrapperStateScale.setOnClickListener(mStateClickListener);

            mLayoutAspectRatio = findViewById(R.id.layout_aspect_ratio);
            mLayoutRotate = findViewById(R.id.layout_rotate_wheel);
            mLayoutScale = findViewById(R.id.layout_scale_wheel);

            setupAspectRatioWidget(intent);
            setupRotateWidget();// 横向旋转滚轮bufen
            setupScaleWidget();// 横向放大缩小滚轮bufen
            setupStatesWrapper();
        }
    }

    /**
     * This method extracts all data from the incoming intent and setups views properly.
     */
//    private void setImageData(@NonNull Intent intent, ArrayList<LocalMedia> mlist1) {
//        // 初始化裁剪路径bufen
//        // 裁剪视图bufen
////                    ArrayList<LocalMedia> selectedResult = selectorConfig.getSelectedResult();
//        ArrayList<LocalMedia> selectedResult = mlist1;
//        ArrayList<LocalMedia> result = new ArrayList<>(selectedResult);
//        Uri srcUri = null;
//        Uri destinationUri = null;
//        ArrayList<String> dataCropSource = new ArrayList<>();
//        for (int i = 0; i < result.size(); i++) {
//            LocalMedia media = result.get(i);
//            dataCropSource.add(media.getAvailablePath());
//            if (srcUri == null && PictureMimeType.isHasImage(media.getMimeType())) {
//                String currentCropPath = media.getAvailablePath();
//                if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(currentCropPath)) {
//                    srcUri = Uri.parse(currentCropPath);
//                } else {
//                    srcUri = Uri.fromFile(new File(currentCropPath));
//                }
//                String fileName = DateUtils.getCreateFileName("CROP_") + ".jpg";
////                    Context context = getAppContext();
//                File externalFilesDir = new File(FileDirMap.getFileDirPath(BaseApp7.get(),
//                        SelectMimeType.TYPE_IMAGE));
//                File outputFile = new File(externalFilesDir.getAbsolutePath(), fileName);
//                destinationUri = Uri.fromFile(outputFile);
//            }
//        }
//        intent.putExtra(UCrop.EXTRA_INPUT_URI, srcUri);
//        intent.putExtra(UCrop.EXTRA_OUTPUT_URI, destinationUri);
////        mCropOptionsBundle.putParcelable(EXTRA_OUTPUT_URI, destination);
//        //
//        Uri inputUri1 = intent.getParcelableExtra(UCrop.EXTRA_INPUT_URI);
//        Uri outputUri1 = intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
//        //
//        Uri inputUri = srcUri;
//        Uri outputUri = destinationUri;
//        processOptions(intent);
//
//        if (inputUri != null && outputUri != null) {
//            try {
//                String outputDir = getIntent().getStringExtra(UCrop.Options.EXTRA_CROP_OUTPUT_DIR);
//                if (!TextUtils.isEmpty(outputDir) && "file".equalsIgnoreCase(outputUri.getScheme())) {
//                    File originalFile = new File(outputUri.getPath());
//                    outputUri = Uri.fromFile(new File(outputDir, originalFile.getName()));
//                }
//                outputUri = FileUtils.replaceOutputUri(PictureCameraActivity2.this, isForbidCropGifWebp, inputUri, outputUri);
//                mGestureCropImageView.setImageUri(inputUri, outputUri, isUseCustomBitmap);
//            } catch (Exception e) {
//                setResultError(e);
//                finish();
//            }
//        } else {
//            setResultError(new NullPointerException(getString(R.string.ucrop_error_input_data_is_absent)));
//            finish();
//        }
//    }


    /**
     * Configures and styles both status bar and toolbar.
     */
    private void setupAppBar() {
//        setStatusBarColor(mStatusBarColor);
//
//        final Toolbar toolbar = findViewById(R.id.toolbar);
//
//        // Set all of the Toolbar coloring
//        toolbar.setBackgroundColor(mToolbarColor);
//        toolbar.setTitleTextColor(mToolbarWidgetColor);
//
//        final TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
//        toolbarTitle.setTextColor(mToolbarWidgetColor);
//        toolbarTitle.setText(mToolbarTitle);
//        toolbarTitle.setTextSize(mToolbarTitleSize);
//
//        // Color buttons inside the Toolbar
//        Drawable stateButtonDrawable = AppCompatResources.getDrawable(this, mToolbarCancelDrawable).mutate();
//        ColorFilter colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(mToolbarWidgetColor, BlendModeCompat.SRC_ATOP);
//        stateButtonDrawable.setColorFilter(colorFilter);
//        toolbar.setNavigationIcon(stateButtonDrawable);
//        setSupportActionBar(toolbar);
//        final ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
    }

    /**
     * Sets status-bar color for L devices.
     *
     * @param color - status-bar color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    private void initiateRootViews() {
        mUCropView = findViewById(R.id.ucrop);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();

        mGestureCropImageView.setTransformImageListener(mImageListener);

        ((ImageView) findViewById(R.id.image_view_logo)).setColorFilter(mLogoColor, PorterDuff.Mode.SRC_ATOP);

        findViewById(R.id.ucrop_frame).setBackgroundColor(mRootViewBackgroundColor);
        if (!mShowBottomControls) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.ucrop_frame).getLayoutParams();
            params.bottomMargin = 0;
            findViewById(R.id.ucrop_frame).requestLayout();
        }
    }


    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {
            setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
            setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            mUCropView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
            mBlockingView.setClickable(false);
            if (getIntent().getBooleanExtra(UCrop.Options.EXTRA_CROP_FORBID_GIF_WEBP, false)) {
//                Uri inputUri = getIntent().getParcelableExtra(UCrop.EXTRA_INPUT_URI);
                Log.e("ssssssssss", mBlockingView.isClickable() + "");
                Uri inputUri = srcUri;
                String mimeType = FileUtils.getMimeTypeFromMediaContentUri(PictureCameraActivity2.this, inputUri);
                if (FileUtils.isGif(mimeType) || FileUtils.isWebp(mimeType)) {
                    mBlockingView.setClickable(true);
                }
            }
            mShowLoader = false;
            supportInvalidateOptionsMenu();
            Log.e("ssssssssss", mBlockingView.isClickable() + "");
        }

        @Override
        public void onLoadFailure(@NonNull Exception e) {
            setResultError(e);
            finish();
        }

    };

    protected void setResultUri(Uri uri, float resultAspectRatio, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        Uri inputUri = getIntent().getParcelableExtra(UCrop.EXTRA_INPUT_URI);
        setResult(RESULT_OK, new Intent().putExtra(UCrop.EXTRA_OUTPUT_URI, uri).putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio).putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, imageWidth).putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, imageHeight).putExtra(UCrop.EXTRA_OUTPUT_OFFSET_X, offsetX).putExtra(UCrop.EXTRA_OUTPUT_OFFSET_Y, offsetY)
//                .putExtra(UCrop.EXTRA_CROP_INPUT_ORIGINAL, FileUtils.getInputPath(inputUri))
                .putExtra(UCrop.EXTRA_CROP_INPUT_ORIGINAL, FileUtils.getInputPath(srcUri)));
    }

    protected void setResultError(Throwable throwable) {
        setResult(UCrop.RESULT_ERROR, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }


    private void setupAspectRatioWidget(@NonNull Intent intent) {

        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioList == null || aspectRatioList.isEmpty()) {
            aspectRationSelectedByDefault = 2;

            aspectRatioList = new ArrayList<>();
            aspectRatioList.add(new AspectRatio(null, 1, 1));
            aspectRatioList.add(new AspectRatio(null, 3, 4));
            aspectRatioList.add(new AspectRatio(getString(R.string.ucrop_label_original).toUpperCase(), CropImageView.SOURCE_IMAGE_ASPECT_RATIO, CropImageView.SOURCE_IMAGE_ASPECT_RATIO));
            aspectRatioList.add(new AspectRatio(null, 3, 2));
            aspectRatioList.add(new AspectRatio(null, 16, 9));
        }

        LinearLayout wrapperAspectRatioList = findViewById(R.id.layout_aspect_ratio);
        wrapperAspectRatioList.removeAllViews();
//        wrapperAspectRatioList.setVisibility(View.VISIBLE);

        FrameLayout wrapperAspectRatio;
        AspectRatioTextView aspectRatioTextView;
        mCropAspectRatioViews = new ArrayList<>();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        for (AspectRatio aspectRatio : aspectRatioList) {
            wrapperAspectRatio = (FrameLayout) getLayoutInflater().inflate(R.layout.ucrop_aspect_ratio, null);
            wrapperAspectRatio.setLayoutParams(lp);
            aspectRatioTextView = ((AspectRatioTextView) wrapperAspectRatio.getChildAt(0));
            aspectRatioTextView.setActiveColor(mActiveControlsWidgetColor);
            aspectRatioTextView.setAspectRatio(aspectRatio);

            wrapperAspectRatioList.addView(wrapperAspectRatio);
            mCropAspectRatioViews.add(wrapperAspectRatio);
        }

        mCropAspectRatioViews.get(aspectRationSelectedByDefault).setSelected(true);// 选中原始比例bufen

        for (ViewGroup cropAspectRatioView : mCropAspectRatioViews) {
            cropAspectRatioView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView) ((ViewGroup) v).getChildAt(0)).getAspectRatio(v.isSelected()));
                    mGestureCropImageView.setImageToWrapCropBounds();
                    if (!v.isSelected()) {
                        for (ViewGroup cropAspectRatioView : mCropAspectRatioViews) {
                            cropAspectRatioView.setSelected(cropAspectRatioView == v);
                        }
                    }
                }
            });
        }
    }

    private void setupRotateWidget() {
        mTextViewRotateAngle = findViewById(R.id.text_view_rotate);
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScroll(float delta, float totalDistance) {
                mGestureCropImageView.postRotate(delta / ROTATE_WIDGET_SENSITIVITY_COEFFICIENT);
            }

            @Override
            public void onScrollEnd() {
                mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                mGestureCropImageView.cancelAllAnimations();
            }
        });

        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor(mActiveControlsWidgetColor);


        findViewById(R.id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRotation();
            }
        });
        findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateByAngle(90);
            }
        });

        setAngleTextColor(mActiveControlsWidgetColor);
    }

    private void setupScaleWidget() {
        mTextViewScalePercent = findViewById(R.id.text_view_scale);
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0) {
                    mGestureCropImageView.zoomInImage(mGestureCropImageView.getCurrentScale() + delta * ((mGestureCropImageView.getMaxScale() - mGestureCropImageView.getMinScale()) / SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                } else {
                    mGestureCropImageView.zoomOutImage(mGestureCropImageView.getCurrentScale() + delta * ((mGestureCropImageView.getMaxScale() - mGestureCropImageView.getMinScale()) / SCALE_WIDGET_SENSITIVITY_COEFFICIENT));
                }
            }

            @Override
            public void onScrollEnd() {
                mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override
            public void onScrollStart() {
                mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor(mActiveControlsWidgetColor);

        setScaleTextColor(mActiveControlsWidgetColor);
    }


    /**
     * Use {@link #mActiveControlsWidgetColor} for color filter
     */
    private void setupStatesWrapper() {
        ImageView stateScaleImageView = findViewById(R.id.image_view_state_scale);
        ImageView stateRotateImageView = findViewById(R.id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = findViewById(R.id.image_view_state_aspect_ratio);

        stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView.getDrawable(), mActiveControlsWidgetColor));
        stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView.getDrawable(), mActiveControlsWidgetColor));
        stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), mActiveControlsWidgetColor));
    }


    private void setAngleText(float angle) {
        if (mTextViewRotateAngle != null) {
            mTextViewRotateAngle.setText(String.format(Locale.getDefault(), "%.1f°", angle));
        }
    }

    private void setAngleTextColor(int textColor) {
        if (mTextViewRotateAngle != null) {
            mTextViewRotateAngle.setTextColor(textColor);
        }
    }

    private void setScaleText(float scale) {
        if (mTextViewScalePercent != null) {
            mTextViewScalePercent.setText(String.format(Locale.getDefault(), "%d%%", (int) (scale * 100)));
        }
    }

    private void setScaleTextColor(int textColor) {
        if (mTextViewScalePercent != null) {
            mTextViewScalePercent.setTextColor(textColor);
        }
    }

    private void resetRotation() {
        mGestureCropImageView.postRotate(-mGestureCropImageView.getCurrentAngle());
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        mGestureCropImageView.postRotate(angle);
        mGestureCropImageView.setImageToWrapCropBounds();
    }

    private final View.OnClickListener mStateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!v.isSelected()) {
                setWidgetState(v.getId());
            }
        }
    };

    private void setInitialState() {
        if (mShowBottomControls) {
            if (mWrapperStateAspectRatio.getVisibility() == View.VISIBLE) {
                setWidgetState(R.id.state_aspect_ratio);
            } else {
                setWidgetState(R.id.state_scale);
            }
        } else {
            setAllowedGestures(0);
        }
    }

    private void setWidgetState(@IdRes int stateViewId) {
        if (!mShowBottomControls) {
            return;
        }

        mWrapperStateAspectRatio.setSelected(stateViewId == R.id.state_aspect_ratio);
        mWrapperStateRotate.setSelected(stateViewId == R.id.state_rotate);
        mWrapperStateScale.setSelected(stateViewId == R.id.state_scale);

        mLayoutAspectRatio.setVisibility(stateViewId == R.id.state_aspect_ratio ? View.VISIBLE : View.GONE);
        mLayoutRotate.setVisibility(stateViewId == R.id.state_rotate ? View.VISIBLE : View.GONE);
        mLayoutScale.setVisibility(stateViewId == R.id.state_scale ? View.VISIBLE : View.GONE);

        changeSelectedTab(stateViewId);

        if (stateViewId == R.id.state_scale) {
            setAllowedGestures(0);
        } else if (stateViewId == R.id.state_rotate) {
            setAllowedGestures(1);
        } else {
            setAllowedGestures(2);
        }
    }

    private void changeSelectedTab(int stateViewId) {
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.ucrop_photobox), mControlsTransition);

        mWrapperStateScale.findViewById(R.id.text_view_scale).setVisibility(stateViewId == R.id.state_scale ? View.VISIBLE : View.GONE);
        mWrapperStateAspectRatio.findViewById(R.id.text_view_crop).setVisibility(stateViewId == R.id.state_aspect_ratio ? View.VISIBLE : View.GONE);
        mWrapperStateRotate.findViewById(R.id.text_view_rotate).setVisibility(stateViewId == R.id.state_rotate ? View.VISIBLE : View.GONE);

    }

    private void setAllowedGestures(int tab) {
        mGestureCropImageView.setScaleEnabled(mAllowedGestures[tab] == ALL || mAllowedGestures[tab] == SCALE);
        mGestureCropImageView.setRotateEnabled(mAllowedGestures[tab] == ALL || mAllowedGestures[tab] == ROTATE);
        mGestureCropImageView.setGestureEnabled(getIntent().getBooleanExtra(UCrop.Options.EXTRA_DRAG_IMAGES, true));
    }

    /**
     * Adds view that covers everything below the Toolbar.
     * When it's clickable - user won't be able to click/touch anything below the Toolbar.
     * Need to block user input while loading and cropping an image.
     */
    private void addBlockingView() {
        if (mBlockingView == null) {
            mBlockingView = new View(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.rlfh1);
            mBlockingView.setLayoutParams(lp);
            mBlockingView.setClickable(true);
        }
//        ((RelativeLayout) findViewById(R.id.ucrop_photobox)).removeView(mBlockingView);
        ((RelativeLayout) findViewById(R.id.ucrop_photobox)).addView(mBlockingView);
        // 重置裁剪页面参数bufen
        rl1.setVisibility(View.VISIBLE);
        mCameraView.setVisibility(View.GONE);
    }


    /**
     * This method extracts {@link UCrop.Options #optionsBundle} from incoming intent
     * and setups Activity, {@link OverlayView} and {@link CropImageView} properly.
     */
    @SuppressWarnings("deprecation")
    private void processOptions(@NonNull Intent intent) {
        // Bitmap compression options
        String compressionFormatName = intent.getStringExtra(UCrop.Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        mCompressFormat = (compressFormat == null) ? DEFAULT_COMPRESS_FORMAT : compressFormat;

        mCompressQuality = intent.getIntExtra(UCrop.Options.EXTRA_COMPRESSION_QUALITY, UCropActivity.DEFAULT_COMPRESS_QUALITY);

        // Gestures options
        int[] allowedGestures = intent.getIntArrayExtra(UCrop.Options.EXTRA_ALLOWED_GESTURES);
        if (allowedGestures != null && allowedGestures.length == TABS_COUNT) {
            mAllowedGestures = allowedGestures;
        }

        isUseCustomBitmap = intent.getBooleanExtra(UCrop.Options.EXTRA_CROP_CUSTOM_LOADER_BITMAP, false);

        // Crop image view options
        mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(UCrop.Options.EXTRA_MAX_BITMAP_SIZE, CropImageView.DEFAULT_MAX_BITMAP_SIZE));
        mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER, CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER));
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));

        // Overlay view options
        mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra(UCrop.Options.EXTRA_FREE_STYLE_CROP, OverlayView.DEFAULT_FREESTYLE_CROP_MODE != OverlayView.FREESTYLE_CROP_MODE_DISABLE));
        mOverlayView.setDragSmoothToCenter(intent.getBooleanExtra(UCrop.Options.EXTRA_CROP_DRAG_CENTER, false));
        mOverlayView.setDimmedColor(intent.getIntExtra(UCrop.Options.EXTRA_DIMMED_LAYER_COLOR, getResources().getColor(R.color.ucrop_color_default_dimmed)));
        mOverlayView.setCircleStrokeColor(intent.getIntExtra(UCrop.Options.EXTRA_CIRCLE_STROKE_COLOR, getResources().getColor(R.color.ucrop_color_default_dimmed)));
        mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra(UCrop.Options.EXTRA_CIRCLE_DIMMED_LAYER, OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER));

        mOverlayView.setShowCropFrame(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_FRAME, OverlayView.DEFAULT_SHOW_CROP_FRAME));
        mOverlayView.setCropFrameColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_COLOR, getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));

        mOverlayView.setShowCropGrid(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_GRID, OverlayView.DEFAULT_SHOW_CROP_GRID));
        mOverlayView.setCropGridRowCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_ROW_COUNT, OverlayView.DEFAULT_CROP_GRID_ROW_COUNT));
        mOverlayView.setCropGridColumnCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLUMN_COUNT, OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT));
        mOverlayView.setCropGridColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLOR, getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));
        mOverlayView.setDimmedStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CIRCLE_STROKE_WIDTH_LAYER, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));
        // Aspect ratio options
        float aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, -1);
        float aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, -1);

        int aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList<AspectRatio> aspectRatioList = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS);

        if (aspectRatioX >= 0 && aspectRatioY >= 0) {
            if (mWrapperStateAspectRatio != null) {
                mWrapperStateAspectRatio.setVisibility(View.GONE);
            }
            float targetAspectRatio = aspectRatioX / aspectRatioY;
            mGestureCropImageView.setTargetAspectRatio(Float.isNaN(targetAspectRatio) ? CropImageView.SOURCE_IMAGE_ASPECT_RATIO : targetAspectRatio);
        } else if (aspectRatioList != null && aspectRationSelectedByDefault < aspectRatioList.size()) {
            float targetAspectRatio = aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioX() / aspectRatioList.get(aspectRationSelectedByDefault).getAspectRatioY();
            mGestureCropImageView.setTargetAspectRatio(Float.isNaN(targetAspectRatio) ? CropImageView.SOURCE_IMAGE_ASPECT_RATIO : targetAspectRatio);
        } else {
            mGestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
        }

        // Result bitmap max size options
        int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
        int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);

        if (maxSizeX > 0 && maxSizeY > 0) {
            mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }


}
