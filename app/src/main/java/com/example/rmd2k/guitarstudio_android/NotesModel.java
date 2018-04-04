package com.example.rmd2k.guitarstudio_android;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;
import android.view.Window;
import android.view.WindowManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by CHT1HTSH3236 on 2018/3/21.
 */

class NotesModel {

    static final double MINIM_WIDTH = 15 * 1.5 * 1.5 * 1.5;
    static final double CROTCHETA_WIDTH = 15 * 1.5 * 1.5;
    static final double QUAVER_WIDTH = 15 * 1.5;
    static final double DEMIQUAVER_WIDTH = 15;

    private Map rootNoteDic;
    private ArrayList notesSizeArray;

    private static final NotesModel ourInstance = new NotesModel();

    static NotesModel getInstance() {
        return ourInstance;
    }

    private NotesModel() {
    }

    public Map getRootNoteDic() {
        return rootNoteDic;
    }

    public int getBarNum() {
        // TODO
        return 10;
    }

    public ArrayList getBar(int barNo) {
        // TODO
        return new ArrayList();
    }

    public void setGuitarNotesWithNotesTitle(Context context, String notesTitle) {
        rootNoteDic = getGuitarNotesFromGuitarNotesTitle(context, notesTitle);
    }

    public Map getGuitarNotesFromGuitarNotesTitle(Context context, String guitarNotesTitle) {

        String path = context.getFilesDir().toString();
        String plistPath = path + "/" + guitarNotesTitle + ".plist";
        File file = new File(plistPath);
        if (file.exists()) {
            Log.v("DEBUG", plistPath);
        }

        Map rootDic = parsePlistToMap(context, plistPath);
        return rootDic;
    }

