package com.example.rmd2k.guitarstudio_android.Tools.Game;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.rmd2k.guitarstudio_android.DataModel.GuitarNotes;
import com.example.rmd2k.guitarstudio_android.DataModel.Note;
import com.example.rmd2k.guitarstudio_android.DataModel.NoteNoData;
import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.Utils.NotePlayUtils;

import java.util.ArrayList;


public class GameView extends RelativeLayout {

    private static final int NOTE_VIEW_NUM = 50;

    Context mContext;
    NotePlayUtils notePlayUtils;

    int screen_width;               // 屏幕宽度
    int screen_height;              // 屏幕高度
    int refreshCount = 0;           // 刷新次数
    int maxBarIndex;                // 最右边的barLine号
    int barLineNum = 0;             // barLine总数

    NotesModel notesModel;
    GuitarNotes rootDir;

    boolean isInitialized = false;

    int speed;                      // 每分钟节拍数
    int flatNumInOneBar;            // 一个小节中的节拍数
    int lengthPerRefresh;           // 每次刷新偏移的距离
    int totalRefreshLength = 0;     // 总移动距离

    int barWidth;                   // 小节长度
    int flatWidth;                  // 节拍长度
    int minimWidth;                 // 二分音符长度
    int crotchetaWidth;             // 四分音符长度
    int quaverWidth;                // 八分音符长度
    int demiQuaverWidth;            // 十六分音符长度

    int noteStartX = 0;             // 音符开始X坐标

    ArrayList<NoteView> noteViews;
    int lastNoteNo = -1;
    int lastBarNo = -1;

    int barStartY;                  // 六线谱开始Y坐标
    int offsetY;                    // 六线谱间隔

