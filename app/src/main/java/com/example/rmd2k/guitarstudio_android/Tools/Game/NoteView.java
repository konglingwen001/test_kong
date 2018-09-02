package com.example.rmd2k.guitarstudio_android.Tools.Game;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class NoteView extends View {

    boolean isVisible = false;
    String noteType;
    String fretNo;

    public NoteView(Context context) {
        super(context);
        init();
    }

    public NoteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }
}
