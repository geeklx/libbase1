//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.util.Log;

import com.ttv.face.enums.CompareModel;
import com.ttv.face.enums.DetectFaceOrientPriority;
import com.ttv.face.enums.DetectMode;
import com.ttv.face.enums.DetectModel;
import com.ttv.face.enums.ExtractType;
import com.ttv.face.model.TTVImageInfo;

import java.util.List;

public class FaceSDK extends ContextWrapper {
    public static final int TTV_NONE = 0;
    public static final int TTV_FACE_DETECT = 1;
    public static final int TTV_FACE_RECOGNITION = 4;
    public static final int TTV_AGE = 8;
    public static final int TTV_GENDER = 16;
    public static final int TTV_LIVENESS = 128;
    public static final int TTV_IMAGEQUALITY = 512;
    public static final int TTV_IR_LIVENESS = 1024;
    public static final int TTV_MASK_DETECT = 4096;
    public static final int TTV_UPDATE_FACEDATA = 8192;
    public static final int TTV_OC_0 = 1;
    public static final int TTV_OC_90 = 2;
    public static final int TTV_OC_270 = 3;
    public static final int TTV_OC_180 = 4;
    public static final int TTV_OC_30 = 5;
    public static final int TTV_OC_60 = 6;
    public static final int TTV_OC_120 = 7;
    public static final int TTV_OC_150 = 8;
    public static final int TTV_OC_210 = 9;
    public static final int TTV_OC_240 = 10;
    public static final int TTV_OC_300 = 11;
    public static final int TTV_OC_330 = 12;
    public static final int CP_PAF_BGR24 = 513;
    public static final int CP_PAF_GRAY = 1793;
    public static final int CP_PAF_NV21 = 2050;
    public static final int CP_PAF_DEPTH_U16 = 3074;
    private ErrorInfo detectError = new ErrorInfo();
    private ErrorInfo detectQualityError = new ErrorInfo();
    private ErrorInfo faceDataError = new ErrorInfo();
    private ErrorInfo frError = new ErrorInfo();
    private long handle = 0L;
    private float[] imageQualityArray;
    private ErrorInfo initError = new ErrorInfo();
    private AgeInfo[] mAgeInfoArray;
    private FaceInfo[] mFaceInfoArray;
    private GenderInfo[] mGenderInfoArray;
    private LivenessInfo[] mIrLivenessInfoArray;
    private LivenessInfo[] mLivenessInfoArray;
    private MaskInfo[] mMaskInfoArray;
    private ErrorInfo processError = new ErrorInfo();
    private ErrorInfo processIrError = new ErrorInfo();

    private native String nativeGetCurrentHWID();

    private native int nativeSetActivation(String var1);

    private native int nativeDetectFaces(long var1, TTVImageInfo var3, int var4, FaceInfo[] var5, ErrorInfo var6);

    private native int nativeDetectFaces(long var1, byte[] var3, int var4, int var5, int var6, int var7, FaceInfo[] var8, ErrorInfo var9);

    private native void nativeExtractFaceFeature(long var1, TTVImageInfo var3, Rect var4, int var5, byte[] var6, int var7, int var8, int var9, byte[] var10, ErrorInfo var11);

    private native void nativeExtractFaceFeature(long var1, byte[] var3, int var4, int var5, int var6, Rect var7, int var8, byte[] var9, int var10, int var11, int var12, byte[] var13, ErrorInfo var14);

    private native int nativeGetAge(long var1, AgeInfo[] var3, ErrorInfo var4);

    private native int nativeGetFaceCount(long var1);

    private native FaceFeatureInfo nativeGetFaceFeature(long var1, int var3);

    private native int nativeGetGender(long var1, GenderInfo[] var3, ErrorInfo var4);

    private native int nativeGetIrLiveness(long var1, LivenessInfo[] var3, ErrorInfo var4);

    private native int nativeGetLiveness(long var1, LivenessInfo[] var3, ErrorInfo var4);

