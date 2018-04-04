package com.example.rmd2k.guitarstudio_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by CHT1HTSH3236 on 2018/3/20.
 */

public class GuitarNotesView extends View implements View.OnClickListener {

    public GuitarNotesView(Context context) {
        super(context);
    }

    public GuitarNotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        setBackgroundColor(Color.RED);

        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);

        float width = getWidth();
        float lineStartX = 50;
        float lineStartY = 50;
        float lineEndX = width - 50;
        float offsetY = 20;
        float[] points = {lineStartX, lineStartY, lineEndX, lineStartY,
                          lineStartX, lineStartY + offsetY, lineEndX, lineStartY + offsetY,
                          lineStartX, lineStartY + offsetY * 2, lineEndX, lineStartY + offsetY * 2,
                          lineStartX, lineStartY + offsetY * 3, lineEndX, lineStartY + offsetY * 3,
                          lineStartX, lineStartY + offsetY * 4, lineEndX, lineStartY + offsetY * 4,
                          lineStartX, lineStartY + offsetY * 5, lineEndX, lineStartY + offsetY * 5};
        canvas.drawLines(points, mPaint);

        NotesModel notesModel = NotesModel.getInstance();
        notesModel.getGuitarNotesFromGuitarNotesTitle(getContext(), "music1");

        super.onDraw(canvas);
    }


    @Override
    public void onClick(View v) {
        System.out.println("GuitarNotesView clicked");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("touched");
        return super.onTouchEvent(event);
    }
}
