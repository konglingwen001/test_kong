package com.example.rmd2k.guitarstudio_android.DataModel;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;
import android.view.WindowManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
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

    private static final String TAG = "NotesModel";

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

    private ArrayList<String> guitarNotesFiles = new ArrayList<>();

    private static Context mContext;
    private static EditNoteInfo currentEditNote;

    private GuitarNotes oldGuitarNotes = null;
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

    public int getGuitarNotesFilesNum() {
        return guitarNotesFiles.size();
    }

    public String getGuitarNotesFile(int index) {
        return guitarNotesFiles.get(index);
    }

    public ArrayList<String> getGuitarNotesFileArray() {
        return guitarNotesFiles;
    }

    public void reloadGuitarNotesFiles() {
        guitarNotesFiles.clear();
        String[] files = mContext.fileList();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".plist")) {
                guitarNotesFiles.add(files[i].split("\\.")[0]);
            }
        }
    }

    private static void initData() {
        currentEditNote = new EditNoteInfo(0, 0, 0, -1, -1);
    }

    public int px2dp(float pxValue) {
        final float scale =  mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int getNoteSize() {
        return dp2px(NOTE_SIZE);
    }
    public int getLineWidth() {
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

    public String getGuitarNoteName() {
        return rootNoteDic.getGuitarNotesName();
    }

    public EditNoteInfo getCurrentEditNote() {
        return currentEditNote;
    }

    public void setCurrentEditPos(EditNoteInfo note) {
        currentEditNote.setBarNo(note.getBarNo());
        currentEditNote.setNoteNo(note.getNoteNo());
        currentEditNote.setFretNo(note.getFretNo());
        currentEditNote.setStringNo(note.getStringNo());
        currentEditNote.setPlayType(note.getPlayType());
    }

    public void setCurrentEditPos(int barNo, int noteNo, int stringNo) {
        if (currentEditNote == null) {
            currentEditNote = new EditNoteInfo();
        }
        currentEditNote.setBarNo(barNo);
        currentEditNote.setNoteNo(noteNo);
        currentEditNote.setStringNo(stringNo);
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

    public NoteNoData getNoteNoData(int barNo, int noteNo) {
        ArrayList<NoteNoData> noteNoDataArrayList = getNoteNoArray(barNo);
        if (noteNoDataArrayList == null) {
            Log.e(TAG, "noteNoDataArray is null");
            return new NoteNoData();
        }
        return noteNoDataArrayList.get(noteNo);
    }

    public void changeEditNoteType(String type) {
        int barNo = currentEditNote.getBarNo();
        int noteNo = currentEditNote.getNoteNo();

        NoteNoData noteNoData = getNoteNoData(barNo, noteNo);
        noteNoData.setNoteType(type);
    }

    public void changeEditNoteFretNo(int fretNo) {

        // 获取音符编辑框所在的位置，包括小节序号、音符序号、吉他弦号
        int barNo = currentEditNote.getBarNo();
        int noteNo = currentEditNote.getNoteNo();
        int stringNo = currentEditNote.getStringNo();

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
        int barNo = currentEditNote.getBarNo();
        ArrayList barNoArray = getBarNoArray();

        if (barNo == barNoArray.size() - 1) {
            currentEditNote.setBarNo(barNo - 1);
            currentEditNote.setNoteNo(0);
            currentEditNote.setStringNo(1);
        }

        barNoArray.remove(barNo);
        calNotesSize();
    }

    public void copyAssetFilesToFileDir(Activity activity) {

        FileOutputStream fos = null;
        InputStream is = null;
        AssetManager manager = activity.getApplicationContext().getAssets();

        try {
            String[] files = manager.list("");
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".plist") || files[i].endsWith(".ogg")) {
                    fos = activity.openFileOutput(files[i], Context.MODE_PRIVATE);
                    is = manager.open(files[i]);

                    int count = 0;
                    byte buf[] = new byte[1024];
                    while ((count = is.read(buf)) > 0) {
                        fos.write(buf, 0, count);
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (IOException e) {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * @param guitarNoteName
     */
    public void setGuitarNotesWithNotesTitle(String guitarNoteName) {
        rootNoteDic = getGuitarNotesFromGuitarNotesTitle(guitarNoteName);
        notesSizeArray = new ArrayList<>();
        calNotesSize();
    }

    public GuitarNotes getGuitarNotesFromGuitarNotesTitle(String guitarNoteName) {

        if (rootNoteDic == null) {
            loadGuitarNotes(guitarNoteName);
        }
        return rootNoteDic;

    }

    private void loadGuitarNotes(String guitarNoteName) {
        String path = mContext.getFilesDir().toString();
        String plistPath = path + "/" + guitarNoteName + ".plist";
        File file = new File(plistPath);
        if (file.exists()) {
            Log.v("DEBUG", plistPath);
        }

        try {
            parsePlistToMap(plistPath);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("over");
    }

    public void addBarNoData(BarNoData barNoData) {
        rootNoteDic.getBarNoDataArray().add(barNoData);
    }

    public void addBarNoDataAtIndex(BarNoData barNoData, int barNo) {
        rootNoteDic.getBarNoDataArray().add(barNo, barNoData);
    }

    public void removeBarNoData(int barNo) {
        ArrayList<BarNoData> barNoDataArrayList = rootNoteDic.getBarNoDataArray();
        barNoDataArrayList.remove(barNo);
        if (barNoDataArrayList.size() > 0) {
            if (barNo == 0) {
                setCurrentEditPos(0, 0, 1);
            } else {
                setCurrentEditPos(barNo - 1, 0, 1);
            }
        } else {
            addBlankBarNoData(-1);
        }
    }

    public void addBlankBarNoData(int barNo) {
        BarNoData barNoData = new BarNoData();
        ArrayList<NoteNoData> noteNoDataArrayList = new ArrayList<>();
        barNoData.setNoteNoDataArray(noteNoDataArrayList);
        addBarNoDataAtIndex(barNoData, barNo + 1);
        addBlankNoteNoData(barNo + 1, 0);

        setCurrentEditPos(barNo + 1, 0, 1);
    }

    public void addNoteNoData(NoteNoData noteNoData, int barNo) {
        BarNoData barNoData = rootNoteDic.getBarNoDataArray().get(barNo);
        if (barNoData == null) {
            Log.e(TAG, "barNoData is null!");
            return;
        }
        barNoData.getNoteNoDataArray().add(noteNoData);
    }

    public void addNoteNoDataAtIndex(NoteNoData noteNoData, int barNo, int noteNo) {
        BarNoData barNoData = rootNoteDic.getBarNoDataArray().get(barNo);
        if (barNoData == null) {
            Log.e(TAG, "barNoData is null!");
            return;
        }
        barNoData.getNoteNoDataArray().add(noteNo, noteNoData);
    }

    public void addBlankNoteNoData(int barNo, int noteNo) {
        NoteNoData noteNoData = new NoteNoData();
        noteNoData.setNoteType("4");
        ArrayList<Note> noteArrayList = new ArrayList<>();
        Note note = new Note();
        note.setStringNo("-1");
        note.setFretNo("-1");
        note.setPlayType("Normal");
        noteArrayList.add(note);
        noteNoData.setNoteArray(noteArrayList);
        addNoteNoDataAtIndex(noteNoData, barNo, noteNo);
    }

    public void removeNoteNoData(int barNo, int noteNo) {
        BarNoData barNoData = rootNoteDic.getBarNoDataArray().get(barNo);
        if (barNoData == null) {
            Log.e(TAG, "barNoData is null!");
            return;
        }
        ArrayList<NoteNoData> noteNoDataArrayList = barNoData.getNoteNoDataArray();
        noteNoDataArrayList.remove(noteNo);

        if (noteNoDataArrayList.size() == 0) {
            removeBarNoData(barNo);
            if (barNo > 0) {
                setCurrentEditPos(barNo - 1, 0, 1);
            } else {
                setCurrentEditPos(barNo, 0, 1);
            }
        } else {
            if (noteNo != 0) {
                setCurrentEditPos(barNo, noteNo - 1, 1);
            }
        }

    }

    private Map<String, Object> parsePlistToMap(String path) throws NoSuchMethodException, IllegalAccessException {
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
        } catch (XmlPullParserException | IOException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            oldGuitarNotes = rootNoteDic.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isNoteChanged() {
        return !oldGuitarNotes.equals(rootNoteDic);
    }


    public void deleteGuitarNotes(String guitarNoteName, int position) {
        guitarNotesFiles.remove(position);
        mContext.deleteFile(guitarNoteName + ".plist");
    }

    public void saveGuitarNotes(String guitarNoteName) {

        parseToPlist(guitarNoteName);
    }

    public void parseToPlist(String guitarNoteName) {

        int barNoDataNum = 0;
        int noteNoDataNum = 0;
        int noteNum = 0;
        BarNoData barNoData;
        NoteNoData noteNoData;
        Note note;
        String fileName = guitarNoteName + ".plist";
        barNoDataNum = rootNoteDic.getBarNoDataArray().size();

        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").getBytes());
            fos.write(("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n").getBytes());
            fos.write(("<plist version=\"1.0\">\n").getBytes());
            fos.write(("<dict>\n").getBytes());
            fos.write(("\t<key>GuitarNotesName</key>\n").getBytes());
            fos.write(("\t<string>" + guitarNoteName + "</string>\n").getBytes());
            fos.write(("\t<key>Speed</key>\n").getBytes());
            fos.write(("\t<string>" + rootNoteDic.getSpeed() + "</string>\n").getBytes());
            fos.write(("\t<key>BarNum</key>\n").getBytes());
            fos.write(("\t<integer>" + barNoDataNum + "</integer>\n").getBytes());
            fos.write(("\t<key>Flat</key>\n").getBytes());
            fos.write(("\t<string>" + rootNoteDic.getFlat() + "</string>\n").getBytes());
            fos.write(("\t<key>GuitarNotes</key>\n").getBytes());
            fos.write(("\t<array>\n").getBytes());

            for (int barNo = 0; barNo < barNoDataNum; barNo++) {
                barNoData = rootNoteDic.getBarNoDataArray().get(barNo);
                fos.write(("\t\t<array>\n").getBytes());
                noteNoDataNum = barNoData.getNoteNoDataArray().size();
                for (int noteNo = 0; noteNo < noteNoDataNum; noteNo++) {
                    noteNoData = barNoData.getNoteNoDataArray().get(noteNo);
                    fos.write(("\t\t\t<dict>\n").getBytes());
                    fos.write(("\t\t\t\t<key>NoteType</key>\n").getBytes());
                    fos.write(("\t\t\t\t<string>" + noteNoData.getNoteType() + "</string>\n").getBytes());
                    fos.write(("\t\t\t\t<key>noteArray</key>\n").getBytes());
                    fos.write(("\t\t\t\t<array>\n").getBytes());

                    noteNum = noteNoData.getNoteArray().size();
                    for (int index = 0; index < noteNum; index++) {
                        note = noteNoData.getNoteArray().get(index);
                        fos.write(("\t\t\t\t\t<dict>\n").getBytes());
                        fos.write(("\t\t\t\t\t\t<key>FretNo</key>\n").getBytes());
                        fos.write(("\t\t\t\t\t\t<integer>" + note.getFretNo() + "</integer>\n").getBytes());
                        fos.write(("\t\t\t\t\t\t<key>PlayType</key>\n").getBytes());
                        fos.write(("\t\t\t\t\t\t<string>" + note.getPlayType() + "</string>\n").getBytes());
                        fos.write(("\t\t\t\t\t\t<key>StringNo</key>\n").getBytes());
                        fos.write(("\t\t\t\t\t\t<integer>" + note.getStringNo() + "</integer>\n").getBytes());
                        fos.write(("\t\t\t\t\t</dict>\n").getBytes());
                    }
                    fos.write(("\t\t\t\t</array>\n").getBytes());
                    fos.write(("\t\t\t</dict>\n").getBytes());
                }
                fos.write(("\t\t</array>\n").getBytes());
            }
            fos.write(("\t</array>\n").getBytes());
            fos.write(("</dict>\n").getBytes());
            fos.write(("</plist>\n").getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BarSize calBarSizeWithNoteNoArray(BarNoData barNoData, float currentMinimWidth, float currentCrotchetaWidth, float currentQuaverWidth, float currentDemiquaverWidth) {

        // 当前小节宽度初始化
        float barWidth = 0;

        // 音符个数初始化
        int minimNum = 0;
        int crotchetaNum = 0;
        int quaverNum = 0;
        int demiquaverNum = 0;

        // 统计音符总数
        String noteType = "";
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

    public LineSize getLineSize(int lineNo) {
        if (lineNo >= notesSizeArray.size()) {
            Log.e(TAG, "lineNo(" + lineNo + ") is out of size(" + notesSizeArray.size() + ")");
            return null;
        }
        return notesSizeArray.get(lineNo);
    }

    public ArrayList<Float> getBarWidthArray(int lineNo) {
        return getLineSize(lineNo).getBarWidthArray();
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

        float lineTotalWidth = getLineTotalWidth();

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
            if (lineBarWidth < lineTotalWidth) {
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
                LineSize lineSize = calLineSize(startBarNo, barNo, minimSum, crotchetaSum, quaverSum, demiquaverSum);
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
        LineSize lineSize = calLineSize(startBarNo, barNoArray.size(), minimSum, crotchetaSum, quaverSum, demiquaverSum);
        notesSizeArray.add(lineSize);

    }

    private LineSize calLineSize(int startBarNo, int endBarNo, int minimSum, int crotchetaSum, int quaverSum, int demiquaverSum) {

        float guitarNotesWidth = getLineTotalWidth();
        ArrayList<BarNoData> barNoArray = getBarNoArray();

        LineSize lineSize = new LineSize();
        lineSize.setStartBarNo(startBarNo);
        lineSize.setBarNum(endBarNo - startBarNo);

        // 音符个数减去最后要丢弃的一小节的音符数，重新计算音符宽度
        float currentDemiquaverWidth = guitarNotesWidth / (demiquaverSum + quaverSum * 1.5f + crotchetaSum * 1.5f * 1.5f + minimSum * 1.5f * 1.5f * 1.5f);
        float currentQuaverWidth = currentDemiquaverWidth * 1.5f;
        float currentCrotchetaWidth = currentQuaverWidth * 1.5f;
        float currentMinimWidth = currentCrotchetaWidth * 1.5f;
        lineSize.setDemiquaverWidth(currentDemiquaverWidth);
        lineSize.setQuaverWidth(currentQuaverWidth);
        lineSize.setCrotchetaWidth(currentCrotchetaWidth);
        lineSize.setMinimWidth(currentMinimWidth);

        // 通过计算得到的音符宽度重新计算小节宽度，保存在数组中
        float lineBarWidth = 0;
        BarSize barSize;
        ArrayList<Float> barWidthArray = new ArrayList<>();
        barWidthArray.add(0.0f);
        for (int i = startBarNo; i < endBarNo - 1; i++) {
            barSize = calBarSizeWithNoteNoArray(barNoArray.get(i), currentMinimWidth, currentCrotchetaWidth, currentQuaverWidth, currentDemiquaverWidth);
            lineBarWidth += barSize.getBarWidth();
            barWidthArray.add(lineBarWidth);
        }
        barWidthArray.add(guitarNotesWidth);
        lineSize.setBarWidthArray(barWidthArray);

        return lineSize;
    }

    public float getLineTotalWidth() {
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        float guitarNotesWidth = wm.getDefaultDisplay().getWidth() - getLineWidth() * 2;
        return guitarNotesWidth;
    }

    public int getSpeed() {
        String speed = getRootNoteDic().getSpeed();
        return Integer.parseInt(speed);
    }

    public String getTotalNoteType() {
        String tempo = getRootNoteDic().getFlat();
        String totalNoteType = tempo.split("/")[1];
        return totalNoteType;
    }

    public float getFlatTime() {

        float flatTime = 0;
        String tempo = getRootNoteDic().getFlat();
        String totalNoteType = tempo.split("/")[1];
        switch (totalNoteType) {
            case NotesModel.TYPE_MINIM:
                flatTime = 0.5f;
                break;
            case NotesModel.TYPE_CROTCHET:
                flatTime = 0.25f;
                break;
            case NotesModel.TYPE_QUAVER:
                flatTime = 0.125f;
                break;
            case NotesModel.TYPE_DEMIQUAVER:
                flatTime = 0.0625f;
                break;
        }
        return flatTime;
    }

    public float getFlatTimeTotal() {
        float flatTimeTotal = 0;
        float flatTime = getFlatTime();
        String tempo = getRootNoteDic().getFlat();
        int beatsPerBar = Integer.parseInt(tempo.split("/")[0]);
        flatTimeTotal = flatTime * beatsPerBar;
        return flatTimeTotal;
    }

    public int getFlatNumInOneBar() {
        String tempo = getRootNoteDic().getFlat();
        return Integer.parseInt(tempo.split("/")[0]);
    }


    public boolean checkBarStateAtBarNo(int barNo) {
        double noteType = 0;
        double noteTypeSum = 0;
        float flatTimeTotal = getFlatTimeTotal();
        ArrayList noteNoArray = getNoteNoArray(barNo);
        for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
            noteType = Double.parseDouble(getNoteType(barNo, noteNo));
            noteTypeSum += 1.0f / noteType;
        }

        return (noteTypeSum == flatTimeTotal);
    }

    public void removeBlankNote() {
        // 获取音符编辑框所在的位置，包括小节序号、音符序号、吉他弦号
        int barNo = currentEditNote.getBarNo();
        int noteNo = currentEditNote.getNoteNo();

        ArrayList<Note> notesArray = getNotesArray(barNo, noteNo);
        Note note = notesArray.get(0);
        int tmpFretNo = Integer.parseInt(note.getFretNo());
        int tmpStringNo = Integer.parseInt(note.getStringNo());
        if (tmpFretNo == -1 && tmpStringNo == -1) {
            notesArray.remove(note);
        }
    }

    public void addNote(Note note, int barNo, int noteNo) {
        getNotesArray(barNo, noteNo).add(note);
    }
}
