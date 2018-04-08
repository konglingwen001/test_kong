package com.example.rmd2k.guitarstudio_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CHT1HTSH3236 on 2018/3/20.
 */

public class GuitarNotesView extends View implements View.OnClickListener {

    public int lineNo;
    private NotesModel notesModel;

    public GuitarNotesView(Context context) {
        super(context);
        notesModel = NotesModel.getInstance(context);
    }

    public GuitarNotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        notesModel = NotesModel.getInstance(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        System.out.println("lingNo = " + lineNo);

        Paint mPaint = new Paint();
        drawNoteLine(canvas, mPaint, this.lineNo);
        drawNotes(canvas, mPaint, lineNo);
        //drawStem(canvas, mPaint, lineNo);

        super.onDraw(canvas);
    }

    private void drawNoteLine(Canvas canvas, Paint mPaint, int lineNo) {

        float width = getWidth();
        float lineStart = NotesModel.LINE_WIDTH;
        float lineEnd = width - NotesModel.LINE_WIDTH;
        float barStartY = NotesModel.LINE_WIDTH;
        float offsetY = NotesModel.LINE_WIDTH;
        float[] points_stringLine = {lineStart, barStartY, lineEnd, barStartY,
                lineStart, barStartY + offsetY, lineEnd, barStartY + offsetY,
                lineStart, barStartY + offsetY * 2, lineEnd, barStartY + offsetY * 2,
                lineStart, barStartY + offsetY * 3, lineEnd, barStartY + offsetY * 3,
                lineStart, barStartY + offsetY * 4, lineEnd, barStartY + offsetY * 4,
                lineStart, barStartY + offsetY * 5, lineEnd, barStartY + offsetY * 5};
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        canvas.drawLines(points_stringLine, mPaint);

        float barLineX = NotesModel.LINE_WIDTH;
        ArrayList<Float> barWidthArray = (ArrayList)notesModel.getNotesSizeArray().get(lineNo).get(NotesModel.KEY_SIZEDIC_BAR_WIDTH_ARRAY);
        float[] points_barLine = new float[(barWidthArray.size() + 1) * 4];
        points_barLine[0] = barLineX;
        points_barLine[1] = barStartY;
        points_barLine[2] = barLineX;
        points_barLine[3] = barStartY + offsetY * 5;
        for (int i = 0; i < barWidthArray.size() * 4; i += 4) {
            barLineX += barWidthArray.get(i / 4);
            points_barLine[i + 4] = barLineX;
            points_barLine[i + 5] = barStartY;
            points_barLine[i + 6] = barLineX;
            points_barLine[i + 7] = barStartY + offsetY * 5;
        }
        mPaint.setStrokeWidth(2);
        canvas.drawLines(points_barLine, mPaint);
    }

