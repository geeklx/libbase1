package com.geek.libnsfw.javas;

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.geek.libnsfw.R
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


class NsfwAct2 : AppCompatActivity(), View.OnClickListener {

    var nsfwHelper: NsfwHelper? = null
    var mainAdapter: Nsfw2ActAdapter? = null
    var index = 0
    var listData: ArrayList<MyNsfwBean> = ArrayList<MyNsfwBean>()
    var selectList: List<LocalMedia>? = null
    var tv_version: TextView? = null
    var rv: RecyclerView? = null
    var bt_sc_assets: Button? = null
    var bt_sc_from_other: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch(Dispatchers.IO) {
//            NSFWHelper.getNSFWScore(BitmapFactory.decodeFile(file.path)).let { result ->
//                withContext(Dispatchers.Main) {
//                    onResult(result)
//                }
//            }
//        }
        setContentView(R.layout.activity_mainnsfw2)
        tv_version = findViewById(R.id.tv_version)
        rv = findViewById(R.id.rv)
        bt_sc_assets = findViewById(R.id.bt_sc_assets)
        bt_sc_from_other = findViewById(R.id.bt_sc_from_other)
        initNsfwHelper()
        initAdapter()
        initClickListener()
//        tv_version?.text = "当前版本：${this.packageManager.getPackageInfo(packageName, 0).versionName}"
        tv_version?.text = "当前版本：${AppUtils.getAppVersionName(packageName)}"
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) { //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            );
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_sc_assets -> {
                reScAssetsImgs()
            }
            R.id.bt_sc_from_other -> {
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())//全部.ofAll()、图片.、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(20)// 最大图片选择数量 int
                    .minSelectNum(1)// 最小选择数量 int
                    .imageSpanCount(3)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选  or PictureConfig.SINGLE
                    .isPreviewImage(true)// 是否可预览图片 true or false
                    .isCamera(false)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .selectionData(selectList)
//                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .imageEngine(GlideEngineNsfw.createGlideEngine())// 外部传入图片加载引擎，必传项
//                    .forResult(0x01);//结果回调onActivityResult code
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        //                        fun onResult(result: ArrayList<LocalMedia?>?) {
//                            if (selectList != null && selectList?.size ?: 0 > 0)
//                                reScFromImgs(selectList!!)
//                        }
                        override fun onResult(result: MutableList<LocalMedia?>?) {
//                            TODO("Not yet implemented")
                            if (result != null && result.size > 0)
                                reScFromImgs(result)
                        }

                        override fun onCancel() {

                        }
                    })
            }
        }
    }


//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            selectList = PictureSelector.obtainMultipleResult(data)
//            if (selectList != null && selectList?.size ?: 0 > 0)
//                reScFromImgs(selectList!!)
//        }
//    }


    private fun initClickListener() {
        bt_sc_assets?.setOnClickListener(this)
        bt_sc_from_other?.setOnClickListener(this)
    }

    private fun initAdapter() {
        mainAdapter = Nsfw2ActAdapter(null)
        rv?.layoutManager = LinearLayoutManager(this)
        rv?.adapter = mainAdapter
    }

    private fun initNsfwHelper() {
        nsfwHelper = NsfwHelper.getInstance(this, false, 4)
    }

    private fun reScFromImgs(list: MutableList<LocalMedia?>) {
        index = 0
        mainAdapter?.setNewInstance(null)
        listData = ArrayList<MyNsfwBean>()
        Thread(Runnable {
            for (lm in list) {
                val bitmap = BitmapFactory.decodeFile(lm!!.realPath)
                if (bitmap == null) {
                    return@Runnable
                }
                listData.add(MyNsfwBean(0.0f, 0.0f, lm.realPath, bitmap))
                nsfwHelper?.scanBitmap(bitmap) { sfw, nsfw ->
                    listData[index].sfw = sfw
                    listData[index].nsfw = nsfw
                    mainAdapter?.addData(listData[index])
                    mainAdapter?.notifyItemInserted(index)
                    rv?.scrollToPosition(index)
                    index++
                }
            }
        }).start()
    }

    private fun reScAssetsImgs() {
        index = 0
        mainAdapter?.setNewInstance(null)
        listData = ArrayList<MyNsfwBean>()
        for (a in resources.assets.list("img")!!) {
            val path = "img/${a}"
            val b = BitmapFactory.decodeStream(resources.assets.open(path))
            listData.add(MyNsfwBean(0f, 0f, path, b))
            nsfwHelper?.scanBitmap(b) { sfw, nsfw ->
                listData[index].sfw = sfw
                listData[index].nsfw = nsfw
                mainAdapter?.addData(listData[index])
                mainAdapter?.notifyItemInserted(index)
                rv?.scrollToPosition(index)
                index++
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        nsfwHelper?.destroy()
    }
}
