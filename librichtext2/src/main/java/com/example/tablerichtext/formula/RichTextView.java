package com.example.tablerichtext.formula;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tablerichtext.R;
import com.example.tablerichtext.htmltext.HtmlImageLoader;
import com.example.tablerichtext.htmltext.HtmlText;
import com.example.tablerichtext.htmltext.OnTagClickListener;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

import java.util.List;

/**
 * 富文本
 */
public class RichTextView extends AppCompatTextView {

    private Context mContext;

    public RichTextView(Context context) {
        super(context);
        init(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    public void setMathText(String text) {
        //同步画笔颜色，使生成图片与文字颜色一致
        AjLatexMath.setColor(getCurrentTextColor());
        //添加点击
        setMovementMethod(LinkMovementMethod.getInstance());
        HtmlText.from(text)
                .setImageLoader(new HtmlImageLoader() {
                    @Override
                    public void loadImage(String url, final Callback callback) {
                        // Glide sample, you can also use other image loader
//                        Glide.with(mContext)
//                                .load(url)
//                                .asBitmap()
//                                .into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(Bitmap resource,
//                                                                GlideAnimation<? super Bitmap> glideAnimation) {
//                                        callback.onLoadComplete(resource);
//                                    }
//
//                                    @Override
//                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                        callback.onLoadFailed();
//                                    }
//                                });
                        //
                        Glide.with(mContext).asBitmap()
                                .load(url)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        callback.onLoadComplete(resource);

                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        callback.onLoadFailed();
                                    }

                                });
                    }

                    @Override
                    public Drawable getDefaultDrawable() {
                        return ContextCompat.getDrawable(mContext, R.mipmap.image_placeholder_loading);
                    }

                    @Override
                    public Drawable getErrorDrawable() {
                        return ContextCompat.getDrawable(mContext, R.mipmap.image_placeholder_fail);
                    }

                    @Override
                    public int getMaxWidth() {
                        return getTextWidth();
                    }

                    @Override
                    public boolean fitWidth() {
                        return false;
                    }
                })
                .setOnTagClickListener(new OnTagClickListener() {
                    @Override
                    public void onImageClick(Context context, List<String> imageUrlList, int position) {
                        // image click
                        Toast.makeText(context, imageUrlList.get(position), Toast.LENGTH_SHORT).show();
                        Log.i("zxzx", "========" + imageUrlList.get(position));
                    }

                    @Override
                    public void onLinkClick(Context context, String url) {
                        // link click
                        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                        Log.i("zxzx", "========" + url);
                    }
                })
                .into(this);
    }

    private int getTextWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels - this.getPaddingLeft() - this.getPaddingRight();
    }
}
