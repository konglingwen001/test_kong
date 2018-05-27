package com.example.rmd2k.guitarstudio_android.MyZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.rmd2k.guitarstudio_android.DataModel.EditNoteInfo;
import com.example.rmd2k.guitarstudio_android.DataModel.Note;
import com.example.rmd2k.guitarstudio_android.DataModel.LineSize;
import com.example.rmd2k.guitarstudio_android.DataModel.NoteNoData;
import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.MyZone.GuitarNoteViewActivity.MyHandler;

import java.util.ArrayList;

/**
 * Created by CHT1HTSH3236 on 2018/3/20.
 */

public class GuitarNotesViewCell extends View implements View.OnClickListener {

    private static final String TAG = "GuitarNotesViewCell";

    private int lineNo;
    private MyHandler myHandler;
    private NotesModel notesModel;
    private Paint mPaint;

    public GuitarNotesViewCell(Context context) {
        super(context);
        notesModel = NotesModel.getInstance(context);
        mPaint = new Paint();
    }

    public GuitarNotesViewCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        notesModel = NotesModel.getInstance(context);
        mPaint = new Paint();
    }

    public void setLineNo(int lineNo) {
        if (this.lineNo == lineNo) {
            return;
        }
        this.lineNo = lineNo;
        invalidate(); //调用onDraw()刷新界面
    }

    public void setMyHandler(MyHandler myHandler) {
        this.myHandler = myHandler;
    }

    public void refresh() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawNoteLine(canvas, lineNo);
        drawNotes(canvas, lineNo);
        drawStem(canvas, lineNo);

        super.onDraw(canvas);
    }

    private void drawNoteLine(Canvas canvas, int lineNo) {

        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        float lineStart = notesModel.getLineWidth();
        float lineEnd = width - notesModel.getLineWidth();
        float barStartY = notesModel.getLineWidth();
        float offsetY = notesModel.getLineWidth();
        float[] points_stringLine = {lineStart, barStartY, lineEnd, barStartY,
                lineStart, barStartY + offsetY, lineEnd, barStartY + offsetY,
                lineStart, barStartY + offsetY * 2, lineEnd, barStartY + offsetY * 2,
                lineStart, barStartY + offsetY * 3, lineEnd, barStartY + offsetY * 3,
                lineStart, barStartY + offsetY * 4, lineEnd, barStartY + offsetY * 4,
                lineStart, barStartY + offsetY * 5, lineEnd, barStartY + offsetY * 5};
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(notesModel.dp2px(1));
        canvas.drawLines(points_stringLine, mPaint);

        float barLineX = 0;
        ArrayList<Float> barWidthArray = notesModel.getBarWidthArray(lineNo);
        float[] points_barLine = new float[barWidthArray.size() * 4];
        for (int i = 0; i < barWidthArray.size(); i++) {
            barLineX = barWidthArray.get(i) + notesModel.getLineWidth();
            points_barLine[i * 4] = barLineX;
            points_barLine[i * 4 + 1] = barStartY;
            points_barLine[i * 4 + 2] = barLineX;
            points_barLine[i * 4 + 3] = barStartY + offsetY * 5;
        }
        mPaint.setStrokeWidth(notesModel.dp2px(2));
        canvas.drawLines(points_barLine, mPaint);
    }

    private void drawNotes(Canvas canvas, int lineNo) {
        EditNoteInfo editNote = notesModel.getCurrentEditNote();
        int editBarNo = editNote.getBarNo();
        int editNoteNo = editNote.getNoteNo();
        int editStringNo = editNote.getStringNo();

        float barStartX = notesModel.getLineWidth();
        float barStartY = notesModel.getLineWidth();
        float offsetY = notesModel.getLineWidth();

        mPaint.setTextSize(notesModel.getNoteSize()); //提前设置字体，用于绘制音符时计算字体大小

        ArrayList<LineSize> lineSizeArray = notesModel.getNotesSizeArray();
        LineSize lineSize = lineSizeArray.get(lineNo);
        int lineBarNum = lineSize.getBarNum();
        ArrayList barWidthArr = lineSize.getBarWidthArray();

        // 绘制吉他音符
        int startBarNo = lineSize.getStartBarNo();
        float noteCenterX = 0, noteCenterY = 0; // 音符中心坐标
        int noteColor;
        for (int barNo = startBarNo; barNo < startBarNo + lineBarNum; barNo++) {

            noteColor = notesModel.checkBarStateAtBarNo(barNo) ? Color.BLACK : Color.RED;

            // 小节开始X坐标设置
            noteCenterX = barStartX + Float.parseFloat(barWidthArr.get(barNo - startBarNo).toString());

            ArrayList<NoteNoData> noteNoArray = notesModel.getNoteNoArray(barNo);
            for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
                ArrayList<Note> notes = noteNoArray.get(noteNo).getNoteArray();
                String noteType = notesModel.getNoteType(barNo, noteNo);

                // 设定音符X坐标
                noteCenterX += lineSize.getNoteWidth(noteType);

                // 根据触摸位置所在的音符位置绘制音符编辑框
                if (barNo == editBarNo && noteNo == editNoteNo) {
                    mPaint.setColor(Color.GREEN);
                    noteCenterY = barStartY + (editStringNo - 1) * offsetY + notesModel.dp2px(0.5f);
                    canvas.drawRect(new RectF(noteCenterX - notesModel.getNoteSize() / 2, noteCenterY- notesModel.getNoteSize() / 2, noteCenterX - notesModel.getNoteSize() / 2 + notesModel.getNoteSize(), noteCenterY- notesModel.getNoteSize() / 2 + notesModel.getNoteSize()), mPaint);
                }

                for (int i = 0; i < notes.size(); i++) {

                    Note note = notes.get(i);
                    int stringNo = Integer.parseInt(note.getStringNo());

                    String fretNo = note.getFretNo();
                    Rect fretNoSize = new Rect();
                    mPaint.getTextBounds(fretNo, 0, fretNo.length(), fretNoSize);

                    noteCenterY = barStartY + (stringNo - 1) * offsetY + notesModel.dp2px(0.5f);

                    // 没有被选中的音符绘制白色背景，避免横线贯穿音符
                    if (!(barNo == editBarNo && noteNo == editNoteNo && stringNo == editStringNo)) {
                        mPaint.setColor(Color.WHITE);
                        canvas.drawRect(new RectF(noteCenterX - fretNoSize.width() / 2, noteCenterY - fretNoSize.height() / 2, noteCenterX + fretNoSize.width() / 2, noteCenterY + fretNoSize.height() / 2), mPaint);
                    }

                    // 绘制音符(音符为空时不绘制)
                    if (stringNo != -1 && !fretNo.equals("-1")) {
                        mPaint.setColor(noteColor);
                        canvas.drawText(fretNo, noteCenterX - fretNoSize.width() / 2, noteCenterY + fretNoSize.height() / 2, mPaint);
                    }

                }
            }
        }
    }

    private void drawStem(Canvas canvas, int lineNo) {

        float flatTimeTotal = notesModel.getFlatTimeTotal();
        float flatTime = notesModel.getFlatTime();

        float height = this.getHeight();
        ArrayList<LineSize> lineSizeArray = notesModel.getNotesSizeArray();
        LineSize lineSize = lineSizeArray.get(lineNo);
        int lineBarNum = lineSize.getBarNum();
        ArrayList barWidthArr = lineSize.getBarWidthArray();

        // 绘制符干
        int startBarNo = lineSize.getStartBarNo();
        float barStartX = notesModel.getNoteSize(), barStartY = notesModel.getNoteSize();
        float noteCenterX = 0; // 音符中心坐标
        int stemColor;
        for (int barNo = startBarNo; barNo < startBarNo + lineBarNum; barNo++) {

            stemColor = notesModel.checkBarStateAtBarNo(barNo) ? Color.BLACK : Color.RED;
            mPaint.setColor(stemColor);

            // 小节开始X坐标设置
            noteCenterX = barStartX + Float.parseFloat(barWidthArr.get(barNo - startBarNo).toString());

            float currentNoteWidth = 0;
            float flatSum = 0;
            String preNoteType = "";
            ArrayList noteNoArray = notesModel.getNoteNoArray(barNo);
            for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
                String noteType = notesModel.getNoteType(barNo, noteNo);

                // 设定符干X坐标
                switch (noteType) {
                    case NotesModel.TYPE_MINIM:
                        if (preNoteType.equals(NotesModel.TYPE_QUAVER)) {
                            // 前一个音符为八分音符
                            // 绘制符干连接线
                            mPaint.setStrokeWidth(notesModel.dp2px(2));
                            canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX + notesModel.getNoteSize(), height - notesModel.getLineWidth(), mPaint);
                        } else if (preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                            // 前一个音符为十六分音符
                            // 绘制符干连接线
                            mPaint.setStrokeWidth(notesModel.dp2px(2));
                            canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX + notesModel.getNoteSize(), height - notesModel.getLineWidth(), mPaint);
                            canvas.drawLine(noteCenterX, height - notesModel.getLineWidth() * 1.5f, noteCenterX + notesModel.getNoteSize(), height - notesModel.getLineWidth() * 1.5f, mPaint);
                        }
                        flatSum += 0.5f;
                        noteCenterX += lineSize.getMinimWidth();
                        break;
                    case NotesModel.TYPE_CROTCHET:
                        if (preNoteType.equals(NotesModel.TYPE_QUAVER)) {
                            // 前一个音符为八分音符
                            // 绘制符干连接线
                            mPaint.setStrokeWidth(notesModel.dp2px(2));
                            canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX + notesModel.getNoteSize(), height - notesModel.getLineWidth(), mPaint);
                        } else if (preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                            // 前一个音符为十六分音符
                            // 绘制符干连接线
                            mPaint.setStrokeWidth(notesModel.dp2px(2));
                            canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX + notesModel.getNoteSize(), height - notesModel.getLineWidth(), mPaint);
                            canvas.drawLine(noteCenterX, height - notesModel.getLineWidth() * 1.5f, noteCenterX + notesModel.getNoteSize(), height - notesModel.getLineWidth() * 1.5f, mPaint);
                        }
                        flatSum += 0.25f;
                        noteCenterX += lineSize.getCrotchetaWidth();
                        break;
                    case NotesModel.TYPE_QUAVER:
                        flatSum += 0.125f;
                        currentNoteWidth = lineSize.getQuaverWidth();
                        noteCenterX += currentNoteWidth;

                        if (flatSum <= flatTime) {
                            // 判断前一个音符种类
                            if (preNoteType.equals("")) {
                                // 当前音符为音符的开始音符，保存
                                preNoteType = noteType;
                            } else {
                                if (preNoteType.equals(NotesModel.TYPE_QUAVER) || preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                                    // 前一个音符为八分音符
                                    // 绘制符干连接线
                                    mPaint.setStrokeWidth(notesModel.dp2px(2));
                                    canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX - currentNoteWidth, height - notesModel.getLineWidth(), mPaint);
                                }
                            }
                        }
                        break;
                    case NotesModel.TYPE_DEMIQUAVER:
                        flatSum += 0.0625f;
                        currentNoteWidth = lineSize.getDemiquaverWidth();
                        noteCenterX += currentNoteWidth;

                        if (flatSum <= flatTime) {
                            // 判断前一个音符种类
                            if (preNoteType.equals("")) {
                                // 当前音符为音符的开始音符，保存
                                preNoteType = noteType;
                            } else {
                                if (preNoteType.equals(NotesModel.TYPE_QUAVER)) {
                                    // 前一个音符为八分音符
                                    // 绘制符干连接线
                                    mPaint.setStrokeWidth(notesModel.dp2px(2));
                                    canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX - currentNoteWidth, height - notesModel.getLineWidth(), mPaint);
                                } else if (preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                                    mPaint.setStrokeWidth(notesModel.dp2px(2));
                                    canvas.drawLine(noteCenterX, height - notesModel.getLineWidth(), noteCenterX - currentNoteWidth, height - notesModel.getLineWidth(), mPaint);
                                    canvas.drawLine(noteCenterX, height - notesModel.getLineWidth() * 1.5f, noteCenterX - currentNoteWidth, height - notesModel.getLineWidth() * 1.5f, mPaint);
                                }
                            }
                        }
                        break;
                }

                // 绘制符干
                mPaint.setStrokeWidth(notesModel.dp2px(1));
                canvas.drawLine(noteCenterX, height - notesModel.getLineWidth() * 3, noteCenterX, height - notesModel.getLineWidth(), mPaint);

                preNoteType = noteType;
                if (flatSum == flatTime) {
                    flatSum = 0;
                    preNoteType = "";
                }

            }
        }
    }


    @Override
    public void onClick(View v) {
        System.out.println("GuitarNotesView clicked");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, System.currentTimeMillis() + " : action down");
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, System.currentTimeMillis() + " : action up");
                locateEditPos(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, System.currentTimeMillis() + " : action move");
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void locateEditPos(MotionEvent event) {
        float posX = event.getX();
        float posY = event.getY();

        int oldLineNo = 0;
        EditNoteInfo note = notesModel.getCurrentEditNote();
        ArrayList<LineSize> notesSizeArray = notesModel.getNotesSizeArray();
        for (int i = 0; i < notesSizeArray.size(); i++) {
            LineSize lineSize = notesSizeArray.get(i);
            if (note.getBarNo() >= lineSize.getStartBarNo() && note.getBarNo() < lineSize.getStartBarNo() + lineSize.getBarNum()) {
                oldLineNo = i;
                break;
            }
        }
        Message message = new Message();
        message.what = oldLineNo;
        myHandler.sendMessage(message);

        boolean result = calRect(posX, posY);
        if (result) {
            invalidate();
        }
    }

    private boolean calRect(float posX, float posY) {
        EditNoteInfo resultNote = new EditNoteInfo();
        float barStartX = notesModel.getLineWidth();
        float barStartY = notesModel.getLineWidth();

        if (isOutsideOfGuitarNotes(posX, posY)) {
            return false;
        }

        LineSize lineSize = notesModel.getLineSize(lineNo);
        int barNo = lineSize.getStartBarNo();
        ArrayList barWidthArray = lineSize.getBarWidthArray();

        // 判断触摸位置所属的小节，i从1开始，因为barWidthArray[0] == 0
        for (int i = 1; i < barWidthArray.size(); i++) {
            float width = Float.parseFloat(barWidthArray.get(i).toString());
            if (posX > notesModel.getLineWidth() + width) {
                barNo++;
                barStartX = notesModel.getLineWidth() + width;
            } else {
                break;
            }
        }

        int stringNo = (int)((posY - barStartY + notesModel.getNoteSize() / 2) / notesModel.getNoteSize()) + 1;
        resultNote.setStringNo(stringNo);
        float addPosX = barStartX;
        float currentWidth = 0;

        ArrayList noteNoArray = notesModel.getNoteNoArray(barNo);
        for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
            String noteType = notesModel.getNoteType(barNo, noteNo);
            currentWidth = lineSize.getNoteWidth(noteType);
            addPosX += currentWidth;

            if (posX < addPosX) {

                if (noteNo == 0) {
                    resultNote.setBarNo(barNo);
                    resultNote.setNoteNo(noteNo);
                } else if (posX > addPosX - currentWidth / 2) {
                    resultNote.setBarNo(barNo);
                    resultNote.setNoteNo(noteNo);
                } else {
                    resultNote.setBarNo(barNo);
                    resultNote.setNoteNo(noteNo - 1);
                }
                break;
            } else {
                if (noteNo == noteNoArray.size() - 1) {
                    resultNote.setBarNo(barNo);
                    resultNote.setNoteNo(noteNo);
                    break;
                }
            }
        }

        // 当音符编辑框位置没有改变时，不刷新吉他谱
        if (resultNote.equals(notesModel.getCurrentEditNote())) {
            return false;
        }

        // 更新音符编辑框位置，并刷新吉他谱
        notesModel.setCurrentEditPos(resultNote);
        return true;

    }

    private boolean isOutsideOfGuitarNotes(float posX, float posY) {

        // 判断X坐标是否在吉他谱内
        if (posX < notesModel.getLineWidth() || posX > this.getWidth() - notesModel.getLineWidth()) {
            return true;
        }

        // 判断Y坐标是否在吉他谱内
        if (posY < notesModel.getNoteSize() / 2) {
            return true;
        } else if (notesModel.getNoteSize() + notesModel.getNoteSize() * 5 + notesModel.getNoteSize() / 2 < posY) {
            return true;
        }

        return false;
    }
}
