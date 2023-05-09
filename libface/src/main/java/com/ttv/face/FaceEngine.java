//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;
import com.ttv.face.FaceSDK;
import com.ttv.face.enums.CompareModel;
import com.ttv.face.enums.DetectFaceOrientPriority;
import com.ttv.face.enums.DetectMode;
import com.ttv.face.enums.ExtractType;
import com.ttv.face.imageutil.TTVImageFormat;
import com.ttv.face.imageutil.TTVImageUtil;

public class FaceEngine {
    private static FaceEngine instance;
    private FaceSDK fvSDK;
    private FaceSDK fiSDK;
    private FaceSDK ffSDK;
    private Context mContext;

    private FaceEngine(Context context) {
        this.mContext = context;
    }

    public static synchronized FaceEngine getInstance(Context context) {
        if (instance == null) {
            instance = new FaceEngine(context);
        }

        return instance;
    }

    public String getCurrentHWID() {
        FaceSDK faceSDK = new FaceSDK(this.mContext);
        return faceSDK.getCurrentHWID();
    }

    public int setActivation(String license) {
        FaceSDK faceSDK = new FaceSDK(this.mContext);
        return faceSDK.setActivation(license);
    }

    public void init(int maxFaceNum) {
        if (instance != null) {
            this.fvSDK = new FaceSDK(this.mContext);
            this.fvSDK.init(this.mContext, DetectMode.TTV_DETECT_MODE_VIDEO, DetectFaceOrientPriority.TTV_OP_ALL_OUT, maxFaceNum, 1);
            this.fiSDK = new FaceSDK(this.mContext);
            this.fiSDK.init(this.mContext, DetectMode.TTV_DETECT_MODE_IMAGE, DetectFaceOrientPriority.TTV_OP_ALL_OUT, maxFaceNum, 4249);
            this.ffSDK = new FaceSDK(this.mContext);
            this.ffSDK.init(this.mContext, DetectMode.TTV_DETECT_MODE_IMAGE, DetectFaceOrientPriority.TTV_OP_ALL_OUT, maxFaceNum, 4);
        }
    }

    public void unInit() {
        if (instance != null) {
            if (this.fvSDK != null) {
                this.fvSDK.unInit();
            }

            if (this.fiSDK != null) {
                this.fiSDK.unInit();
            }

            if (this.ffSDK != null) {
                this.ffSDK.unInit();
            }

        }
    }

