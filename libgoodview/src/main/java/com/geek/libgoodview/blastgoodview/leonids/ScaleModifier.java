package com.geek.libgoodview.blastgoodview.leonids;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:57
 * @description:类作用
 */
public class ScaleModifier implements ParticleModifier {

    private float mInitialValue;
    private float mFinalValue;
    private long mEndTime;
    private long mStartTime;
    private long mDuration;
    private float mValueIncrement;
    private Interpolator mInterpolator;

    public ScaleModifier (float initialValue, float finalValue, long startMilis, long endMilis, Interpolator interpolator) {
        mInitialValue = initialValue;
        mFinalValue = finalValue;
        mStartTime = startMilis;
        mEndTime = endMilis;
        mDuration = mEndTime - mStartTime;
        mValueIncrement = mFinalValue-mInitialValue;
        mInterpolator = interpolator;
    }

    public ScaleModifier (float initialValue, float finalValue, long startMilis, long endMilis) {
        this (initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
    }

    @Override
    public void apply(Particle particle, long miliseconds) {
        if (miliseconds < mStartTime) {
            particle.mScale = mInitialValue;
        }
        else if (miliseconds > mEndTime) {
            particle.mScale = mFinalValue;
        }
        else {
            float interpolaterdValue = mInterpolator.getInterpolation((miliseconds- mStartTime)*1f/mDuration);
            float newScale = mInitialValue + mValueIncrement*interpolaterdValue;
            particle.mScale = newScale;
        }
    }

}
