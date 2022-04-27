package com.geek.libgoodview.blastgoodview

import android.app.Activity
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.geek.libgoodview.R
import com.geek.libgoodview.blastgoodview.leonids.ParticleSystem
import com.geek.libgoodview.blastgoodview.leonids.ScaleModifier
import com.geek.libgoodview.blastgoodview.utils.ApplicationUtils
import com.geek.libgoodview.blastgoodview.utils.DeviceUtils
import com.geek.libgoodview.blastgoodview.utils.ImageUtils
import java.util.*
import kotlin.math.min

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:35
 * @description:类作用
 */
class LikeJetEffect {

    companion object {
        private const val JET_INTERVAL = 120L
        private const val JET_DURATION = 700L
        private const val TARGET_SIZE_MIN_DP = 26
        private const val TARGET_SIZE_MAX_DP = 33
    }


    private var timer: Timer? = null


    private var downTime: Long = 0L

    private var likeCount = 0

    private var likeNumView: ViewGroup? = null

    private var handler = Handler()

    private var hideLikeNumRunnable = Runnable { likeNumView?.visibility = View.GONE }

    private var removeContinueState = Runnable { isContinueState = false }

    /**
     * 0 未点赞状态
     * 1 已点赞状态
     * 2 正在点赞状态
     * 3 正在取消点赞状态
     */
    private var likeState = 0


    /**
     * 是否进入了延续状态
     */
    private var isContinueState = false

    private var mLikeOnLikeClickListener: OnLikeCLickListener? = null

    val iconInts = intArrayOf(
        R.mipmap.like_emoji_1,
        R.mipmap.like_emoji_2,
        R.mipmap.like_emoji_3,
        R.mipmap.like_emoji_4,
        R.mipmap.like_emoji_5,
        R.mipmap.like_emoji_6,
        R.mipmap.like_emoji_7,
        R.mipmap.like_emoji_8
    )

