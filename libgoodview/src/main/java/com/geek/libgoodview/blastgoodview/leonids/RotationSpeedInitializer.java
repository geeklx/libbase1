package com.geek.libgoodview.blastgoodview.leonids;

import java.util.Random;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:54
 * @description:类作用
 */
public class RotationSpeedInitializer implements ParticleInitializer {

    private float mMinRotationSpeed;
    private float mMaxRotationSpeed;

    public RotationSpeedInitializer(float minRotationSpeed,	float maxRotationSpeed) {
        mMinRotationSpeed = minRotationSpeed;
        mMaxRotationSpeed = maxRotationSpeed;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        float rotationSpeed = r.nextFloat()*(mMaxRotationSpeed-mMinRotationSpeed) + mMinRotationSpeed;
        p.mRotationSpeed = rotationSpeed;
    }

}