    private Map parsePlistToMap(Context context, String path) {
        Stack<Object> stack = new Stack<>();
        AssetManager manager = context.getAssets();
        String key_t = "";
        try {
            InputStream in = manager.open("music.plist");
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
                            key_t = parser.nextText();
                            rootNoteDic.put(key_t, "");
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList getBarNoArray() {
        return new ArrayList();
    }

    public Map calBarSizeWithNoteNoArray(Object bar, double currentDemiquaverWidth , double currentQuaverWidth , double currentCrotchetaWidth , double currentMinimWidth) {

        ArrayList noteNoArray = (ArrayList)bar;

        // 当前小节宽度初始化
        double barWidth = 0;

        // 音符个数初始化
        int minimNum = 0;
        int crotchetaNum = 0;
        int quaverNum = 0;
        int demiquaverNum = 0;

        // 统计音符总数
        String noteType = "";//[notesDic valueForKey:@"NoteType"];
        for (int noteNo = 0; noteNo < noteNoArray.size(); noteNo++) {
            noteType = ((Map)(noteNoArray.get(noteNo))).get("NoteType").toString();
            if (noteType.equals("2")) {
                minimNum++;
            } else if (noteType.equals("4")) {
                crotchetaNum++;
            } else if (noteType.equals("8")) {
                quaverNum++;
            } else if (noteType.equals("16")) {
                demiquaverNum++;
            }
        }

        // 最后一个音符宽度
        if (noteType.equals("2")) {
            minimNum++;
        } else if (noteType.equals("4")) {
            crotchetaNum++;
        } else if (noteType.equals("8")) {
            quaverNum++;
        } else if (noteType.equals("16")) {
            demiquaverNum++;
        }

        // 计算小节宽度总和
        barWidth = minimNum * currentMinimWidth + crotchetaNum * currentCrotchetaWidth + quaverNum * currentQuaverWidth + demiquaverNum * currentDemiquaverWidth;

        // 保存结果并返回
        Map resultDic = new HashMap();
        resultDic.put("barWidth", barWidth);
        resultDic.put("mininNum", minimNum);
        resultDic.put("crotchetaNum", crotchetaNum);
        resultDic.put("quaverNum", quaverNum);
        resultDic.put("demiquaverNum", demiquaverNum);

        return resultDic;
    }

    public void getNotesSize() {

    }

    public void calNotesSize(Context context) {

        int minimNum = 0;           // 单个小节二分音符个数
        int crotchetaNum = 0;       // 单个小节四分音符个数
        int quaverNum = 0;          // 单个小节八分音符个数
        int demiquaverNum = 0;      // 单个小节十六分音符个数

        int minimSum = 0;           // 整行二分音符总数
        int crotchetaSum = 0;       // 整行四分音符总数
        int quaverSum = 0;          // 整行八分音符总数
        int demiquaverSum = 0;      // 整行十六分音符总数

        double currentMinimWidth = 0;
        double currentCrotchetaWidth = 0;
        double currentQuaverWidth = 0;
        double currentDemiquaverWidth = 0;

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        double guitarNotesWidth = wm.getDefaultDisplay().getWidth() - 100;

        int startBarNo = 0;         // 每行第一小节的barNo
        double lineBarWidth = 0;     // 行所有小节总宽度
        ArrayList barNoArray = getBarNoArray();
        notesSizeArray.clear();
        for (int barNo = 0; barNo < barNoArray.size(); barNo++) {
            Map sizeDic = calBarSizeWithNoteNoArray(barNoArray.get(barNo), MINIM_WIDTH, CROTCHETA_WIDTH, QUAVER_WIDTH, DEMIQUAVER_WIDTH);

            minimNum = Integer.parseInt(sizeDic.get("minimNum").toString());
            crotchetaNum = Integer.parseInt(sizeDic.get("crotchetaNum").toString());
            quaverNum = Integer.parseInt(sizeDic.get("quaverNum").toString());
            demiquaverNum = Integer.parseInt(sizeDic.get("demiquaverNum").toString());

            // 计算小节宽度总和
            lineBarWidth += Double.parseDouble(sizeDic.get("barWidth").toString());

            // 当前行所有小节宽度总和小于吉他谱宽度时，累加音符数
            if (lineBarWidth < guitarNotesWidth) {
                minimSum += minimNum;
                crotchetaSum += crotchetaNum;
                quaverSum += quaverNum;
                demiquaverSum += demiquaverNum;
            } else {
                Map<String, Object> lineDic = new HashMap();
                lineDic.put("startBarNo", startBarNo);
                lineDic.put("barNum", barNo - startBarNo);

                // 音符个数减去最后要丢弃的一小节的音符数，重新计算音符宽度
                currentDemiquaverWidth = guitarNotesWidth / (demiquaverSum + quaverSum * 1.5 + crotchetaSum * 1.5 * 1.5 + minimSum * 1.5 * 1.5 * 1.5);
                currentQuaverWidth = currentDemiquaverWidth * 1.5;
                currentCrotchetaWidth = currentQuaverWidth * 1.5;
                currentMinimWidth = currentCrotchetaWidth * 1.5;
                lineDic.put("demiquaverWidth", currentDemiquaverWidth);
                lineDic.put("quaverWidth", currentQuaverWidth);
                lineDic.put("crotchetaWidth", currentCrotchetaWidth);
                lineDic.put("minimWidth", currentMinimWidth);

                // 通过计算得到的音符宽度重新计算小节宽度，保存在数组中
                lineBarWidth = 0;
                ArrayList<Double> barWidthArray = new ArrayList();
                barWidthArray.add(0.0);
                for (int i = startBarNo; i < barNo - 1; i++) {
                    Map resultDic = calBarSizeWithNoteNoArray(barNoArray.get(i), currentDemiquaverWidth, currentQuaverWidth, currentCrotchetaWidth, currentMinimWidth);
                    lineBarWidth += Double.parseDouble(resultDic.get("barWidth").toString());
                    barWidthArray.add(lineBarWidth);
                }
                barWidthArray.add(guitarNotesWidth);
                lineDic.put("barWidthArray", barWidthArray.clone());
                notesSizeArray.add(lineDic);

                // 重置参数
                // 下一行小节开始序号，设置为丢弃的小节序号
                // 小节序号减一，从丢弃的小节序号重新开始计算
                startBarNo = barNo--;

                // 行所有小节宽度初始值设置为丢弃小节的宽度
                lineBarWidth = 0;
                minimSum = 0;
                crotchetaSum = 0;
                quaverSum = 0;
                demiquaverSum = 0;

            }


        }

        // 最后一行，以最小音符长度计算尺寸

        // 计算行小节宽度
        ArrayList<Double> barWidthArray = new ArrayList();
        lineBarWidth = 0;
        barWidthArray.add(0.0);
        for (int i = startBarNo; i < barNoArray.size(); i++) {
            Map resultDic = calBarSizeWithNoteNoArray(barNoArray.get(i), currentDemiquaverWidth, currentQuaverWidth, currentCrotchetaWidth, currentMinimWidth);
            lineBarWidth += Double.parseDouble(resultDic.get("barWidth").toString());
            barWidthArray.add(lineBarWidth);
        }

        // 保存
        Map<String, Object> lineDic = new HashMap();
        lineDic.put("startBarNo", startBarNo);
        lineDic.put("barNum", barNoArray.size() - startBarNo);
        lineDic.put("demiquaverWidth", DEMIQUAVER_WIDTH);
        lineDic.put("quaverWidth", QUAVER_WIDTH);
        lineDic.put("crotchetaWidth", CROTCHETA_WIDTH);
        lineDic.put("minimWidth", MINIM_WIDTH);
        lineDic.put("barWidthArray", barWidthArray.clone());
        notesSizeArray.add(lineDic);

    }
}
