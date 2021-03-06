//package com.fosung.lighthouse.fosunglibs;
//
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.blankj.utilcode.util.ToastUtils;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.animators.AnimationType;
//import com.luck.picture.lib.app.PictureAppMaster;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
//import com.luck.picture.lib.entity.MediaExtraInfo;
//import com.luck.picture.lib.listener.OnResultCallbackListener;
//import com.luck.picture.lib.style.PictureSelectorUIStyle;
//import com.luck.picture.lib.style.PictureWindowAnimationStyle;
//import com.luck.picture.lib.tools.MediaUtils;
//import com.luck.picture.lib.tools.SdkVersionUtils;
//
//import org.jetbrains.annotations.NotNull;
//import org.tensorflow.lite.Interpreter;
//
//import java.util.List;
//
//
//public class ScanAct2 extends AppCompatActivity {
//
//    private final String TAG = getClass().getSimpleName();
//
//    TextView tv;
//    TextView tv0;
//    TextView tv1;
//    ImageView iv0;
//    ImageView iv1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_act2);
//        tv = findViewById(R.id.tv);
//        tv0 = findViewById(R.id.tv0);
//        tv1 = findViewById(R.id.tv1);
//        iv0 = findViewById(R.id.iv0);
//        iv1 = findViewById(R.id.iv1);
//    }
//
//    public void onClickBtn(View v) {
////        Intent intent = new Intent(this, ScannerAct2.class);
////        startActivity(intent);
//    }
//
//    public void onClickBtn2(View v) {
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
//////                String path = getExternalFilesDir("image").getAbsolutePath() + "/a.jpg";
//////                String s = ScannerUtils.decodeText(path);
//////                BankCardInfoBean b = ScannerUtils.getBankCardInfo("6222600260001072444");
//////                String s = b == null ? null : b.toString();
////                String path = getExternalFilesDir("image").getAbsolutePath() + "/a.png";
////                final String s = ScannerUtils.decodeText(path);
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        tv.setText("result=" + s);
////                    }
////                });
////            }
////        }).start();
//        //
//        PictureSelector.create(ScanAct2.this)
//                .openCamera(PictureMimeType.ofAll())// ?????????????????????????????????????????? ??????????????????????????????or??????
//                .imageEngine(GlideEngine2.createGlideEngine())// ??????????????????????????????????????????
//                //.theme(themeId)// ?????????????????? ???????????? values/styles   ?????????R.style.picture.white.style v2.3.3??? ????????????setPictureStyle()????????????
//                .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())
//                //.setPictureStyle(mPictureParameterStyle)// ???????????????????????????
//                //.setPictureCropStyle(mCropParameterStyle)// ???????????????????????????
//                .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle())// ?????????????????????????????????
//                .isWeChatStyle(true)// ????????????????????????????????????
//                .isUseCustomCamera(false)// ???????????????????????????
////                                        .setLan,guage(language)// ???????????????????????????
////                                        .isPageStrategy(cbPage.isChecked())// ???????????????????????? & ??????????????????????????????
//                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// ??????????????????
//                .isWithVideoImage(true)// ?????????????????????????????????,??????ofAll???????????????
//                //.isSyncCover(true)// ???????????????MediaStore???????????????????????????????????????????????????????????????????????????
//                //.isCameraAroundState(false) // ????????????????????????????????????false??????????????????????????? ???????????????????????????????????????
//                //.isCameraRotateImage(false) // ????????????????????????????????????
//                //.isAutoRotating(false)// ???????????????????????????????????????
////                                        .isMaxSelectEnabledMask(cbEnabledMask.isChecked())// ?????????????????????????????????????????????????????????
//                //.isAutomaticTitleRecyclerTop(false)// ?????????????????????RecyclerView????????????????????????,??????true
//                //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// ????????????????????????????????????????????????10??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                //.setOutputCameraPath(createCustomCameraOutPath())// ???????????????????????????
//                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// ?????????????????????????????????
////                        .setCaptureLoadingColor(ContextCompat.getColor(mActivity, R.color.app_color_blue))
////                                        .maxSelectNum(maxSelectNum)// ????????????????????????
//                .minSelectNum(1)// ??????????????????
//                .maxVideoSelectNum(1) // ????????????????????????
//                //.minVideoSelectNum(1)// ????????????????????????
//                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// ?????????AndroidQ????????????????????????????????????????????????
//                .imageSpanCount(4)// ??????????????????
//                //.queryFileSize() // ??????????????????,?????????
//                //.filterMinFileSize(5)// ???????????????????????????kb
//                //.filterMaxFileSize()// ???????????????????????????kb
//                .isReturnEmpty(false)// ????????????????????????????????????????????????
//                .closeAndroidQChangeWH(true)//??????????????????????????????????????????,?????????true
//                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// ??????????????????????????????????????????,?????????false
//                .isAndroidQTransform(true)// ??????????????????Android Q ??????????????????????????????????????????compress(false); && .isEnableCrop(false);??????,????????????
//                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// ????????????Activity????????????????????????????????????
//                .isOriginalImageControl(false)// ????????????????????????????????????????????????true?????????????????????????????????????????????????????????????????????????????????
//                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// ?????????????????????????????????????????????????????????????????????????????????
//                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// ?????????????????????????????????
//                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// ??????????????????????????????????????????????????????
//                //.bindCustomPermissionsObtainListener(new MyPermissionsObtainCallback())// ?????????????????????
//                //.cameraFileName(System.currentTimeMillis() +".jpg")    // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ????????????????????????????????????api
//                //.renameCompressFile(System.currentTimeMillis() +".jpg")// ??????????????????????????? ????????????????????????????????????????????????????????????????????????
//                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// ??????????????????????????? ????????????????????????????????????????????????????????????????????????
//                .selectionMode(PictureConfig.SINGLE)// ?????? or ??????
//                .isSingleDirectReturn(true)// ????????????????????????????????????PictureConfig.SINGLE???????????????
//                .isPreviewImage(true)// ?????????????????????
//                .isPreviewVideo(false)// ?????????????????????
//                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// ??????????????????????????????
//                //.queryMimeTypeConditions(PictureMimeType.ofWEBP())
//                .isEnablePreviewAudio(false) // ?????????????????????
//                .isCamera(true)// ????????????????????????
//                //.isMultipleSkipCrop(false)// ????????????????????????????????????????????????
//                //.isMultipleRecyclerAnimation(false)// ??????????????????????????????????????????
//                .isZoomAnim(true)// ?????????????????? ???????????? ??????true
//                //.imageFormat(PictureMimeType.PNG)// ??????????????????????????????,??????jpeg,Android Q??????PictureMimeType.PNG_Q
//                .isEnableCrop(true)// ????????????
//                //.basicUCropConfig()//??????????????????UCropOptions????????????????????????PictureSelector???????????????????????????????????????????????????
//                .isCompress(true)// ????????????
//                //.compressFocusAlpha(true)// ?????????????????????????????????
//                //.compressEngine(ImageCompressEngine.createCompressEngine()) // ?????????????????????
//                //.compressQuality(80)// ??????????????????????????? 0~ 100
//                .synOrAsy(false)//??????true?????????false ?????? ????????????
//                //.queryMaxFileSize(10)// ????????????M?????????????????????????????????  ??????M
//                //.compressSavePath(getPath())//????????????????????????
//                //.sizeMultiplier(0.5f)// glide ?????????????????? 0~1?????? ????????? .glideOverride()?????? ???????????????
//                //.glideOverride(160, 160)// glide ??????????????????????????????????????????????????????????????????????????????????????? ???????????????
//                .withAspectRatio(0, 0)// ???????????? ???16:9 3:2 3:4 1:1 ????????????
//                .hideBottomControls(true)// ????????????uCrop???????????????????????????
//                .isGif(true)// ????????????gif??????
//                //.isWebp(false)// ????????????webp??????,????????????
//                //.isBmp(false)//????????????bmp??????,????????????
//                .freeStyleCropEnabled(true)// ????????????????????????
//                .circleDimmedLayer(false)// ??????????????????
//                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// ????????????????????????
//                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// ??????????????????????????????
//                //.setCircleStrokeWidth(3)// ??????????????????????????????
//                .showCropFrame(true)// ?????????????????????????????? ???????????????????????????false
//                .showCropGrid(true)// ?????????????????????????????? ???????????????????????????false
//                .isOpenClickSound(false)// ????????????????????????
////                                        .selectionData(mAdapter.getData())// ????????????????????????
//                //.isDragFrame(false)// ????????????????????????(??????)
//                //.videoMinSecond(10)// ??????????????????????????????
//                //.videoMaxSecond(15)// ??????????????????????????????
//                //.recordVideoSecond(10)//?????????????????? ??????60s
//                //.isPreviewEggs(true)// ??????????????? ????????????????????????????????????(???????????????????????????????????????????????????)
//                //.cropCompressQuality(90)// ??????????????? ??????cutOutQuality()
//                .cutOutQuality(90)// ?????????????????? ??????100
//                //.cutCompressFormat(Bitmap.CompressFormat.PNG.name())//??????????????????Format???????????????JPEG
//                .minimumCompressSize(100)// ????????????kb??????????????????
//                //.cropWH()// ???????????????????????????????????????????????????????????????
//                //.cropImageWideHigh()// ???????????????????????????????????????????????????????????????
//                //.rotateEnabled(false) // ???????????????????????????
//                //.scaleEnabled(false)// ?????????????????????????????????
//                //.videoQuality()// ?????????????????? 0 or 1
//                //.forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
//                .forResult(new OnResultCallbackListener<LocalMedia>() {
//                    @Override
//                    public void onResult(List<LocalMedia> result) {
//                        for (LocalMedia media : result) {
//                            if (media.getWidth() == 0 || media.getHeight() == 0) {
//                                if (PictureMimeType.isHasImage(media.getMimeType())) {
//                                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
//                                    media.setWidth(imageExtraInfo.getWidth());
//                                    media.setHeight(imageExtraInfo.getHeight());
//                                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
//                                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
//                                    media.setWidth(videoExtraInfo.getWidth());
//                                    media.setHeight(videoExtraInfo.getHeight());
//                                }
//                            }
//                            Log.i("TAG", "?????????: " + media.getFileName());
//                            Log.i("TAG", "????????????:" + media.isCompressed());
//                            Log.i("TAG", "??????:" + media.getCompressPath());
//                            Log.i("TAG", "??????:" + media.getPath());
//                            Log.i("TAG", "????????????:" + media.getRealPath());
//                            Log.i("TAG", "????????????:" + media.isCut());
//                            Log.i("TAG", "??????:" + media.getCutPath());
//                            Log.i("TAG", "??????????????????:" + media.isOriginal());
//                            Log.i("TAG", "????????????:" + media.getOriginalPath());
//                            Log.i("TAG", "Android Q ??????Path:" + media.getAndroidQToPath());
//                            Log.i("TAG", "??????: " + media.getWidth() + "x" + media.getHeight());
//                            Log.i("TAG", "Size: " + media.getSize());
//
//                            Log.i("TAG", "onResult: " + media.toString());
//
//                            // TODO ????????????PictureSelectorExternalUtils.getExifInterface();??????????????????????????????????????????????????????????????????????????????
//                        }
//                        String ImgUrl = result.get(0).getAndroidQToPath();
////                        Glide.with(ScanAct1.this).load(ImgUrl).into(iv1);
//                        Glide.with(ScanAct2.this)
//                                .asBitmap()
//                                .load(ImgUrl)
//                                .into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {
//                                        Drawable newDraw = new BitmapDrawable(bitmap);
//                                        iv0.setImageDrawable(newDraw);
//                                        //
////                                        final String s = TextRecognition.recognize(ImgUrl);
////                                        tv0.setText("result=" + s);
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//    }
//
//    public void onClickBtn3(View v) {
//        //                String path = getExternalFilesDir("image").getAbsolutePath() + "/a.jpg";
////                String s = ScannerUtils.decodeText(path);
////                BankCardInfoBean b = ScannerUtils.getBankCardInfo("6222600260001072444");
////                String s = b == null ? null : b.toString();
//        PictureSelector.create(ScanAct2.this)
//                .openCamera(PictureMimeType.ofAll())// ?????????????????????????????????????????? ??????????????????????????????or??????
//                .imageEngine(GlideEngine2.createGlideEngine())// ??????????????????????????????????????????
//                //.theme(themeId)// ?????????????????? ???????????? values/styles   ?????????R.style.picture.white.style v2.3.3??? ????????????setPictureStyle()????????????
//                .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())
//                //.setPictureStyle(mPictureParameterStyle)// ???????????????????????????
//                //.setPictureCropStyle(mCropParameterStyle)// ???????????????????????????
//                .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle())// ?????????????????????????????????
//                .isWeChatStyle(true)// ????????????????????????????????????
//                .isUseCustomCamera(false)// ???????????????????????????
////                                        .setLan,guage(language)// ???????????????????????????
////                                        .isPageStrategy(cbPage.isChecked())// ???????????????????????? & ??????????????????????????????
//                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// ??????????????????
//                .isWithVideoImage(true)// ?????????????????????????????????,??????ofAll???????????????
//                //.isSyncCover(true)// ???????????????MediaStore???????????????????????????????????????????????????????????????????????????
//                //.isCameraAroundState(false) // ????????????????????????????????????false??????????????????????????? ???????????????????????????????????????
//                //.isCameraRotateImage(false) // ????????????????????????????????????
//                //.isAutoRotating(false)// ???????????????????????????????????????
////                                        .isMaxSelectEnabledMask(cbEnabledMask.isChecked())// ?????????????????????????????????????????????????????????
//                //.isAutomaticTitleRecyclerTop(false)// ?????????????????????RecyclerView????????????????????????,??????true
//                //.loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// ????????????????????????????????????????????????10??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                //.setOutputCameraPath(createCustomCameraOutPath())// ???????????????????????????
//                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// ?????????????????????????????????
////                        .setCaptureLoadingColor(ContextCompat.getColor(mActivity, R.color.app_color_blue))
////                                        .maxSelectNum(maxSelectNum)// ????????????????????????
//                .minSelectNum(1)// ??????????????????
//                .maxVideoSelectNum(1) // ????????????????????????
//                //.minVideoSelectNum(1)// ????????????????????????
//                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// ?????????AndroidQ????????????????????????????????????????????????
//                .imageSpanCount(4)// ??????????????????
//                //.queryFileSize() // ??????????????????,?????????
//                //.filterMinFileSize(5)// ???????????????????????????kb
//                //.filterMaxFileSize()// ???????????????????????????kb
//                .isReturnEmpty(false)// ????????????????????????????????????????????????
//                .closeAndroidQChangeWH(true)//??????????????????????????????????????????,?????????true
//                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// ??????????????????????????????????????????,?????????false
//                .isAndroidQTransform(true)// ??????????????????Android Q ??????????????????????????????????????????compress(false); && .isEnableCrop(false);??????,????????????
//                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// ????????????Activity????????????????????????????????????
//                .isOriginalImageControl(false)// ????????????????????????????????????????????????true?????????????????????????????????????????????????????????????????????????????????
//                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// ?????????????????????????????????????????????????????????????????????????????????
//                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// ?????????????????????????????????
//                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// ??????????????????????????????????????????????????????
//                //.bindCustomPermissionsObtainListener(new MyPermissionsObtainCallback())// ?????????????????????
//                //.cameraFileName(System.currentTimeMillis() +".jpg")    // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ????????????????????????????????????api
//                //.renameCompressFile(System.currentTimeMillis() +".jpg")// ??????????????????????????? ????????????????????????????????????????????????????????????????????????
//                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// ??????????????????????????? ????????????????????????????????????????????????????????????????????????
//                .selectionMode(PictureConfig.SINGLE)// ?????? or ??????
//                .isSingleDirectReturn(true)// ????????????????????????????????????PictureConfig.SINGLE???????????????
//                .isPreviewImage(true)// ?????????????????????
//                .isPreviewVideo(false)// ?????????????????????
//                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// ??????????????????????????????
//                //.queryMimeTypeConditions(PictureMimeType.ofWEBP())
//                .isEnablePreviewAudio(false) // ?????????????????????
//                .isCamera(true)// ????????????????????????
//                //.isMultipleSkipCrop(false)// ????????????????????????????????????????????????
//                //.isMultipleRecyclerAnimation(false)// ??????????????????????????????????????????
//                .isZoomAnim(true)// ?????????????????? ???????????? ??????true
//                //.imageFormat(PictureMimeType.PNG)// ??????????????????????????????,??????jpeg,Android Q??????PictureMimeType.PNG_Q
//                .isEnableCrop(true)// ????????????
//                //.basicUCropConfig()//??????????????????UCropOptions????????????????????????PictureSelector???????????????????????????????????????????????????
//                .isCompress(true)// ????????????
//                //.compressFocusAlpha(true)// ?????????????????????????????????
//                //.compressEngine(ImageCompressEngine.createCompressEngine()) // ?????????????????????
//                //.compressQuality(80)// ??????????????????????????? 0~ 100
//                .synOrAsy(false)//??????true?????????false ?????? ????????????
//                //.queryMaxFileSize(10)// ????????????M?????????????????????????????????  ??????M
//                //.compressSavePath(getPath())//????????????????????????
//                //.sizeMultiplier(0.5f)// glide ?????????????????? 0~1?????? ????????? .glideOverride()?????? ???????????????
//                //.glideOverride(160, 160)// glide ??????????????????????????????????????????????????????????????????????????????????????? ???????????????
//                .withAspectRatio(0, 0)// ???????????? ???16:9 3:2 3:4 1:1 ????????????
//                .hideBottomControls(true)// ????????????uCrop???????????????????????????
//                .isGif(true)// ????????????gif??????
//                //.isWebp(false)// ????????????webp??????,????????????
//                //.isBmp(false)//????????????bmp??????,????????????
//                .freeStyleCropEnabled(true)// ????????????????????????
//                .circleDimmedLayer(false)// ??????????????????
//                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// ????????????????????????
//                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// ??????????????????????????????
//                //.setCircleStrokeWidth(3)// ??????????????????????????????
//                .showCropFrame(true)// ?????????????????????????????? ???????????????????????????false
//                .showCropGrid(true)// ?????????????????????????????? ???????????????????????????false
//                .isOpenClickSound(false)// ????????????????????????
////                                        .selectionData(mAdapter.getData())// ????????????????????????
//                //.isDragFrame(false)// ????????????????????????(??????)
//                //.videoMinSecond(10)// ??????????????????????????????
//                //.videoMaxSecond(15)// ??????????????????????????????
//                //.recordVideoSecond(10)//?????????????????? ??????60s
//                //.isPreviewEggs(true)// ??????????????? ????????????????????????????????????(???????????????????????????????????????????????????)
//                //.cropCompressQuality(90)// ??????????????? ??????cutOutQuality()
//                .cutOutQuality(90)// ?????????????????? ??????100
//                //.cutCompressFormat(Bitmap.CompressFormat.PNG.name())//??????????????????Format???????????????JPEG
//                .minimumCompressSize(100)// ????????????kb??????????????????
//                //.cropWH()// ???????????????????????????????????????????????????????????????
//                //.cropImageWideHigh()// ???????????????????????????????????????????????????????????????
//                //.rotateEnabled(false) // ???????????????????????????
//                //.scaleEnabled(false)// ?????????????????????????????????
//                //.videoQuality()// ?????????????????? 0 or 1
//                //.forResult(PictureConfig.CHOOSE_REQUEST);//????????????onActivityResult code
//                .forResult(new OnResultCallbackListener<LocalMedia>() {
//                    @Override
//                    public void onResult(List<LocalMedia> result) {
//                        for (LocalMedia media : result) {
//                            if (media.getWidth() == 0 || media.getHeight() == 0) {
//                                if (PictureMimeType.isHasImage(media.getMimeType())) {
//                                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
//                                    media.setWidth(imageExtraInfo.getWidth());
//                                    media.setHeight(imageExtraInfo.getHeight());
//                                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
//                                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
//                                    media.setWidth(videoExtraInfo.getWidth());
//                                    media.setHeight(videoExtraInfo.getHeight());
//                                }
//                            }
//                            Log.i("TAG", "?????????: " + media.getFileName());
//                            Log.i("TAG", "????????????:" + media.isCompressed());
//                            Log.i("TAG", "??????:" + media.getCompressPath());
//                            Log.i("TAG", "??????:" + media.getPath());
//                            Log.i("TAG", "????????????:" + media.getRealPath());
//                            Log.i("TAG", "????????????:" + media.isCut());
//                            Log.i("TAG", "??????:" + media.getCutPath());
//                            Log.i("TAG", "??????????????????:" + media.isOriginal());
//                            Log.i("TAG", "????????????:" + media.getOriginalPath());
//                            Log.i("TAG", "Android Q ??????Path:" + media.getAndroidQToPath());
//                            Log.i("TAG", "??????: " + media.getWidth() + "x" + media.getHeight());
//                            Log.i("TAG", "Size: " + media.getSize());
//
//                            Log.i("TAG", "onResult: " + media.toString());
//
//                            // TODO ????????????PictureSelectorExternalUtils.getExifInterface();??????????????????????????????????????????????????????????????????????????????
//                        }
//                        String ImgUrl = result.get(0).getAndroidQToPath();
////                        Glide.with(ScanAct1.this).load(ImgUrl).into(iv1);
//                        Glide.with(ScanAct2.this)
//                                .asBitmap()
//                                .load(ImgUrl)
//                                .into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {
//                                        Drawable newDraw = new BitmapDrawable(bitmap);
//                                        iv1.setImageDrawable(newDraw);
//                                        //
//                                        final String s;
//                                        try {
//                                            s = String.valueOf(decodeNsfw(ScanAct2.this, bitmap));
//                                            ToastUtils.showLong("???????????????" + s);
//                                            tv1.setText("???????????????" + s);
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//
//    }
//
//    public void onClickBtn4(View v) {
////        Intent intent = new Intent(this, ScannerAct2.class);
////        startActivity(intent);
//    }
//
//    public void onClickBtn5(View v) {
////        Intent intent = new Intent(this, ScannerAct2.class);
////        startActivity(intent);
//    }
//
//    public void onClickBtn6(View v) {
////        Intent intent = new Intent(this, ScannerAct2.class);
////        startActivity(intent);
//    }
//
//    /**
//     * ???????????????????????????????????????
//     *
//     * @param context
//     * @param bmp
//     * @return ??????0.3????????????????????????????????????????????????
//     * @throws Exception
//     */
//    public static float decodeNsfw(Context context, Bitmap bmp) throws Exception {
////        Interpreter tflite = NsfwUtils.getInterpreter(context);
////        float f = NsfwUtils.decode(tflite, bmp);
////        NsfwUtils.release(tflite);
//        return 1f;
//    }
//
//
//}
