package com.geek.libgoodview.blastgoodview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.geek.libgoodview.R
import kotlin.math.max

/**
 * @author:wangshouxue
 * @date:2022/3/28 下午3:42
 * @description:类作用
 */
class LikeNumber : AppCompatImageView {

    private var number = 0
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmaps: Array<Bitmap?> = arrayOfNulls(10)

    private var numberResIds = arrayOf(
        R.mipmap.icon_like_num_0,
        R.mipmap.icon_like_num_1,
        R.mipmap.icon_like_num_2,
        R.mipmap.icon_like_num_3,
        R.mipmap.icon_like_num_4,
        R.mipmap.icon_like_num_5,
        R.mipmap.icon_like_num_6,
        R.mipmap.icon_like_num_7,
        R.mipmap.icon_like_num_8,
        R.mipmap.icon_like_num_9,
    )

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        var i = 0
        numberResIds.iterator().forEach {
            bitmaps[i] = BitmapFactory.decodeResource(context.resources, it)
            i++
        }
    }

    fun setNumber(num: Int) {
        number = num
        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var numStr = number.toString()
        numStr.forEachIndexed { indexOut, c ->
            var startX = 0.0f
            for (index in 1..indexOut) {
                val digitalOld = numStr[index - 1].toString().toInt()
                startX += bitmaps[digitalOld]?.width!!
            }
            val digital = c.toString().toInt()
            bitmaps[digital]?.let {
                canvas?.drawBitmap(it, startX, 0f, paint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var numStr = number.toString()
        var totalWidth = 0
        var bestHeight = 0
        numStr.forEach { numChar ->
            val digital = numChar.toString().toInt()
            bitmaps[digital]?.let {
                totalWidth += it.width
                bestHeight = max(bestHeight, it.height)
            }
        }
        setMeasuredDimension(totalWidth, bestHeight)
    }
}