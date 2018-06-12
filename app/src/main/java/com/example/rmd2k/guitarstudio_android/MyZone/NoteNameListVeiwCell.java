package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by CHT1HTSH3236 on 2018/6/11.
 */

public class NoteNameListVeiwCell extends ConstraintLayout {

    private static final String TAG = "NoteNameListVeiwCell";

    public NoteNameListVeiwCell(Context context) {
        super(context);
    }

    public NoteNameListVeiwCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

//    private float downPosX;
//    private float downPosY;
//    private float currPosX;
//    private float currPosY;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downPosX = event.getX();
//                downPosY = event.getY();
//                Log.d(TAG, System.currentTimeMillis() + " : action move");
//                return true;
//            case MotionEvent.ACTION_UP:
//                Log.d(TAG, System.currentTimeMillis() + " : action up");
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                currPosX = event.getX();
//                currPosY = event.getY();
//                Log.d(TAG, System.currentTimeMillis() + " : action move");
//                return true;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG, System.currentTimeMillis() + " : action cancel");
//                return true;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}
