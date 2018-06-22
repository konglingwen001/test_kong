package com.example.rmd2k.guitarstudio_android.MyZone;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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

    private int tvNoteTitleWidth;
    private int btnDeleteWidth;
    private float offsetX;
    private boolean isRightRestrainted = false;
    private float downPosX;
    private float downPosY;
    private float currPosX;
    private float currPosY;
    private boolean horizontalSlide = false;
    private int screenWidth;

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
                //Log.d(TAG, System.currentTimeMillis() + " : action down1");
                return true;
            case MotionEvent.ACTION_MOVE:
                //Log.d(TAG, System.currentTimeMillis() + " : action move1");
                currPosX = event.getX();
                currPosY = event.getY();
                offsetX = currPosX - downPosX;
                if (Math.abs(offsetX) > 5) {
                    horizontalSlide = true;
                }
                if (Math.abs(currPosY - downPosY) > 10 && !horizontalSlide) {
                    return false;
                }

                if (horizontalSlide) {
                    LayoutParams lp = (LayoutParams) tvNoteTitle.getLayoutParams();
                    lp.width = (tvNoteTitleWidth + (int)offsetX) < (screenWidth - notesModel.dp2px(BTN_DELETE_WIDTH) * 2) ? (screenWidth - notesModel.dp2px(BTN_DELETE_WIDTH) * 2) : (tvNoteTitleWidth + (int)offsetX);
                    tvNoteTitle.setLayoutParams(lp);
                    //Log.i(TAG, "width:" + (tvNoteTitleWidth + (int)offsetX));

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
                //Log.d(TAG, System.currentTimeMillis() + " : action up1");
                handleBounce(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.d(TAG, System.currentTimeMillis() + " : action cancel1");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleBounce(MotionEvent event) {
        currPosX = event.getX();
        currPosY = event.getY();
        offsetX = currPosX - downPosX;

        int newWidth;
        if (tvNoteTitleWidth + offsetX < (screenWidth - BTN_DELETE_WIDTH / 2)) {
            // btnDelete露出部分大于一半时
            newWidth = screenWidth - notesModel.dp2px(BTN_DELETE_WIDTH);
        } else {
            // btnDelete露出部分小于一半时
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
