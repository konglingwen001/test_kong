package com.example.rmd2k.guitarstudio_android.MyZone;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.R;

/**
 * Created by CHT1HTSH3236 on 2018/6/11.
 */

public class NoteNameListVeiwCell extends ConstraintLayout {

    private static final String TAG = "NoteNameListVeiwCell";

    Button btnDelete;
    TextView tvNoteTitle;

    public NoteNameListVeiwCell(Context context) {
        super(context);
    }

    public NoteNameListVeiwCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    int screenWidth;
    int getScreenWidth() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnDelete = findViewById(R.id.btnDelete);
        tvNoteTitle = findViewById(R.id.tvNoteTitle);
        screenWidth = getScreenWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //btnDelete.setWidth((int)offsetX - 10);
        Log.i(TAG, "onlayout");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    int tvNoteTitleWidth;
    float offsetX;
    private float downPosX;
    private float downPosY;
    private float currPosX;
    private float currPosY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPosX = event.getX();
                downPosY = event.getY();
                tvNoteTitleWidth = tvNoteTitle.getWidth();
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
                if (Math.abs(currPosY - downPosY) > 10) {
                    return false;
                }
                offsetX = currPosX - downPosX;
                if (Math.abs(offsetX) > 5) {
                    LayoutParams lp = (LayoutParams) tvNoteTitle.getLayoutParams();
                    lp.width = (tvNoteTitleWidth + (int)offsetX) < (screenWidth - btnDelete.getWidth() * 1.5) ? (screenWidth - btnDelete.getWidth() * 2) : (tvNoteTitleWidth + (int)offsetX);
                    tvNoteTitle.setLayoutParams(lp);
                    Log.i(TAG, "width:" + (tvNoteTitleWidth + (int)offsetX));
                }
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

        int btnWidth = btnDelete.getWidth();
        offsetX = currPosX - downPosX;
        int newWidth;
        if (tvNoteTitleWidth + offsetX < (screenWidth - btnWidth / 2)) {
            newWidth = screenWidth - btnWidth;
        } else {
            newWidth = screenWidth;
        }

        ValueAnimator animator = ValueAnimator.ofInt(tvNoteTitle.getLayoutParams().width, newWidth);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currVal = (int) animation.getAnimatedValue();
                tvNoteTitle.getLayoutParams().width = currVal;
                tvNoteTitle.requestLayout();
            }
        });
        animator.start();
    }
}
