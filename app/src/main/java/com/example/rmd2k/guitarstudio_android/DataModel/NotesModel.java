package com.example.rmd2k.guitarstudio_android.DataModel;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;
import android.view.WindowManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

/**
 * Created by CHT1HTSH3236 on 2018/3/21.
 */

public class NotesModel {

    public static final float NOTE_SIZE = 15;
    public static final float LINE_WIDTH = NOTE_SIZE;

    private static final float MINIM_WIDTH = NOTE_SIZE * 1.5f * 1.5f * 1.5f;
    private static final float CROTCHETA_WIDTH = NOTE_SIZE * 1.5f * 1.5f;
    private static final float QUAVER_WIDTH = NOTE_SIZE * 1.5f;
    private static final float DEMIQUAVER_WIDTH = NOTE_SIZE;

    public static final String TYPE_MINIM = "2";
    public static final String TYPE_CROTCHET = "4";
    public static final String TYPE_QUAVER = "8";
    public static final String TYPE_DEMIQUAVER = "16";

    private static Context mContext;
    private static Note currentEditNote;

    private GuitarNotes rootNoteDic = null;
    private ArrayList<LineSize> notesSizeArray = null;

    private static NotesModel ourInstance = null;

    public static NotesModel getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new NotesModel();
            mContext = context;

