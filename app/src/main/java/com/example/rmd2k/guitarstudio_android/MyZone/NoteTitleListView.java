package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

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

    private boolean checkBtnDeleteVisible() {
        int count = getCount();
        NoteNameListVeiwCell cell;
        for (int i = 0; i < count; i++) {
            cell = (NoteNameListVeiwCell) getChildAt(i);
            if (cell.isBtnDeleteVisible()) {
                return true;
            }
        }
        return false;
    }

    /*
        1.
     */

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e("KONG", "ListView action down");
//                if (myFragment_Handler != null && checkBtnDeleteVisible()) {
//                    Log.e("KONG", "myHandler");
//                    Message msg = Message.obtain();
//                    msg.what = REFRESH_LIST;
//                    myFragment_Handler.sendMessage(msg);
//                    return true;
//                }
//                horizontalSlide = false;
//                downPosX = event.getX();
//                downPosY = event.getY();
//                //break;
//                return false;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("KONG", "ListView action move");
//                if (horizontalSlide) {
//                    return false;
//                }
//
//                currPosX = event.getX();
//                currPosY = event.getY();
//                offsetX = Math.abs(currPosX - downPosX);
//                offsetY = Math.abs(currPosY - downPosY);
//                if (offsetX > 5 || offsetY > 5) {
//                    // 当垂直或者水平移动大于5时，开始判断是上下滑动还是左右滑动，并且返回false，不拦截事件
//                    if (offsetX > offsetY) {
//                        // 左右滑动
//                        horizontalSlide = true; // 锁定左右滑动
//                        return false;
//                    } else {
//                        if (!horizontalSlide) {
//                            // 上下滑动
//                            return true;
//                        } else {
//                            // 左右滑动
//                            return false;
//                        }
//                    }
//                }
//                //break;
//                return false;
//            case MotionEvent.ACTION_UP:
//                Log.e("KONG", "ListView action up");
////                currPosX = event.getX();
////                currPosY = event.getY();
////                offsetX = Math.abs(currPosX - downPosX);
////                offsetY = Math.abs(currPosY - downPosY);
////
////                if (offsetX < 5 && offsetY < 5) {
////                    Log.e("KONG", "Clicked");
////                    int position = this.pointToPosition((int)currPosX, (int)currPosY);
////                    //performItemClick(this, position, 0);
////                    return true;
////                }
//                //break;
//                return false;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return super.onInterceptTouchEvent(event);
//    }
//
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
