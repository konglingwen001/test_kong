package com.example.rmd2k.guitarstudio_android;

import android.content.Context;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.GridLayout;

/**
 * TODO: document your custom view class.
 */
public class NoteEditView extends ConstraintLayout {

    GridLayout gridNoteType;

    public NoteEditView(Context context) {
        super(context);
    }

    public NoteEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.note_edit_view, this);
    }

    public NoteEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