    float[] points_stringLine = new float[24];
    int[] points_barLine;

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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!isInitialized) {
            isInitialized = true;
            int height = getHeight();
            Log.e("AAA", "onLayout height:" + height);

            barStartY = notesModel.getLineWidth();
            offsetY = height / 7;

            for (int i = 0; i < 6; i++) {
                points_stringLine[i * 4] = 0;
                points_stringLine[1 + i * 4] = barStartY + offsetY * (i + 1);
                points_stringLine[2 + i * 4] = screen_width;
                points_stringLine[3 + i * 4] = barStartY + offsetY * (i + 1);
            }

            points_barLine = new int[barLineNum * 2 * 4];
            for (int i = 0; i < barLineNum; i++) {
                points_barLine[4 * i] = screen_width + barWidth * i;
                points_barLine[1 + 4 * i] = barStartY + offsetY;
                points_barLine[2 + 4 * i] = screen_width + barWidth * i;
                points_barLine[3 + 4 * i] = barStartY + offsetY * 6;
            }
        }
    }

    private void init(Context context) {

        Log.e("AAA", "init");
        notePlayUtils = NotePlayUtils.getInstance(context);

        setWillNotDraw(false);

        mContext = context;
        notesModel = NotesModel.getInstance(mContext);
        notesModel.getGuitarNotesFromGuitarNotesTitle("天空之城");
        rootDir = notesModel.getRootNoteDic();

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels;

        speed = notesModel.getSpeed();
        flatNumInOneBar = notesModel.getFlatNumInOneBar();


        double timePerFlat = 60.0 / speed;
        double refreshFrequency = 1.0 / 60;
        double timePerBar = timePerFlat * flatNumInOneBar;
        int refreshTimes = (int)(timePerBar / refreshFrequency);

        barWidth = 1600;
        flatWidth = barWidth / flatNumInOneBar;
        minimWidth = getNoteWidth(NotesModel.TYPE_MINIM);
        crotchetaWidth = getNoteWidth(NotesModel.TYPE_CROTCHET);
        quaverWidth = getNoteWidth(NotesModel.TYPE_QUAVER);
        demiQuaverWidth = getNoteWidth(NotesModel.TYPE_DEMIQUAVER);
        lengthPerRefresh = barWidth / refreshTimes;

        noteViews = new ArrayList<>();

        barLineNum = screen_width / barWidth + 1;
        maxBarIndex = barLineNum - 1;

        initNoteViews();
    }

    private void initNoteViews() {
        for (int i = 0; i < NOTE_VIEW_NUM; i++) {
            NoteView noteView = new NoteView(mContext);
            noteView.isAttachedToNote = false;
            noteView.isVisible = false;
            noteView.isAddedToView = false;
            noteView.setBackgroundColor(getThemeBackgroundColor());
            noteViews.add(noteView);
        }
    }

    private int getNoteWidth(String type) {
        int noteWidth;
        String totalNoteType = notesModel.getTotalNoteType();
        noteWidth = (int)(flatWidth * Double.parseDouble(totalNoteType) / Double.parseDouble(type));
        return noteWidth;

    }

    private int getThemeBackgroundColor() {
        TypedArray array = mContext.getTheme().obtainStyledAttributes(new int[] {
                android.R.attr.colorBackground,
                android.R.attr.textColorPrimary,
        });
        int backgroundColor = array.getColor(0, 0xFF00FF);
        return backgroundColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        totalRefreshLength += lengthPerRefresh;

        Paint mPaint = new Paint();

        drawNoteLine(canvas, mPaint);
        drawNotes(canvas, mPaint);

        refreshCount++;

    }

    int currNoteOffset = 0;
    int currNoteNo = 0;
    NoteNoData noteNoData;
    private void drawNotes(Canvas canvas, Paint mPaint) {
        int currBarNo = totalRefreshLength / barWidth;
        int offset_barlength = totalRefreshLength % barWidth;

        if (currBarNo > lastBarNo) {
            // barNo变更
            lastBarNo = currBarNo;

            // 添加第一个note
            noteNoData = notesModel.getNoteNoData(currBarNo, 0);
            currNoteOffset = getCurrNoteOffset(noteNoData.getNoteType());
            addNotes(currBarNo, 0);

            lastNoteNo = 0;
            currNoteNo = 1;
        } else if (offset_barlength > currNoteOffset) {
            // 判断当前移动的距离是否经过新的note
            noteNoData = notesModel.getNoteNoData(currBarNo, currNoteNo);
            currNoteOffset += getCurrNoteOffset(noteNoData.getNoteType());

            addNotes(currBarNo, currNoteNo);

            // noteNo变更
            lastNoteNo = currNoteNo;
            currNoteNo ++;
        }

        // 显示绑定的note
        int left;   // 距离左端的距离
        int top;    // 距离上端的距离
        for (int i = 0; i < noteViews.size(); i++) {
            NoteView noteView = noteViews.get(i);
            if (noteView.isAttachedToNote && !noteView.isAddedToView) {
                // 当view数组中的view已经与note绑定，并且没有加入到parentView中，加入view，并将isVisible设为true
                addView(noteView);
                noteView.isAddedToView = true;
                noteView.isVisible = true;
            }
            if (noteView.isVisible) {

                LayoutParams lp = (LayoutParams) noteView.getLayoutParams();
                noteView.startX -= lengthPerRefresh;
                if (noteView.startX + noteView.width < 0) {
                    // 当noteView完全消失时，删除View
                    noteView.isAttachedToNote = false;
                    noteView.isVisible = false;
                    continue;
                }
                left = noteView.startX;

                // 音符为休止符时，不描画
                if (noteView.stringNo == -1) {
                    top = 0;
                } else {
                    top = (int) (points_stringLine[1 + (noteView.stringNo - 1) * 4] - noteView.height / 2);
                }

                lp.addRule(ALIGN_PARENT_LEFT);
                lp.leftMargin = left;
                lp.addRule(ALIGN_PARENT_TOP);
                lp.topMargin = top;
                lp.width = noteView.width;
                lp.height = noteView.height;
                noteView.setLayoutParams(lp);

            }
        }
    }

    private void addNotes(int currBarNo, int currNoteNo) {
        NoteNoData currNoteNoData = notesModel.getNoteNoData(currBarNo, currNoteNo);
        ArrayList<Note> notes = notesModel.getNotesArray(currBarNo, currNoteNo);
        for (Note note : notes) {
            boolean isAttached = false;
            for (NoteView noteView : noteViews) {
                if (!noteView.isAttachedToNote && noteView.isAddedToView) {
                    // view没有绑定note并且已经加入界面(也就是显示已经加入过界面但是超过显示区域不显示的view，不用重新加入界面)
                    attachViewToNote(noteView, currNoteNoData, note);
                    isAttached = true;
                    break;
                }
            }
            if (!isAttached) {
                for (NoteView noteView : noteViews) {
                    if (!noteView.isAttachedToNote && !noteView.isAddedToView) {
                        // view没有绑定note并且没有加入界面
                        attachViewToNote(noteView, currNoteNoData, note);
                        break;
                    }
                }
            }
        }
    }

    private void attachViewToNote(NoteView noteView, NoteNoData currNoteNoData, Note note) {

        int stringNo = Integer.parseInt(note.getStringNo());
        int fretNo = Integer.parseInt(note.getFretNo());
        if (stringNo > 0) {
            //notePlayUtils.playNote(stringNo, fretNo);
        } else {
            //notePlayUtils.stopPlay();
        }

        noteView.isAttachedToNote = true;
        noteView.isVisible = true;
        noteView.noteType = currNoteNoData.getNoteType();
        noteView.fretNo = note.getFretNo();
        noteView.stringNo = Integer.parseInt(note.getStringNo());

        noteView.width = getCurrNoteOffset(currNoteNoData.getNoteType());
        if (stringNo == -1) {
            noteView.height = 0;
        } else {
            noteView.height = offsetY;
        }
        noteView.startX = screen_width;

    }

    private int getCurrNoteOffset(String type) {
        int offset = 0;
        switch (type) {
            case NotesModel.TYPE_MINIM:
                offset = minimWidth;
                break;
            case NotesModel.TYPE_CROTCHET:
                offset = crotchetaWidth;
                break;
            case NotesModel.TYPE_QUAVER:
                offset = quaverWidth;
                break;
            case NotesModel.TYPE_DEMIQUAVER:
                offset = demiQuaverWidth;
                break;
        }
        return offset;
    }

    private void drawNoteLine(Canvas canvas, Paint mPaint) {

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(notesModel.dp2px(1));
        canvas.drawLines(points_stringLine, mPaint);

        for (int i = 0; i < barLineNum; i++) {
            points_barLine[    4 * i] -= lengthPerRefresh;
            points_barLine[2 + 4 * i] -= lengthPerRefresh;
            if (points_barLine[    4 * i] < 0) {
                points_barLine[    4 * i] = points_barLine[    4 * maxBarIndex] + barWidth;
                points_barLine[2 + 4 * i] = points_barLine[2 + 4 * maxBarIndex] + barWidth;
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