    private native LivenessParam nativeGetLivenessParam(long var1);

    private native int nativeGetMask(long var1, MaskInfo[] var3, ErrorInfo var4);

    private native float nativeImageQualityDetect(long var1, TTVImageInfo var3, Rect var4, int var5, byte[] var6, int var7, int var8, ErrorInfo var9);

    private native float nativeImageQualityDetect(long var1, byte[] var3, int var4, int var5, int var6, Rect var7, int var8, byte[] var9, int var10, int var11, ErrorInfo var12);

    private native long nativeInitFaceEngine(Context var1, long var2, int var4, int var5, int var6, ErrorInfo var7);

    private native float nativePairMatching(long var1, byte[] var3, byte[] var4, int var5, ErrorInfo var6);

    private native void nativeProcess(long var1, TTVImageInfo var3, FaceInfo[] var4, int var5, ErrorInfo var6);

    private native void nativeProcess(long var1, byte[] var3, int var4, int var5, int var6, FaceInfo[] var7, int var8, ErrorInfo var9);

    private native void nativeProcessIr(long var1, TTVImageInfo var3, FaceInfo[] var4, int var5, ErrorInfo var6);

    private native void nativeProcessIr(long var1, byte[] var3, int var4, int var5, int var6, FaceInfo[] var7, int var8, ErrorInfo var9);

    private native int nativeRegisterFaceFeature(long var1, FaceFeatureInfo var3);

    private native int nativeRegisterFaceFeatureArray(long var1, FaceFeatureInfo[] var3);

    private native int nativeRemoveFaceFeature(long var1, int var3);

    private native SearchResult nativeSearchFaceFeature(long var1, byte[] var3, int var4);

    private native int nativeSetFaceAttributeParam(long var1, FaceAttributeParam var3);

    private native int nativeSetLivenessParam(long var1, LivenessParam var3);

    private native int nativeUnInitFaceEngine(long var1);

    private native int nativeUpdateFaceData(long var1, TTVImageInfo var3, FaceInfo[] var4, ErrorInfo var5);

    private native int nativeUpdateFaceData(long var1, byte[] var3, int var4, int var5, int var6, FaceInfo[] var7, ErrorInfo var8);

    private native int nativeUpdateFaceFeature(long var1, FaceFeatureInfo var3);

    private static native int nativeProcess1();

    private static native int nativeProcess2();

    private static native int nativeProcess3();

    private static native int nativeProcess4();

    private static native int nativeProcess5();

    private static native int nativeProcess6();

    private static native int nativeProcess7();

    private static native int nativeProcess8();

    public FaceSDK(Context context) {
        super(context);
    }

    public String getCurrentHWID() {
        return this.nativeGetCurrentHWID();
    }

    public int setActivation(String license) {
        return this.nativeSetActivation(license);
    }

    public int init(Context context, DetectMode detectMode, DetectFaceOrientPriority detectFaceOrientPriority, int detectFaceMaxNum, int combinedMask) {
        if (this.handle != 0L) {
            return 5;
        } else if (context != null && detectMode != null && detectFaceOrientPriority != null) {
            this.handle = this.nativeInitFaceEngine(context, detectMode.getMode(), detectFaceOrientPriority.getPriority(), detectFaceMaxNum, combinedMask, this.initError);
            if (this.initError.getCode() == 0) {
                int i7;
                if ((combinedMask & 1) != 0) {
                    this.mFaceInfoArray = new FaceInfo[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.mFaceInfoArray[i7] = new FaceInfo();
                    }
                }

                if ((combinedMask & 8) != 0) {
                    this.mAgeInfoArray = new AgeInfo[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.mAgeInfoArray[i7] = new AgeInfo();
                    }
                }

                if ((combinedMask & 16) != 0) {
                    this.mGenderInfoArray = new GenderInfo[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.mGenderInfoArray[i7] = new GenderInfo();
                    }
                }

                if ((combinedMask & 128) != 0) {
                    this.mLivenessInfoArray = new LivenessInfo[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.mLivenessInfoArray[i7] = new LivenessInfo();
                    }
                }

                if ((combinedMask & 1024) != 0) {
                    this.mIrLivenessInfoArray = new LivenessInfo[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.mIrLivenessInfoArray[i7] = new LivenessInfo();
                    }
                }

                if ((combinedMask & 4096) != 0) {
                    this.mMaskInfoArray = new MaskInfo[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.mMaskInfoArray[i7] = new MaskInfo();
                    }
                }

                if ((combinedMask & 512) != 0) {
                    this.imageQualityArray = new float[detectFaceMaxNum];

                    for (i7 = 0; i7 < detectFaceMaxNum; ++i7) {
                        this.imageQualityArray[i7] = 0.0F;
                    }
                }
            }

            Log.e("ddddd", "init error code: " + this.initError.code);
            return this.initError.code;
        } else {
            return 2;
        }
    }

