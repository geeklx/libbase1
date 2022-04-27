package com.geek.libgoodview.blastgoodview.leonids;

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:49
 * @description:类作用
 */
public interface ParticleModifier {

    /**
     * modifies the specific value of a particle given the current miliseconds
     * @param particle
     * @param miliseconds
     */
    void apply(Particle particle, long miliseconds);

}
