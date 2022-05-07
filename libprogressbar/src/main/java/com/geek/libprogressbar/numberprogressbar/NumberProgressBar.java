package com.geek.libprogressbar.numberprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.geek.libprogressbar.R;

/**
 * 下方带数字的条形进度条
 */
public class NumberProgressBar extends View {

    public interface ProgressCompleteListener {
        public abstract void complete(long max, long progress);
    }

    private ProgressCompleteListener mProgressCompleteListener;

    private long mMaxProgress = 100;
    private long mCurrentProgress = 0;
    private int mReachedBarColor;
    private int mUnreachedBarColor;
    private int mTextColor;
    private float mTextSize;
    private float mReachedBarHeight;
    private float mUnreachedBarHeight;

    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";

    private float mDrawImageWidth;
    private float mDrawTextStart;
    private float mDrawTextEnd;
    private float mDrawImageStart;
    private float mDrawImageEnd;
    private int mDrawableIcon;

    private String mCurrentDrawText;
    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;
    private Paint mTextPaint;
    private Paint mImagePaint;

    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);

    private Bitmap mBitmap;
    private Drawable mDrawable;

    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar,
                defStyleAttr, 0);

        mReachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_reached_color, default_reached_color);
        mUnreachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_unreached_color, default_unreached_color);
        mTextColor = attributes.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_size, 0);

        mReachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_reached_bar_height, 0);
        mUnreachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_unreached_bar_height, 0);
        mDrawableIcon = attributes.getResourceId(R.styleable.NumberProgressBar_progress_icon, 0);

        setProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        setMax(attributes.getInt(R.styleable.NumberProgressBar_progress_maxs, 100));

        mBitmap = BitmapFactory.decodeResource(getResources(), mDrawableIcon);
        attributes.recycle();
        initializePainters();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) mTextSize, Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        calculateDrawRectF();

        canvas.drawRoundRect(mUnreachedRectF, mUnreachedBarHeight / 2, mUnreachedBarHeight / 2, mUnreachedBarPaint);

        canvas.drawRoundRect(mReachedRectF, mReachedBarHeight / 2, mReachedBarHeight / 2, mReachedBarPaint);

        canvas.drawText(mCurrentDrawText, mDrawTextStart, mDrawTextEnd, mTextPaint);

        canvas.drawBitmap(mBitmap, mDrawImageStart, mDrawImageEnd, mImagePaint);
    }

    private void initializePainters() {
        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);

        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mImagePaint = new Paint();
    }

    private void calculateDrawRectF() {

        mCurrentDrawText = String.format("%d", getProgress() * 100 / getMax());
        mCurrentDrawText = mCurrentDrawText + "%";
        mDrawImageWidth = mBitmap.getWidth();

        if (getProgress() == 0) {
            mDrawTextStart = getPaddingLeft() - mDrawImageWidth / 2;
            mDrawImageStart = getPaddingLeft() - mDrawImageWidth / 2;
        } else {
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft();
            mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;
            mDrawTextStart = (mReachedRectF.right - mDrawImageWidth * 2 / 3);
            mDrawImageStart = (mReachedRectF.right - mDrawImageWidth * 2 / 3);
        }

        mDrawImageEnd = (int) ((getHeight() / 2.0f) - mBitmap.getHeight() / 2.0f);
        mDrawTextEnd = getHeight();

        if ((mDrawImageStart + mDrawImageWidth * 2 / 3) >= getWidth() - getPaddingRight()) {
            mDrawImageStart = getWidth() - getPaddingRight() - mDrawImageWidth * 2 / 3;
            mDrawTextStart = getWidth() - getPaddingRight() - mDrawImageWidth * 2 / 3;
            mReachedRectF.right = mDrawImageStart + mDrawImageWidth / 2;
        }

        float unreachedBarStart = getPaddingLeft() - (mUnreachedBarHeight - mReachedBarHeight) / 2;
        if (unreachedBarStart < getWidth() - getPaddingRight()) {
            mUnreachedRectF.left = unreachedBarStart;
            mUnreachedRectF.right = getWidth() - getPaddingRight() + (mUnreachedBarHeight - mReachedBarHeight) / 2;
            mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
            mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
        }
    }

    public long getProgress() {
        return mCurrentProgress;
    }

    public long getMax() {
        return mMaxProgress;
    }

    public void setProgress(long progress) {
        if (progress <= getMax() && progress >= 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }

    public void setMax(long maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setProgressCompleteListener(ProgressCompleteListener progressCompleteListener) {
        this.mProgressCompleteListener = progressCompleteListener;
    }

    /**
     * 设置步长
     *
     * @param by
     */
    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
            mProgressCompleteListener.complete(mMaxProgress, mCurrentProgress);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putLong(INSTANCE_MAX, getMax());
        bundle.putLong(INSTANCE_PROGRESS, getProgress());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            initializePainters();
            setMax(bundle.getLong(INSTANCE_MAX));
            setProgress(bundle.getLong(INSTANCE_PROGRESS));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
