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
    private boolean isIntercepted = false;

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
//        if (isIntercepted) {
//            return true;
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN");
                downPosX = event.getX();
                downPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "ACTION_MOVE");
                currPosX = event.getX();
                currPosY = event.getY();
                if (Math.abs(currPosX - downPosX) > 10) {
                    //btnDelete.setFocusable(true);
                }
                if (Math.abs(currPosY - downPosY) < 200) {
                    //Log.i(TAG, "move200");
                    return false;
                } else {
                    isIntercepted = true;
                    return true;
                }
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                isIntercepted = false;
                currPosX = event.getX();
                currPosY = event.getY();
                int position = 0;
                if (Math.abs(currPosX - downPosX) < 5 && Math.abs(currPosY - downPosY) < 5) {
                    position = this.pointToPosition((int)currPosX, (int)currPosY);
                    performItemClick(this, position, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL");
                isIntercepted = false;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
