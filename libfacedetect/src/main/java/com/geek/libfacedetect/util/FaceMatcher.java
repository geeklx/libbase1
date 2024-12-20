package com.geek.libfacedetect.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

import com.geek.libfacedetect.db.UserInfo;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸匹配
 * Created by Luke on 2017/8/22.
 */

public class FaceMatcher {

    private static final String TAG = "FaceMatcher";
    private static int counter;
    public final int UNFINISHED = -2;
    public final int width = 500;
    public final int height = 500;
    public final int NO_MATCHER = -1;
    private final int MAX_COUNTER = 145;
    private final double MY_SIMILARITY = 0.5453346280277906;
    private List<String> mPathList;

    public FaceMatcher(List<UserInfo> users) {
        counter = 0;
        mPathList = new ArrayList<>();
        for (UserInfo user : users) {
            mPathList.add(user.getPath());
        }
    }

    @SuppressLint("LongLogTag")
    public int histogramMatch(Bitmap bitmap) {
        if (counter < MAX_COUNTER) {
            Mat testMat = new Mat();
            Utils.bitmapToMat(bitmap, testMat);
            // 转灰度矩阵
            Imgproc.cvtColor(testMat, testMat, Imgproc.COLOR_RGB2GRAY);
            // 把矩阵的类型转换为Cv_32F，因为在c++代码中会判断类型
            testMat.convertTo(testMat, CvType.CV_32F);
            Log.e("ssssssssssss-histogramMatchtestMat", testMat.width() + "," + testMat.height());
            for (int i = 0; i < mPathList.size(); i++) {
                String path = mPathList.get(i);
                Mat mat = Imgcodecs.imread(path);
//                Mat mat = Highgui.imread(path);
                Log.e("ssssssssssss-histogramMatchmat", mat.width() + "," + mat.height());
//                Imgproc.resize(mat, mat, new Size(mat.width(), mat.height()));
                Imgproc.resize(mat, mat, new Size(width, height));
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
                mat.convertTo(mat, CvType.CV_32F);
                // 直方图比较
                double similarity = Imgproc.compareHist(mat, testMat,
                        Imgproc.CV_COMP_CORREL);
                Log.e(TAG, "histogramMatch: " + similarity);
                if (similarity >= MY_SIMILARITY) {
                    Log.e(TAG, "histogramMatch: " + similarity + ", " + i);
                    return i;
                }
                if (similarity < MY_SIMILARITY && i == mPathList.size() - 1) {
                    Log.e(TAG, "histogramMatch: " + counter);
                    counter++;
                }
            }
            if (mPathList.size() == 0) {
                counter++;
            }
            return UNFINISHED;
        } else {
            Log.e(TAG, "histogramMatch: 匹配结束");
            return NO_MATCHER;
        }
    }

}
