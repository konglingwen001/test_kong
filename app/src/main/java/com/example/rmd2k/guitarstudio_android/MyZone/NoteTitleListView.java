package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.example.rmd2k.guitarstudio_android.R;

/**
 * Created by CHT1HTSH3236 on 2018/6/11.
 */

public class NoteTitleListView extends ListView {

    private static final String TAG = "NoteTitleListView";

    private static final int REFRESH_LIST = 0;
    private static final int SET_CLICKABLE = 1;

    private Handler myFragment_Handler = null;

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

    public void setMyFragment_Handler(Handler handler) {
        myFragment_Handler = handler;
    }

    public boolean checkBtnDeleteVisible() {
        int count = getCount();
        SpringBackScrollView svCell;
        for (int i = 0; i < count; i++) {
            svCell = getChildAt(i).findViewById(R.id.svCell);
            if (svCell.isBtnDeleteVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public void hideAllBtnDelete() {
        SpringBackScrollView svCell;
        for (int i = 0; i < getChildCount(); i++) {
            svCell = getChildAt(i).findViewById(R.id.svCell);
            if (svCell.isBtnDeleteVisible()) {
                svCell.setBtnDeleteVisible(false);
                svCell.isHideByListView = true;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    SpringBackScrollView springBackScrollView = null;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //我们想知道当前点击了哪一行
        int position;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPosX = (int) event.getX();
                downPosY = (int) event.getY();

                position = pointToPosition((int)event.getX(), (int)event.getY());
                Log.e("KONG", "listview touch down:" + position);
                if (position != INVALID_POSITION) {
                    int pos = position + getFirstVisiblePosition();
                    springBackScrollView = getChildAt(position + getFirstVisiblePosition()).findViewById(R.id.svCell);
                }
                if (checkBtnDeleteVisible()) {
                    hideAllBtnDelete();
                    return false;
                }
                return true;
                //break;
            case MotionEvent.ACTION_MOVE:
                position = pointToPosition((int)event.getX(), (int)event.getY());
                Log.e("KONG", "listview touch move:" + position);
                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = Math.abs(currPosX - downPosX);
                offsetY = Math.abs(currPosY - downPosY);
                break;
            case MotionEvent.ACTION_UP:
                position = pointToPosition((int)event.getX(), (int)event.getY());
                Log.e("KONG", "listview touch up:" + position);
                break;
            case MotionEvent.ACTION_CANCEL:
                position = pointToPosition((int)event.getX(), (int)event.getY());
                Log.e("KONG", "listview touch cancel:" + position);
                break;
        }

        if (springBackScrollView != null && !checkBtnDeleteVisible()) {
            springBackScrollView.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }
}
