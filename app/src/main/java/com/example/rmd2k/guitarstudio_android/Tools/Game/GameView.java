package com.example.rmd2k.guitarstudio_android.Tools.Game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;


public class GameView extends View {

    Context mContext;

    int screen_width;       // 屏幕宽度
    int screen_height;      // 屏幕高度
    int refreshCount = 0;   // 刷新次数
    int maxBarIndex;        // 最右边的barLine号
    int barLineNum = 0;     // barLine总数

    NotesModel notesModel;

    double flatTime;        //
    int speed;              // 每分钟节拍数
    int flatNumInOneBar;    // 一个小节中的节拍数
    int barLength;          // 小节长度
    int lengthPerRefresh;   // 每次刷新偏移的距离

    float barStartY;        // 六线谱开始Y坐标
    float offsetY;          // 六线谱间隔

    float[] points_stringLine = new float[24];
    float[] points_barLine;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }

    private void init(Context context) {
        mContext = context;
        notesModel = NotesModel.getInstance(mContext);
        notesModel.getGuitarNotesFromGuitarNotesTitle("天空之城");

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;


        flatTime = notesModel.getFlatTime();
        speed = notesModel.getSpeed();
        flatNumInOneBar = notesModel.getFlatNumInOneBar();


        double timePerFlat = 60.0 / speed;
        double refreshFrequency = 1.0 / 60;
        double timePerBar = timePerFlat * flatNumInOneBar;
        int refreshTimes = (int)(timePerBar / refreshFrequency);

        barLength = 800;
        lengthPerRefresh = barLength / refreshTimes;


        barLineNum = screen_width / barLength + 1;
        maxBarIndex = barLineNum - 1;

        barStartY = notesModel.getLineWidth();
        offsetY = (int)(notesModel.getLineWidth() * 1.5);

        for (int i = 0; i < 6; i++) {
            points_stringLine[    i * 4] = 0;
            points_stringLine[1 + i * 4] = barStartY + offsetY * i;
            points_stringLine[2 + i * 4] = screen_width;
            points_stringLine[3 + i * 4] = barStartY + offsetY * i;
        }

        points_barLine = new float[barLineNum * 2 * 4];
        for (int i = 0; i < barLineNum; i++) {
            points_barLine[    4 * i] = screen_width + barLength * i;
            points_barLine[1 + 4 * i] = barStartY;
            points_barLine[2 + 4 * i] = screen_width + barLength * i;
            points_barLine[3 + 4 * i] = barStartY + offsetY * 5;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mPaint = new Paint();

        drawNoteLine(canvas, mPaint);
        drawNotes(canvas, mPaint);

        refreshCount++;

    }

    private void drawNotes(Canvas canvas, Paint mPaint) {

    }

    private void drawNoteLine(Canvas canvas, Paint mPaint) {

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(notesModel.dp2px(1));
        canvas.drawLines(points_stringLine, mPaint);

        for (int i = 0; i < barLineNum; i++) {
            points_barLine[    4 * i] -= lengthPerRefresh;
            points_barLine[2 + 4 * i] -= lengthPerRefresh;
            if (points_barLine[    4 * i] < 0) {
                points_barLine[    4 * i] = points_barLine[    4 * maxBarIndex] + barLength;
                points_barLine[2 + 4 * i] = points_barLine[2 + 4 * maxBarIndex] + barLength;
                maxBarIndex = i;
            }
        }
        mPaint.setStrokeWidth(notesModel.dp2px(2));
        for (int i = 0; i < barLineNum; i++) {
            if (points_barLine[4 * i] >= 0 && points_barLine[4 * i] <= screen_width) {
                canvas.drawLine(points_barLine[4 * i], points_barLine[1 + 4 * i], points_barLine[2 + 4 * i], points_barLine[3 + 4 * i], mPaint);
            }
        }
    }
}