    public int unInit() {
        if (this.handle == 0L) {
            return 5;
        } else {
            int unInitEngineCode = this.nativeUnInitFaceEngine(this.handle);
            if (unInitEngineCode != 0) {
                return unInitEngineCode;
            } else {
                this.handle = 0L;
                return unInitEngineCode;
            }
        }
    }

    public int setFaceAttributeParam(FaceAttributeParam faceAttributeParam) {
        if (faceAttributeParam != null && !(faceAttributeParam.getEyeopenThreshold() > 1.0F) && !(faceAttributeParam.getEyeopenThreshold() < 0.0F) && !(faceAttributeParam.getMouthcloseThreshold() > 1.0F) && !(faceAttributeParam.getMouthcloseThreshold() < 0.0F) && !(faceAttributeParam.getWearGlassesThreshold() > 1.0F) && !(faceAttributeParam.getWearGlassesThreshold() < 0.0F)) {
            return this.handle == 0L ? 5 : this.nativeSetFaceAttributeParam(this.handle, faceAttributeParam);
        } else {
            return 2;
        }
    }

    public int detectFaces(byte[] data, int width, int height, int format, List<FaceInfo> faceInfoList) {
        return this.detectFaces(data, width, height, format, DetectModel.RGB, faceInfoList);
    }

