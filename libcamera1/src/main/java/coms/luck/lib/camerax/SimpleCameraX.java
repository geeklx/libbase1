package coms.luck.lib.camerax;

import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_ALLOWED_GESTURES;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_ASPECT_RATIO_OPTIONS;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CIRCLE_DIMMED_LAYER;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CIRCLE_STROKE_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CIRCLE_STROKE_WIDTH_LAYER;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_COMPRESSION_FORMAT_NAME;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_COMPRESSION_QUALITY;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_CUSTOM_LOADER_BITMAP;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_DRAG_CENTER;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_FORBID_GIF_WEBP;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_FORBID_SKIP;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_FRAME_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_FRAME_STROKE_WIDTH;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_GRID_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_GRID_COLUMN_COUNT;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_GRID_ROW_COUNT;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_GRID_STROKE_WIDTH;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_OUTPUT_DIR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_CROP_OUTPUT_FILE_NAME;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_DARK_STATUS_BAR_BLACK;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_DIMMED_LAYER_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_DRAG_IMAGES;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_FREE_STYLE_CROP;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_GALLERY_BAR_BACKGROUND;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_HIDE_BOTTOM_CONTROLS;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_MAX_BITMAP_SIZE;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_MAX_SCALE_MULTIPLIER;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_MULTIPLE_ASPECT_RATIO;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_SHOW_CROP_FRAME;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_SHOW_CROP_GRID;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_SKIP_CROP_MIME_TYPE;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_STATUS_BAR_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_TOOL_BAR_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_COLOR_CONTROLS_WIDGET_ACTIVE;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_LOGO_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_TITLE_TEXT_SIZE_TOOLBAR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR;
import static coms.luck.lib.camerax.SimpleCameraX.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import coms.luck.lib.camerax.listener.OnSimpleXPermissionDeniedListener;
import coms.luck.lib.camerax.listener.OnSimpleXPermissionDescriptionListener;
import coms.luck.lib.camerax.utils.FileUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import coms.yalantis.ucrop.UCrop;
import coms.yalantis.ucrop.UCropDevelopConfig;
import coms.yalantis.ucrop.UCropImageEngine;
import coms.yalantis.ucrop.model.AspectRatio;

/**
 * @author：luck
 * @date：2021/11/29 7:52 下午
 * @describe：SimpleCameraX
 */
public class SimpleCameraX {

    private static final String EXTRA_PREFIX = AppUtils.getAppPackageName();

    public static final String EXTRA_OUTPUT_PATH_DIR = EXTRA_PREFIX + ".OutputPathDir";

    public static final String EXTRA_CAMERA_FILE_NAME = EXTRA_PREFIX + ".CameraFileName";

    public static final String EXTRA_CAMERA_MODE = EXTRA_PREFIX + ".CameraMode";

    public static final String EXTRA_VIDEO_FRAME_RATE = EXTRA_PREFIX + ".VideoFrameRate";

    public static final String EXTRA_VIDEO_BIT_RATE = EXTRA_PREFIX + ".VideoBitRate";

    public static final String EXTRA_CAMERA_AROUND_STATE = EXTRA_PREFIX + ".CameraAroundState";

    public static final String EXTRA_RECORD_VIDEO_MAX_SECOND = EXTRA_PREFIX + ".RecordVideoMaxSecond";

    public static final String EXTRA_RECORD_VIDEO_MIN_SECOND = EXTRA_PREFIX + ".RecordVideoMinSecond";

    public static final String EXTRA_CAMERA_IMAGE_FORMAT = EXTRA_PREFIX + ".CameraImageFormat";

    public static final String EXTRA_CAMERA_IMAGE_FORMAT_FOR_Q = EXTRA_PREFIX + ".CameraImageFormatForQ";

    public static final String EXTRA_CAMERA_VIDEO_FORMAT = EXTRA_PREFIX + ".CameraVideoFormat";

    public static final String EXTRA_CAMERA_VIDEO_FORMAT_FOR_Q = EXTRA_PREFIX + ".CameraVideoFormatForQ";

    public static final String EXTRA_CAPTURE_LOADING_COLOR = EXTRA_PREFIX + ".CaptureLoadingColor";

    public static final String EXTRA_DISPLAY_RECORD_CHANGE_TIME = EXTRA_PREFIX + ".DisplayRecordChangeTime";

