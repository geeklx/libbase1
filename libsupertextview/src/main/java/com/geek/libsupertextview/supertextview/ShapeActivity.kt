package com.geek.libsupertextview.supertextview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geek.libsupertextview.R
import com.geek.libsupertextview.helper.ShapeBuilder
import com.geek.libsupertextview.helper.ShapeType
import com.geek.libsupertextview.shape.ShapeTextView

/**
 * <pre>
 * @author : Allen
 * e-mail  : lygttpod@163.com
 * date    : 2019/05/31
 * desc    :
</pre> *
 */
class ShapeActivity : AppCompatActivity() {
    private var shape_text_view: ShapeTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_shape)
        shape_text_view = findViewById(R.id.shape_text_view)
        shape_text_view?.setOnClickListener {
            shape_text_view?.shapeBuilder?.let {
                it.setShapeType(ShapeType.RECTANGLE)
                    .setShapeSolidColor(resources.getColor(R.color.colorAccent))
                    .setShapeStrokeColor(resources.getColor(R.color.colorPrimary))
                    .setShapeStrokeWidth(2)
                    .setShapeCornersRadius(30f)
                    .into(shape_text_view)
            }
        }

    }

    /**
     * 如果ShapeXXXView无法满足你的布局需求可以，使用ShapeBuilder对任何view设置shape属性
     * 用法如下：
     *          normalShape.into("你的任意View")
     */
    private val normalShape: ShapeBuilder
        get() = ShapeBuilder()
            .setShapeType(ShapeType.RECTANGLE)
            .setShapeSolidColor(resources.getColor(R.color.colorAccent))
            .setShapeStrokeColor(resources.getColor(R.color.colorPrimary))
            .setShapeStrokeWidth(2)
            .setShapeCornersRadius(30f)

}
