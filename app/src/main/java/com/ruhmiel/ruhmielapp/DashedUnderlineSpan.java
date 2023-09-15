package com.ruhmiel.ruhmielapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.style.ReplacementSpan;

import androidx.core.content.ContextCompat;

class DashedUnderlineSpan extends ReplacementSpan {
    private Paint p = new Paint();
    private int mWidth;
    private String mSpan;

    private float mSpanLength = 0F;
    private boolean mLengthIsCached = false;
    private Float mOffsetY = 0f;

    DashedUnderlineSpan(int _color, String _spannedText, Context context) {
        float mStrokeWidth = context.getResources().getDimension(R.dimen.stroke_width);
        float mDashPathEffect = context.getResources().getDimension(R.dimen.dash_path_effect);
        mOffsetY = context.getResources().getDimension(R.dimen.offset_y);

        p = new Paint();
        p.setColor(_color);
        p.setStyle(Paint.Style.STROKE);
        p.setPathEffect(new DashPathEffect(new float[]{mDashPathEffect, mDashPathEffect}, 0));
        p.setStrokeWidth(mStrokeWidth);
        mSpan = _spannedText;
        mSpanLength = _spannedText.length();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mWidth = (int) paint.measureText(text, start, end);
        return mWidth;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        canvas.drawText(text, start, end, x, y, paint);
        if (!mLengthIsCached)
            mSpanLength = paint.measureText(mSpan);
        Path path = new Path();
        path.moveTo(x, y + mOffsetY);
        path.lineTo(x + mSpanLength, y + mOffsetY);
        canvas.drawPath(path, this.p);
    }
}

