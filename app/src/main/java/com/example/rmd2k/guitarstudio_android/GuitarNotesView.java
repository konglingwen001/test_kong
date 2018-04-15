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

    private int lineNo;
    private NotesModel notesModel;
    private Paint mPaint;

    public GuitarNotesView(Context context) {
        super(context);
        notesModel = NotesModel.getInstance(context);
        mPaint = new Paint();
    }

    public GuitarNotesView(Context context, @Nullable AttributeSet attrs) {
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

    @Override
    protected void onDraw(Canvas canvas) {

        System.out.println("lingNo = " + lineNo);

        drawNoteLine(canvas, lineNo);
        drawNotes(canvas, lineNo);
        drawStem(canvas, lineNo);

        super.onDraw(canvas);
    }

    private void drawNoteLine(Canvas canvas, int lineNo) {

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

        float barLineX = 0;
        ArrayList<Float> barWidthArray = (ArrayList)notesModel.getNotesSizeArray().get(lineNo).get(NotesModel.KEY_SIZEDIC_BAR_WIDTH_ARRAY);
        float[] points_barLine = new float[barWidthArray.size() * 4];
        for (int i = 0; i < barWidthArray.size(); i++) {
            barLineX = barWidthArray.get(i) + NotesModel.LINE_WIDTH;
            points_barLine[i * 4] = barLineX;
            points_barLine[i * 4 + 1] = barStartY;
            points_barLine[i * 4 + 2] = barLineX;
            points_barLine[i * 4 + 3] = barStartY + offsetY * 5;
        }
        mPaint.setStrokeWidth(2);
        canvas.drawLines(points_barLine, mPaint);
    }

    private void drawNotes(Canvas canvas, int lineNo) {
        Map<String, Integer> editNotePos = notesModel.getCurrentEditPos();
        int editBarNo = editNotePos.get(NotesModel.KEY_CURR_BARNO);
        int editNoteNo = editNotePos.get(NotesModel.KEY_CURR_NOTENO);
        int editStringNo = editNotePos.get(NotesModel.KEY_CURR_STRINGNO);

        float barStartX = NotesModel.LINE_WIDTH;
        float barStartY = NotesModel.LINE_WIDTH;
        float offsetY = NotesModel.LINE_WIDTH;

        mPaint.setTextSize(50); //提前设置字体，用于绘制音符时计算字体大小

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
                        canvas.drawText(fretNo, noteCenterX - fretNoSize.width() / 2, noteCenterY + fretNoSize.height() / 2, mPaint);
                    }

                }
            }
        }
    }

    private void drawStem(Canvas canvas, int lineNo) {
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

        float height = this.getHeight();
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
                                mPaint.setStrokeWidth(4);
                                canvas.drawLine(noteCenterX, height - NotesModel.LINE_WIDTH, noteCenterX - currentNoteWidth, height - NotesModel.LINE_WIDTH, mPaint);
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
                                mPaint.setStrokeWidth(4);
                                canvas.drawLine(noteCenterX, height - NotesModel.LINE_WIDTH, noteCenterX - currentNoteWidth, height - NotesModel.LINE_WIDTH, mPaint);
                            } else if (preNoteType.equals(NotesModel.TYPE_DEMIQUAVER)) {
                                mPaint.setStrokeWidth(4);
                                canvas.drawLine(noteCenterX, height - NotesModel.LINE_WIDTH, noteCenterX - currentNoteWidth, height - NotesModel.LINE_WIDTH, mPaint);
                                canvas.drawLine(noteCenterX, height - NotesModel.LINE_WIDTH * 1.5f, noteCenterX - currentNoteWidth, height - NotesModel.LINE_WIDTH * 1.5f, mPaint);
                            }
                        }
                        break;
                }

                // 绘制符干
                mPaint.setStrokeWidth(2);
                canvas.drawLine(noteCenterX, height - NotesModel.LINE_WIDTH * 3, noteCenterX, height - NotesModel.LINE_WIDTH, mPaint);

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
        float posX = event.getRawX();
        float posY = event.getRawY();

        System.out.println("PosX:" + posX + "   PosY:" + posY);

        boolean result = calRect(posX, posY);
        if (result) {

        }
        return super.onTouchEvent(event);
    }

    private boolean calRect(float posX, float posY) {
        Map resultDic = new HashMap();
        float barStartX = NotesModel.LINE_WIDTH;
        float barStartY = NotesModel.LINE_WIDTH;

        if (isOutsideOfGuitarNotes(posX, posY)) {
            return false;
        }

        Map lineSizeDic = notesModel.getNotesSizeArray().get(lineNo);
        float minimWidth = Float.parseFloat(lineSizeDic.get(NotesModel.KEY_SIZEDIC_MINIM_WIDTH).toString());
        float crotchetaWidth = Float.parseFloat(lineSizeDic.get(NotesModel.KEY_SIZEDIC_CROTCHETA_WIDTH).toString());
        float quaverWidth = Float.parseFloat(lineSizeDic.get(NotesModel.KEY_SIZEDIC_QUAVER_WIDTH).toString());
        float demiquaverWidth = Float.parseFloat(lineSizeDic.get(NotesModel.KEY_SIZEDIC_DEMIQUAVER_WIDTH).toString());
        int barNo = Integer.parseInt(lineSizeDic.get(NotesModel.KEY_SIZEDIC_START_BARNO).toString());
        ArrayList barWidthArray = (ArrayList)lineSizeDic.get(NotesModel.KEY_SIZEDIC_BAR_WIDTH_ARRAY);

        // 判断触摸位置所属的小节，i从1开始，因为barWidthArray[0] == 0
        for (int i = 1; i < barWidthArray.size(); i++) {
            float width = Float.parseFloat(barWidthArray.get(i).toString());
            if (posX > NotesModel.LINE_WIDTH + width) {
                barNo++;
                barStartX = NotesModel.LINE_WIDTH + width;
            } else {
                break;
            }
        }

        int stringNo = (int)((posY - barStartY + NotesModel.NOTE_SIZE / 2) / NotesModel.NOTE_SIZE) + 1;
        resultDic.put(NotesModel.KEY_STRING_NO, stringNo);
        float addPosX = barStartX;
        float currentWidth = 0;

        ArrayList noteNoArray = notesModel.getNoteNoArray(barNo);
        for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
            String noteType = notesModel.getNoteType(barNo, noteNo);

            switch (noteType) {
                case NotesModel.TYPE_MINIM:
                    currentWidth = minimWidth;
                    break;
                case NotesModel.TYPE_CROTCHET:
                    currentWidth = crotchetaWidth;
                    break;
                case NotesModel.TYPE_QUAVER:
                    currentWidth = quaverWidth;
                    break;
                case NotesModel.TYPE_DEMIQUAVER:
                    currentWidth =  demiquaverWidth;
                    break;
            }
            addPosX += currentWidth;

            if (posX < addPosX) {

                if (noteNo == 0) {
                    resultDic.put(NotesModel.KEY_CURR_BARNO, barNo);
                    resultDic.put(NotesModel.KEY_CURR_NOTENO, noteNo);
                } else if (posX > addPosX - currentWidth / 2) {
                    resultDic.put(NotesModel.KEY_CURR_BARNO, barNo);
                    resultDic.put(NotesModel.KEY_CURR_NOTENO, noteNo);
                } else {
                    resultDic.put(NotesModel.KEY_CURR_BARNO, barNo);
                    resultDic.put(NotesModel.KEY_CURR_NOTENO, noteNo - 1);
                }
                break;
            } else {
                if (noteNo == noteNoArray.size() - 1) {
                    resultDic.put(NotesModel.KEY_CURR_BARNO, barNo);
                    resultDic.put(NotesModel.KEY_CURR_NOTENO, noteNo);
                    break;
                }
            }

        }

        // 当音符编辑框位置没有改变时，不刷新吉他谱
        if (resultDic.equals(notesModel.getCurrentEditPos())) {
            return false;
        }

        // 更新音符编辑框位置，并刷新吉他谱
        notesModel.setCurrentEditPos(resultDic);
        return true;

    }

    private boolean isOutsideOfGuitarNotes(float posX, float posY) {

        // 判断X坐标是否在吉他谱内
        if (posX < NotesModel.LINE_WIDTH || posX > this.getWidth() - NotesModel.LINE_WIDTH) {
            return true;
        }

        // 判断Y坐标是否在吉他谱内
        if (posY < NotesModel.NOTE_SIZE / 2) {
            return true;
        } else if (NotesModel.NOTE_SIZE + NotesModel.NOTE_SIZE * 5 + NotesModel.NOTE_SIZE / 2 < posY) {
            return true;
        }

        return false;
    }
}
