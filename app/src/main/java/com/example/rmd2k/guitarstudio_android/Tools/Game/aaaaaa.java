package com.example.rmd2k.guitarstudio_android.Tools.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class aaaaaa extends LinearLayout {

    //GameView gameView;

    public aaaaaa(Context context) {
        super(context);
        init();
    }

    public aaaaaa(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public aaaaaa(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        //gameView = findViewById(R.id.gameView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //gameView.invalidate();
    }


}
