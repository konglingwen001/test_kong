package com.example.rmd2k.guitarstudio_android.Tools.Tuner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;

public class InstrumentPanelView extends View {

    private static final int SCALE_NUM = 50;

    private NotesModel notesModel;
    private Paint mPaint;

    public InstrumentPanelView(Context context) {
        super(context);
        init(context);
    }

    public InstrumentPanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InstrumentPanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        notesModel = NotesModel.getInstance(context);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawScales(canvas);
    }

    private void drawScales(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(2);
        canvas.drawLine(0, 1, width, 1, mPaint);
        canvas.drawLine(0, height, width, height, mPaint);

        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(4);

        int x;
        int offsetY;
        int startInterval = notesModel.dp2px(20);
        int bottomInterval = notesModel.dp2px(20);
        int scaleHeight = notesModel.dp2px(10);
        double interval = ((double)width - (double)startInterval * 2) / (double)SCALE_NUM;
        int value;
        String text;
        for (int i = 0; i <= SCALE_NUM; i++) {
            x = startInterval + (int)(interval * i);
            if (i % 5 == 0) {
                offsetY = scaleHeight;
                value = (i / 5 - 5) * 30;
                Rect textSize = new Rect();
                text = value + "";
                mPaint.setTextSize(25);
                mPaint.getTextBounds(text, 0, text.length(), textSize);
                canvas.drawText(text, x - textSize.width() / 2, height - textSize.height(), mPaint);
            } else {
                offsetY = 0;
            }
            canvas.drawLine(x, height - bottomInterval, x, height - bottomInterval - scaleHeight - offsetY, mPaint);
        }
    }
}
