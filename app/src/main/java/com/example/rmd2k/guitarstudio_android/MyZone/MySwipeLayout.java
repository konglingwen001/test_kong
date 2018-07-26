package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.OverScroller;

import com.example.rmd2k.guitarstudio_android.R;

public class MySwipeLayout extends ConstraintLayout {

    OverScroller overScroller;
    Button btnDelete;

    boolean isBtnDeleteVisible = false;

    public MySwipeLayout(Context context) {
        super(context);
        init();
    }

    public MySwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        overScroller = new OverScroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnDelete = findViewById(R.id.btnDelete);
    }

    int downX;
    int downY;
    int downScrollX;
    int downScrollY;
    int currX;
    int currY;
    int offsetX;
    int offsetY;
    boolean isVerticalScroll = false;
    boolean isHorizontalScroll = false;
    public void onSwipe(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isVerticalScroll = false;
                isHorizontalScroll = false;
                downX = (int)event.getX();
                downY = (int)event.getY();
                downScrollX = getScrollX();
                downScrollY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                currX = (int)event.getX();
                currY = (int)event.getY();
                offsetX = currX - downX;
                offsetY = currY - downY;

                if (Math.abs(offsetX) > 5) {
                    scrollTo(downScrollX-offsetX, getScrollY());
                }
                break;
            case MotionEvent.ACTION_UP:
                currX = (int)event.getX();
                currY = (int)event.getY();
                offsetX = currX - downX;
                offsetY = currY - downY;
                if (-downScrollX + offsetX < -btnDelete.getWidth() / 2) {
                    overScroller.startScroll(getScrollX(), getScrollY(), -downScrollX + offsetX + btnDelete.getWidth(), 0);
                    isBtnDeleteVisible = true;
                } else {
                    overScroller.startScroll(getScrollX(), getScrollY(), -downScrollX + offsetX, 0);
                    isBtnDeleteVisible = false;
                }
                invalidate();
                break;
        }
    }

    public void smoothHideDeleteButton() {
        isBtnDeleteVisible = false;
        overScroller.startScroll(getScrollX(), getScrollY(), -btnDelete.getWidth(), 0);
        invalidate();
    }

    public void hideDeleteButton() {
        isBtnDeleteVisible = false;
        scrollTo(0, 0);
        invalidate();
    }

    public void smoothShowDeleteButton() {
        isBtnDeleteVisible = true;
        overScroller.startScroll(getScrollX(), getScrollY(), btnDelete.getWidth(), 0);
        invalidate();
    }

    public void showDeleteButton() {
        isBtnDeleteVisible = true;
        scrollTo(btnDelete.getWidth(), 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }
}
