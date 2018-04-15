package com.example.rmd2k.guitarstudio_android;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by CHT1HTSH3236 on 2018/3/21.
 */

class NotesModel {

    public static final float NOTE_SIZE = 50;
    public static final float LINE_WIDTH = 50;

    private static final float MINIM_WIDTH = NOTE_SIZE * 1.5f * 1.5f * 1.5f;
    private static final float CROTCHETA_WIDTH = NOTE_SIZE * 1.5f * 1.5f;
    private static final float QUAVER_WIDTH = NOTE_SIZE * 1.5f;
    private static final float DEMIQUAVER_WIDTH = NOTE_SIZE;

    public static final String KEY_BAR_WIDTH = "barWidth";
    public static final String KEY_MINIM_NUM = "minimNum";
    public static final String KEY_CROTCHETA_NUM = "crotchetaNum";
    public static final String KEY_QUAVER_NUM = "quaverNum";
    public static final String KEY_DEMIQUAVER_NUM = "demiquaverNum";

    public static final String KEY_CURR_BARNO = "currBarNo";
    public static final String KEY_CURR_NOTENO = "currNoteNo";
    public static final String KEY_CURR_STRINGNO = "currStringNo";

    public static final String KEY_SIZEDIC_START_BARNO = "startBarNo";
    public static final String KEY_SIZEDIC_BAR_NUM = "barNum";
    public static final String KEY_SIZEDIC_DEMIQUAVER_WIDTH = "demiquaverWidth";
    public static final String KEY_SIZEDIC_QUAVER_WIDTH = "quaverWidth";
    public static final String KEY_SIZEDIC_CROTCHETA_WIDTH = "crotchetaWidth";
    public static final String KEY_SIZEDIC_MINIM_WIDTH = "minimWidth";
    public static final String KEY_SIZEDIC_BAR_WIDTH_ARRAY = "barWidthArray";
    public static final String KEY_SIZEDIC_BAR_WDITH = "barWidth";

    public static final String KEY_NOTE_TYPE = "NoteType";
    public static final String KEY_NOTE_ARRAY = "noteArray";
    public static final String KEY_STRING_NO = "StringNo";
    public static final String KEY_FRET_NO = "FretNo";
    public static final String KEY_PLAY_TYPE = "PlayType";

    public static final String KEY_ROOT_BAR_NUM = "BarNum";
    public static final String KEY_ROOT_FLAT = "Flat";
    public static final String KEY_ROOT_TIME = "Time";
    public static final String KEY_ROOT_GUITAR_NOTES = "GuitarNotes";

    public static final String TYPE_MINIM = "2";
    public static final String TYPE_CROTCHET = "4";
    public static final String TYPE_QUAVER = "8";
    public static final String TYPE_DEMIQUAVER = "16";

    private static Context mContext;
    private static Map<String, Integer> currentEditNotePos;

    private Map<String, Object> rootNoteDic = null;
    private ArrayList<Map> notesSizeArray = null;

    private static NotesModel ourInstance = null;

    static NotesModel getInstance(Context context) {
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
        currentEditNotePos = new HashMap<>();
        currentEditNotePos.put(KEY_CURR_BARNO, 0);
        currentEditNotePos.put(KEY_CURR_NOTENO, 0);
        currentEditNotePos.put(KEY_CURR_STRINGNO, 1);
    }

    public Map<String, Object> getRootNoteDic() {
        return rootNoteDic;
    }

    public Map<String, Integer> getCurrentEditPos() {
        return currentEditNotePos;
    }

    public void setCurrentEditPos(Map<String, Integer> currPos) {
        currentEditNotePos = currPos;
    }

    public int getBarNum() {
        if (getNotesSizeArray() == null) {
            return 0;
        }
        return getNotesSizeArray().size();
    }

    public ArrayList<ArrayList> getBarNoArray() {
        if (rootNoteDic == null) {
            return null;
        }
        return (ArrayList<ArrayList>)getRootNoteDic().get("GuitarNotes");
    }

    public ArrayList<ArrayList> getNoteNoArray(int barNo) {
        if (getBarNoArray() == null) {
            return null;
        }
        return getBarNoArray().get(barNo);
    }

    public Map getNotesArray(int barNo, int noteNo) {
        if (getNoteNoArray(barNo) == null) {
            return null;
        }
        return (Map)getNoteNoArray(barNo).get(noteNo);
    }

    public String getNoteType(int barNo, int noteNo) {
        Map noteDic = getNotesArray(barNo, noteNo);
        if (noteDic == null) {
            return null;
        }
        return noteDic.get(KEY_NOTE_TYPE).toString();
    }

