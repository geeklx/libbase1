package com.geek.libgoodview.blastgoodview.leonids;

import java.util.Random;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:53
 * @description:类作用
 */
public class RotationInitializer implements ParticleInitializer {

    private int mMinAngle;
    private int mMaxAngle;

    public RotationInitializer(int minAngle, int maxAngle) {
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.mInitialRotation = (mMinAngle == mMaxAngle) ? mMinAngle : r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
    }

}
