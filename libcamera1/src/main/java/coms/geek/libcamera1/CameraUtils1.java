package coms.geek.libcamera1;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import coms.luck.lib.camerax.BaseApp7;
import coms.luck.lib.camerax.CameraImageEngine;
import coms.luck.lib.camerax.SimpleCameraX;
import coms.luck.lib.camerax.listener.OnSimpleXPermissionDeniedListener;
import coms.luck.lib.camerax.listener.OnSimpleXPermissionDescriptionListener;
import coms.luck.lib.camerax.permissions.SimpleXPermissionUtil;
import coms.luck.picture.lib.basic.PictureContentResolver;
import coms.luck.picture.lib.basic.PictureMediaScannerConnection;
import coms.luck.picture.lib.basic.PictureSelectionCameraModel;
import coms.luck.picture.lib.basic.PictureSelectionModel;
import coms.luck.picture.lib.basic.PictureSelectionSystemModel;
import coms.luck.picture.lib.basic.PictureSelector;
import coms.luck.picture.lib.config.Crop;
import coms.luck.picture.lib.config.PictureMimeType;
import coms.luck.picture.lib.config.SelectLimitType;
import coms.luck.picture.lib.config.SelectMimeType;
import coms.luck.picture.lib.config.SelectModeConfig;
import coms.luck.picture.lib.config.SelectorConfig;
import coms.luck.picture.lib.dialog.RemindDialog;
import coms.luck.picture.lib.engine.CompressFileEngine;
import coms.luck.picture.lib.engine.CropFileEngine;
import coms.luck.picture.lib.engine.ImageEngine;
import coms.luck.picture.lib.engine.UriToFileTransformEngine;
import coms.luck.picture.lib.entity.LocalMedia;
import coms.luck.picture.lib.interfaces.OnBitmapWatermarkEventListener;
import coms.luck.picture.lib.interfaces.OnCameraInterceptListener;
import coms.luck.picture.lib.interfaces.OnCustomLoadingListener;
import coms.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import coms.luck.picture.lib.interfaces.OnPermissionDescriptionListener;
import coms.luck.picture.lib.interfaces.OnSelectLimitTipsListener;
import coms.luck.picture.lib.language.LanguageConfig;
import coms.luck.picture.lib.permissions.PermissionConfig;
import coms.luck.picture.lib.style.PictureSelectorStyle;
import coms.luck.picture.lib.style.SelectMainStyle;
import coms.luck.picture.lib.style.TitleBarStyle;
import coms.luck.picture.lib.utils.ActivityCompatHelper;
import coms.luck.picture.lib.utils.DateUtils;
import coms.luck.picture.lib.utils.DensityUtil;
import coms.luck.picture.lib.utils.MediaUtils;
import coms.luck.picture.lib.utils.PictureFileUtils;
import coms.luck.picture.lib.utils.SandboxTransformUtils;
import coms.luck.picture.lib.utils.SdkVersionUtils;
import coms.luck.picture.lib.utils.StyleUtils;
import coms.luck.picture.lib.utils.ToastUtils;
import coms.luck.picture.lib.widget.MediumBoldTextView;
import coms.yalantis.ucrop.UCrop;
import coms.yalantis.ucrop.UCropImageEngine;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;
import top.zibin.luban.OnRenameListener;

public class CameraUtils1 {
    private static volatile CameraUtils1 instance;
    private Context mContext;

    private final static String TAG_EXPLAIN_VIEW = "TAG_EXPLAIN_VIEW";

    public CameraUtils1(Context context) {
        mContext = context;
    }

    public static CameraUtils1 getInstance(Context context) {
        if (instance == null) {
            synchronized (CameraUtils1.class) {
                instance = new CameraUtils1(context);
            }
        }
        return instance;
    }