    fun attach(view: View, listener: OnLikeCLickListener) {
        this.mLikeOnLikeClickListener = listener
        initLikeNumLayout(view)

        likeCount = 0

        view.setOnTouchListener { v, event ->
//            if (!UserDataUtils.getInstance().isLogin) {
//                mLikeOnLikeClickListener?.onClick()
//                return@setOnTouchListener true
//            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    timer?.cancel()
                    downTime = System.currentTimeMillis()
                    timer = Timer()
                    var delay = 250L
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            view.post {
                                updateLikeNum()
                                startJetAnimator(view)
                            }
                        }
                    }, delay, JET_INTERVAL)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    timer?.cancel()
                    handler.removeCallbacks(hideLikeNumRunnable)
                    handler.postDelayed(hideLikeNumRunnable, 800L)
                    var clickDuration = System.currentTimeMillis() - downTime
                    //小于200走外部的click事件，只做一次发射表情，不显示数量
                    if (clickDuration < 200) {
                        //触发快速点击
                        when (likeState) {
                            0 -> {
                                //当前状态未点赞
                                isContinueState = false
                                updateLikeNum()
                                startJetAnimator(view)
                                mLikeOnLikeClickListener?.onClick()
                            }
                            1 -> {
                                //当前状态已点赞
                                handler.removeCallbacks(removeContinueState)
                                if (isContinueState) {
                                    handler.postDelayed(removeContinueState, 400)
                                    startJetAnimator(view)
                                    updateLikeNum()
                                } else {
                                    likeCount = 0
                                    mLikeOnLikeClickListener?.onClick()
                                }
                            }
                            2 -> {
                                //如果是正在点赞中，可以触发连赞效果
                                startJetAnimator(view)
                                updateLikeNum()
                                isContinueState = true
                            }
                            3 -> {
                                isContinueState = false
                                //如果是取消点赞中，则不触发连赞效果
                                startJetAnimator(view)
                                updateLikeNum()
                            }
                        }
                    } else {
                        isContinueState = false
                        //触发长按
                        when (likeState) {
                            0 -> {
                                startJetAnimator(view)
                                updateLikeNum()
                                mLikeOnLikeClickListener?.onClick()
                            }
                            1 -> {
                                startJetAnimator(view)
                                updateLikeNum()
                            }
                            2, 3 -> {
                                startJetAnimator(view)
                                updateLikeNum()
                            }
                        }
                        if (likeState == 0) {
                            mLikeOnLikeClickListener?.onClick()
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    /**
     * 设置是否已经点赞，是为了支持长按点赞，不支持长按取消赞
     */
    fun setLikeState(like: Int) {
        this.likeState = like
    }

    private fun initLikeNumLayout(view: View) {
        val activity: Activity = view.context as Activity
        val parentView = activity.findViewById<ViewGroup>(android.R.id.content)
        likeNumView =
            LayoutInflater.from(activity).inflate(
                R.layout.layout_like_counter,
                parentView,
                false
            ) as ViewGroup
        parentView.addView(likeNumView)

        view.post {
            var location = IntArray(2)
            view.getLocationInWindow(location)
            likeNumView?.translationX = location[0].toFloat() - DeviceUtils.dp2px(view.context, 40f)
//            likeNumView?.translationY = location[1].toFloat() - DeviceUtils.dp2px(view.context, 60f)
            //控制数字部分距离点赞图标的距离
            likeNumView?.translationY =
                location[1].toFloat() - DeviceUtils.dp2px(view.context, 140f)
        }
    }

    private fun updateLikeNum() {
        likeNumView?.visibility = View.VISIBLE
        likeCount++
        var imvCount = likeNumView?.findViewById<LikeNumber>(R.id.imv_count)
        var imvTxt = likeNumView?.findViewById<ImageView>(R.id.imv_txt)

        imvCount?.setNumber(likeCount)
        when (likeCount) {
            in 1..19 -> imvTxt?.setImageResource(R.mipmap.icon_like_text_1)
            in 20..39 -> imvTxt?.setImageResource(R.mipmap.icon_like_text_2)
            else -> imvTxt?.setImageResource(R.mipmap.icon_like_text_3)
        }
    }

    private fun startJetAnimator(view: View) {
        val ps = ParticleSystem(
            view.context as Activity?,
            5,
            iconInts,
            JET_DURATION
        )
//        ps.setScaleRange(1.8f, 1.8f)
        ps.setSpeedModuleAndAngleRange(0.6f, 0.8f, 180, 300)
//        ps.setAcceleration(0.0001f, 90)
        ps.setAccelerationModuleAndAndAngleRange(0.0002f, 0.0006f, 90, 90)
        ps.setRotationSpeedRange(90f, 180f)
        ps.setFadeOut(100, AccelerateInterpolator())
        ps.addModifier(
            ScaleModifier(
                getMinScale(),
                getMaxScale(),
                100,
                JET_DURATION - 200,
                AccelerateInterpolator()
            )
        )

//        ps.setSpeedByComponentsRange(-0.000003f,-0.0000003f,-0.0000003f,-0.0000003f)
        ps.oneShot(view, 5, DecelerateInterpolator(2.0f))
    }

    private fun getMinScale(): Float {
        val size = ImageUtils.getImageSize(ApplicationUtils.getTopActivity(), iconInts[0])
        val minLength: Float = min(size.width, size.height).toFloat()
        val sizeMinPx = DeviceUtils.dp2px(
            ApplicationUtils.getTopActivity(),
            TARGET_SIZE_MIN_DP.toFloat()
        )
        return sizeMinPx / minLength
    }

    private fun getMaxScale(): Float {
        val size = ImageUtils.getImageSize(ApplicationUtils.getTopActivity(), iconInts[0])
        val minLength: Float = min(size.width, size.height).toFloat()
        val sizeMinPx = DeviceUtils.dp2px(
            ApplicationUtils.getTopActivity(),
            TARGET_SIZE_MAX_DP.toFloat()
        )
        return sizeMinPx / minLength
    }


    interface OnLikeCLickListener {
        fun onClick()
    }
}