    private void drawNotes(Canvas canvas, Paint mPaint, int lineNo) {
        Map<String, Integer> editNotePos = notesModel.getCurrentEditPos();
        int editBarNo = editNotePos.get(NotesModel.KEY_CURR_BARNO);
        int editNoteNo = editNotePos.get(NotesModel.KEY_CURR_NOTENO);
        int editStringNo = editNotePos.get(NotesModel.KEY_CURR_STRINGNO);

        float barStartX = NotesModel.LINE_WIDTH;
        float barStartY = NotesModel.LINE_WIDTH;
        float offsetY = NotesModel.LINE_WIDTH;


        ArrayList<Map> noteSizeArray = notesModel.getNotesSizeArray();
        Map noteSizeDic = noteSizeArray.get(lineNo);
        int lineBarNum = Integer.parseInt(noteSizeDic.get(NotesModel.KEY_SIZEDIC_BAR_NUM).toString());
        ArrayList barWidthArr = (ArrayList)noteSizeDic.get(NotesModel.KEY_SIZEDIC_BAR_WIDTH_ARRAY);

        // 绘制吉他音符
        int startBarNo = Integer.parseInt(noteSizeDic.get(NotesModel.KEY_SIZEDIC_START_BARNO).toString());
        float noteCenterX = 0, noteCenterY = 0; // 音符中心坐标
        for (int barNo = startBarNo; barNo < startBarNo + lineBarNum; barNo++) {

            // 小节开始X坐标设置
            noteCenterX = barStartX + Float.parseFloat(barWidthArr.get(barNo - startBarNo).toString());

            ArrayList noteNoArray = notesModel.getNoteNoArray(barNo);
            for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
                ArrayList notes = (ArrayList)((Map)noteNoArray.get(noteNo)).get(NotesModel.KEY_NOTE_ARRAY);
                String noteType = notesModel.getNoteType(barNo, noteNo);

                // 设定音符X坐标
                switch (noteType) {
                    case NotesModel.TYPE_MINIM:
                        noteCenterX += Double.parseDouble(noteSizeDic.get(NotesModel.KEY_SIZEDIC_MINIM_WIDTH).toString());
                        break;
                    case NotesModel.TYPE_CROTCHET:
                        noteCenterX += Double.parseDouble(noteSizeDic.get(NotesModel.KEY_SIZEDIC_CROTCHETA_WIDTH).toString());
                        break;
                    case NotesModel.TYPE_QUAVER:
                        noteCenterX += Double.parseDouble(noteSizeDic.get(NotesModel.KEY_SIZEDIC_QUAVER_WIDTH).toString());
                        break;
                    case NotesModel.TYPE_DEMIQUAVER:
                        noteCenterX += Double.parseDouble(noteSizeDic.get(NotesModel.KEY_SIZEDIC_DEMIQUAVER_WIDTH).toString());
                        break;
                }

                // 根据触摸位置所在的音符位置绘制音符编辑框
                if (barNo == editBarNo && noteNo == editNoteNo) {
                    mPaint.setColor(Color.GREEN);
                    noteCenterY = barStartY + (editStringNo - 1) * offsetY + 0.5f;
                    canvas.drawRect(new RectF(noteCenterX - NotesModel.NOTE_SIZE / 2, noteCenterY- NotesModel.NOTE_SIZE / 2, noteCenterX - NotesModel.NOTE_SIZE / 2 + NotesModel.NOTE_SIZE, noteCenterY- NotesModel.NOTE_SIZE / 2 + NotesModel.NOTE_SIZE), mPaint);
                }

                for (int i = 0; i < notes.size(); i++) {

                    Map note = (Map)notes.get(i);
                    int stringNo = Integer.parseInt(note.get(NotesModel.KEY_STRING_NO).toString());

                    String fretNo = note.get(NotesModel.KEY_FRET_NO).toString();
                    Rect fretNoSize = new Rect();
                    mPaint.getTextBounds(fretNo, 0, fretNo.length(), fretNoSize);

                    noteCenterY = barStartY + (stringNo - 1) * offsetY + 0.5f;

                    // 没有被选中的音符绘制白色背景，避免横线贯穿音符
                    if (!(barNo == editBarNo && noteNo == editNoteNo && stringNo == editStringNo)) {
                        mPaint.setColor(Color.GREEN);
                        canvas.drawRect(new RectF(noteCenterX - fretNoSize.width() / 2, noteCenterY - fretNoSize.height() / 2, noteCenterX + fretNoSize.width() / 2, noteCenterY + fretNoSize.height() / 2), mPaint);
                    }

                    // 绘制音符(音符为空时不绘制)
                    if (stringNo != -1 && !fretNo.equals("-1")) {
                        mPaint.setColor(Color.BLACK);
                        mPaint.setTextSize(50);
                        canvas.drawText(fretNo, noteCenterX - fretNoSize.width() / 2, noteCenterY - fretNoSize.height() / 2, mPaint);
                    }

                }
            }
        }
    }

    private void drawStem(Canvas canvas, Paint mPaint, int lineNo) {
        float flatTimeTotal = 0;
        String flat = notesModel.getRootNoteDic().get(NotesModel.KEY_ROOT_FLAT).toString();
        int flatNum = Integer.parseInt(flat.split("/")[0]);
        String totalNoteType = flat.split("/")[1];
        switch (totalNoteType) {
            case NotesModel.TYPE_MINIM:
                flatTimeTotal = 0.5f;
                break;
            case NotesModel.TYPE_CROTCHET:
                flatTimeTotal = 0.25f;
                break;
            case NotesModel.TYPE_QUAVER:
                flatTimeTotal = 0.125f;
                break;
            case NotesModel.TYPE_DEMIQUAVER:
                flatTimeTotal = 0.0625f;
                break;
        }

        ArrayList noteSizeArray = notesModel.getNotesSizeArray();
        Map noteSizeDic = (Map) noteSizeArray.get(lineNo);
        int lineBarNum = Integer.parseInt(noteSizeDic.get(NotesModel.KEY_SIZEDIC_BAR_NUM).toString());
        ArrayList barWidthArr = (ArrayList) noteSizeDic.get(NotesModel.KEY_SIZEDIC_BAR_WIDTH_ARRAY);

        // 绘制符干
        int startBarNo = Integer.parseInt(noteSizeDic.get(NotesModel.KEY_SIZEDIC_START_BARNO).toString());
        float barStartX = NotesModel.NOTE_SIZE, barStartY = NotesModel.NOTE_SIZE;
        float noteCenterX = 0; // 音符中心坐标
        for (int barNo = startBarNo; barNo < startBarNo + lineBarNum; barNo++) {
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
                        flatSum += 0.5f;
                        noteCenterX += Float.parseFloat(noteSizeDic.get(NotesModel.KEY_SIZEDIC_MINIM_WIDTH).toString());
                        break;
                    case NotesModel.TYPE_CROTCHET:
                        flatSum += 0.25f;
                        noteCenterX += Float.parseFloat(noteSizeDic.get(NotesModel.KEY_SIZEDIC_CROTCHETA_WIDTH).toString());
                        break;
                    case NotesModel.TYPE_QUAVER:
                        flatSum += 0.125f;
                        currentNoteWidth = Float.parseFloat(noteSizeDic.get(NotesModel.KEY_SIZEDIC_QUAVER_WIDTH).toString());
                        noteCenterX += currentNoteWidth;

                        // 判断前一个音符种类
                        if (preNoteType.equals("")) {
                            // 当前音符为音符的开始音符，保存
                            preNoteType = noteType;
                        } else {
                            if (preNoteType.equals(NotesModel.TYPE_QUAVER) || preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                                // 前一个音符为八分音符
                                // 绘制符干连接线
                                mPaint.setStrokeWidth(2);
                                canvas.drawLine(noteCenterX, 150 - 15, noteCenterX - currentNoteWidth, 150 - 15, mPaint);
                            }
                        }
                        break;
                    case NotesModel.TYPE_DEMIQUAVER:
                        flatSum += 0.0625f;
                        currentNoteWidth = Float.parseFloat(noteSizeDic.get(NotesModel.KEY_SIZEDIC_DEMIQUAVER_WIDTH).toString());
                        noteCenterX += currentNoteWidth;

                        // 判断前一个音符种类
                        if (preNoteType.equals("")) {
                            // 当前音符为音符的开始音符，保存
                            preNoteType = noteType;
                        } else {
                            if (preNoteType.equals(NotesModel.TYPE_QUAVER)) {
                                // 前一个音符为八分音符
                                // 绘制符干连接线
                                mPaint.setStrokeWidth(2);
                                canvas.drawLine(noteCenterX, 150 - 15, noteCenterX - currentNoteWidth, 150 - 15, mPaint);
                            } else if (preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                                mPaint.setStrokeWidth(2);
                                canvas.drawLine(noteCenterX, 150 - 15, noteCenterX - currentNoteWidth, 150 - 15, mPaint);
                                canvas.drawLine(noteCenterX, 150 - 20, noteCenterX - currentNoteWidth, 150 - 20, mPaint);
                            }
                        }
                        break;
                }

                // 绘制符干
                mPaint.setStrokeWidth(1);
                canvas.drawLine(noteCenterX, barStartY + 90, noteCenterX, 150 - 15, mPaint);

                preNoteType = noteType;
                if (flatSum == flatTimeTotal) {
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
        System.out.println("touched");
        return super.onTouchEvent(event);
    }
}