    /**
     * 创建自定义输出目录
     *
     * @return
     */
    public String getSandboxMarkDir() {
        File externalFilesDir = mContext.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Mark");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }


    /**
     * 压缩引擎
     *
     * @return
     */
    public ImageFileCompressEngine getCompressFileEngine(boolean cb_compress) {
        return cb_compress ? new ImageFileCompressEngine() : null;
    }

    /**
     * 自定义压缩
     */
    public class ImageFileCompressEngine implements CompressFileEngine {

        @Override
        public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
            Luban.with(context).load(source).ignoreBy(100).setRenameListener(new OnRenameListener() {
                @Override
                public String rename(String filePath) {
                    int indexOf = filePath.lastIndexOf(".");
                    String postfix = indexOf != -1 ? filePath.substring(indexOf) : ".jpg";
                    return DateUtils.getCreateFileName("CMP_") + postfix;
                }
            }).filter(new CompressionPredicate() {
                @Override
                public boolean apply(String path) {
                    if (PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path)) {
                        return true;
                    }
                    return !PictureMimeType.isUrlHasGif(path);
                }
            }).setCompressListener(new OnNewCompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(String source, File compressFile) {
                    if (call != null) {
                        call.onCallback(source, compressFile.getAbsolutePath());
                    }
                }

                @Override
                public void onError(String source, Throwable e) {
                    if (call != null) {
                        call.onCallback(source, null);
                    }
                }
            }).launch();
        }
    }

    /**
     * 创建相机自定义输出目录
     *
     * @return
     */
    public String getSandboxCameraOutputPath(boolean sandbox) {
        if (sandbox) {
            File externalFilesDir = mContext.getExternalFilesDir("");
            File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
            if (!customFile.exists()) {
                customFile.mkdirs();
            }
            return customFile.getAbsolutePath() + File.separator;
        } else {
            return "";
        }
    }

    /**
     * 拦截自定义提示
     *
     * @return
     */
    public OnSelectLimitTipsListener getMeOnSelectLimitTipsListener(boolean cb_compress) {
        return cb_compress ? new MeOnSelectLimitTipsListener() : null;
    }

    /**
     * 拦截自定义提示
     */
    public class MeOnSelectLimitTipsListener implements OnSelectLimitTipsListener {


        @Override
        public boolean onSelectLimitTips(Context context, @Nullable LocalMedia media, SelectorConfig config, int limitType) {
            if (limitType == SelectLimitType.SELECT_MIN_SELECT_LIMIT) {
                ToastUtils.showToast(context, "图片最少不能低于" + config.minSelectNum + "张");
                return true;
            } else if (limitType == SelectLimitType.SELECT_MIN_VIDEO_SELECT_LIMIT) {
                ToastUtils.showToast(context, "视频最少不能低于" + config.minVideoSelectNum + "个");
                return true;
            } else if (limitType == SelectLimitType.SELECT_MIN_AUDIO_SELECT_LIMIT) {
                ToastUtils.showToast(context, "音频最少不能低于" + config.minAudioSelectNum + "个");
                return true;
            }
            return false;
        }
    }

    /**
     * 给图片添加水印
     */
    public OnBitmapWatermarkEventListener getAddBitmapWatermarkListener(boolean cb_watermark) {
        return cb_watermark ? new MeBitmapWatermarkEventListener(getSandboxMarkDir()) : null;
    }

    /**
     * 给图片添加水印
     */
    public class MeBitmapWatermarkEventListener implements OnBitmapWatermarkEventListener {
        private final String targetPath;

        public MeBitmapWatermarkEventListener(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public void onAddBitmapWatermark(Context context, String srcPath, String mimeType, OnKeyValueResultCallbackListener call) {
            if (PictureMimeType.isHasHttp(srcPath) || PictureMimeType.isHasVideo(mimeType)) {
                // 网络图片和视频忽略，有需求的可自行扩展
                call.onCallback(srcPath, "");
            } else {
                // 暂时只以图片为例
                Glide.with(context).asBitmap().sizeMultiplier(0.6F).load(srcPath).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Bitmap watermark = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_mark_win);
                        Bitmap watermarkBitmap = ImageUtil1.createWaterMaskRightTop(context, resource, watermark, 15, 15);
                        watermarkBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                        watermarkBitmap.recycle();
                        FileOutputStream fos = null;
                        String result = null;
                        try {
                            File targetFile = new File(targetPath, DateUtils.getCreateFileName("Mark_") + ".jpg");
                            fos = new FileOutputStream(targetFile);
                            fos.write(stream.toByteArray());
                            fos.flush();
                            result = targetFile.getAbsolutePath();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            PictureFileUtils.close(fos);
                            PictureFileUtils.close(stream);
                        }
                        if (call != null) {
                            call.onCallback(srcPath, result);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        if (call != null) {
                            call.onCallback(srcPath, "");
                        }
                    }
                });
            }
        }
    }

    /**
     * 自定义loading
     *
     * @return
     */
    public OnCustomLoadingListener getCustomLoadingListener(boolean cb_custom_loading) {
        if (cb_custom_loading) {
            return new OnCustomLoadingListener() {
                @Override
                public Dialog create(Context context) {
                    return new CustomLoadingDialog1(context);
                }
            };
        }
        return null;
    }


    /**
     * 自定义沙盒文件处理
     */
    public UriToFileTransformEngine getMeSandboxFileEngine(boolean cb_watermark) {
        return cb_watermark ? new MeSandboxFileEngine() : null;
    }

    /**
     * 自定义沙盒文件处理
     */
    public class MeSandboxFileEngine implements UriToFileTransformEngine {

        @Override
        public void onUriToFileAsyncTransform(Context context, String srcPath, String mineType, OnKeyValueResultCallbackListener call) {
            if (call != null) {
                call.onCallback(srcPath, SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType));
            }
        }
    }


    /**
     * 权限说明
     *
     * @return
     */
    public OnPermissionDescriptionListener getPermissionDescriptionListener(boolean cb_permission_desc) {
        return cb_permission_desc ? new MeOnPermissionDescriptionListener() : null;
    }

    /**
     * 添加权限说明
     */
    public class MeOnPermissionDescriptionListener implements OnPermissionDescriptionListener {

        @Override
        public void onPermissionDescription(Fragment fragment, String[] permissionArray) {
            View rootView = fragment.requireView();
            if (rootView instanceof ViewGroup) {
                addPermissionDescription(false, (ViewGroup) rootView, permissionArray);
            }
        }

        @Override
        public void onDismiss(Fragment fragment) {
            removePermissionDescription((ViewGroup) fragment.requireView());
        }
    }

    /**
     * 添加权限说明
     *
     * @param viewGroup
     * @param permissionArray
     */
    public void addPermissionDescription(boolean isHasSimpleXCamera, ViewGroup viewGroup, String[] permissionArray) {
        int dp10 = DensityUtil.dip2px(viewGroup.getContext(), 10);
        int dp15 = DensityUtil.dip2px(viewGroup.getContext(), 15);
        MediumBoldTextView view = new MediumBoldTextView(viewGroup.getContext());
        view.setTag(TAG_EXPLAIN_VIEW);
        view.setTextSize(14);
        view.setTextColor(Color.parseColor("#333333"));
        view.setPadding(dp10, dp15, dp10, dp15);

        String title;
        String explain;

        if (TextUtils.equals(permissionArray[0], PermissionConfig.CAMERA[0])) {
            title = "相机权限使用说明";
            explain = "相机权限使用说明\n用户app用于拍照/录视频";
        } else if (TextUtils.equals(permissionArray[0], Manifest.permission.RECORD_AUDIO)) {
            if (isHasSimpleXCamera) {
                title = "麦克风权限使用说明";
                explain = "麦克风权限使用说明\n用户app用于录视频时采集声音";
            } else {
                title = "录音权限使用说明";
                explain = "录音权限使用说明\n用户app用于采集声音";
            }
        } else {
            title = "存储权限使用说明";
            explain = "存储权限使用说明\n用户app写入/下载/保存/读取/修改/删除图片、视频、文件等信息";
        }
        int startIndex = 0;
        int endOf = startIndex + title.length();
        SpannableStringBuilder builder = new SpannableStringBuilder(explain);
        builder.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(viewGroup.getContext(), 16)), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(0xFF333333), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.setText(builder);
        view.setBackground(ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.ps_demo_permission_desc_bg));

        if (isHasSimpleXCamera) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = DensityUtil.getStatusBarHeight(viewGroup.getContext());
            layoutParams.leftMargin = dp10;
            layoutParams.rightMargin = dp10;
            viewGroup.addView(view, layoutParams);
        } else {
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topToBottom = R.id.title_bar;
            layoutParams.leftToLeft = ConstraintSet.PARENT_ID;
            layoutParams.leftMargin = dp10;
            layoutParams.rightMargin = dp10;
            viewGroup.addView(view, layoutParams);
        }
    }

    /**
     * 移除权限说明
     *
     * @param viewGroup
     */
    public void removePermissionDescription(ViewGroup viewGroup) {
        View tagExplainView = viewGroup.findViewWithTag(TAG_EXPLAIN_VIEW);
        viewGroup.removeView(tagExplainView);
    }


    /**
     * 创建音频自定义输出目录
     *
     * @return
     */
    public String getSandboxAudioOutputPath(boolean cb_custom_sandbox) {
        if (cb_custom_sandbox) {
            File externalFilesDir = mContext.getExternalFilesDir("");
            File customFile = new File(externalFilesDir.getAbsolutePath(), "Sound");
            if (!customFile.exists()) {
                customFile.mkdirs();
            }
            return customFile.getAbsolutePath() + File.separator;
        } else {
            return "";
        }
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    public UCrop.Options BuildOptions(PictureSelectorStyle selectorStyle) {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setShowCropFrame(true);
        options.setShowCropGrid(true);
        options.setCircleDimmedLayer(false);
        options.withAspectRatio(-1, -1);
        options.setCropOutputPathDir(getSandboxPath());
        options.isCropDragSmoothToCenter(false);
        options.setSkipCropMimeType(getNotSupportCrop(true));
        options.isForbidCropGifWebp(true);
        options.isForbidSkipMultipleCrop(true);
        options.setMaxScaleMultiplier(100);
        //
//        camera.setHideBottomControls(false);
//        camera.setFreeStyleCropEnabled(true);
//        camera.setShowCropFrame(true);
//        camera.setShowCropGrid(true);
//        camera.setCircleDimmedLayer(false);
//        camera.withAspectRatio(-1, -1);
//        camera.setCropOutputPathDir(getSandboxPath());
//        camera.isCropDragSmoothToCenter(false);
//        camera.setSkipCropMimeType(getNotSupportCrop(true));
//        camera.isForbidCropGifWebp(true);
//        camera.isForbidSkipMultipleCrop(true);
//        camera.setMaxScaleMultiplier(100);

        if (selectorStyle != null && selectorStyle.getSelectMainStyle().getStatusBarColor() != 0) {
            SelectMainStyle mainStyle = selectorStyle.getSelectMainStyle();
            boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
            int statusBarColor = mainStyle.getStatusBarColor();
            options.isDarkStatusBarBlack(isDarkStatusBarBlack);
            if (StyleUtils.checkStyleValidity(statusBarColor)) {
                options.setStatusBarColor(statusBarColor);
                options.setToolbarColor(statusBarColor);
            } else {
                options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
                options.setToolbarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
            }
            TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
            if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleTextColor())) {
                options.setToolbarWidgetColor(titleBarStyle.getTitleTextColor());
            } else {
                options.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.ps_color_white));
            }
        } else {
            options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
            options.setToolbarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
            options.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.ps_color_white));
        }
        return options;
    }

    public String[] getNotSupportCrop(boolean cb_skip_not_gif) {
        if (cb_skip_not_gif) {
            return new String[]{PictureMimeType.ofGIF(), PictureMimeType.ofWEBP()};
        }
        return null;
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    public String getSandboxPath() {
        File externalFilesDir = mContext.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }


    /**
     * SimpleCameraX权限拒绝后回调
     *
     * @return
     */
    public OnSimpleXPermissionDeniedListener getSimpleXPermissionDeniedListener(boolean cb_permission_desc) {
        return cb_permission_desc ? new MeOnSimpleXPermissionDeniedListener() : null;
    }

    /**
     * SimpleCameraX添加权限说明
     */
    public class MeOnSimpleXPermissionDeniedListener implements OnSimpleXPermissionDeniedListener {

        @Override
        public void onDenied(Context context, String permission, int requestCode) {
            String tips;
            if (TextUtils.equals(permission, Manifest.permission.RECORD_AUDIO)) {
                tips = "缺少麦克风权限\n可能会导致录视频无法采集声音";
            } else {
                tips = "缺少相机权限\n可能会导致不能使用摄像头功能";
            }
            RemindDialog dialog = RemindDialog.buildDialog(context, tips);
            dialog.setButtonText("去设置");
            dialog.setButtonTextColor(0xFF7D7DFF);
            dialog.setContentTextColor(0xFF333333);
            dialog.setOnDialogClickListener(new RemindDialog.OnDialogClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleXPermissionUtil.goIntentSetting((Activity) context, requestCode);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * SimpleCameraX权限说明
     *
     * @return
     */
    public OnSimpleXPermissionDescriptionListener getSimpleXPermissionDescriptionListener(boolean cb_permission_desc) {
        return cb_permission_desc ? new MeOnSimpleXPermissionDescriptionListener() : null;
    }

    /**
     * SimpleCameraX添加权限说明
     */
    public class MeOnSimpleXPermissionDescriptionListener implements OnSimpleXPermissionDescriptionListener {

        @Override
        public void onPermissionDescription(Context context, ViewGroup viewGroup, String permission) {
            addPermissionDescription(true, viewGroup, new String[]{permission});
        }

        @Override
        public void onDismiss(ViewGroup viewGroup) {
            removePermissionDescription(viewGroup);
        }
    }


    /**
     * 裁剪引擎
     *
     * @return
     */
    public ImageFileCropEngine getCropFileEngine(boolean cb_crop, PictureSelectorStyle selectorStyle) {
        return cb_crop ? new ImageFileCropEngine(selectorStyle) : null;
    }

    /**
     * 自定义裁剪
     */
    public class ImageFileCropEngine implements CropFileEngine {

        PictureSelectorStyle selectorStyle;

        public ImageFileCropEngine(PictureSelectorStyle selectorStyle) {
            this.selectorStyle = selectorStyle;
        }

        @Override
        public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
            UCrop.Options options = BuildOptions(selectorStyle);
            UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
            uCrop.withOptions(options);
            uCrop.setImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils1.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                    Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (call != null) {
                                call.onCall(resource);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (call != null) {
                                call.onCall(null);
                            }
                        }
                    });
                }
            });
            uCrop.start(fragment.requireActivity(), fragment, requestCode);
        }
    }


    /**
     * 自定义相机事件
     *
     * @return
     */
    public OnCameraInterceptListener getCustomCameraEvent(boolean cb_custom_camera, PictureSelectorStyle selectorStyle) {
        return cb_custom_camera ? new MeOnCameraInterceptListener(selectorStyle) : null;
    }

    /**
     * 自定义拍照
     */
    public class MeOnCameraInterceptListener implements OnCameraInterceptListener {
        PictureSelectorStyle selectorStyle;

        public MeOnCameraInterceptListener(PictureSelectorStyle selectorStyle) {
            this.selectorStyle = selectorStyle;
        }

        @Override
        public void openCamera(Fragment fragment, int cameraMode, int requestCode) {
            //
//            SimpleCameraX camera = SimpleCameraX.of();
//            camera.isAutoRotation(true);
//            camera.setCameraMode(cameraMode);
//            camera.setVideoFrameRate(25);
//            camera.setVideoBitRate(3 * 1024 * 1024);
//            camera.isDisplayRecordChangeTime(true);
//            camera.isManualFocusCameraPreview(true);
//            camera.isZoomCameraPreview(true);
//            camera.setOutputPathDir(getSandboxCameraOutputPath(true));//
//            camera.setPermissionDeniedListener(getSimpleXPermissionDeniedListener(true));
//            camera.setPermissionDescriptionListener(getSimpleXPermissionDescriptionListener(true));
//            camera.setImageEngine(new CameraImageEngine() {
//                @Override
//                public void loadImage(Context context, String url, ImageView imageView) {
//                    Glide.with(context).load(url).into(imageView);
//                }
//            });
//            camera.start(fragment.requireActivity(), fragment, requestCode);
//            SimpleCameraX camera = SimpleCameraX.of("hs.act.slbapp.PictureCameraActivity2");// 原始
            SimpleCameraX camera = SimpleCameraX.of("hs.act.slbapp.PictureCameraActivity3");// 测试
            // 裁剪设置bufen
            camera.setHideBottomControls(false);
            camera.setFreeStyleCropEnabled(true);
            camera.setShowCropFrame(true);
            camera.setShowCropGrid(true);
            camera.setCircleDimmedLayer(false);
            camera.withAspectRatio(-1, -1);
            camera.setCropOutputPathDir(getSandboxPath());
            camera.isCropDragSmoothToCenter(false);
            camera.setSkipCropMimeType(getNotSupportCrop(true));
            camera.isForbidCropGifWebp(true);
            camera.isForbidSkipMultipleCrop(true);
            camera.setMaxScaleMultiplier(100);
            if (selectorStyle != null && selectorStyle.getSelectMainStyle().getStatusBarColor() != 0) {
                SelectMainStyle mainStyle = selectorStyle.getSelectMainStyle();
                boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
                int statusBarColor = mainStyle.getStatusBarColor();
                camera.isDarkStatusBarBlack(isDarkStatusBarBlack);
                if (StyleUtils.checkStyleValidity(statusBarColor)) {
                    camera.setStatusBarColor(statusBarColor);
                    camera.setToolbarColor(statusBarColor);
                } else {
                    camera.setStatusBarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
                    camera.setToolbarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
                }
                TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
                if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleTextColor())) {
                    camera.setToolbarWidgetColor(titleBarStyle.getTitleTextColor());
                } else {
                    camera.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.ps_color_white));
                }
            } else {
                camera.setStatusBarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
                camera.setToolbarColor(ContextCompat.getColor(mContext, R.color.ps_color_grey));
                camera.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.ps_color_white));
            }
            // 拍照设置属性bufen
            camera.isAutoRotation(true);
            camera.setCameraMode(cameraMode);
            camera.setVideoFrameRate(25);
            camera.setVideoBitRate(3 * 1024 * 1024);
            camera.isDisplayRecordChangeTime(true);
            camera.isManualFocusCameraPreview(true);
            camera.isZoomCameraPreview(true);
            camera.setOutputPathDir(getSandboxCameraOutputPath(true));
            camera.setPermissionDeniedListener(getSimpleXPermissionDeniedListener(true));
            camera.setPermissionDescriptionListener(getSimpleXPermissionDescriptionListener(true));
            // 预览拍照视图bufen
            camera.setImageEngine(new CameraImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    Glide.with(context).load(url).into(imageView);
                    //
//                    UCrop.Options options = buildOptions();
//                    UCrop uCrop = UCrop.of(srcUri, destinationUri, dataCropSource);
//                    uCrop.withOptions(options);

                }
            });
            //裁剪视图bufen
            camera.setUCropImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils1.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                    Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (call != null) {
                                call.onCall(resource);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (call != null) {
                                call.onCall(null);
                            }
                        }
                    });
                }
            });
            //