    public List<FaceResult> detectFace(byte[] nv21, int width, int height) {
        List<FaceResult> faceResults = new ArrayList();
        if (instance == null) {
            return faceResults;
        } else {
            try {
                List<FaceInfo> faceInfoList = new ArrayList();
                synchronized(this.fiSDK) {
                    int detectCode = this.fiSDK.detectFaces(nv21, width, height, 2050, faceInfoList);
                    if (detectCode != 0 || faceInfoList.isEmpty()) {
                        return faceResults;
                    }

                    for(int i = 0; i < faceInfoList.size(); ++i) {
                        FaceResult faceResult = new FaceResult();
                        faceResult.faceId = ((FaceInfo)faceInfoList.get(i)).getFaceId();
                        faceResult.rect = ((FaceInfo)faceInfoList.get(i)).getRect();
                        faceResult.faceData = ((FaceInfo)faceInfoList.get(i)).getFaceData();
                        faceResult.orient = ((FaceInfo)faceInfoList.get(i)).getOrient();
                        faceResult.pitch = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getPitch();
                        faceResult.roll = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getRoll();
                        faceResult.yaw = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getYaw();
                        faceResults.add(faceResult);
                    }
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return faceResults;
        }
    }

    public List<FaceResult> detectFaceWithVideo(byte[] nv21, int width, int height) {
        List<FaceResult> faceResults = new ArrayList();
        if (instance == null) {
            return faceResults;
        } else {
            try {
                List<FaceInfo> faceInfoList = new ArrayList();
                synchronized(this.fvSDK) {
                    int detectCode = this.fvSDK.detectFaces(nv21, width, height, 2050, faceInfoList);
                    if (detectCode != 0 || faceInfoList.isEmpty()) {
                        return faceResults;
                    }

                    for(int i = 0; i < faceInfoList.size(); ++i) {
                        FaceResult faceResult = new FaceResult();
                        faceResult.faceId = ((FaceInfo)faceInfoList.get(i)).getFaceId();
                        faceResult.rect = ((FaceInfo)faceInfoList.get(i)).getRect();
                        faceResult.faceData = ((FaceInfo)faceInfoList.get(i)).getFaceData();
                        faceResult.orient = ((FaceInfo)faceInfoList.get(i)).getOrient();
                        faceResult.pitch = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getPitch();
                        faceResult.roll = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getRoll();
                        faceResult.yaw = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getYaw();
                        faceResults.add(faceResult);
                    }
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return faceResults;
        }
    }

    public List<FaceResult> detectFace(Bitmap bitmap) {
        List<FaceResult> faceResults = new ArrayList();
        if (instance == null) {
            return faceResults;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return faceResults;
                }

                List<FaceInfo> faceInfoList = new ArrayList();
                synchronized(this.fiSDK) {
                    int detectCode = this.fiSDK.detectFaces(bgr24, width, height, 513, faceInfoList);
                    if (detectCode != 0 || faceInfoList.isEmpty()) {
                        return faceResults;
                    }

                    for(int i = 0; i < faceInfoList.size(); ++i) {
                        FaceResult faceResult = new FaceResult();
                        faceResult.faceId = ((FaceInfo)faceInfoList.get(i)).getFaceId();
                        faceResult.rect = ((FaceInfo)faceInfoList.get(i)).getRect();
                        faceResult.faceData = ((FaceInfo)faceInfoList.get(i)).getFaceData();
                        faceResult.orient = ((FaceInfo)faceInfoList.get(i)).getOrient();
                        faceResult.pitch = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getPitch();
                        faceResult.roll = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getRoll();
                        faceResult.yaw = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getYaw();
                        faceResults.add(faceResult);
                    }
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return faceResults;
        }
    }

    public List<FaceResult> detectFaceWith(Bitmap bitmap) {
        List<FaceResult> faceResults = new ArrayList();
        if (instance == null) {
            return faceResults;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return faceResults;
                }

                List<FaceInfo> faceInfoList = new ArrayList();
                synchronized(this.fvSDK) {
                    int detectCode = this.fvSDK.detectFaces(bgr24, width, height, 513, faceInfoList);
                    if (detectCode != 0 || faceInfoList.isEmpty()) {
                        return faceResults;
                    }

                    for(int i = 0; i < faceInfoList.size(); ++i) {
                        FaceResult faceResult = new FaceResult();
                        faceResult.faceId = ((FaceInfo)faceInfoList.get(i)).getFaceId();
                        faceResult.rect = ((FaceInfo)faceInfoList.get(i)).getRect();
                        faceResult.faceData = ((FaceInfo)faceInfoList.get(i)).getFaceData();
                        faceResult.orient = ((FaceInfo)faceInfoList.get(i)).getOrient();
                        faceResult.pitch = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getPitch();
                        faceResult.roll = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getRoll();
                        faceResult.yaw = ((FaceInfo)faceInfoList.get(i)).getFace3DAngle().getYaw();
                        faceResults.add(faceResult);
                    }
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return faceResults;
        }
    }

    public int extractFeature(Bitmap bitmap, boolean isRegister, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return -1;
                }

                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    FaceFeature faceFeature = new FaceFeature();
                    synchronized(this.ffSDK) {
                        this.ffSDK.extractFaceFeature(bgr24, width, height, 513, faceInfo, isRegister ? ExtractType.REGISTER : ExtractType.RECOGNIZE, isRegister ? 0 : ((FaceResult)faceResults.get(i)).mask, faceFeature);
                    }

                    ((FaceResult)faceResults.get(i)).feature = faceFeature.getFeatureData();
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return 0;
        }
    }

    public int extractFeature(byte[] nv21, int width, int height, boolean isRegister, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    FaceFeature faceFeature = new FaceFeature();
                    synchronized(this.ffSDK) {
                        this.ffSDK.extractFaceFeature(nv21, width, height, 2050, faceInfo, isRegister ? ExtractType.REGISTER : ExtractType.RECOGNIZE, isRegister ? 0 : ((FaceResult)faceResults.get(i)).mask, faceFeature);
                    }

                    ((FaceResult)faceResults.get(i)).feature = faceFeature.getFeatureData();
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return 0;
        }
    }

    public float compareFeature(byte[] feat1, byte[] feat2) {
        if (instance == null) {
            return 0.0F;
        } else if (feat1 != null && feat2 != null) {
            FaceFeature feature1 = new FaceFeature();
            feature1.setFeatureData(feat1);
            FaceFeature feature2 = new FaceFeature();
            feature2.setFeatureData(feat2);
            FaceSimilar faceSimilar = new FaceSimilar();
            this.ffSDK.compareFaceFeature(feature1, feature2, faceSimilar);
            return faceSimilar.getScore();
        } else {
            return 0.0F;
        }
    }

    public SearchResult searchFaceFeature(FaceFeature feature) throws IllegalArgumentException {
        return this.ffSDK.searchFaceFeature(feature);
    }

    public SearchResult searchFaceFeature(FaceFeature feature, CompareModel compareModel) throws IllegalArgumentException {
        return this.ffSDK.searchFaceFeature(feature, compareModel);
    }

    public int registerFaceFeature(FaceFeatureInfo faceFeatureInfo) {
        return this.ffSDK.registerFaceFeature(faceFeatureInfo);
    }

    public int registerFaceFeature(List<FaceFeatureInfo> faceFeatureInfoList) {
        return this.ffSDK.registerFaceFeature(faceFeatureInfoList);
    }

    public int removeFaceFeature(int searchId) {
        return this.ffSDK.removeFaceFeature(searchId);
    }

    public int updateFaceFeature(FaceFeatureInfo faceFeatureInfo) {
        return this.ffSDK.updateFaceFeature(faceFeatureInfo);
    }

    public FaceFeatureInfo getFaceFeature(int searchId) throws IllegalArgumentException {
        return this.ffSDK.getFaceFeature(searchId);
    }

    public int getFaceCount() throws IllegalArgumentException {
        return this.ffSDK.getFaceCount();
    }

    public int livenessProcess(byte[] nv21, int width, int height, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<LivenessInfo> livenessInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(nv21, width, height, 2050, faceInfos, 128);
                        this.fiSDK.getLiveness(livenessInfos);
                    }

                    if (livenessInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).liveness = ((LivenessInfo)livenessInfos.get(0)).getLiveness();
                    }
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return 0;
        }
    }

    public int livenessProcess(Bitmap bitmap, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return -1;
                }

                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<LivenessInfo> livenessInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(bgr24, width, height, 513, faceInfos, 128);
                        this.fiSDK.getLiveness(livenessInfos);
                    }

                    if (livenessInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).liveness = ((LivenessInfo)livenessInfos.get(0)).getLiveness();
                    }
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return 0;
        }
    }

    public int maskProcess(byte[] nv21, int width, int height, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<MaskInfo> maskInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(nv21, width, height, 2050, faceInfos, 4096);
                        this.fiSDK.getMask(maskInfos);
                    }

                    if (maskInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).mask = ((MaskInfo)maskInfos.get(0)).getMask();
                    }
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            return 0;
        }
    }

    public int maskProcess(Bitmap bitmap, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return -1;
                }

                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<MaskInfo> maskInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(bgr24, width, height, 513, faceInfos, 4096);
                        this.fiSDK.getMask(maskInfos);
                    }

                    if (maskInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).mask = ((MaskInfo)maskInfos.get(0)).getMask();
                    }
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return 0;
        }
    }

    public int faceAttrProcess(byte[] nv21, int width, int height, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<LivenessInfo> livenessInfos = new ArrayList();
                    List<GenderInfo> genderInfos = new ArrayList();
                    List<AgeInfo> ageInfos = new ArrayList();
                    List<MaskInfo> maskInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(nv21, width, height, 2050, faceInfos, 4248);
                        this.fiSDK.getLiveness(livenessInfos);
                        this.fiSDK.getGender(genderInfos);
                        this.fiSDK.getAge(ageInfos);
                        this.fiSDK.getMask(maskInfos);
                    }

                    if (livenessInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).liveness = ((LivenessInfo)livenessInfos.get(0)).getLiveness();
                    }

                    if (genderInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).gender = ((GenderInfo)genderInfos.get(0)).getGender();
                    }

                    if (ageInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).age = ((AgeInfo)ageInfos.get(0)).getAge();
                    }

                    if (maskInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).mask = ((MaskInfo)maskInfos.get(0)).getMask();
                    }
                }
            } catch (Exception var15) {
                var15.printStackTrace();
            }

            return 0;
        }
    }

    public int faceAttrProcess(Bitmap bitmap, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return -1;
                }

                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<LivenessInfo> livenessInfos = new ArrayList();
                    List<GenderInfo> genderInfos = new ArrayList();
                    List<AgeInfo> ageInfos = new ArrayList();
                    List<MaskInfo> maskInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(bgr24, width, height, 513, faceInfos, 4248);
                        this.fiSDK.getLiveness(livenessInfos);
                        this.fiSDK.getGender(genderInfos);
                        this.fiSDK.getAge(ageInfos);
                        this.fiSDK.getMask(maskInfos);
                    }

                    if (livenessInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).liveness = ((LivenessInfo)livenessInfos.get(0)).getLiveness();
                    }

                    if (genderInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).gender = ((GenderInfo)genderInfos.get(0)).getGender();
                    }

                    if (ageInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).age = ((AgeInfo)ageInfos.get(0)).getAge();
                    }

                    if (maskInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).mask = ((MaskInfo)maskInfos.get(0)).getMask();
                    }
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }

            return 0;
        }
    }

    public int faceAllProcess(byte[] nv21, int width, int height, boolean isRegister, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<LivenessInfo> livenessInfos = new ArrayList();
                    List<GenderInfo> genderInfos = new ArrayList();
                    List<AgeInfo> ageInfos = new ArrayList();
                    List<MaskInfo> maskInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(nv21, width, height, 2050, faceInfos, 4248);
                        this.fiSDK.getLiveness(livenessInfos);
                        this.fiSDK.getGender(genderInfos);
                        this.fiSDK.getAge(ageInfos);
                        this.fiSDK.getMask(maskInfos);
                    }

                    if (livenessInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).liveness = ((LivenessInfo)livenessInfos.get(0)).getLiveness();
                    }

                    if (genderInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).gender = ((GenderInfo)genderInfos.get(0)).getGender();
                    }

                    if (ageInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).age = ((AgeInfo)ageInfos.get(0)).getAge();
                    }

                    if (maskInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).mask = ((MaskInfo)maskInfos.get(0)).getMask();
                    }

                    FaceFeature faceFeature = new FaceFeature();
                    synchronized(this.ffSDK) {
                        this.ffSDK.extractFaceFeature(nv21, width, height, 2050, faceInfo, isRegister ? ExtractType.REGISTER : ExtractType.RECOGNIZE, isRegister ? 0 : ((FaceResult)faceResults.get(i)).mask, faceFeature);
                    }

                    ((FaceResult)faceResults.get(i)).feature = faceFeature.getFeatureData();
                }
            } catch (Exception var18) {
                var18.printStackTrace();
            }

            return 0;
        }
    }

    public int faceAllProcess(Bitmap bitmap, boolean isRegister, List<FaceResult> faceResults) {
        if (instance == null) {
            return -1;
        } else {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                byte[] bgr24 = TTVImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), TTVImageFormat.BGR24);
                int transformCode = TTVImageUtil.bitmapToImageData(bitmap, bgr24, TTVImageFormat.BGR24);
                if (transformCode != 0) {
                    return -1;
                }

                for(int i = 0; i < faceResults.size(); ++i) {
                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setRect(((FaceResult)faceResults.get(i)).rect);
                    faceInfo.setFaceData(((FaceResult)faceResults.get(i)).faceData);
                    faceInfo.setOrient(((FaceResult)faceResults.get(i)).orient);
                    faceInfo.setFaceId(((FaceResult)faceResults.get(i)).faceId);
                    List<FaceInfo> faceInfos = new ArrayList();
                    faceInfos.add(faceInfo);
                    List<LivenessInfo> livenessInfos = new ArrayList();
                    List<GenderInfo> genderInfos = new ArrayList();
                    List<AgeInfo> ageInfos = new ArrayList();
                    List<MaskInfo> maskInfos = new ArrayList();
                    synchronized(this.fiSDK) {
                        this.fiSDK.process(bgr24, width, height, 513, faceInfos, 4248);
                        this.fiSDK.getLiveness(livenessInfos);
                        this.fiSDK.getGender(genderInfos);
                        this.fiSDK.getAge(ageInfos);
                        this.fiSDK.getMask(maskInfos);
                    }

                    if (livenessInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).liveness = ((LivenessInfo)livenessInfos.get(0)).getLiveness();
                    }

                    if (genderInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).gender = ((GenderInfo)genderInfos.get(0)).getGender();
                    }

                    if (ageInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).age = ((AgeInfo)ageInfos.get(0)).getAge();
                    }

                    if (maskInfos.size() > 0) {
                        ((FaceResult)faceResults.get(i)).mask = ((MaskInfo)maskInfos.get(0)).getMask();
                    }

                    FaceFeature faceFeature = new FaceFeature();
                    synchronized(this.ffSDK) {
                        this.ffSDK.extractFaceFeature(bgr24, width, height, 513, faceInfo, isRegister ? ExtractType.REGISTER : ExtractType.RECOGNIZE, isRegister ? 0 : ((FaceResult)faceResults.get(i)).mask, faceFeature);
                    }

                    ((FaceResult)faceResults.get(i)).feature = faceFeature.getFeatureData();
                }
            } catch (Exception var20) {
                var20.printStackTrace();
            }

            return 0;
        }
    }
}
