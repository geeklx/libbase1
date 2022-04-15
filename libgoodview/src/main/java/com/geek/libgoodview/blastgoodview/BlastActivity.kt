package com.geek.libgoodview.blastgoodview

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.geek.libgoodview.R

class BlastActivity : AppCompatActivity() {
    var isping = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blast)

        val iv = findViewById<ImageView>(R.id.ivc)
        val jetEffect = LikeJetEffect()
        jetEffect.attach(
            findViewById<RelativeLayout>(R.id.rl_likec),
            object : LikeJetEffect.OnLikeCLickListener {
                override fun onClick() {
                    if (isping == 0) {
                        jetEffect.setLikeState(2)
                    } else {
                        jetEffect.setLikeState(3)
                    }
                    val animatorSet = AnimatorInflater.loadAnimator(
                        this@BlastActivity,
                        R.animator.btn_like_click
                    ) as AnimatorSet
                    animatorSet.setTarget(iv)
                    animatorSet.start()
                    animatorSet.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            super.onAnimationStart(animation)
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            try {
//                            更新点赞图标状态及请求点赞接口
//                            if (isping == 0) {
//                                imgLike.setImageDrawable(
//                                    TintUtil.getTintDrawable(
//                                        ContextCompat.getDrawable(
//                                            mContext,
//                                            R.mipmap.ic_forum_like_selected
//                                        ),
//                                        ConfigHelper.getColorMainInt(mContext)
//                                    )
//                                )
//                            } else {
//                                imvLike.setImageResource(R.mipmap.ic_forum_like_unselected)
//                            }
//                            requestPingForum(null)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
                }
            })

//        jetEffect.attach(
//            findViewById<RelativeLayout>(R.id.rl_likec),
//            object : LikeJetEffect.OnLikeCLickListener {
//                override fun onClick() {
//                    if (isping == 0) {
//                        jetEffect.setLikeState(2)
//                    } else {
//                        jetEffect.setLikeState(3)
//                    }
//                    val animatorSet = AnimatorInflater.loadAnimator(
//                        this@BlastActivity,
//                        R.animator.btn_like_click
//                    ) as AnimatorSet
//                    animatorSet.setTarget(findViewById<ImageView>(R.id.ivc))
//                    animatorSet.start()
//                    animatorSet.addListener(object : AnimatorListenerAdapter() {
//                        override fun onAnimationStart(animation: Animator) {
//                            super.onAnimationStart(animation)
//                        }
//
//                        override fun onAnimationEnd(animation: Animator) {
//                            super.onAnimationEnd(animation)
//                            try {
////                            更新点赞图标状态及请求点赞接口
////                            if (isping == 0) {
////                                imgLike.setImageDrawable(
////                                    TintUtil.getTintDrawable(
////                                        ContextCompat.getDrawable(
////                                            mContext,
////                                            R.mipmap.ic_forum_like_selected
////                                        ),
////                                        ConfigHelper.getColorMainInt(mContext)
////                                    )
////                                )
////                            } else {
////                                imvLike.setImageResource(R.mipmap.ic_forum_like_unselected)
////                            }
////                            requestPingForum(null)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                        }
//                    })
//                }
//            })

    }
}