    public static final String EXTRA_MANUAL_FOCUS = EXTRA_PREFIX + ".isManualFocus";

    public static final String EXTRA_ZOOM_PREVIEW = EXTRA_PREFIX + ".isZoomPreview";

    public static final String EXTRA_AUTO_ROTATION = EXTRA_PREFIX + ".isAutoRotation";


    private final Intent mCameraIntent;

    private final Bundle mCameraBundle;

    public static SimpleCameraX of(String intent) {
        return new SimpleCameraX(intent);
    }

    private SimpleCameraX(String intent) {
        mCameraIntent = new Intent(intent);
        mCameraBundle = new Bundle();
    }

    /**
     * Send the camera Intent from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull Activity activity, int requestCode) {
        if (CustomCameraConfig.imageEngine == null) {
            throw new NullPointerException("Missing ImageEngine,please implement SimpleCamerax.setImageEngine");
        }
        activity.startActivityForResult(getIntent(activity), requestCode);
    }


    /**
     * Send the crop Intent with a custom request code
     *
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull Context context, @NonNull Fragment fragment, int requestCode) {
        if (CustomCameraConfig.imageEngine == null) {
            throw new NullPointerException("Missing ImageEngine,please implement SimpleCamerax.setImageEngine");
        }
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    /**
     * Get Intent to start {@link PictureCameraActivity}
     *
     * @return Intent for {@link PictureCameraActivity}
     */
    public Intent getIntent(@NonNull Context context) {
//        mCameraIntent.setClass(context, PictureCameraActivity.class);
        mCameraIntent.putExtras(mCameraBundle);
        return mCameraIntent;
    }

    /**
     * Set Camera Preview Image Engine
     *
     * @param engine
     * @return
     */
    public SimpleCameraX setImageEngine(CameraImageEngine engine) {
        CustomCameraConfig.imageEngine = engine;
        return this;
    }

    /**
     * Permission description
     *
     * @param explainListener
     * @return
     */
    public SimpleCameraX setPermissionDescriptionListener(OnSimpleXPermissionDescriptionListener explainListener) {
        CustomCameraConfig.explainListener = explainListener;
        return this;
    }

    /**
     * Permission denied
     *
     * @param deniedListener
     * @return
     */
    public SimpleCameraX setPermissionDeniedListener(OnSimpleXPermissionDeniedListener deniedListener) {
        CustomCameraConfig.deniedListener = deniedListener;
        return this;
    }

    /**
     * 相机模式
     *
     * @param cameraMode Use {@link CustomCameraConfig}
     * @return
     */
    public SimpleCameraX setCameraMode(int cameraMode) {
        mCameraBundle.putInt(EXTRA_CAMERA_MODE, cameraMode);
        return this;
    }


    /**
     * 视频帧率，越高视频体积越大
     *
     * @param videoFrameRate 0~100
     * @return
     */
    public SimpleCameraX setVideoFrameRate(int videoFrameRate) {
        mCameraBundle.putInt(EXTRA_VIDEO_FRAME_RATE, videoFrameRate);
        return this;
    }

    /**
     * bit率， 越大视频体积越大
     *
     * @param bitRate example 3 * 1024 * 1024
     * @return
     */
    public SimpleCameraX setVideoBitRate(int bitRate) {
        mCameraBundle.putInt(EXTRA_VIDEO_BIT_RATE, bitRate);
        return this;
    }


    /**
     * 相机前置或后置
     *
     * @param isCameraAroundState true 前置,默认false后置
     * @return
     */
    public SimpleCameraX setCameraAroundState(boolean isCameraAroundState) {
        mCameraBundle.putBoolean(EXTRA_CAMERA_AROUND_STATE, isCameraAroundState);
        return this;
    }


    /**
     * 拍照自定义输出路径
     *
     * @param outputPath
     * @return
     */
    public SimpleCameraX setOutputPathDir(String outputPath) {
        mCameraBundle.putString(EXTRA_OUTPUT_PATH_DIR, outputPath);
        return this;
    }

    /**
     * 拍照输出文件名
     *
     * @param fileName
     * @return
     */
    public SimpleCameraX setCameraOutputFileName(String fileName) {
        mCameraBundle.putString(EXTRA_CAMERA_FILE_NAME, fileName);
        return this;
    }

