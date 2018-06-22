package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.R;

/**
 * Created by CHT1HTSH3236 on 2018/6/11.
 */

public class NoteTitleListView extends ListView {

    private static final String TAG = "NoteTitleListView";

    private float downPosX;
    private float downPosY;
    private float currPosX;
    private float currPosY;
    private float offsetX;
    private float offsetY;
    private boolean horizontalSlide = false;

    Button btnDelete;

    public NoteTitleListView(Context context) {
        super(context);
    }

    public NoteTitleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //btnDelete = findViewById(R.id.btnDelete);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, "ACTION_DOWN");
                horizontalSlide = false;
                downPosX = event.getX();
                downPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (horizontalSlide) {
                    return false;
                }
                Log.i(TAG, "ACTION_MOVE");

                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = Math.abs(currPosX - downPosX);
                offsetY = Math.abs(currPosY - downPosY);
                if (offsetX > 5 || offsetY > 5) {
                    // 当垂直或者水平移动大于5时，开始判断是上下滑动还是左右滑动，并且返回false，不拦截事件
                    if (offsetX > offsetY) {
                        // 左右滑动
                        Log.i(TAG, "horizontal");
                        horizontalSlide = true; // 锁定左右滑动
                        return false;
                    } else {
                        if (!horizontalSlide) {
                            // 上下滑动
                            Log.i(TAG, "vertical");
                            return true;
                        } else {
                            // 左右滑动
                            return false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //Log.i(TAG, "ACTION_UP");
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
                //Log.i(TAG, "ACTION_CANCEL");
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
