package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.MotionEvent;
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

    //    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e("KONG", "ListView action down");
//                if (myFragment_Handler != null && checkBtnDeleteVisible()) {
//                    Log.e("KONG", "myHandler");
//                    Message msg = Message.obtain();
//                    msg.what = REFRESH_LIST;
//                    myFragment_Handler.sendMessage(msg);
//                    return true;
//                }
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }

    public void hideAllBtnDelete() {
        SpringBackScrollView svCell;
        for (int i = 0; i < getChildCount(); i++) {
            svCell = getChildAt(i).findViewById(R.id.svCell);
            svCell.setBtnDeleteVisible(false);
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (checkBtnDeleteVisible()) {
//            return false;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        float downPosX = 0;
//        float downPosY = 0;
//        float currPosX = 0;
//        float currPosY = 0;
//        float offsetX = 0;
//        float offsetY = 0;
//
//        SpringBackScrollView springBackScrollView = null;
////        int x = (int) event.getX();
////        int y = (int) event.getY();
////        //我们想知道当前点击了哪一行
////        int position = pointToPosition(x, y);
////        Log.e(TAG, "postion=" + position);
////        if (position != INVALID_POSITION) {
////            springBackScrollView = getChildAt(position + getFirstVisiblePosition()).findViewById(R.id.svCell);
////        }
//
//        //我们想知道当前点击了哪一行
//        int position = pointToPosition((int)event.getX(), (int)event.getY());
//        //Log.e(TAG, "postion=" + position);
//        if (position != INVALID_POSITION) {
//            int pos = position + getFirstVisiblePosition();
//            springBackScrollView = getChildAt(position + getFirstVisiblePosition()).findViewById(R.id.svCell);
//        }
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downPosX = (int) event.getX();
//                downPosY = (int) event.getY();
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("KONG", "listview touch move");
//                currPosX = event.getX();
//                currPosY = event.getY();
//                offsetX = Math.abs(currPosX - downPosX);
//                offsetY = Math.abs(currPosY - downPosY);
//
////                if (offsetX > 5 || offsetY > 5) {
////                    if (offsetX > offsetY) {
////                        springBackScrollView.setPressed(false);
////                        springBackScrollView.cancelPendingInputEvents();
////                    }
////                }
//                break;
//        }
//
//        if (springBackScrollView != null) {
//            //springBackScrollView.onTouchEvent(event);
//            //springBackScrollView.onRequestTouchEvent(event);
//        }
//
//        return super.onTouchEvent(event);
//    }

    /*
        1.
     */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("KONG", "ListView action down");
//                if (myFragment_Handler != null && checkBtnDeleteVisible()) {
//                    Log.e("KONG", "myHandler");
//                    Message msg = Message.obtain();
//                    msg.what = REFRESH_LIST;
//                    myFragment_Handler.sendMessage(msg);
//                    return true;
//                }
                horizontalSlide = false;
                downPosX = event.getX();
                downPosY = event.getY();
                //break;
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.e("KONG", "ListView action move");
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
                Log.e("KONG", "ListView action up");
                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = Math.abs(currPosX - downPosX);
                offsetY = Math.abs(currPosY - downPosY);

                if (offsetX < 5 && offsetY < 5) {
                    Log.e("KONG", "Clicked");
                    int position = this.pointToPosition((int)currPosX, (int)currPosY);
                    setPressed(true);
                    performItemClick(this, position, 0);
                    return true;
                }
                //break;
                return false;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e("KONG", "-----------------------------------------------1");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("KONG", "-----------------------------------------------2");
//                currPosX = event.getX();
//                currPosY = event.getY();
//                offsetX = Math.abs(currPosX - downPosX);
//                offsetY = Math.abs(currPosY - downPosY);
//
//                if (offsetX < 5 && offsetY < 5) {
//                    Log.e("KONG", "Clicked");
//                    int position = this.pointToPosition((int)currPosX, (int)currPosY);
//                    performItemClick(this, position, 0);
//                    return true;
//                }
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }
}