    public Map<String, Object> getNote(int barNo, int noteNo, int stringNo) {
        Map notesDic = getNotesArray(barNo, noteNo);
        if (notesDic == null) {
            return null;
        }
        ArrayList<Map> notes = (ArrayList<Map>)notesDic.get(KEY_NOTE_ARRAY);
        if (notes == null) {
            return null;
        }
        for (Map<String, Object> noteDic:notes) {
            if (noteDic.get(KEY_STRING_NO).equals(stringNo+"")) {
                return noteDic;
            }
        }
        return null;
    }

    public void insertBarAfterBarNo(int barNo) {
        ArrayList barNoArray = getBarNoArray();
        barNoArray.add(barNo + 1, new ArrayList<>());

        insertBlankNoteAtBarNo(barNo + 1);
        calNotesSize();
    }

    public void insertBlankNoteAtBarNo(int barNo) {
        ArrayList noteNoArray = getNoteNoArray(barNo);

        Map<String, Object> notesDic = new HashMap<>();

        ArrayList<Map<String, Object>> notes = new ArrayList<>();
        Map<String, Object> note = new HashMap<>();

        note.put(KEY_STRING_NO, -1);
        note.put(KEY_FRET_NO, -1);
        note.put(KEY_PLAY_TYPE, "Normal");
        notes.add(note);

        notesDic.put(KEY_NOTE_TYPE, 4);
        notesDic.put(KEY_NOTE_ARRAY, notes);

        noteNoArray.add(notesDic);
    }

