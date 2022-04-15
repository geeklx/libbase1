package com.geek.libgoodview.blastgoodview.leonids;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:55
 * @description:类作用
 */
public class AlphaModifier implements ParticleModifier {

    private int mInitialValue;
    private int mFinalValue;
    private long mStartTime;
    private long mEndTime;
    private float mDuration;
    private float mValueIncrement;
    private Interpolator mInterpolator;

    public AlphaModifier(int initialValue, int finalValue, long startMilis, long endMilis, Interpolator interpolator) {
        mInitialValue = initialValue;
        mFinalValue = finalValue;
        mStartTime = startMilis;
        mEndTime = endMilis;
        mDuration = mEndTime - mStartTime;
        mValueIncrement = mFinalValue-mInitialValue;
        mInterpolator = interpolator;
    }

    public AlphaModifier (int initialValue, int finalValue, long startMilis, long endMilis) {
        this(initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
    }

    @Override
    public void apply(Particle particle, long miliseconds) {
        if (miliseconds < mStartTime) {
            particle.mAlpha = mInitialValue;
        }
        else if (miliseconds > mEndTime) {
            particle.mAlpha = mFinalValue;
        }
        else {
            float interpolaterdValue = mInterpolator.getInterpolation((miliseconds- mStartTime)*1f/mDuration);
            int newAlphaValue = (int) (mInitialValue + mValueIncrement*interpolaterdValue);
            particle.mAlpha = newAlphaValue;
        }
    }
}
