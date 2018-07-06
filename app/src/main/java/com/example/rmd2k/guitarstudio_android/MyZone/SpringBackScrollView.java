package com.example.rmd2k.guitarstudio_android.MyZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.example.rmd2k.guitarstudio_android.R;

public class SpringBackScrollView extends HorizontalScrollView {


    private float mDownX;
    private float mFirstX;

    // 子View
    private View childView;

    // 初始的位置
    private Rect normal = new Rect();

    private Handler mHandler = new Handler();

    private int speed = 30;

    private boolean isPull;
    private boolean needSpring = false;

    private int leftMoveOffset;
    Button btnDelete;
    int btnWidth;

    /**
     * 设置回弹的速度。值越大,速度越快。默认为30。
     */
    public void setSpringBackSpeed(int speed) {
        if (speed <= 0) {
            throw new RuntimeException("speed 不能小于或者等于0");
        }
        this.speed = speed;
    }

    public SpringBackScrollView(Context context) {
        super(context);
        init();
    }

    public SpringBackScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 禁用下拉到两端发荧光的效果
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnDelete = findViewById(R.id.btnDelete);

        childView = getChildAt(0);
        if (childView != null) {
            normal.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (childView != null) {
            handleScrollTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void handleScrollTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                needSpring = false;
                mDownX = ev.getX();
                mFirstX = ev.getX();
                break;

            case MotionEvent.ACTION_UP:
                if (needSpring) {
                    springBackLocation();
                } else {
                    btnWidth = btnDelete.getWidth();
                    if (leftMoveOffset > btnWidth / 2) {
                        int offset = -(btnWidth - leftMoveOffset);
                        Log.e("KONG", "offset1:" + offset);
                        //scrollBy(-(btnWidth - leftMoveOffset), 0);

                        this.post(new Runnable() {
                            @Override
                            public void run() {
                                smoothScrollBy(-(btnWidth - leftMoveOffset), 0);
                            }
                        });

                    } else {
                        int offset = leftMoveOffset;
                        Log.e("KONG", "offset2:" + offset);
                        //scrollTo(leftMoveOffset, 0);
                        this.post(new Runnable() {
                            @Override
                            public void run() {
                                smoothScrollBy(leftMoveOffset, 0);
                            }
                        });

                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // 移除滑动的消息队列
                mHandler.removeCallbacksAndMessages(null);

                final float preX = mDownX;
                final float nowX = ev.getX();

                isPull = nowX - mFirstX > 0;

                int deltaX = (int) ((preX - nowX) / 2.5);

                mDownX = nowX;

                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    needSpring = true;
                    // 保存正常的布局位置
                    if (normal.isEmpty()) {
                        normal.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
                        return;
                    }
                    // 移动布局
                    childView.layout(childView.getLeft() - deltaX, childView.getTop(),
                            childView.getRight() - deltaX, childView.getBottom());
                }
                break;

            default:
                break;
        }

    }

    /**
     * 回弹到原始位置
     */
    public void springBackLocation() {
        final int nowRight = childView.getRight();
        final int nowLeft = childView.getLeft();
        final int originRight = normal.right;
        final int originLeft = normal.left;

        Log.i("nsz", "nowRight:" + nowRight + " nowLeft:" + nowLeft
                + " originRight:" + originRight + " originLeft:" + originLeft);

        // 下拉回弹
        if (isPull) {
            int moveRight = nowRight;
            int moveLeft = nowLeft;
            int duration = 0;

            while (moveRight >= originRight) {
                moveRight -= speed;
                moveLeft -= speed;
                duration += 10;
                final int offRight = moveRight;
                final int offLeft = moveLeft;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (offRight <= originRight || offLeft <= originLeft) {
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom);
                        } else {
                            childView.layout(offLeft, normal.top, offRight, normal.bottom);
                        }
                    }
                }, duration);
            }
        }

        // 上拉回弹
        else {
            int moveRight = nowRight;
            int moveLeft = nowLeft;
            int duration = 0;

            while (moveRight <= originRight) {
                moveRight += speed;
                moveLeft += speed;
                duration += 10;
                final int offRight = moveRight;
                final int offLeft = moveLeft;

                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (offRight >= originRight || offLeft >= originLeft) {
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom);
                        } else {
                            childView.layout(offLeft, normal.top, offRight, normal.bottom);
                        }
                    }
                }, duration);
            }
        }
    }

    /**
     * 是否需要移动布局
     */
    public boolean isNeedMove() {
        // 注意：慎重选择
        // 子View的margin和自己的padding对移动有影响,所以子View最好不要设置marginTop和marginBottom。
        // 如果设置了，对判断滑动到底部有些不准确，需要加上下面注释掉margin值，但是不同的机器，测试出有点不一样。

        // 获取到子View的margin值
        // LayoutParams params = (LayoutParams) childView.getLayoutParams();
        // int topMargin = params.topMargin;
        // int bottomMargin = params.bottomMargin;
        // int offset = childView.getHeight() - getHeight() + getPaddingBottom() + getPaddingTop() + topMargin + bottomMargin;
        int offset = childView.getWidth() - getWidth() + getPaddingStart() + getPaddingEnd();
        int scrollX = getScrollX();

        if (scrollX == 0) {
            return true;
        } else if (scrollX == offset) {
            return true;
        }

        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        leftMoveOffset = l;
        //Log.e("KONG", "l: " + l);
    }
}