//            camera.start(fragment.requireActivity(), fragment, requestCode);
            camera.start(fragment.requireActivity(), fragment, Crop.REQUEST_CROP);
        }
    }

    /**
     * copy录音文件至指定目录
     */
    public void copyOutputAudioToDir(SelectorConfig selectorConfig) {
        try {
//            Context context = mImagePreview.getContext();
            if (!TextUtils.isEmpty(selectorConfig.outPutAudioDir)) {
                InputStream inputStream = PictureMimeType.isContent(selectorConfig.cameraPath)
                        ? PictureContentResolver.openInputStream(BaseApp7.get(), Uri.parse(selectorConfig.cameraPath)) : new FileInputStream(selectorConfig.cameraPath);
                String audioFileName;
                if (TextUtils.isEmpty(selectorConfig.outPutAudioFileName)) {
                    audioFileName = "";
                } else {
                    audioFileName = selectorConfig.isOnlyCamera
                            ? selectorConfig.outPutAudioFileName : System.currentTimeMillis() + "_" + selectorConfig.outPutAudioFileName;
                }
                File outputFile = PictureFileUtils.createCameraFile(BaseApp7.get(), selectorConfig.chooseMode, audioFileName, "", selectorConfig.outPutAudioDir);
                FileOutputStream outputStream = new FileOutputStream(outputFile.getAbsolutePath());
                if (PictureFileUtils.writeFileFromIS(inputStream, outputStream)) {
                    MediaUtils.deleteUri(BaseApp7.get(), selectorConfig.cameraPath);
                    selectorConfig.cameraPath = outputFile.getAbsolutePath();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 刷新相册
     *
     * @param media 要刷新的对象
     */
    public void onScannerScanFile(Activity activity, LocalMedia media) {
//        Context context = mImagePreview.getContext();
//        String outputPath = getOutputPath(((Activity) context).getIntent());
        if (ActivityCompatHelper.isDestroy(activity)) {

            return;
        }
        if (SdkVersionUtils.isQ()) {
            if (PictureMimeType.isHasVideo(media.getMimeType()) && PictureMimeType.isContent(media.getPath())) {
                new PictureMediaScannerConnection(activity, media.getRealPath());
            }
        } else {
            String path = PictureMimeType.isContent(media.getPath()) ? media.getRealPath() : media.getPath();
            new PictureMediaScannerConnection(activity, path);
            if (PictureMimeType.isHasImage(media.getMimeType())) {
                File dirFile = new File(path);
                int lastImageId = MediaUtils.getDCIMLastImageId(BaseApp7.get(), dirFile.getParent());
                if (lastImageId != -1) {
                    MediaUtils.removeMedia(BaseApp7.get(), lastImageId);
                }
            }
        }
    }


    // 拍照
    public PictureSelectionCameraModel handleCameraSuccess(ImageEngine imageEngine, PictureSelectorStyle selectorStyle) {
        PictureSelectionCameraModel cameraModel = PictureSelector.create(mContext)
                .openCamera(SelectMimeType.ofImage())
                .setCameraInterceptListener(getCustomCameraEvent(true, selectorStyle))
                .setCropEngine(getCropFileEngine(true, selectorStyle))
                .setCompressEngine(getCompressFileEngine(true))
                .setSelectLimitTipsListener(getMeOnSelectLimitTipsListener(true))
                .setAddBitmapWatermarkListener(getAddBitmapWatermarkListener(false))
                .setCustomLoadingListener(getCustomLoadingListener(true))
                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE)
                .setSandboxFileEngine(getMeSandboxFileEngine(true))
                .setOutputAudioDir(getSandboxAudioOutputPath(true))
                .isOriginalControl(false)
                .setPermissionDescriptionListener(getPermissionDescriptionListener(false));
        return cameraModel;
    }


    // 系统相册
    public PictureSelectionSystemModel handleImgSuccess(ImageEngine imageEngine, PictureSelectorStyle selectorStyle) {
        PictureSelectionSystemModel systemGalleryMode = PictureSelector.create(mContext)
                .openSystemGallery(SelectMimeType.ofImage())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setCompressEngine(getCompressFileEngine(true))
                .setCropEngine(getCropFileEngine(true, selectorStyle))
                .setSkipCropMimeType(getNotSupportCrop(true))
                .setSelectLimitTipsListener(getMeOnSelectLimitTipsListener(true))
                .setAddBitmapWatermarkListener(getAddBitmapWatermarkListener(true))
//                .setVideoThumbnailListener(CameraUtils1.getInstance(mContext).getVideoThumbnailEventListener())
                .setCustomLoadingListener(getCustomLoadingListener(true))
                .isOriginalControl(false)
                .setPermissionDescriptionListener(getPermissionDescriptionListener(true))
                .setSandboxFileEngine(getMeSandboxFileEngine(true));
        return systemGalleryMode;
    }


    // 微信相册
    public PictureSelectionModel handleImgSuccess2(ImageEngine imageEngine, PictureSelectorStyle selectorStyle) {
        PictureSelectionModel systemGalleryMode = PictureSelector.create(mContext)
                .openGallery(SelectMimeType.ofImage())
                .setSelectorUIStyle(selectorStyle)
                .setImageEngine(imageEngine)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setCompressEngine(getCompressFileEngine(true))
                .setCropEngine(getCropFileEngine(true, selectorStyle))
                .setSkipCropMimeType(getNotSupportCrop(true))
                .setSelectLimitTipsListener(getMeOnSelectLimitTipsListener(true))
                .setAddBitmapWatermarkListener(getAddBitmapWatermarkListener(true))
//                .setVideoThumbnailListener(CameraUtils1.getInstance(mContext).getVideoThumbnailEventListener())
                .setCustomLoadingListener(getCustomLoadingListener(true))
                .isOriginalControl(false)
                .isDisplayCamera(false)
                .isDirectReturnSingle(true)
                .setPermissionDescriptionListener(getPermissionDescriptionListener(true))
                .setSandboxFileEngine(getMeSandboxFileEngine(true));
        return systemGalleryMode;
    }


}
