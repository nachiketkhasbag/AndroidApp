package com.khasna.cooker.ImageAdjustments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by nachiket on 4/16/2017.
 */

public class SetSplashScreenImageView extends AppCompatImageView {

    public SetSplashScreenImageView(Context context) {
        super(context);
    }

    public SetSplashScreenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SetSplashScreenImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = MeasureSpec.getSize(heightMeasureSpec);
            setMeasuredDimension(w/3, h/4);
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
