package com.fosung.lighthouse.fosunglibs1.gsy

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import com.fosung.lighthouse.fosunglibs1.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import org.jetbrains.anko.imageResource

class VideoPlayerDemo1 : NormalGSYVideoPlayer {

    private var ivSpeed: ImageView? = null
    private var speedF = 1f
    private var ou: OrientationUtils? = null
    var mDuration = 0

    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag!!) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun init(context: Context) {
        super.init(context)
        ivSpeed = findViewById(R.id.iv_speed)
        setSpeed(speedF, true)
        ivSpeed!!.setOnClickListener { changeSpeed() }
        mProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val currentTime = mDuration * p1 / 100
                mCurrentTimeTextView.text = CommonUtil.stringForTime(currentTime.toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                callback?.callBack(1, 0f)
                if (mVideoAllCallBack != null && isCurrentMediaListener) {
                    if (isIfCurrentIsFullscreen) {
                        Debuger.printfLog("onClickSeekbarFullscreen")
                        mVideoAllCallBack.onClickSeekbarFullscreen(mOriginUrl, mTitle, this)
                    } else {
                        Debuger.printfLog("onClickSeekbar")
                        mVideoAllCallBack.onClickSeekbar(mOriginUrl, mTitle, this)
                    }
                }
                if (gsyVideoManager != null && mHadPlay) {
                    try {
                        val time = seekBar!!.progress * mDuration / 100
                        gsyVideoManager.seekTo(time.toLong())
                    } catch (e: Exception) {
                        Debuger.printfWarning(e.toString())
                    }
                }
            }

        })
        mBottomContainer.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->

            if (v.visibility == View.INVISIBLE) {

                callback?.callBack(4, 0f)
            } else if (v.visibility == View.VISIBLE) {

            } else if (v.visibility == View.GONE) {

            }
        }
    }

    fun setProgress(progress: Int) {
        if (mProgressBar != null) {
            mProgressBar.progress = progress
        }

    }

    fun startClick(click: () -> Unit) {
        mStartButton.setOnClickListener {
            if (mHideKey && mIfCurrentIsFullscreen) {
                CommonUtil.hideNavKey(mContext)
            }
            click()
            clickStartIcon()
        }
    }

    /**
     * 播放按键点击
     */
    override fun clickStartIcon() {
        if (TextUtils.isEmpty(mUrl)) {
//            Debuger.printfError("********" + resources.getString(R.string.no_url))
            //Toast.makeText(getActivityContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
            return
        }
        if (mCurrentState == GSYVideoView.CURRENT_STATE_NORMAL || mCurrentState == GSYVideoView.CURRENT_STATE_ERROR) {
//            if (isShowNetConfirm) {
//                showWifiDialog()
//                return
//            }
            startButtonLogic()
        } else if (mCurrentState == GSYVideoView.CURRENT_STATE_PLAYING) {
            try {
                onVideoPause()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            setStateAndUi(GSYVideoView.CURRENT_STATE_PAUSE)
            if (mVideoAllCallBack != null && isCurrentMediaListener) {
                if (mIfCurrentIsFullscreen) {
                    Debuger.printfLog("onClickStopFullscreen")
                    mVideoAllCallBack.onClickStopFullscreen(mOriginUrl, mTitle, this)
                } else {
                    Debuger.printfLog("onClickStop")
                    mVideoAllCallBack.onClickStop(mOriginUrl, mTitle, this)
                }
            }
        } else if (mCurrentState == GSYVideoView.CURRENT_STATE_PAUSE) {
            if (mVideoAllCallBack != null && isCurrentMediaListener) {
                if (mIfCurrentIsFullscreen) {
                    Debuger.printfLog("onClickResumeFullscreen")
                    mVideoAllCallBack.onClickResumeFullscreen(mOriginUrl, mTitle, this)
                } else {
                    Debuger.printfLog("onClickResume")
                    mVideoAllCallBack.onClickResume(mOriginUrl, mTitle, this)
                }
            }
            if (!mHadPlay && !mStartAfterPrepared) {
                startAfterPrepared()
            }
            try {
                gsyVideoManager.start()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            setStateAndUi(GSYVideoView.CURRENT_STATE_PLAYING)
        } else if (mCurrentState == GSYVideoView.CURRENT_STATE_AUTO_COMPLETE) {
            lastProgress = 0
            startButtonLogic()
        }
        callback?.callBack(3, 0f)
    }

    private fun changeSpeed() {
        if (speedF == 1f) {
            speedF = 1.2f

            ivSpeed!!.imageResource = R.mipmap.icon_bs2
        } else if (speedF == 1.2f) {
            speedF = 1.5f

            ivSpeed!!.imageResource = R.mipmap.icon_bs3
        } else if (speedF == 1.5f) {
            speedF = 2f

            ivSpeed!!.imageResource = R.mipmap.icon_bs4
        } else if (speedF == 2f) {
            speedF = 1f
            ivSpeed!!.imageResource = R.mipmap.icon_bs1
        }
        setSpeed(speedF, true)
        callback?.callBack(2, speedF)
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_video_spark_kaoyan
    }


    var lastProgress = 0
    override fun onPrepared() {
        super.onPrepared()
        mDuration = duration.toInt()
        if (lastProgress > 0) {
            seekTo(lastProgress.toLong())
        }
    }

    fun startWithLand(ou: OrientationUtils, click: () -> Unit) {
        startPlayLogic()
//        ou.resolveByClick()
        backButton.visibility = View.VISIBLE
        backButton.setOnClickListener {
            click()
        }
        fullscreenButton.visibility = View.GONE
    }

    fun startWithChange(ou: OrientationUtils, click: () -> Unit) {
        startPlayLogic()
//        ou.resolveByClick()
        backButton.visibility = View.VISIBLE
        backButton.setOnClickListener {
            if (ou.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                ou?.resolveByClick()
            } else {
                click()
            }

        }
//        fullscreenButton.visibility = View.GONE
        fullscreenButton.setOnClickListener() {
            ou?.resolveByClick()
        }
    }

    override fun onError(what: Int, extra: Int) {
        super.onError(what, extra)
        changeUiToCompleteShow()
    }

    override fun resolveFullVideoShow(
        context: Context,
        gsyVideoPlayer: GSYBaseVideoPlayer,
        frameLayout: FrameLayout
    ) {
        super.resolveFullVideoShow(context, gsyVideoPlayer, frameLayout)
        ou = OrientationUtils(context as Activity, gsyVideoPlayer)
        ou!!.resolveByClick()//设置全屏时 强制设为横屏
    }

    override fun resolveNormalVideoShow(
        oldF: View?,
        vp: ViewGroup,
        gsyVideoPlayer: GSYVideoPlayer?
    ) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)
        ou!!.resolveByClick()//退出全屏时 强制设为竖屏
    }

    /**
     * ondestory时调用
     */
    public fun releaseOU() {
        ou?.let {
            it.releaseListener()
        }
    }

    private var callback: OptionCallback? = null
    public fun setCallback(callback: OptionCallback) {
        this.callback = callback
    }

    interface OptionCallback {
        fun callBack(type: Int, speed: Float)
    }

    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.imageResource = R.mipmap.replay_ic_playon1
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.imageResource = R.mipmap.replay_ic_playoff1
            } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                imageView.imageResource = R.mipmap.ic_replay
            } else {
                imageView.imageResource = R.mipmap.replay_ic_playoff1

            }
        }
    }
}
