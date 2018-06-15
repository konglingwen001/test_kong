package com.example.rmd2k.guitarstudio_android;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by CHT1HTSH3236 on 2018/6/14.
 */

public class TestLayout extends LinearLayout {

    Button btnTest;
    Button btnAnim;
    private static final String TAG = "TestLayout";
    int oldWidth = 200;

    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnTest = findViewById(R.id.btnTest);
        btnAnim = findViewById(R.id.btnAnim);
        btnAnim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ValueAnimator animator = ValueAnimator.ofInt(btnTest.getLayoutParams().width, 100);
                animator.setDuration(1000);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int currVal = (int) animation.getAnimatedValue();
                        btnTest.getLayoutParams().width = currVal;
                        btnTest.requestLayout();
                    }
                });
                animator.start();
                //ObjectAnimator.ofInt(btnTest, "width", 500).setDuration(1000).start();
                //ObjectAnimator.ofFloat(btnTest, "rotationX", 0.0f, 90f).setDuration(1000).start();
            }
        });
    }

    int currWidth;

    float offsetX;
    private float downPosX;
    private float downPosY;
    private float currPosX;
    private float currPosY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currWidth = btnTest.getWidth();
                downPosX = event.getX();
                downPosY = event.getY();
                Log.d(TAG, System.currentTimeMillis() + " : action down1");
                //break;
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, System.currentTimeMillis() + " : action up1");

                handleBounce(event);
                break;
            //return true;
            case MotionEvent.ACTION_MOVE:
                //Log.d(TAG, System.currentTimeMillis() + " : action move1");
                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = currPosX - downPosX;

                LayoutParams lp = (LayoutParams) btnTest.getLayoutParams();
                lp.width = currWidth + (int)offsetX;
                btnTest.setLayoutParams(lp);
                Log.i(TAG, "width:" + (currWidth + (int)offsetX));

                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, System.currentTimeMillis() + " : action cancel1");
                break;
            //return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleBounce(MotionEvent event) {
        currPosX = event.getX();
        currPosY = event.getY();

        currWidth = btnTest.getWidth();
        Log.i(TAG, "currWidth=" + currWidth);
        offsetX = currPosX - downPosX;
        ObjectAnimator.ofInt(btnTest, "width", oldWidth).setDuration(1000).start();
    }
}
