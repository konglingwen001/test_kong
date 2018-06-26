package com.example.rmd2k.guitarstudio_android.MyZone;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;

/**
 * Created by CHT1HTSH3236 on 2018/6/11.
 */

public class NoteNameListVeiwCell extends ConstraintLayout {

    private static final String TAG = "NoteNameListVeiwCell";

    private static final float BTN_DELETE_WIDTH = 100;
    private static final int REFRESH_LIST = 0;
    private static final int SET_CLICKABLE = 1;
    private static final int CLICKABLE = 0;

    private int tvNoteTitleWidth;
    private int btnDeleteWidth;
    private float offsetX;
    private float offsetY;
    private boolean isRightRestrainted = false;
    private boolean isBtnDeleteVisible = false;
    private float downPosX;
    private float downPosY;
    private float currPosX;
    private float currPosY;
    private boolean horizontalSlide = false;
    private int screenWidth;

    MyFragment.MyHandler myHandler;
    NotesModel notesModel;
    Button btnDelete;
    TextView tvNoteTitle;

    public NoteNameListVeiwCell(Context context) {
        super(context);
    }

    public NoteNameListVeiwCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        notesModel = NotesModel.getInstance(getContext());
    }


    int getScreenWidth() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnDelete = findViewById(R.id.btnDelete);
        tvNoteTitle = findViewById(R.id.tvNoteTitle);
        screenWidth = getScreenWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                horizontalSlide = false;
                downPosX = event.getX();
                downPosY = event.getY();
                tvNoteTitleWidth = tvNoteTitle.getWidth();
                btnDeleteWidth = btnDelete.getWidth();
                return true;
            case MotionEvent.ACTION_MOVE:
                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = currPosX - downPosX;
                offsetY = currPosY - downPosY;
                if (Math.abs(offsetX) > 5 || Math.abs(offsetY) > 5) {
                    // 当垂直或者水平移动大于5时，开始判断是上下滑动还是左右滑动，并且返回false，不拦截事件
                    if (Math.abs(offsetX) > Math.abs(offsetY)) {
                        // 左右滑动
                        horizontalSlide = true; // 锁定左右滑动
                    } else {
                        if (!horizontalSlide) {
                            // 上下滑动
                            return false;
                        }
                    }
                }

                if (horizontalSlide) {
                    LayoutParams lp = (LayoutParams) tvNoteTitle.getLayoutParams();
                    lp.width = (tvNoteTitleWidth + (int)offsetX) < (screenWidth - notesModel.dp2px(BTN_DELETE_WIDTH) * 2) ? (screenWidth - notesModel.dp2px(BTN_DELETE_WIDTH) * 2) : (tvNoteTitleWidth + (int)offsetX);
                    tvNoteTitle.setLayoutParams(lp);

                    if ((btnDelete.getX() + btnDeleteWidth) <= screenWidth && !isRightRestrainted) {
                        // 左滑时，btnDelete完全显示

                        // 切换btnDelete的宽度约束为MATCH_CONSTRAINT
                        LayoutParams btnLp = (LayoutParams) btnDelete.getLayoutParams();
                        btnLp.width = LayoutParams.MATCH_CONSTRAINT;
                        btnDelete.setLayoutParams(btnLp);

                        // 将btnDelete的右端约束设置与父控件右端对齐
                        ConstraintLayout constraintLayout = findViewById(R.id.noteTitleLayout);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(btnDelete.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                        constraintSet.applyTo(constraintLayout);
                        isRightRestrainted = true;
                    }
                    if (tvNoteTitle.getWidth() + BTN_DELETE_WIDTH >= screenWidth && isRightRestrainted) {
                        // 右滑时，btnDelete开始消失

                        // 切换btnDelete的宽度约束为定长200
                        LayoutParams btnLp = (LayoutParams) btnDelete.getLayoutParams();
                        btnLp.width = notesModel.dp2px(BTN_DELETE_WIDTH);
                        btnDelete.setLayoutParams(btnLp);

                        // 取消btnDelete的右端约束
                        ConstraintLayout constraintLayout = findViewById(R.id.noteTitleLayout);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(constraintLayout);
                        constraintSet.clear(btnDelete.getId(), ConstraintSet.RIGHT);
                        constraintSet.applyTo(constraintLayout);
                        isRightRestrainted = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                handleBounce(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void hideBtnDelete() {
        if (isBtnDeleteVisible) {
            bounceAnimate(false);
        }
    }

    private void handleBounce(MotionEvent event) {
        currPosX = event.getX();
        currPosY = event.getY();
        offsetX = currPosX - downPosX;

        if (tvNoteTitleWidth + offsetX < (screenWidth - BTN_DELETE_WIDTH / 2)) {
            // btnDelete露出部分大于一半时
            isBtnDeleteVisible = true;
            Message msg = Message.obtain();
            msg.what = SET_CLICKABLE;
            msg.arg1 = CLICKABLE;

        } else {
            // btnDelete露出部分小于一半时
            isBtnDeleteVisible = false;
        }

        bounceAnimate(isBtnDeleteVisible);
    }

    private void bounceAnimate(boolean visible) {

        int newWidth;
        if (visible) {
            newWidth = screenWidth - notesModel.dp2px(BTN_DELETE_WIDTH);
        } else {
            newWidth = screenWidth;
        }

        // 动画显示benDelete长度的变化
        ValueAnimator animator = ValueAnimator.ofInt(tvNoteTitle.getLayoutParams().width, newWidth);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currVal = (int) animation.getAnimatedValue();
                tvNoteTitle.getLayoutParams().width = currVal;
                tvNoteTitle.requestLayout();
            }
        });
        animator.start();
    }
}