    public void removeBar() {
        int barNo = currentEditNotePos.get(KEY_CURR_BARNO);
        ArrayList barNoArray = getBarNoArray();

        if (barNo == barNoArray.size() - 1) {
            currentEditNotePos.put(KEY_CURR_BARNO, barNo - 1);
            currentEditNotePos.put(KEY_CURR_NOTENO, 0);
            currentEditNotePos.put(KEY_CURR_STRINGNO, 1);
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

    public Map<String, Object> getGuitarNotesFromGuitarNotesTitle(String guitarNotesTitle) {

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

    private Map<String, Object> parsePlistToMap(String path) {
        Stack<Object> stack = new Stack<>();
        AssetManager manager = mContext.getAssets();
        String key_t = "";
        try {
            //InputStream in = manager.open("music.plist");
            InputStream in = manager.open("天空之城.plist");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "utf-8");
            int event = parser.getEventType();
            while (XmlPullParser.END_DOCUMENT != event) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        rootNoteDic = new HashMap();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dict")) {
                            if (stack.empty()) {
                                stack.push(rootNoteDic);
                            } else {
                                ArrayList<Object> arrayList = (ArrayList<Object>) stack.peek();
                                HashMap<String, Object> map = new HashMap<>();
                                arrayList.add(map);
                                stack.push(map);
                            }
                            break;
                        } else if (parser.getName().equals("key")) {
                            HashMap<String, Object> map = (HashMap<String, Object>)stack.peek();
                            key_t = parser.nextText();
                            map.put(key_t, "");
                        } else if (parser.getName().equals("string")) {
                            HashMap<String, Object> map = (HashMap<String, Object>)stack.peek();
                            map.put(key_t, parser.nextText());
                        } else if (parser.getName().equals("integer")) {
                            HashMap<String, Object> map = (HashMap<String, Object>)stack.peek();
                            map.put(key_t, parser.nextText());
                        } else if (parser.getName().equals("array")) {

                            ArrayList<Object> array = new ArrayList<>();

                            Object object_t = stack.peek();
                            if (object_t instanceof HashMap) {
                                HashMap<String, Object> map_t = (HashMap)object_t;
                                map_t.put(key_t, array);
                            } else if (object_t instanceof ArrayList) {
                                ArrayList<Object> array_t = (ArrayList)object_t;
                                array_t.add(array);
                            }

                            stack.push(array);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("array")) {
                            stack.pop();
                        } else if (parser.getName().equals("dict")) {
                            stack.pop();
                        }
                        break;

                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Map calBarSizeWithNoteNoArray(Object bar, float currentMinimWidth, float currentCrotchetaWidth, float currentQuaverWidth, float currentDemiquaverWidth) {

        ArrayList noteNoArray = (ArrayList)bar;

        // 当前小节宽度初始化
        float barWidth = 0;

        // 音符个数初始化
        int minimNum = 0;
        int crotchetaNum = 0;
        int quaverNum = 0;
        int demiquaverNum = 0;

        // 统计音符总数
        String noteType = "";//[notesDic valueForKey:@"NoteType"];
        for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
            noteType = ((Map)(noteNoArray.get(noteNo))).get(KEY_NOTE_TYPE).toString();
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
        Map<String, Object> resultDic = new HashMap<>();
        resultDic.put(KEY_BAR_WIDTH, barWidth);
        resultDic.put(KEY_MINIM_NUM, minimNum);
        resultDic.put(KEY_CROTCHETA_NUM, crotchetaNum);
        resultDic.put(KEY_QUAVER_NUM, quaverNum);
        resultDic.put(KEY_DEMIQUAVER_NUM, demiquaverNum);

        return resultDic;
    }

    public ArrayList<Map> getNotesSizeArray() {
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
        float guitarNotesWidth = wm.getDefaultDisplay().getWidth() - 100;

        boolean isFirstBarInLine = true;
        int startBarNo = 0;         // 每行第一小节的barNo
        float lineBarWidth = 0;     // 行所有小节总宽度
        ArrayList barNoArray = getBarNoArray();
        notesSizeArray.clear();
        for (int barNo = 0; barNo < barNoArray.size(); barNo++) {
            Map sizeDic = calBarSizeWithNoteNoArray(barNoArray.get(barNo), MINIM_WIDTH, CROTCHETA_WIDTH, QUAVER_WIDTH, DEMIQUAVER_WIDTH);

            minimNum = Integer.parseInt(sizeDic.get(KEY_MINIM_NUM).toString());
            crotchetaNum = Integer.parseInt(sizeDic.get(KEY_CROTCHETA_NUM).toString());
            quaverNum = Integer.parseInt(sizeDic.get(KEY_QUAVER_NUM).toString());
            demiquaverNum = Integer.parseInt(sizeDic.get(KEY_DEMIQUAVER_NUM).toString());

            // 计算小节宽度总和
            lineBarWidth += Float.parseFloat(sizeDic.get(KEY_BAR_WIDTH).toString());

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
                Map<String, Object> lineDic = new HashMap<>();
                lineDic.put(KEY_SIZEDIC_START_BARNO, startBarNo);
                lineDic.put(KEY_SIZEDIC_BAR_NUM, barNo - startBarNo);

                // 音符个数减去最后要丢弃的一小节的音符数，重新计算音符宽度
                currentDemiquaverWidth = guitarNotesWidth / (demiquaverSum + quaverSum * 1.5f + crotchetaSum * 1.5f * 1.5f + minimSum * 1.5f * 1.5f * 1.5f);
                currentQuaverWidth = currentDemiquaverWidth * 1.5f;
                currentCrotchetaWidth = currentQuaverWidth * 1.5f;
                currentMinimWidth = currentCrotchetaWidth * 1.5f;
                lineDic.put(KEY_SIZEDIC_DEMIQUAVER_WIDTH, currentDemiquaverWidth);
                lineDic.put(KEY_SIZEDIC_QUAVER_WIDTH, currentQuaverWidth);
                lineDic.put(KEY_SIZEDIC_CROTCHETA_WIDTH, currentCrotchetaWidth);
                lineDic.put(KEY_SIZEDIC_MINIM_WIDTH, currentMinimWidth);

                // 通过计算得到的音符宽度重新计算小节宽度，保存在数组中
                lineBarWidth = 0;
                ArrayList<Float> barWidthArray = new ArrayList<>();
                barWidthArray.add(0.0f);
                for (int i = startBarNo; i < barNo - 1; i++) {
                    Map resultDic = calBarSizeWithNoteNoArray(barNoArray.get(i), currentMinimWidth, currentCrotchetaWidth, currentQuaverWidth, currentDemiquaverWidth);
                    lineBarWidth += Double.parseDouble(resultDic.get(KEY_SIZEDIC_BAR_WDITH).toString());
                    barWidthArray.add(lineBarWidth);
                }
                barWidthArray.add(guitarNotesWidth);
                lineDic.put(KEY_SIZEDIC_BAR_WIDTH_ARRAY, barWidthArray.clone());
                notesSizeArray.add(lineDic);

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
        ArrayList<Float> barWidthArray = new ArrayList();
        lineBarWidth = 0;
        barWidthArray.add(0.0f);
        for (int i = startBarNo; i < barNoArray.size(); i++) {
            Map resultDic = calBarSizeWithNoteNoArray(barNoArray.get(i), currentDemiquaverWidth, currentQuaverWidth, currentCrotchetaWidth, currentMinimWidth);
            lineBarWidth += Double.parseDouble(resultDic.get(KEY_SIZEDIC_BAR_WDITH).toString());
            barWidthArray.add(lineBarWidth);
        }

        // 保存
        Map<String, Object> lineDic = new HashMap();
        lineDic.put(KEY_SIZEDIC_START_BARNO, startBarNo);
        lineDic.put(KEY_SIZEDIC_BAR_NUM, barNoArray.size() - startBarNo);
        lineDic.put(KEY_SIZEDIC_DEMIQUAVER_WIDTH, DEMIQUAVER_WIDTH);
        lineDic.put(KEY_SIZEDIC_QUAVER_WIDTH, QUAVER_WIDTH);
        lineDic.put(KEY_SIZEDIC_CROTCHETA_WIDTH, CROTCHETA_WIDTH);
        lineDic.put(KEY_SIZEDIC_MINIM_WIDTH, MINIM_WIDTH);
        lineDic.put(KEY_SIZEDIC_BAR_WIDTH_ARRAY, barWidthArray.clone());
        notesSizeArray.add(lineDic);

    }

    public void setNoteFret(int fretNo) {

        // 获取音符编辑框所在的位置，包括小节序号、音符序号、吉他弦号
        int barNo = currentEditNotePos.get(KEY_CURR_BARNO);
        int noteNo = currentEditNotePos.get(KEY_CURR_NOTENO);
        int stringNo = currentEditNotePos.get(KEY_CURR_STRINGNO);

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

        ArrayList notesArray = (ArrayList)getNotesArray(barNo, noteNo).get(KEY_NOTE_ARRAY);
        Map<String, Object> editNote = getNote(barNo, noteNo, stringNo);
        if (editNote == null) {  // 当音符编辑框所在位置没有音符时，添加新的音符

            Map<String, Object> mutableEditNote = new HashMap<>();
            mutableEditNote.put(KEY_STRING_NO, stringNo);
            mutableEditNote.put(KEY_FRET_NO, fretNo);
            mutableEditNote.put(KEY_PLAY_TYPE, ((Map)notesArray.get(0)).get(KEY_PLAY_TYPE));
            notesArray.add(mutableEditNote);
            //[self addNote:mutableEditNote atBarNo:barNo andNoteNo:noteNo];

        } else {    // 当音符编辑框所在位置有音符时，修改原有音符

            int oldFretNo = Integer.parseInt(editNote.get(KEY_FRET_NO).toString());
            // 当原有音符为1或2时，将原有音符乘以10再加上输入音符，结果作为新的音符
            if (oldFretNo == 1 || oldFretNo == 2) {
                fretNo = oldFretNo * 10 + fretNo;
            }
            editNote.put(KEY_FRET_NO, fretNo);

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
        int barNo = currentEditNotePos.get(KEY_CURR_BARNO);
        int noteNo = currentEditNotePos.get(KEY_CURR_NOTENO);

        ArrayList notesArray = (ArrayList)getNotesArray(barNo, noteNo).get(KEY_NOTE_ARRAY);
        Map note = (Map)notesArray.get(0);
        int tmpFretNo = Integer.parseInt(note.get(KEY_FRET_NO).toString());
        int tmpStringNo = Integer.parseInt(note.get(KEY_STRING_NO).toString());
        if (tmpFretNo == -1 && tmpStringNo == -1) {
            notesArray.remove(note);
        }
    }

    public void addNote(Map editNote, int barNo, int noteNo) {
        Map<String, Object> tmpRootDic = new HashMap<>();
        tmpRootDic.put(KEY_ROOT_BAR_NUM, rootNoteDic.get(KEY_ROOT_BAR_NUM));
        tmpRootDic.put(KEY_ROOT_FLAT, rootNoteDic.get(KEY_ROOT_FLAT));
        tmpRootDic.put(KEY_ROOT_TIME, rootNoteDic.get(KEY_ROOT_TIME));
        ArrayList tmpBarNoArray = new ArrayList();

        ArrayList barNoArray = (ArrayList)rootNoteDic.get(KEY_ROOT_GUITAR_NOTES);
        for (int tmpBarNo = 0; tmpBarNo < barNoArray.size(); tmpBarNo++) {
            ArrayList noteNoArray = (ArrayList)barNoArray.get(tmpBarNo);
            if (tmpBarNo == barNo) {
                ArrayList tmpNoteNoArray = new ArrayList();
                for (int tmpNoteNo = 0; tmpNoteNo < noteNoArray.size(); tmpNoteNo++) {
                    ArrayList notesArray = (ArrayList)noteNoArray.get(tmpNoteNo);
                    if (tmpNoteNo == noteNo) {
                        ArrayList tmpNotesArray = (ArrayList)notesArray.clone();
                        tmpNotesArray.add(editNote);
                        tmpNoteNoArray.add(tmpNotesArray);
                    } else {
                        tmpNoteNoArray.add(notesArray);
                    }
                }
                tmpBarNoArray.add(tmpNoteNoArray);
            } else {
                tmpBarNoArray.add(noteNoArray);
            }
        }
        tmpRootDic.put(KEY_ROOT_GUITAR_NOTES, tmpBarNoArray);
        rootNoteDic = tmpRootDic;

    }
}
