package com.geek.libgoodview.blastgoodview.leonids;

import java.util.Random;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:53
 * @description:类作用
 */
public class ScaleInitializer implements ParticleInitializer {

    private float mMaxScale;
    private float mMinScale;

    public ScaleInitializer(float minScale, float maxScale) {
        mMinScale = minScale;
        mMaxScale = maxScale;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        float scale = r.nextFloat()*(mMaxScale-mMinScale) + mMinScale;
        p.mScale = scale;
    }
}
