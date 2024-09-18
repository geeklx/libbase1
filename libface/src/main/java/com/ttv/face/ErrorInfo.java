//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ttv.face;

public class ErrorInfo {
    public static final int MOK = 0;
    public static final int MERR_BASIC_BASE = 0;
    public static final int MERR_UNKNOWN = 1;
    public static final int MERR_INVALID_PARAM = 2;
    public static final int MERR_UNSUPPORTED = 3;
    public static final int MERR_NO_MEMORY = 4;
    public static final int MERR_BAD_STATE = 5;
    public static final int MERR_USER_CANCEL = 6;
    public static final int MERR_EXPIRED = 7;
    public static final int MERR_USER_PAUSE = 8;
    public static final int MERR_BUFFER_OVERFLOW = 9;
    public static final int MERR_BUFFER_UNDERFLOW = 10;
    public static final int MERR_NO_DISKSPACE = 11;
    public static final int MERR_COMPONENT_NOT_EXIST = 12;
    public static final int MERR_GLOBAL_DATA_NOT_EXIST = 13;
    public static final int MERRP_IMGCODEC = 14;
    public static final int MERR_FILE_GENERAL = 15;
    public static final int MERR_FSDK_BASE = 28672;
    public static final int MERR_FSDK_INVALID_APP_ID = 28673;
    public static final int MERR_FSDK_INVALID_SDK_ID = 28674;
    public static final int MERR_FSDK_INVALID_ID_PAIR = 28675;
    public static final int MERR_FSDK_MISMATCH_ID_AND_SDK = 28676;
    public static final int MERR_FSDK_SYSTEM_VERSION_UNSUPPORTED = 28677;
    public static final int MERR_FSDK_FR_ERROR_BASE = 73728;
    public static final int MERR_FSDK_FR_INVALID_MEMORY_INFO = 73729;
    public static final int MERR_FSDK_FR_INVALID_IMAGE_INFO = 73730;
    public static final int MERR_FSDK_FR_INVALID_FACE_INFO = 73731;
    public static final int MERR_FSDK_FR_NO_GPU_AVAILABLE = 73732;
    public static final int MERR_FSDK_FR_MISMATCHED_FEATURE_LEVEL = 73733;
    public static final int MERR_FSDK_FACEFEATURE_ERROR_BASE = 81920;
    public static final int MERR_FSDK_FACEFEATURE_UNKNOWN = 81921;
    public static final int MERR_FSDK_FACEFEATURE_MEMORY = 81922;
    public static final int MERR_FSDK_FACEFEATURE_INVALID_FORMAT = 81923;
    public static final int MERR_FSDK_FACEFEATURE_INVALID_PARAM = 81924;
    public static final int MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL = 81925;
    public static final int MERR_FSDK_FACEFEATURE_EXPIRED = 81926;
    public static final int MERR_FSDK_FACEFEATURE_MISSFACE = 81927;
    public static final int MERR_FSDK_FACEFEATURE_NO_FACE = 81928;
    public static final int MERR_FSDK_FACEFEATURE_FACEDATA = 81929;
    public static final int MERR_TTV_EX_BASE = 86016;
    public static final int MERR_TTV_EX_FEATURE_UNSUPPORTED_ON_INIT = 86017;
    public static final int MERR_TTV_EX_FEATURE_UNINITED = 86018;
    public static final int MERR_TTV_EX_FEATURE_UNPROCESSED = 86019;
    public static final int MERR_TTV_EX_FEATURE_UNSUPPORTED_ON_PROCESS = 86020;
    public static final int MERR_TTV_EX_INVALID_IMAGE_INFO = 86021;
    public static final int MERR_TTV_EX_INVALID_FACE_INFO = 86022;
    public static final int MERR_TTV_BASE = 90112;
    public static final int MERR_TTV_ACTIVATION_FAIL = 90113;
    public static final int MERR_TTV_ALREADY_ACTIVATED = 90114;
    public static final int MERR_TTV_NOT_ACTIVATED = 90115;
    public static final int MERR_TTV_SCALE_NOT_SUPPORT = 90116;
    public static final int MERR_TTV_ACTIVEFILE_SDKTYPE_MISMATCH = 90117;
    public static final int MERR_TTV_DEVICE_MISMATCH = 90118;
    public static final int MERR_TTV_UNIQUE_IDENTIFIER_ILLEGAL = 90119;
    public static final int MERR_TTV_PARAM_NULL = 90120;
    public static final int MERR_TTV_VERSION_NOT_SUPPORT = 90122;
    public static final int MERR_TTV_SIGN_ERROR = 90123;
    public static final int MERR_TTV_DATABASE_ERROR = 90124;
    public static final int MERR_TTV_UNIQUE_CHECKOUT_FAIL = 90125;
    public static final int MERR_TTV_COLOR_SPACE_NOT_SUPPORT = 90126;
    public static final int MERR_TTV_IMAGE_WIDTH_HEIGHT_NOT_SUPPORT = 90127;
    public static final int MERR_TTV_READ_PHONE_STATE_DENIED = 90128;
    public static final int MERR_TTV_ACTIVEKEY_BASE = 98304;
    public static final int MERR_TTV_ACTIVEKEY_COULDNT_CONNECT_SERVER = 98305;
    public static final int MERR_TTV_ACTIVEKEY_SERVER_SYSTEM_ERROR = 98306;
    public static final int MERR_TTV_ACTIVEKEY_POST_PARM_ERROR = 98307;
    public static final int MERR_TTV_ACTIVEKEY_PARM_MISMATCH = 98308;
    public static final int MERR_TTV_ACTIVEKEY_ACTIVEKEY_ACTIVATED = 98309;
    public static final int MERR_TTV_ACTIVEKEY_ACTIVEKEY_FORMAT_ERROR = 98310;
    public static final int MERR_TTV_ACTIVEKEY_APPID_PARM_MISMATCH = 98311;
    public static final int MERR_TTV_ACTIVEKEY_SDK_FILE_MISMATCH = 98312;
    public static final int MERR_TTV_ACTIVEKEY_EXPIRED = 98313;
    public static final int MERR_TTV_ACTIVEKEY_HWID_ERROR = 98314;
    public static final int MERR_TTV_ACTIVEKEY_PERMISSION = 98315;
    int code = -1;

    ErrorInfo() {
    }

    public int getCode() {
        return this.code;
    }
}
