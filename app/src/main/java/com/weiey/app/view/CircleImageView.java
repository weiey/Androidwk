package com.weiey.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.weiey.app.R;

@SuppressLint("NewApi")
public class CircleImageView extends AppCompatImageView {
    private int mDefaultImageId;
    private int mCornerRadius = 0;
    private boolean mCircle = false;
    private boolean mSquare = false;
    private int mBorderWidth = 0;
    private int mBorderColor = Color.WHITE;

    private Paint mBorderPaint;

    private Delegate mDelegate;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initCustomAttrs(context, attrs);

        initBorderPaint();

        setDefaultImage();
    }

    private void initBorderPaint() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }
    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.CircleImageView_android_src) {
            mDefaultImageId = typedArray.getResourceId(attr, 0);
        } else if (attr == R.styleable.CircleImageView_civ_circle) {
            mCircle = typedArray.getBoolean(attr, mCircle);
        } else if (attr == R.styleable.CircleImageView_civ_cornerRadius) {
            mCornerRadius = typedArray.getDimensionPixelSize(attr, mCornerRadius);
        } else if (attr == R.styleable.CircleImageView_civ_square) {
            mSquare = typedArray.getBoolean(attr, mSquare);
        } else if (attr == R.styleable.CircleImageView_civ_borderWidth) {
            mBorderWidth = typedArray.getDimensionPixelSize(attr, mBorderWidth);
        } else if (attr == R.styleable.CircleImageView_civ_borderColor) {
            mBorderColor = typedArray.getColor(attr, mBorderColor);
        }
    }

    private void setDefaultImage() {
        if (mDefaultImageId != 0) {
            setImageResource(mDefaultImageId);
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        setImageDrawable(getResources().getDrawable(resId));
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable instanceof BitmapDrawable && mCornerRadius > 0) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getRoundedDrawable(getContext(), bitmap, mCornerRadius));
            } else {
                super.setImageDrawable(drawable);
            }
        } else if (drawable instanceof BitmapDrawable && mCircle) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getCircleDrawable(getContext(), bitmap));
            } else {
                super.setImageDrawable(drawable);
            }
        } else {
            super.setImageDrawable(drawable);
        }
        notifyDrawableChanged(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCircle || mSquare) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCircle && mBorderWidth > 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 1.0f * mBorderWidth / 2, mBorderPaint);
        }
    }

    private void notifyDrawableChanged(Drawable drawable) {
        if (mDelegate != null) {
            mDelegate.onDrawableChanged(drawable);
        }
    }

    public void setCornerRadius(int cornerRadius) {
        mCornerRadius = cornerRadius;
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public interface Delegate {
        void onDrawableChanged(Drawable drawable);
    }


    public static RoundedBitmapDrawable getCircleDrawable(Context context, Bitmap src) {
        Bitmap dst;
        if (src.getWidth() >= src.getHeight()) {
            dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight() / 2, 0, src.getHeight(), src.getHeight());
        } else {
            dst = Bitmap.createBitmap(src, 0, src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(), src.getWidth());
        }

        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), dst);
        circleDrawable.setAntiAlias(true);
        circleDrawable.setCircular(true);
        return circleDrawable;
    }

    public static RoundedBitmapDrawable getRoundedDrawable(Context context, Bitmap bitmap, float cornerRadius) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setAntiAlias(true);
        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        return roundedBitmapDrawable;
    }

}
