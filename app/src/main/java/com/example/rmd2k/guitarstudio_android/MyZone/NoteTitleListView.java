package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by CHT1HTSH3236 on 2018/6/11.
 */

public class NoteTitleListView extends ListView {

    private static final String TAG = "NoteTitleListView";

    public MyFragment.MyHandler myHandler = null;

    private float downPosX;
    private float downPosY;
    private float currPosX;
    private float currPosY;
    private float offsetX;
    private float offsetY;
    private boolean horizontalSlide = false;

    public NoteTitleListView(Context context) {
        super(context);
    }

    public NoteTitleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (myHandler != null) {
                    myHandler.sendMessage(Message.obtain());
                }
                horizontalSlide = false;
                downPosX = event.getX();
                downPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (horizontalSlide) {
                    return false;
                }

                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = Math.abs(currPosX - downPosX);
                offsetY = Math.abs(currPosY - downPosY);
                if (offsetX > 5 || offsetY > 5) {
                    // 当垂直或者水平移动大于5时，开始判断是上下滑动还是左右滑动，并且返回false，不拦截事件
                    if (offsetX > offsetY) {
                        // 左右滑动
                        horizontalSlide = true; // 锁定左右滑动
                        return false;
                    } else {
                        if (!horizontalSlide) {
                            // 上下滑动
                            return true;
                        } else {
                            // 左右滑动
                            return false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = Math.abs(currPosX - downPosX);
                offsetY = Math.abs(currPosY - downPosY);

                if (offsetX < 5 && offsetY < 5) {
                    int position = this.pointToPosition((int)currPosX, (int)currPosY);
                    performItemClick(this, position, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
