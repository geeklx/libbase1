package com.geek.libnsfw.javas;

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.geek.libnsfw.R

class Nsfw2ActAdapter(nsfwList: List<MyNsfwBean>?) :
    BaseQuickAdapter<MyNsfwBean, BaseViewHolder>(R.layout.item_mainnsfw2,
        nsfwList as MutableList<MyNsfwBean>?
    ) {

    override fun convert(holder: BaseViewHolder, item: MyNsfwBean) {
        val textView = holder.getView<TextView>(R.id.tv_text)
        val imageView = holder.getView<ImageView>(R.id.iv)
        val view = holder.getView<RelativeLayout>(R.id.view)
        var color = ContextCompat.getColor(context, R.color.nsfw1)
        when (item.nsfw) {
            in 0.0..0.2 -> {
                color = ContextCompat.getColor(context, R.color.nsfw3)
            }
            in 0.2..0.8 -> {
                color = ContextCompat.getColor(context, R.color.nsfw2)
            }
        }
        textView.text =
            "path = ${"img/${item.path}"} \n\nSFW score: ${item.sfw}\n\nNSFW score: ${item.nsfw}"
        imageView.setImageBitmap(item.bitmap)
        view.setBackgroundColor(color)
    }
}