    /**
     * 视频最大录制时长 单位：秒
     *
     * @param maxSecond
     * @return
     */
    public SimpleCameraX setRecordVideoMaxSecond(int maxSecond) {
        mCameraBundle.putInt(EXTRA_RECORD_VIDEO_MAX_SECOND, maxSecond * 1000 + 500);
        return this;
    }

    /**
     * 视频最小录制时长 单位：秒
     *
     * @param minSecond
     * @return
     */
    public SimpleCameraX setRecordVideoMinSecond(int minSecond) {
        mCameraBundle.putInt(EXTRA_RECORD_VIDEO_MIN_SECOND, minSecond * 1000);
        return this;
    }

    /**
     * 图片输出类型
     * <p>
     * 比如 xxx.jpg or xxx.png
     * </p>
     *
     * @param format
     * @return
     */
    public SimpleCameraX setCameraImageFormat(String format) {
        mCameraBundle.putString(EXTRA_CAMERA_IMAGE_FORMAT, format);
        return this;
    }

    /**
     * Android Q 以上 图片输出类型
     * <p>
     * 比如 "image/jpeg"
     * </p>
     *
     * @param format
     * @return
     */
    public SimpleCameraX setCameraImageFormatForQ(String format) {
        mCameraBundle.putString(EXTRA_CAMERA_IMAGE_FORMAT_FOR_Q, format);
        return this;
    }

    /**
     * 视频输出类型
     * <p>
     * 比如 xxx.mp4
     * </p>
     *
     * @param format
     * @return
     */
    public SimpleCameraX setCameraVideoFormat(String format) {
        mCameraBundle.putString(EXTRA_CAMERA_VIDEO_FORMAT, format);
        return this;
    }

    /**
     * Android Q 以上 视频输出类型
     * <p>
     * 比如 "video/mp4"
     * </p>
     *
     * @param format
     * @return
     */
    public SimpleCameraX setCameraVideoFormatForQ(String format) {
        mCameraBundle.putString(EXTRA_CAMERA_VIDEO_FORMAT_FOR_Q, format);
        return this;
    }

    /**
     * 拍照Loading的色值
     *
     * @param color
     * @return
     */
    public SimpleCameraX setCaptureLoadingColor(int color) {
        mCameraBundle.putInt(EXTRA_CAPTURE_LOADING_COLOR, color);
        return this;
    }

    /**
     * 是否显示录制时间
     *
     * @param isDisplayRecordTime
     * @return
     */
    public SimpleCameraX isDisplayRecordChangeTime(boolean isDisplayRecordTime) {
        mCameraBundle.putBoolean(EXTRA_DISPLAY_RECORD_CHANGE_TIME, isDisplayRecordTime);
        return this;
    }

    /**
     * 是否手动点击对焦
     *
     * @param isManualFocus
     * @return
     */
    public SimpleCameraX isManualFocusCameraPreview(boolean isManualFocus) {
        mCameraBundle.putBoolean(EXTRA_MANUAL_FOCUS, isManualFocus);
        return this;
    }

    /**
     * 是否可缩放相机
     *
     * @param isZoom
     * @return
     */
    public SimpleCameraX isZoomCameraPreview(boolean isZoom) {
        mCameraBundle.putBoolean(EXTRA_ZOOM_PREVIEW, isZoom);
        return this;
    }

    /**
     * 是否自动纠偏
     *
     * @param isAutoRotation
     * @return
     */
    public SimpleCameraX isAutoRotation(boolean isAutoRotation) {
        mCameraBundle.putBoolean(EXTRA_AUTO_ROTATION, isAutoRotation);
        return this;
    }

