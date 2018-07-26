package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MySwipeListView extends ListView {

    private int downX;
    private int downY;
    private int currX;
    private int currY;
    private int offsetX;
    private int offsetY;
    private int position;
    private MySwipeLayout mySwipeLayout;
    private boolean isVerticalScroll = false;
    private boolean isHorizontalScroll = false;

    public MySwipeListView(Context context) {
        super(context);
    }

    public MySwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("KONG", "listview down");
                if (hideAllDeleteButton()) {
                    return false;
                }
                isVerticalScroll = false;
                isHorizontalScroll = false;
                downX = (int)event.getX();
                downY = (int)event.getY();
                position = pointToPosition(downX, downY);
                mySwipeLayout = (MySwipeLayout) getChildAt(position - getFirstVisiblePosition());
                if (mySwipeLayout == null) {
                    Log.e("KONG3", "position:" + position);
                    break;
                }
                mySwipeLayout.onSwipe(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("KONG", "listview move");
                currX = (int)event.getX();
                currY = (int)event.getY();
                offsetX = Math.abs(currX - downX);
                offsetY = Math.abs(currY - downY);
                if (offsetY > 5 && !isHorizontalScroll) {
                    Log.e("KONG", "vertical");
                    isVerticalScroll = true;
                }
                if (offsetX > 5 && !isVerticalScroll) {
                    Log.e("KONG", "horizontal");

                    setPressed(false);
                    mySwipeLayout.setPressed(false);

                    isHorizontalScroll = true;
                    mySwipeLayout.onSwipe(event);
                    return true; // 自己处理move事件，不让上层处理上下滑动
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("KONG", "listview up");
                currX = (int)event.getX();
                currY = (int)event.getY();
                offsetX = Math.abs(currX - downX);
                offsetY = Math.abs(currY - downY);
                if (offsetX > 5 && isHorizontalScroll) {
                    mySwipeLayout.onSwipe(event);
                    return true; // 返回true不触发点击事件
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean hideAllDeleteButton() {
        boolean result = false;
        MySwipeLayout mySwipeLayout;
        int first = getFirstVisiblePosition();
        int last = getLastVisiblePosition();
        for (int i = first; i <= last; i++) {
            mySwipeLayout = (MySwipeLayout) getChildAt(i - first);
            if (mySwipeLayout != null && mySwipeLayout.isBtnDeleteVisible) {
                result = true;
                mySwipeLayout.smoothHideDeleteButton();
            }
        }
        return result;
    }
}
