package com.example.rmd2k.guitarstudio_android.Tools.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class NoteView extends View {

    private static final int CORNER_SIZE = 40;

    private Paint mPaint;

    boolean isAttachedToNote = false;
    boolean isVisible = false;
    boolean isAddedToView = false;
    boolean isRestNote = false;
    String noteType;
    String fretNo;
    int stringNo;
    int width;
    int height;
    int startX;

    public NoteView(Context context) {
        this(context, null);
    }

    public NoteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.GREEN);
        RectF rec = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rec, CORNER_SIZE, CORNER_SIZE, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(40);
        mPaint.setStrokeWidth(3);
        canvas.drawText(fretNo, 0, 0, mPaint);
    }
}