    /**
     * 保存相机输出的路径
     *
     * @param intent
     * @param uri
     */
    public static void putOutputUri(Intent intent, Uri uri) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    }

    /**
     * 获取保存相机输出的路径
     *
     * @param intent
     * @return
     */
    public static String getOutputPath(Intent intent) {
        Uri uri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        if (uri == null) {
            return "";
        }
        return FileUtils.isContent(uri.toString()) ? uri.toString() : uri.getPath();
    }


    /**
     * 裁剪bufen
     */
    public static final int REQUEST_CROP = 69;
    public static final int RESULT_ERROR = 96;
    public static final int MIN_SIZE = 10;
    //    private static final String EXTRA_PREFIX = BuildConfig.LIBRARY_PACKAGE_NAME;
    public static final String EXTRA_CROP_TOTAL_DATA_SOURCE = EXTRA_PREFIX + ".CropTotalDataSource";
    public static final String EXTRA_CROP_INPUT_ORIGINAL = EXTRA_PREFIX + ".CropInputOriginal";

    public static final String EXTRA_INPUT_URI = EXTRA_PREFIX + ".InputUri";
    public static final String EXTRA_OUTPUT_URI = EXTRA_PREFIX + ".OutputUri";
    public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = EXTRA_PREFIX + ".CropAspectRatio";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = EXTRA_PREFIX + ".ImageWidth";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = EXTRA_PREFIX + ".ImageHeight";
    public static final String EXTRA_OUTPUT_OFFSET_X = EXTRA_PREFIX + ".OffsetX";
    public static final String EXTRA_OUTPUT_OFFSET_Y = EXTRA_PREFIX + ".OffsetY";
    public static final String EXTRA_ERROR = EXTRA_PREFIX + ".Error";

    public static final String EXTRA_ASPECT_RATIO_X = EXTRA_PREFIX + ".AspectRatioX";
    public static final String EXTRA_ASPECT_RATIO_Y = EXTRA_PREFIX + ".AspectRatioY";

    public static final String EXTRA_MAX_SIZE_X = EXTRA_PREFIX + ".MaxSizeX";
    public static final String EXTRA_MAX_SIZE_Y = EXTRA_PREFIX + ".MaxSizeY";


    /**
     * Set one of {@link android.graphics.Bitmap.CompressFormat} that will be used to save resulting Bitmap.
     */
    public void setCompressionFormat(@NonNull Bitmap.CompressFormat format) {
        mCameraBundle.putString(EXTRA_COMPRESSION_FORMAT_NAME, format.name());
    }

    /**
     * Set one of {@linkcontext.getExternalFilesDir()} The path that will be used to save
     * when clipping multiple drawings
     * Valid when multiple pictures are cropped
     */
    public void setCropOutputPathDir(@NonNull String dir) {
        mCameraBundle.putString(EXTRA_CROP_OUTPUT_DIR, dir);
    }

    /**
     * File name after clipping output
     * Valid when multiple pictures are cropped
     * <p>
     * When multiple pictures are cropped, the front will automatically keep up with the timestamp
     * </p>
     */
    public void setCropOutputFileName(@NonNull String fileName) {
        mCameraBundle.putString(EXTRA_CROP_OUTPUT_FILE_NAME, fileName);
    }

    /**
     * @param isForbidSkipCrop - It is forbidden to skip when cutting multiple drawings
     */
    public void isForbidSkipMultipleCrop(boolean isForbidSkipCrop) {
        mCameraBundle.putBoolean(EXTRA_CROP_FORBID_SKIP, isForbidSkipCrop);
    }

    /**
     * Get the bitmap of the uCrop resource using the custom loader
     *
     * @param isUseBitmap
     */
    public SimpleCameraX isUseCustomLoaderBitmap(boolean isUseBitmap) {
        mCameraBundle.putBoolean(EXTRA_CROP_CUSTOM_LOADER_BITMAP, isUseBitmap);
        return this;
    }

    /**
     * isDragCenter
     *
     * @param isDragCenter Crop and drag automatically center
     */
    public void isCropDragSmoothToCenter(boolean isDragCenter) {
        mCameraBundle.putBoolean(EXTRA_CROP_DRAG_CENTER, isDragCenter);
    }

    /**
     * @param isForbidCropGifWebp - Do you need to support clipping dynamic graphs gif or webp
     */
    public void isForbidCropGifWebp(boolean isForbidCropGifWebp) {
        mCameraBundle.putBoolean(EXTRA_CROP_FORBID_GIF_WEBP, isForbidCropGifWebp);
    }

    /**
     * Set compression quality [0-100] that will be used to save resulting Bitmap.
     */
    public void setCompressionQuality(@IntRange(from = 0) int compressQuality) {
        mCameraBundle.putInt(EXTRA_COMPRESSION_QUALITY, compressQuality);
    }

    /**
     * Choose what set of gestures will be enabled on each tab - if any.
     */
    public void setAllowedGestures(@GestureTypes int tabScale,
                                   @GestureTypes int tabRotate,
                                   @GestureTypes int tabAspectRatio) {
        mCameraBundle.putIntArray(EXTRA_ALLOWED_GESTURES, new int[]{tabScale, tabRotate, tabAspectRatio});
    }


    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;

    @IntDef({NONE, SCALE, ROTATE, ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {

    }

    /**
     * This method sets multiplier that is used to calculate max image scale from min image scale.
     *
     * @param maxScaleMultiplier - (minScale * maxScaleMultiplier) = maxScale
     */
    public void setMaxScaleMultiplier(@FloatRange(from = 1.0, fromInclusive = false) float maxScaleMultiplier) {
        mCameraBundle.putFloat(EXTRA_MAX_SCALE_MULTIPLIER, maxScaleMultiplier);
    }

    /**
     * This method sets animation duration for image to wrap the crop bounds
     *
     * @param durationMillis - duration in milliseconds
     */
    public void setImageToCropBoundsAnimDuration(@IntRange(from = MIN_SIZE) int durationMillis) {
        mCameraBundle.putInt(EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, durationMillis);
    }

    /**
     * Setter for max size for both width and height of bitmap that will be decoded from an input Uri and used in the view.
     *
     * @param maxBitmapSize - size in pixels
     */
    public void setMaxBitmapSize(@IntRange(from = MIN_SIZE) int maxBitmapSize) {
        mCameraBundle.putInt(EXTRA_MAX_BITMAP_SIZE, maxBitmapSize);
    }

    /**
     * @param color - desired color of dimmed area around the crop bounds
     */
    public void setDimmedLayerColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_DIMMED_LAYER_COLOR, color);
    }

    /**
     * @param color - desired color of dimmed stroke area around the crop bounds
     */
    public void setCircleStrokeColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_CIRCLE_STROKE_COLOR, color);
    }

    /**
     * @param isCircle - set it to true if you want dimmed layer to have an circle inside
     */
    public void setCircleDimmedLayer(boolean isCircle) {
        mCameraBundle.putBoolean(EXTRA_CIRCLE_DIMMED_LAYER, isCircle);
    }

    /**
     * @param show - set to true if you want to see a crop frame rectangle on top of an image
     */
    public void setShowCropFrame(boolean show) {
        mCameraBundle.putBoolean(EXTRA_SHOW_CROP_FRAME, show);
    }

    /**
     * @param color - desired color of crop frame
     */
    public void setCropFrameColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_CROP_FRAME_COLOR, color);
    }

    /**
     * @param width - desired width of crop frame line in pixels
     */
    public void setCropFrameStrokeWidth(@IntRange(from = 0) int width) {
        mCameraBundle.putInt(EXTRA_CROP_FRAME_STROKE_WIDTH, width);
    }

    /**
     * @param show - set to true if you want to see a crop grid/guidelines on top of an image
     */
    public void setShowCropGrid(boolean show) {
        mCameraBundle.putBoolean(EXTRA_SHOW_CROP_GRID, show);
    }

    /**
     * @param count - crop grid rows count.
     */
    public void setCropGridRowCount(@IntRange(from = 0) int count) {
        mCameraBundle.putInt(EXTRA_CROP_GRID_ROW_COUNT, count);
    }

    /**
     * @param count - crop grid columns count.
     */
    public void setCropGridColumnCount(@IntRange(from = 0) int count) {
        mCameraBundle.putInt(EXTRA_CROP_GRID_COLUMN_COUNT, count);
    }

    /**
     * @param color - desired color of crop grid/guidelines
     */
    public void setCropGridColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_CROP_GRID_COLOR, color);
    }

    /**
     * @param width - desired width of crop grid lines in pixels
     */
    public void setCropGridStrokeWidth(@IntRange(from = 0) int width) {
        mCameraBundle.putInt(EXTRA_CROP_GRID_STROKE_WIDTH, width);
    }

    /**
     * @param width Set the circular clipping border
     */
    public void setCircleStrokeWidth(@IntRange(from = 0) int width) {
        mCameraBundle.putInt(EXTRA_CIRCLE_STROKE_WIDTH_LAYER, width);
    }

    /**
     * @param color - desired resolved color of the gallery bar background
     */
    public void setCropGalleryBarBackgroundResources(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_GALLERY_BAR_BACKGROUND, color);
    }

    /**
     * @param color - desired resolved color of the toolbar
     */
    public void setToolbarColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_TOOL_BAR_COLOR, color);
    }

    /**
     * @param color - desired resolved color of the statusbar
     */
    public void setStatusBarColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_STATUS_BAR_COLOR, color);
    }


    /**
     * @paramIs the font of the status bar black
     */
    public void isDarkStatusBarBlack(boolean isDarkStatusBarBlack) {
        mCameraBundle.putBoolean(EXTRA_DARK_STATUS_BAR_BLACK, isDarkStatusBarBlack);
    }

    /**
     * Can I drag and drop images when crop
     *
     * @param isDragImages
     */
    public void isDragCropImages(boolean isDragImages) {
        mCameraBundle.putBoolean(EXTRA_DRAG_IMAGES, isDragImages);
    }

    /**
     * @param color - desired resolved color of the active and selected widget and progress wheel middle line (default is white)
     */
    public void setActiveControlsWidgetColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_UCROP_COLOR_CONTROLS_WIDGET_ACTIVE, color);
    }

    /**
     * @param color - desired resolved color of Toolbar text and buttons (default is darker orange)
     */
    public void setToolbarWidgetColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, color);
    }

    /**
     * @param text - desired text for Toolbar title
     */
    public void setToolbarTitle(@Nullable String text) {
        mCameraBundle.putString(EXTRA_UCROP_TITLE_TEXT_TOOLBAR, text);
    }

    /**
     * @param textSize - desired text for Toolbar title
     */
    public void setToolbarTitleSize(int textSize) {
        if (textSize > 0) {
            mCameraBundle.putInt(EXTRA_UCROP_TITLE_TEXT_SIZE_TOOLBAR, textSize);
        }
    }

    /**
     * @param drawable - desired drawable for the Toolbar left cancel icon
     */
    public void setToolbarCancelDrawable(@DrawableRes int drawable) {
        mCameraBundle.putInt(EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, drawable);
    }

    /**
     * @param drawable - desired drawable for the Toolbar right crop icon
     */
    public void setToolbarCropDrawable(@DrawableRes int drawable) {
        mCameraBundle.putInt(EXTRA_UCROP_WIDGET_CROP_DRAWABLE, drawable);
    }

    /**
     * @param color - desired resolved color of logo fill (default is darker grey)
     */
    public void setLogoColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_UCROP_LOGO_COLOR, color);
    }

    /**
     * @param hide - set to true to hide the bottom controls (shown by default)
     */
    public SimpleCameraX setHideBottomControls(boolean hide) {
        mCameraBundle.putBoolean(EXTRA_HIDE_BOTTOM_CONTROLS, hide);
        return this;
    }

    /**
     * @param enabled - set to true to let user resize crop bounds (disabled by default)
     */
    public void setFreeStyleCropEnabled(boolean enabled) {
        mCameraBundle.putBoolean(EXTRA_FREE_STYLE_CROP, enabled);
    }

    /**
     * Pass an ordered list of desired aspect ratios that should be available for a user.
     *
     * @param selectedByDefault - index of aspect ratio option that is selected by default (starts with 0).
     * @param aspectRatio       - list of aspect ratio options that are available to user
     */
    public void setAspectRatioOptions(int selectedByDefault, AspectRatio... aspectRatio) {
        if (selectedByDefault >= aspectRatio.length) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "Index [selectedByDefault = %d] (0-based) cannot be higher or equal than aspect ratio options count [count = %d].",
                    selectedByDefault, aspectRatio.length));
        }
        mCameraBundle.putInt(EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, selectedByDefault);
        mCameraBundle.putParcelableArrayList(EXTRA_ASPECT_RATIO_OPTIONS, new ArrayList<Parcelable>(Arrays.asList(aspectRatio)));
    }

    /**
     * Skip crop mimeType
     *
     * @param mimeTypes Use example {@link { image/gift or image/webp ... }}
     * @return
     */
    public void setSkipCropMimeType(String... mimeTypes) {
        if (mimeTypes != null && mimeTypes.length > 0) {
            mCameraBundle.putStringArrayList(EXTRA_SKIP_CROP_MIME_TYPE, new ArrayList<>(Arrays.asList(mimeTypes)));
        }
    }

    /**
     * @param color - desired background color that should be applied to the root view
     */
    public void setRootViewBackgroundColor(@ColorInt int color) {
        mCameraBundle.putInt(EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, color);
    }

    /**
     * Set an aspect ratio for crop bounds.
     * User won't see the menu with other ratios options.
     *
     * @param x aspect ratio X
     * @param y aspect ratio Y
     */
    public void withAspectRatio(float x, float y) {
        mCameraBundle.putFloat(EXTRA_ASPECT_RATIO_X, x);
        mCameraBundle.putFloat(EXTRA_ASPECT_RATIO_Y, y);
    }

    /**
     * The corresponding crop scale of each graph in multi graph crop
     *
     * @param aspectRatio - The corresponding crop scale of each graph in multi graph crop
     */
    public void setMultipleCropAspectRatio(AspectRatio... aspectRatio) {
        float aspectRatioX = mCameraBundle.getFloat(EXTRA_ASPECT_RATIO_X, 0);
        float aspectRatioY = mCameraBundle.getFloat(EXTRA_ASPECT_RATIO_Y, 0);
        if (aspectRatio.length > 0 && aspectRatioX <= 0 && aspectRatioY <= 0) {
            withAspectRatio(aspectRatio[0].getAspectRatioX(), aspectRatio[0].getAspectRatioY());
        }
        mCameraBundle.putParcelableArrayList(EXTRA_MULTIPLE_ASPECT_RATIO, new ArrayList<Parcelable>(Arrays.asList(aspectRatio)));
    }


    /**
     * Set an aspect ratio for crop bounds that is evaluated from source image width and height.
     * User won't see the menu with other ratios options.
     */
    public void useSourceImageAspectRatio() {
        mCameraBundle.putFloat(EXTRA_ASPECT_RATIO_X, 0);
        mCameraBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 0);
    }

    /**
     * Set maximum size for result cropped image.
     *
     * @param width  max cropped image width
     * @param height max cropped image height
     */
    public void withMaxResultSize(@IntRange(from = MIN_SIZE) int width, @IntRange(from = MIN_SIZE) int height) {
        mCameraBundle.putInt(EXTRA_MAX_SIZE_X, width);
        mCameraBundle.putInt(EXTRA_MAX_SIZE_Y, height);
    }


    /**
     * Class that helps to setup advanced configs that are not commonly used.
     * Use it with method {@link#withOptions(Options)}
     */
    public static class Options {

        public static final String EXTRA_COMPRESSION_FORMAT_NAME = EXTRA_PREFIX + ".CompressionFormatName";
        public static final String EXTRA_COMPRESSION_QUALITY = EXTRA_PREFIX + ".CompressionQuality";

        public static final String EXTRA_CROP_OUTPUT_DIR = EXTRA_PREFIX + ".CropOutputDir";

        public static final String EXTRA_CROP_OUTPUT_FILE_NAME = EXTRA_PREFIX + ".CropOutputFileName";

        public static final String EXTRA_CROP_FORBID_GIF_WEBP = EXTRA_PREFIX + ".ForbidCropGifWebp";

        public static final String EXTRA_CROP_FORBID_SKIP = EXTRA_PREFIX + ".ForbidSkipCrop";

        public static final String EXTRA_DARK_STATUS_BAR_BLACK = EXTRA_PREFIX + ".isDarkStatusBarBlack";

        public static final String EXTRA_DRAG_IMAGES = EXTRA_PREFIX + ".isDragImages";

        public static final String EXTRA_CROP_CUSTOM_LOADER_BITMAP = EXTRA_PREFIX + ".CustomLoaderCropBitmap";

        public static final String EXTRA_CROP_DRAG_CENTER = EXTRA_PREFIX + ".DragSmoothToCenter";

        public static final String EXTRA_ALLOWED_GESTURES = EXTRA_PREFIX + ".AllowedGestures";

        public static final String EXTRA_MAX_BITMAP_SIZE = EXTRA_PREFIX + ".MaxBitmapSize";
        public static final String EXTRA_MAX_SCALE_MULTIPLIER = EXTRA_PREFIX + ".MaxScaleMultiplier";
        public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = EXTRA_PREFIX + ".ImageToCropBoundsAnimDuration";

        public static final String EXTRA_DIMMED_LAYER_COLOR = EXTRA_PREFIX + ".DimmedLayerColor";
        public static final String EXTRA_CIRCLE_STROKE_COLOR = EXTRA_PREFIX + ".CircleStrokeColor";
        public static final String EXTRA_CIRCLE_DIMMED_LAYER = EXTRA_PREFIX + ".CircleDimmedLayer";

        public static final String EXTRA_SHOW_CROP_FRAME = EXTRA_PREFIX + ".ShowCropFrame";
        public static final String EXTRA_CROP_FRAME_COLOR = EXTRA_PREFIX + ".CropFrameColor";
        public static final String EXTRA_CROP_FRAME_STROKE_WIDTH = EXTRA_PREFIX + ".CropFrameStrokeWidth";

        public static final String EXTRA_SHOW_CROP_GRID = EXTRA_PREFIX + ".ShowCropGrid";

        public static final String EXTRA_CROP_GRID_ROW_COUNT = EXTRA_PREFIX + ".CropGridRowCount";
        public static final String EXTRA_CROP_GRID_COLUMN_COUNT = EXTRA_PREFIX + ".CropGridColumnCount";
        public static final String EXTRA_CROP_GRID_COLOR = EXTRA_PREFIX + ".CropGridColor";
        public static final String EXTRA_CROP_GRID_STROKE_WIDTH = EXTRA_PREFIX + ".CropGridStrokeWidth";
        public static final String EXTRA_CIRCLE_STROKE_WIDTH_LAYER = EXTRA_PREFIX + ".CircleStrokeWidth";
        public static final String EXTRA_GALLERY_BAR_BACKGROUND = EXTRA_PREFIX + ".GalleryBarBackground";

        public static final String EXTRA_TOOL_BAR_COLOR = EXTRA_PREFIX + ".ToolbarColor";
        public static final String EXTRA_STATUS_BAR_COLOR = EXTRA_PREFIX + ".StatusBarColor";
        public static final String EXTRA_UCROP_COLOR_CONTROLS_WIDGET_ACTIVE = EXTRA_PREFIX + ".UcropColorControlsWidgetActive";

        public static final String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = EXTRA_PREFIX + ".UcropToolbarWidgetColor";
        public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = EXTRA_PREFIX + ".UcropToolbarTitleText";
        public static final String EXTRA_UCROP_TITLE_TEXT_SIZE_TOOLBAR = EXTRA_PREFIX + ".UcropToolbarTitleTextSize";
        public static final String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = EXTRA_PREFIX + ".UcropToolbarCancelDrawable";
        public static final String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = EXTRA_PREFIX + ".UcropToolbarCropDrawable";

        public static final String EXTRA_UCROP_LOGO_COLOR = EXTRA_PREFIX + ".UcropLogoColor";

        public static final String EXTRA_HIDE_BOTTOM_CONTROLS = EXTRA_PREFIX + ".HideBottomControls";
        public static final String EXTRA_FREE_STYLE_CROP = EXTRA_PREFIX + ".FreeStyleCrop";

        public static final String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = EXTRA_PREFIX + ".AspectRatioSelectedByDefault";
        public static final String EXTRA_ASPECT_RATIO_OPTIONS = EXTRA_PREFIX + ".AspectRatioOptions";
        public static final String EXTRA_SKIP_CROP_MIME_TYPE = EXTRA_PREFIX + ".SkipCropMimeType";

        public static final String EXTRA_MULTIPLE_ASPECT_RATIO = EXTRA_PREFIX + ".MultipleAspectRatio";

        public static final String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = EXTRA_PREFIX + ".UcropRootViewBackgroundColor";


//        private final Bundle mCameraBundle;

//        public Options() {
//            mCameraBundle = new Bundle();
//        }

//        @NonNull
//        public Bundle getOptionBundle() {
//            return mCameraBundle;
//        }


    }


    /**
     * Set Multiple Crop gallery Preview Image Engine
     *
     * @param engine
     * @return
     */
    public void setUCropImageEngine(UCropImageEngine engine) {
        ArrayList<String> dataSource = mCameraBundle.getStringArrayList(EXTRA_CROP_TOTAL_DATA_SOURCE);
        boolean isUseBitmap = mCameraBundle.getBoolean(UCrop.Options.EXTRA_CROP_CUSTOM_LOADER_BITMAP, false);
        if ((dataSource != null && dataSource.size() > 1) || isUseBitmap) {
            if (engine == null) {
                throw new NullPointerException("Missing ImageEngine,please implement UCrop.setImageEngine");
            }
        }
        UCropDevelopConfig.imageEngine = engine;
    }


}