    public int detectFaces(byte[] data, int width, int height, int format, DetectModel detectModel, List<FaceInfo> faceInfoList) {
        if (format != 2050 && format != 513 && format != 1793 && format != 3074) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (faceInfoList != null && data != null && detectModel != null) {
                if (!isImageDataValid(data, width, height, format)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    faceInfoList.clear();
                    int count = this.nativeDetectFaces(this.handle, data, width, height, format, detectModel.getModel(), this.mFaceInfoArray, this.detectError);
                    if (count > 0) {
                        for (int i = 0; i < count; ++i) {
                            faceInfoList.add(new FaceInfo(this.mFaceInfoArray[i]));
                        }
                    }

                    return this.detectError.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int detectFaces(TTVImageInfo ttvImageInfo, List<FaceInfo> faceInfoList) {
        return this.detectFaces(ttvImageInfo, DetectModel.RGB, faceInfoList);
    }

    public int detectFaces(TTVImageInfo ttvImageInfo, DetectModel detectModel, List<FaceInfo> faceInfoList) {
        if (faceInfoList != null && ttvImageInfo != null) {
            int imageFormat = ttvImageInfo.getImageFormat();
            if (imageFormat != 2050 && imageFormat != 513 && imageFormat != 1793 && imageFormat != 3074) {
                return 90126;
            } else if (ttvImageInfo.getWidth() > 0 && ttvImageInfo.getHeight() > 0) {
                if (!isImageDataValid(ttvImageInfo)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    faceInfoList.clear();
                    int count = this.nativeDetectFaces(this.handle, ttvImageInfo, detectModel.getModel(), this.mFaceInfoArray, this.detectError);
                    if (count > 0) {
                        for (int i = 0; i < count; ++i) {
                            faceInfoList.add(new FaceInfo(this.mFaceInfoArray[i]));
                        }
                    }

                    return this.detectError.code;
                }
            } else {
                return 90127;
            }
        } else {
            return 2;
        }
    }

    public int updateFaceData(byte[] data, int width, int height, int format, List<FaceInfo> faceInfoList) {
        if (format != 2050 && format != 513 && format != 1793 && format != 3074) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (faceInfoList != null && data != null) {
                if (!isImageDataValid(data, width, height, format)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.nativeUpdateFaceData(this.handle, data, width, height, format, (FaceInfo[]) faceInfoList.toArray(new FaceInfo[0]), this.faceDataError);
                    return this.faceDataError.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int updateFaceData(TTVImageInfo ttvImageInfo, List<FaceInfo> faceInfoList) {
        if (faceInfoList != null && ttvImageInfo != null) {
            int imageFormat = ttvImageInfo.getImageFormat();
            if (imageFormat != 2050 && imageFormat != 513 && imageFormat != 1793 && imageFormat != 3074) {
                return 90126;
            } else if (ttvImageInfo.getWidth() > 0 && ttvImageInfo.getHeight() > 0) {
                if (!isImageDataValid(ttvImageInfo)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.nativeUpdateFaceData(this.handle, ttvImageInfo, (FaceInfo[]) faceInfoList.toArray(new FaceInfo[0]), this.faceDataError);
                    return this.faceDataError.code;
                }
            } else {
                return 90127;
            }
        } else {
            return 2;
        }
    }

    public int process(byte[] data, int width, int height, int format, List<FaceInfo> faceInfoList, int combinedMask) {
        if (format != 2050 && format != 513) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (faceInfoList != null && data != null) {
                if (!isImageDataValid(data, width, height, format)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.nativeProcess(this.handle, data, width, height, format, (FaceInfo[]) faceInfoList.toArray(new FaceInfo[0]), combinedMask, this.processError);
                    return this.processError.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int process(TTVImageInfo ttvImageInfo, List<FaceInfo> faceInfoList, int combinedMask) {
        if (faceInfoList != null && ttvImageInfo != null) {
            if (ttvImageInfo.getImageFormat() != 2050 && ttvImageInfo.getImageFormat() != 513) {
                return 90126;
            } else if (ttvImageInfo.getWidth() > 0 && ttvImageInfo.getHeight() > 0) {
                if (!isImageDataValid(ttvImageInfo)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.nativeProcess(this.handle, ttvImageInfo, (FaceInfo[]) faceInfoList.toArray(new FaceInfo[0]), combinedMask, this.processError);
                    return this.processError.code;
                }
            } else {
                return 90127;
            }
        } else {
            return 2;
        }
    }

    public int processIr(byte[] data, int width, int height, int format, List<FaceInfo> faceInfoList, int combinedMask) {
        if (format != 2050 && format != 1793 && format != 3074) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (faceInfoList != null && data != null) {
                if (!isImageDataValid(data, width, height, format)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.nativeProcessIr(this.handle, data, width, height, format, (FaceInfo[]) faceInfoList.toArray(new FaceInfo[0]), combinedMask, this.processIrError);
                    return this.processIrError.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int processIr(TTVImageInfo ttvImageInfo, List<FaceInfo> faceInfoList, int combinedMask) {
        if (faceInfoList != null && ttvImageInfo != null) {
            if (ttvImageInfo.getImageFormat() != 2050 && ttvImageInfo.getImageFormat() != 1793 && ttvImageInfo.getImageFormat() != 3074) {
                return 90126;
            } else if (ttvImageInfo.getWidth() > 0 && ttvImageInfo.getHeight() > 0) {
                if (!isImageDataValid(ttvImageInfo)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    this.nativeProcessIr(this.handle, ttvImageInfo, (FaceInfo[]) faceInfoList.toArray(new FaceInfo[0]), combinedMask, this.processIrError);
                    return this.processIrError.code;
                }
            } else {
                return 90127;
            }
        } else {
            return 2;
        }
    }

    public int imageQualityDetect(byte[] data, int width, int height, int format, FaceInfo faceInfo, int isMask, ImageQualitySimilar imageQualitySimilar) {
        if (width > 0 && height > 0) {
            if (faceInfo != null && data != null) {
                if (format != 2050 && format != 1793 && format != 513) {
                    return 90126;
                } else if (!isImageDataValid(data, width, height, format)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    imageQualitySimilar.score = this.nativeImageQualityDetect(this.handle, data, width, height, format, faceInfo.getRect(), faceInfo.getOrient(), faceInfo.getFaceData(), 5000, isMask, this.detectQualityError);
                    return this.detectQualityError.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int imageQualityDetect(TTVImageInfo ttvImageInfo, FaceInfo faceInfo, int isMask, ImageQualitySimilar imageQualitySimilar) {
        if (faceInfo != null && ttvImageInfo != null) {
            if (ttvImageInfo.getWidth() > 0 && ttvImageInfo.getHeight() > 0) {
                if (ttvImageInfo.getImageFormat() != 2050 && ttvImageInfo.getImageFormat() != 1793 && ttvImageInfo.getImageFormat() != 513) {
                    return 90126;
                } else if (!isImageDataValid(ttvImageInfo)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    imageQualitySimilar.score = this.nativeImageQualityDetect(this.handle, ttvImageInfo, faceInfo.getRect(), faceInfo.getOrient(), faceInfo.getFaceData(), 5000, isMask, this.detectQualityError);
                    return this.detectQualityError.code;
                }
            } else {
                return 90127;
            }
        } else {
            return 2;
        }
    }

    public int extractFaceFeature(byte[] data, int width, int height, int format, FaceInfo faceInfo, ExtractType extractType, int mask, FaceFeature feature) {
        if (format != 2050 && format != 513 && format != 1793) {
            return 90126;
        } else if (width > 0 && height > 0) {
            if (feature != null && feature.getFeatureData() != null && feature.getFeatureData().length >= 2056 && faceInfo != null && extractType != null && data != null) {
                if (!isImageDataValid(data, width, height, format)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    if (mask != 1) {
                        mask = 0;
                    }

                    this.nativeExtractFaceFeature(this.handle, data, width, height, format, faceInfo.getRect(), faceInfo.getOrient(), faceInfo.getFaceData(), 5000, extractType.getExtractType(), mask, feature.getFeatureData(), this.frError);
                    return this.frError.code;
                }
            } else {
                return 2;
            }
        } else {
            return 90127;
        }
    }

    public int extractFaceFeature(TTVImageInfo ttvImageInfo, FaceInfo faceInfo, ExtractType extractType, int mask, FaceFeature feature) {
        if (feature != null && feature.getFeatureData() != null && feature.getFeatureData().length >= 2056 && faceInfo != null && extractType != null && ttvImageInfo != null) {
            if (ttvImageInfo.getImageFormat() != 2050 && ttvImageInfo.getImageFormat() != 513 && ttvImageInfo.getImageFormat() != 1793) {
                return 90126;
            } else if (ttvImageInfo.getWidth() > 0 && ttvImageInfo.getHeight() > 0) {
                if (!isImageDataValid(ttvImageInfo)) {
                    return 86021;
                } else if (this.handle == 0L) {
                    return 5;
                } else {
                    if (mask != 1) {
                        mask = 0;
                    }

                    this.nativeExtractFaceFeature(this.handle, ttvImageInfo, faceInfo.getRect(), faceInfo.getOrient(), faceInfo.getFaceData(), 5000, extractType.getExtractType(), mask, feature.getFeatureData(), this.frError);
                    return this.frError.code;
                }
            } else {
                return 90127;
            }
        } else {
            return 2;
        }
    }

    public int compareFaceFeature(FaceFeature feature1, FaceFeature feature2, FaceSimilar faceSimilar) {
        return this.compareFaceFeature(feature1, feature2, CompareModel.LIFE_PHOTO, faceSimilar);
    }

    public int compareFaceFeature(FaceFeature feature1, FaceFeature feature2, CompareModel compareModel, FaceSimilar faceSimilar) {
        if (feature1 != null && feature1.getFeatureData() != null && feature2 != null && feature2.getFeatureData() != null && faceSimilar != null && compareModel != null) {
            if (this.handle == 0L) {
                return 5;
            } else {
                faceSimilar.score = this.nativePairMatching(this.handle, feature1.getFeatureData(), feature2.getFeatureData(), compareModel.getModel(), this.frError);
                return this.frError.code;
            }
        } else {
            return 2;
        }
    }

    public SearchResult searchFaceFeature(FaceFeature feature) throws IllegalArgumentException {
        return this.searchFaceFeature(feature, CompareModel.LIFE_PHOTO);
    }

    public SearchResult searchFaceFeature(FaceFeature feature, CompareModel compareModel) throws IllegalArgumentException {
        if (feature != null && feature.getFeatureData() != null && compareModel != null) {
            if (this.handle != 0L) {
                return this.nativeSearchFaceFeature(this.handle, feature.getFeatureData(), compareModel.getModel());
            } else {
                throw new IllegalArgumentException(String.format("%d: bad state", 5));
            }
        } else {
            throw new IllegalArgumentException(String.format("%d: invalid parameters", 2));
        }
    }

    public int registerFaceFeature(FaceFeatureInfo faceFeatureInfo) {
        if (faceFeatureInfo != null && faceFeatureInfo.getFeatureData() != null) {
            return this.handle == 0L ? 5 : this.nativeRegisterFaceFeature(this.handle, faceFeatureInfo);
        } else {
            return 2;
        }
    }

    public int registerFaceFeature(List<FaceFeatureInfo> faceFeatureInfoList) {
        if (faceFeatureInfoList != null && !faceFeatureInfoList.isEmpty()) {
            return this.handle == 0L ? 5 : this.nativeRegisterFaceFeatureArray(this.handle, (FaceFeatureInfo[]) faceFeatureInfoList.toArray(new FaceFeatureInfo[0]));
        } else {
            return 2;
        }
    }

    public int removeFaceFeature(int searchId) {
        return this.handle == 0L ? 5 : this.nativeRemoveFaceFeature(this.handle, searchId);
    }

    public int updateFaceFeature(FaceFeatureInfo faceFeatureInfo) {
        if (faceFeatureInfo != null && faceFeatureInfo.getFeatureData() != null) {
            return this.handle == 0L ? 5 : this.nativeUpdateFaceFeature(this.handle, faceFeatureInfo);
        } else {
            return 2;
        }
    }

    public FaceFeatureInfo getFaceFeature(int searchId) throws IllegalArgumentException {
        if (this.handle != 0L) {
            return this.nativeGetFaceFeature(this.handle, searchId);
        } else {
            throw new IllegalArgumentException(String.format("%d: bad state", 5));
        }
    }

    public int getFaceCount() throws IllegalArgumentException {
        if (this.handle != 0L) {
            return this.nativeGetFaceCount(this.handle);
        } else {
            throw new IllegalArgumentException(String.format("%d: bad state", 5));
        }
    }

    public int getAge(List<AgeInfo> ageInfoList) {
        if (ageInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            ageInfoList.clear();
            int count = this.nativeGetAge(this.handle, this.mAgeInfoArray, this.processError);
            if (count > 0) {
                for (int i = 0; i < count; ++i) {
                    ageInfoList.add(new AgeInfo(this.mAgeInfoArray[i]));
                }
            }

            return this.processError.code;
        }
    }

    public int getGender(List<GenderInfo> genderInfoList) {
        if (genderInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            genderInfoList.clear();
            int count = this.nativeGetGender(this.handle, this.mGenderInfoArray, this.processError);
            if (count > 0) {
                for (int i = 0; i < count; ++i) {
                    genderInfoList.add(new GenderInfo(this.mGenderInfoArray[i]));
                }
            }

            return this.processError.code;
        }
    }

    public LivenessParam getLivenessParam() throws IllegalArgumentException {
        if (this.handle != 0L) {
            return this.nativeGetLivenessParam(this.handle);
        } else {
            throw new IllegalArgumentException(String.format("%d: bad state", 5));
        }
    }

    public int setLivenessParam(LivenessParam livenessParam) {
        if (livenessParam != null && !(livenessParam.getIrThreshold() > 1.0F) && !(livenessParam.getIrThreshold() < 0.0F) && !(livenessParam.getRgbThreshold() > 1.0F) && !(livenessParam.getRgbThreshold() < 0.0F) && !(livenessParam.getFqThreshold() > 1.0F) && !(livenessParam.getFqThreshold() < 0.0F)) {
            return this.handle == 0L ? 5 : this.nativeSetLivenessParam(this.handle, livenessParam);
        } else {
            return 2;
        }
    }

    public int getLiveness(List<LivenessInfo> livenessInfoList) {
        if (livenessInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            livenessInfoList.clear();
            int count = this.nativeGetLiveness(this.handle, this.mLivenessInfoArray, this.processError);

            for (int i = 0; i < count; ++i) {
                livenessInfoList.add(new LivenessInfo(this.mLivenessInfoArray[i]));
            }

            return this.processError.code;
        }
    }

    public int getIrLiveness(List<LivenessInfo> irLivenessInfoList) {
        if (irLivenessInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            irLivenessInfoList.clear();
            int count = this.nativeGetIrLiveness(this.handle, this.mIrLivenessInfoArray, this.processIrError);

            for (int i = 0; i < count; ++i) {
                irLivenessInfoList.add(new LivenessInfo(this.mIrLivenessInfoArray[i]));
            }

            return this.processIrError.code;
        }
    }

    public int getMask(List<MaskInfo> maskInfoList) {
        if (maskInfoList == null) {
            return 2;
        } else if (this.handle == 0L) {
            return 5;
        } else {
            maskInfoList.clear();
            int count = this.nativeGetMask(this.handle, this.mMaskInfoArray, this.processError);

            for (int i = 0; i < count; ++i) {
                maskInfoList.add(new MaskInfo(this.mMaskInfoArray[i]));
            }

            return this.processError.code;
        }
    }

    private static boolean isImageDataValid(byte[] data, int width, int height, int format) {
        return format == 2050 && (height & 1) == 0 && data.length == width * height * 3 / 2 || format == 513 && data.length == width * height * 3 || format == 1793 && data.length == width * height || format == 3074 && data.length == width * height * 2;
    }

    private static boolean isImageDataValid(TTVImageInfo ttvImageInfo) {
        boolean z = true;
        byte[][] planes = ttvImageInfo.getPlanes();
        int[] strides = ttvImageInfo.getStrides();
        if (planes != null && strides != null && planes.length == strides.length) {
            byte[][] var4 = planes;
            int var5 = planes.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                byte[] plane = var4[var6];
                if (plane == null || plane.length == 0) {
                    return false;
                }
            }

            switch (ttvImageInfo.getImageFormat()) {
                case 513:
                case 1793:
                case 3074:
                    if (planes.length != 1 || planes[0].length != ttvImageInfo.getStrides()[0] * ttvImageInfo.getHeight()) {
                        z = false;
                    }

                    return z;
                case 2050:
                    if ((ttvImageInfo.getHeight() & 1) != 0 || planes.length != 2 || planes[0].length != planes[1].length * 2 || planes[0].length != ttvImageInfo.getStrides()[0] * ttvImageInfo.getHeight() || planes[1].length != ttvImageInfo.getStrides()[1] * ttvImageInfo.getHeight() / 2) {
                        z = false;
                    }

                    return z;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    static {
        System.loadLibrary("ttvsdk");
    }
}
