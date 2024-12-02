package com.fosung.lighthouse.fosunglibs1.gsy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fosung.lighthouse.fosunglibs1.R
import com.geek.libutils.app.BaseApp
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_video.video_player
import org.jetbrains.anko.toast
import java.io.File

open class VideoActivity : AppCompatActivity() {

    private var tempFilePath: String? = "file://"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val tempDir = File(
            BaseApp.get().getExternalFilesDir(null),
            "/temp/"
        )
        //            File tempFile = new File(tempDir, fileName + ".mp4");
        val tempFile = File(tempDir, "1890731971994668416.mp4")
        tempFilePath = "file://" + tempFile.getPath()
        initVideo(tempFilePath!!)

    }


    private fun initVideo(videoUrl0: String) {
        val option = GSYVideoOptionBuilder().setDismissControlTime(10000)

        option.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                //开始播放
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                super.onAutoComplete(url, *objects)


            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
                super.onPlayError(url, *objects)
            }
        }).build(video_player)
//        videoParam?.let { video ->
//            val progress = progressMap[video.key]?.toInt()
//            video_player.lastProgress = progress ?: 0//添加上次观看的进度
//        }

        when {
            videoUrl0.startsWith("http") -> {
                video_player.setUp(videoUrl0, true, "标题")
            }

            tempFilePath != null -> {
                video_player.setUp(tempFilePath, true, "标题")
            }

            else -> {
                toast("未知错误")
                onBackPressed()
            }
        }
        video_player.setIsTouchWiget(true)
        playVideo()
    }

    open fun playVideo() {
        val ou = OrientationUtils(this, video_player)
        video_player.startWithLand(ou) {
            onBackPressed()
        }
    }


    override fun onResume() {
        super.onResume()
        video_player.onVideoResume()

    }

    override fun onPause() {
        super.onPause()
        video_player.onVideoPause()


    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onDestroy() {
        GSYVideoManager.releaseAllVideos()
        video_player.releaseOU()
        video_player.setVideoAllCallBack(null)
//        if (tempFilePath != null) {
//            Thread {
//                FileUtils.deleteFile(tempFilePath!!)
//            }.start()
//        }
        //
        super.onDestroy()
    }


}