            initData();
        }

        return ourInstance;
    }

    private NotesModel() {
    }

    private static void initData() {
        currentEditNote = new Note("-1", "-1", "-1", "-1", "-1");
    }

    public int px2dp(float pxValue) {
        final float scale =  mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public float getNoteSize() {
        return dp2px(NOTE_SIZE);
    }
    public float getLineWidth() {
        return dp2px(LINE_WIDTH);
    }
    public float getMinimWidth() {
        return dp2px(MINIM_WIDTH);
    }
    public float getCrotchetaWidth() {
        return dp2px(CROTCHETA_WIDTH);
    }
    public float getQuaverWidth() {
        return dp2px(QUAVER_WIDTH);
    }
    public float getDemiquaverWidth() {
        return dp2px(DEMIQUAVER_WIDTH);
    }


    public GuitarNotes getRootNoteDic() {
        return rootNoteDic;
    }

    public Note getCurrentEditNote() {
        return currentEditNote;
    }

    public void setCurrentEditPos(Note note) {
        currentEditNote.setBarNo(note.getBarNo());
        currentEditNote.setNoteNo(note.getNoteNo());
        currentEditNote.setFretNo(note.getFretNo());
        currentEditNote.setStringNo(note.getStringNo());
        currentEditNote.setPlayType(note.getPlayType());
    }

    public int getBarNum() {
        if (getNotesSizeArray() == null) {
            return 0;
        }
        return getNotesSizeArray().size();
    }

    public ArrayList<BarNoData> getBarNoArray() {
        if (rootNoteDic == null) {
            return null;
        }
        return getRootNoteDic().getBarNoDataArray();
    }

    public ArrayList<NoteNoData> getNoteNoArray(int barNo) {
        ArrayList<BarNoData> barNoArray = getBarNoArray();
        if (barNoArray == null) {
            return null;
        }
        BarNoData barNoData = barNoArray.get(barNo);
        if (barNoData == null) {
            return null;
        }
        return barNoData.getNoteNoDataArray();
    }

    public ArrayList<Note> getNotesArray(int barNo, int noteNo) {
        ArrayList<NoteNoData> noteNoArray = getNoteNoArray(barNo);
        if (noteNoArray == null) {
            return null;
        }
        NoteNoData noteNoData = noteNoArray.get(noteNo);
        if (noteNoData == null) {
            return null;
        }
        return noteNoData.getNoteArray();
    }

    public String getNoteType(int barNo, int noteNo) {
        ArrayList<NoteNoData> noteNoArray = getNoteNoArray(barNo);
        if (noteNoArray == null) {
            return null;
        }
        NoteNoData noteNoData = noteNoArray.get(noteNo);
        if (noteNoData == null) {
            return null;
        }
        return noteNoData.getNoteType();
    }

    public Note getNote(int barNo, int noteNo, int stringNo) {
        ArrayList<Note> notes = getNotesArray(barNo, noteNo);
        if (notes == null) {
            return null;
        }
        for (Note note:notes) {
            if (note.getStringNo().equals(stringNo+"")) {
                return note;
            }
        }
        return null;
    }

    public void insertBarAfterBarNo(int barNo) {
        ArrayList barNoArray = getBarNoArray();
        barNoArray.add(barNo + 1, new ArrayList<BarNoData>());

        insertBlankNoteAtBarNo(barNo + 1);
        calNotesSize();
    }

    public void insertBlankNoteAtBarNo(int barNo) {
        ArrayList<NoteNoData> noteNoArray = getNoteNoArray(barNo);
        NoteNoData noteNoData = new NoteNoData();
        Note note = new Note();
        note.setStringNo(-1 + "");
        note.setFretNo(-1 + "");
        note.setPlayType("Normal");
        ArrayList<Note> noteArray = new ArrayList<>();
        noteArray.add(note);
        noteNoData.setNoteArray(noteArray);
        noteNoArray.add(noteNoData);
    }

    public void removeBar() {
        int barNo = Integer.parseInt(currentEditNote.getBarNo());
        ArrayList barNoArray = getBarNoArray();

        if (barNo == barNoArray.size() - 1) {
            currentEditNote.setBarNo(String.valueOf(barNo - 1));
            currentEditNote.setNoteNo(String.valueOf(0));
            currentEditNote.setStringNo(String.valueOf(1));
        }

        barNoArray.remove(barNo);
        calNotesSize();
    }

    /**
     * @param notesTitle
     */
    public void setGuitarNotesWithNotesTitle(String notesTitle) {
        rootNoteDic = getGuitarNotesFromGuitarNotesTitle(notesTitle);
        notesSizeArray = new ArrayList<>();
        calNotesSize();
    }

    public GuitarNotes getGuitarNotesFromGuitarNotesTitle(String guitarNotesTitle) {

        if (rootNoteDic == null) {
            loadGuitarNotes(guitarNotesTitle);
        }
        return rootNoteDic;

    }

    private void loadGuitarNotes(String guitarNotesTitle) {
        String path = mContext.getFilesDir().toString();
        String plistPath = path + "/" + guitarNotesTitle + ".plist";
        File file = new File(plistPath);
        if (file.exists()) {
            Log.v("DEBUG", plistPath);
        }

        parsePlistToMap(plistPath);

        System.out.println("over");
    }

    private void addBarNoData(BarNoData barNoData) {
        rootNoteDic.getBarNoDataArray().add(barNoData);
    }

    private void addNoteNoData(int barNo, NoteNoData noteNoData) {
        rootNoteDic.getBarNoDataArray().get(barNo).getNoteNoDataArray().add(noteNoData);
    }

    private void addNote(int barNo, int noteNo, Note note) {
        rootNoteDic.getBarNoDataArray().get(barNo).getNoteNoDataArray().get(barNo).getNoteArray().add(note);
    }

    private Map<String, Object> parsePlistToMap(String path) {
        Stack<Object> stack = new Stack<>();
        AssetManager manager = mContext.getAssets();
        String key_t = "";
        int barNo = 0;
        int noteNo = 0;
        try {
            //InputStream in = manager.open("music.plist");
            InputStream in = manager.open("天空之城.plist");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "utf-8");
            int event = parser.getEventType();
            while (XmlPullParser.END_DOCUMENT != event) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        rootNoteDic = new GuitarNotes();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dict")) {
                            if (stack.empty()) {
                                stack.push(rootNoteDic);
                            }
                            else {

                                Object object = stack.peek();
                                if (object instanceof BarNoData) {
                                    NoteNoData noteNoData = new NoteNoData();
                                    BarNoData barNoData = (BarNoData)object;
                                    if (barNoData.getNoteNoDataArray() == null) {
                                        barNoData.setNoteNoDataArray(new ArrayList<NoteNoData>());
                                    }
                                    barNoData.getNoteNoDataArray().add(noteNoData);
                                    stack.push(noteNoData);
                                } else if (object instanceof NoteNoData) {
                                    Note note = new Note();
                                    NoteNoData noteNoData = (NoteNoData)object;
                                    if (noteNoData.getNoteArray() == null) {
                                        noteNoData.setNoteArray(new ArrayList<Note>());
                                    }
                                    noteNoData.getNoteArray().add(note);
                                    stack.push(note);
                                }
                            }
                            break;
                        } else if (parser.getName().equals("key")) {
                            key_t = parser.nextText();
                        } else if (parser.getName().equals("string")) {
                            Object object = stack.peek();
                            object.getClass().getMethod("set" + key_t, String.class).invoke(object, parser.nextText());
                        } else if (parser.getName().equals("integer")) {
                            Object object = stack.peek();
                            object.getClass().getMethod("set" + key_t, String.class).invoke(object, parser.nextText());
                        } else if (parser.getName().equals("array")) {

                            Object object_t = stack.peek();
                            if (object_t instanceof GuitarNotes) {
                                BarNoData barNoData = new BarNoData();
                                if (rootNoteDic.getBarNoDataArray() == null) {
                                    rootNoteDic.setBarNoDataArray(new ArrayList<BarNoData>());
                                }
                                rootNoteDic.getBarNoDataArray().add(barNoData);
                                stack.push(barNoData);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("array")) {
                            if (stack.peek() instanceof NoteNoData) {
                                break;
                            }
                            if (!stack.empty()) {
                                stack.pop();
                            }
                        } else if (parser.getName().equals("dict")) {
                            if (!stack.empty()) {
                                stack.pop();
                            }
                        }
                        break;

                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

//    private Map<String, Object> parsePlistToMap(String path) {
//        Stack<Object> stack = new Stack<>();
//        Map tmpRootDic = null;
//        AssetManager manager = mContext.getAssets();
//        String key_t = "";
//        int barNo = 0;
//        int noteNo = 0;
//        try {
//            //InputStream in = manager.open("music.plist");
//            InputStream in = manager.open("天空之城.plist");
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setInput(in, "utf-8");
//            int event = parser.getEventType();
//            while (XmlPullParser.END_DOCUMENT != event) {
//                switch (event) {
//                    case XmlPullParser.START_DOCUMENT:
//                        tmpRootDic = new HashMap();
//                        break;
//                    case XmlPullParser.START_TAG:
//                        if (parser.getName().equals("dict")) {
//                            if (stack.empty()) {
//                                stack.push(tmpRootDic);
//                            } else {
//                                ArrayList<Object> arrayList = (ArrayList<Object>) stack.peek();
//                                HashMap<String, Object> map = new HashMap<>();
//                                arrayList.add(map);
//                                stack.push(map);
//
//                                ArrayList<Note> noteArray = new ArrayList<>();
//                                NoteNoData noteNoData = new NoteNoData(noteNo++, "-1", noteArray);
//                                addNoteNoData(barNo, noteNoData);
//                            }
//                            break;
//                        } else if (parser.getName().equals("key")) {
//                            HashMap<String, Object> map = (HashMap<String, Object>)stack.peek();
//                            key_t = parser.nextText();
//                            map.put(key_t, "");
//                        } else if (parser.getName().equals("string")) {
//                            HashMap<String, Object> map = (HashMap<String, Object>)stack.peek();
//                            map.put(key_t, parser.nextText());
//
//                            setDataByDic(barNo, noteNo, key_t, parser.nextText());
//                        } else if (parser.getName().equals("integer")) {
//                            HashMap<String, Object> map = (HashMap<String, Object>)stack.peek();
//                            map.put(key_t, parser.nextText());
//
//                            setDataByDic(barNo, noteNo, key_t, parser.nextText());
//                        } else if (parser.getName().equals("array")) {
//
//                            ArrayList<Object> array = new ArrayList<>();
//
//                            Object object_t = stack.peek();
//                            if (object_t instanceof HashMap) {
//                                HashMap<String, Object> map_t = (HashMap)object_t;
//                                map_t.put(key_t, array);
//
//                                setDataByDic(barNo, noteNo, key_t, array);
//                            } else if (object_t instanceof ArrayList) {
//                                ArrayList<Object> array_t = (ArrayList)object_t;
//                                array_t.add(array);
//
//                                BarNoData barNoData = new BarNoData(barNo++, (ArrayList)array);
//                                addBarNoData(barNoData);
//                            }
//
//                            stack.push(array);
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        if (parser.getName().equals("array")) {
//                            stack.pop();
//                        } else if (parser.getName().equals("dict")) {
//                            stack.pop();
//                        }
//                        break;
//
//                }
//                event = parser.next();
//            }
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    private BarSize calBarSizeWithNoteNoArray(BarNoData barNoData, float currentMinimWidth, float currentCrotchetaWidth, float currentQuaverWidth, float currentDemiquaverWidth) {

        // 当前小节宽度初始化
        float barWidth = 0;

        // 音符个数初始化
        int minimNum = 0;
        int crotchetaNum = 0;
        int quaverNum = 0;
        int demiquaverNum = 0;

        // 统计音符总数
        String noteType = "";//[notesDic valueForKey:@"NoteType"];
        for (int noteNo = 0; noteNo < barNoData.getNoteNoDataArray().size(); noteNo++) {
            noteType = barNoData.getNoteNoDataArray().get(noteNo).getNoteType();
            switch (noteType) {
                case TYPE_MINIM:
                    minimNum++;
                    break;
                case TYPE_CROTCHET:
                    crotchetaNum++;
                    break;
                case TYPE_QUAVER:
                    quaverNum++;
                    break;
                case TYPE_DEMIQUAVER:
                    demiquaverNum++;
                    break;
            }
        }

        // 最后一个音符宽度
        switch (noteType) {
            case TYPE_MINIM:
                minimNum++;
                break;
            case TYPE_CROTCHET:
                crotchetaNum++;
                break;
            case TYPE_QUAVER:
                quaverNum++;
                break;
            case TYPE_DEMIQUAVER:
                demiquaverNum++;
                break;
        }

        // 计算小节宽度总和
        barWidth = minimNum * currentMinimWidth + crotchetaNum * currentCrotchetaWidth + quaverNum * currentQuaverWidth + demiquaverNum * currentDemiquaverWidth;

        // 保存结果并返回
        BarSize barSize = new BarSize();
        barSize.setBarWidth(barWidth);
        barSize.setMinimNum(minimNum);
        barSize.setCrotchetaNum(crotchetaNum);
        barSize.setQuaverNum(quaverNum);
        barSize.setDemiquaverNum(demiquaverNum);

        return barSize;
    }

    public ArrayList<LineSize> getNotesSizeArray() {
        return notesSizeArray;
    }

    public void calNotesSize() {

        int minimNum = 0;           // 单个小节二分音符个数
        int crotchetaNum = 0;       // 单个小节四分音符个数
        int quaverNum = 0;          // 单个小节八分音符个数
        int demiquaverNum = 0;      // 单个小节十六分音符个数

        int minimSum = 0;           // 整行二分音符总数
        int crotchetaSum = 0;       // 整行四分音符总数
        int quaverSum = 0;          // 整行八分音符总数
        int demiquaverSum = 0;      // 整行十六分音符总数

        float currentMinimWidth = 0;
        float currentCrotchetaWidth = 0;
        float currentQuaverWidth = 0;
        float currentDemiquaverWidth = 0;

        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        float guitarNotesWidth = wm.getDefaultDisplay().getWidth() - getLineWidth() * 2;

        BarSize barSize;
        boolean isFirstBarInLine = true;
        int startBarNo = 0;         // 每行第一小节的barNo
        float lineBarWidth = 0;     // 行所有小节总宽度
        ArrayList<BarNoData> barNoArray = getBarNoArray();
        notesSizeArray.clear();
        for (int barNo = 0; barNo < barNoArray.size(); barNo++) {
            barSize = calBarSizeWithNoteNoArray(barNoArray.get(barNo), getMinimWidth(), getCrotchetaWidth(), getQuaverWidth(), getDemiquaverWidth());

            minimNum = barSize.getMinimNum();
            crotchetaNum = barSize.getCrotchetaNum();
            quaverNum = barSize.getQuaverNum();
            demiquaverNum = barSize.getDemiquaverNum();

            // 计算小节宽度总和
            lineBarWidth += barSize.getBarWidth();

            // 当前行所有小节宽度总和小于吉他谱宽度时，累加音符数
            if (lineBarWidth < guitarNotesWidth) {
                minimSum += minimNum;
                crotchetaSum += crotchetaNum;
                quaverSum += quaverNum;
                demiquaverSum += demiquaverNum;

                isFirstBarInLine = false;
            } else {

                if (isFirstBarInLine) {
                    minimSum += minimNum;
                    crotchetaSum += crotchetaNum;
                    quaverSum += quaverNum;
                    demiquaverSum += demiquaverNum;
                }
                LineSize lineSize = new LineSize();
                lineSize.setStartBarNo(startBarNo);
                lineSize.setBarNum(barNo - startBarNo);

                // 音符个数减去最后要丢弃的一小节的音符数，重新计算音符宽度
                currentDemiquaverWidth = guitarNotesWidth / (demiquaverSum + quaverSum * 1.5f + crotchetaSum * 1.5f * 1.5f + minimSum * 1.5f * 1.5f * 1.5f);
                currentQuaverWidth = currentDemiquaverWidth * 1.5f;
                currentCrotchetaWidth = currentQuaverWidth * 1.5f;
                currentMinimWidth = currentCrotchetaWidth * 1.5f;
                lineSize.setDemiquaverWidth(currentDemiquaverWidth);
                lineSize.setQuaverWidth(currentQuaverWidth);
                lineSize.setCrotchetaWidth(currentCrotchetaWidth);
                lineSize.setMinimWidth(currentMinimWidth);

                // 通过计算得到的音符宽度重新计算小节宽度，保存在数组中
                lineBarWidth = 0;
                ArrayList<Float> barWidthArray = new ArrayList<>();
                barWidthArray.add(0.0f);
                for (int i = startBarNo; i < barNo - 1; i++) {
                    barSize = calBarSizeWithNoteNoArray(barNoArray.get(i), currentMinimWidth, currentCrotchetaWidth, currentQuaverWidth, currentDemiquaverWidth);
                    lineBarWidth += barSize.getBarWidth();
                    barWidthArray.add(lineBarWidth);
                }
                barWidthArray.add(guitarNotesWidth);
                lineSize.setBarWidthArray(barWidthArray);
                notesSizeArray.add(lineSize);

                // 重置参数
                // 下一行小节开始序号，设置为丢弃的小节序号
                // 小节序号减一，从丢弃的小节序号重新开始计算
                startBarNo = barNo;
                if (!isFirstBarInLine) {
                    barNo--;
                }

                // 行所有小节宽度初始值设置为丢弃小节的宽度
                lineBarWidth = 0;
                minimSum = 0;
                crotchetaSum = 0;
                quaverSum = 0;
                demiquaverSum = 0;

                isFirstBarInLine = true;

            }
        }

        // 最后一行，以最小音符长度计算尺寸

        // 计算行小节宽度
        ArrayList<Float> barWidthArray = new ArrayList<>();
        lineBarWidth = 0;
        barWidthArray.add(0.0f);
        for (int i = startBarNo; i < barNoArray.size(); i++) {
            barSize = calBarSizeWithNoteNoArray(barNoArray.get(i), getDemiquaverWidth(), getQuaverWidth(), getCrotchetaWidth(), getMinimWidth());
            lineBarWidth += barSize.getBarWidth();
            barWidthArray.add(lineBarWidth);
        }

        // 保存
        LineSize lineSize = new LineSize();
        lineSize.setStartBarNo(startBarNo);
        lineSize.setBarNum(barNoArray.size() - startBarNo);
        lineSize.setDemiquaverWidth(getDemiquaverWidth());
        lineSize.setQuaverWidth(getQuaverWidth());
        lineSize.setCrotchetaWidth(getCrotchetaWidth());
        lineSize.setMinimWidth(getMinimWidth());
        lineSize.setBarWidthArray(barWidthArray);
        notesSizeArray.add(lineSize);

    }

    public void setNoteFret(int fretNo) {

        // 获取音符编辑框所在的位置，包括小节序号、音符序号、吉他弦号
        int barNo = Integer.parseInt(currentEditNote.getBarNo());
        int noteNo = Integer.parseInt(currentEditNote.getNoteNo());
        int stringNo = Integer.parseInt(currentEditNote.getStringNo());

        // -------------------------------------------------------------------------------------------------------START
        // 修改音符前判断，如果所修改的音符位置该小节最后一个音符位置，并且该小节音符不满时，在该小节最后添加一个空占位音符，用于选中编辑

        ArrayList noteNoArray = getNoteNoArray(barNo);

        // 判断小节音符是否正确与完全
        boolean barIsCorrect = checkBarStateAtBarNo(barNo);

        if (noteNo == noteNoArray.size() - 1 && !barIsCorrect) {
            // 在小节末尾添加占位空音符
            insertBlankNoteAtBarNo(barNo);

            // 重新计算吉他谱尺寸
            calNotesSize();
        }

        // -------------------------------------------------------------------------------------------------------END

        // -------------------------------------------------------------------------------------------------------START
        // 修改音符

        ArrayList<Note> notesArray = getNotesArray(barNo, noteNo);
        Note editNote = getNote(barNo, noteNo, stringNo);
        if (editNote == null) {  // 当音符编辑框所在位置没有音符时，添加新的音符

            Note mutableEditNote = new Note();
            mutableEditNote.setStringNo(stringNo + "");
            mutableEditNote.setFretNo(fretNo + "");
            mutableEditNote.setPlayType(notesArray.get(0).getPlayType());
            notesArray.add(mutableEditNote);
            addNote(mutableEditNote, barNo, noteNo);

        } else {    // 当音符编辑框所在位置有音符时，修改原有音符

            int oldFretNo = Integer.parseInt(editNote.getFretNo());
            // 当原有音符为1或2时，将原有音符乘以10再加上输入音符，结果作为新的音符
            if (oldFretNo == 1 || oldFretNo == 2) {
                fretNo = oldFretNo * 10 + fretNo;
            }
            editNote.setFretNo(fretNo + "");

        }

        // 如果修改位置存在空白占位音符，则删除空白占位音符
        removeBlankNote();

        // -------------------------------------------------------------------------------------------------------END

    }

    public boolean checkBarStateAtBarNo(int barNo) {
        double noteType = 0;
        double noteTypeSum = 0;
        ArrayList noteNoArray = getNoteNoArray(barNo);
        for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
            noteType = Double.parseDouble(getNoteType(barNo, noteNo));
            noteTypeSum += 1.0f / noteType;
        }

        return (noteTypeSum == 1);
    }

    public void removeBlankNote() {
        // 获取音符编辑框所在的位置，包括小节序号、音符序号、吉他弦号
        int barNo = Integer.parseInt(currentEditNote.getBarNo());
        int noteNo = Integer.parseInt(currentEditNote.getNoteNo());

        ArrayList<Note> notesArray = getNotesArray(barNo, noteNo);
        Note note = notesArray.get(0);
        int tmpFretNo = Integer.parseInt(note.getFretNo());
        int tmpStringNo = Integer.parseInt(note.getStringNo());
        if (tmpFretNo == -1 && tmpStringNo == -1) {
            notesArray.remove(note);
        }
    }

    public void addNote(Note editNote, int barNo, int noteNo) {
        getNotesArray(barNo, noteNo).add(editNote);
    }
}
