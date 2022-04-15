package com.geek.libgoodview.blastgoodview.leonids;

import java.util.Random;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:52
 * @description:类作用
 */
public class SpeeddByComponentsInitializer implements ParticleInitializer {

    private float mMinSpeedX;
    private float mMaxSpeedX;
    private float mMinSpeedY;
    private float mMaxSpeedY;

    public SpeeddByComponentsInitializer(float speedMinX, float speedMaxX, float speedMinY, float speedMaxY) {
        mMinSpeedX = speedMinX;
        mMaxSpeedX = speedMaxX;
        mMinSpeedY = speedMinY;
        mMaxSpeedY = speedMaxY;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.mSpeedX = r.nextFloat()*(mMaxSpeedX-mMinSpeedX)+mMinSpeedX;
        p.mSpeedY = r.nextFloat()*(mMaxSpeedY-mMinSpeedY)+mMinSpeedY;
    }

}
