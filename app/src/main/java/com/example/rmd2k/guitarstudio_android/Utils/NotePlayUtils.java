package com.example.rmd2k.guitarstudio_android.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class NotePlayUtils {

    private String currFolder = "";
    private ArrayList<String> lstFiles = new ArrayList<>();
    private SoundPool soundPool;
    private ArrayList<Integer> mArr = new ArrayList<>();
    private boolean isOggFilesInited = false;
    private boolean isSoundPoolInited = false;
    private int[] startNote = { 25, 20, 16, 11, 6, 1 };

    private NotePlayThread notePlayThread = null;
    private Handler myHandler = null;

    private static Context mContext = null;
    private static NotePlayUtils instance = null;

    public static NotePlayUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NotePlayUtils();
            mContext = context;
            instance.initNotePlayer();
        }

        return instance;
    }

    private NotePlayUtils() {
    }

    class MyHandler extends Handler {

        private final WeakReference<NotePlayUtils> mNotePlayUtils;
        public MyHandler(NotePlayUtils notePlayUtils) {
            mNotePlayUtils = new WeakReference<>(notePlayUtils);
        }

        @Override
        public void handleMessage(Message msg) {
            NotePlayUtils notePlayUtils = mNotePlayUtils.get();
            notePlayUtils.soundPool.play(msg.what, 1, 1, 1, 0, 1);
        }
    }

    private class NotePlayThread extends Thread {

        @Override
        public void run() {
            Looper.prepare();
            myHandler = new MyHandler(instance);
            Looper.loop();
        }
    }

    private void initNotePlayer() {

        initSoundPool();

        if (!isOggFilesInited) {
            initOggFileList("main");
        }

        loadSoundPool();

        notePlayThread = new NotePlayThread();
        notePlayThread.start();

        isSoundPoolInited = true;
    }

    public void initOggFileList(String folder) {
        for (int i = 0; i < 44; i++) {
            lstFiles.add(i + ".ogg");
        }
        isOggFilesInited = true;
        currFolder = folder;
    }

    private void initSoundPool() {
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        AudioAttributes attributes = new AudioAttributes.Builder().
                setLegacyStreamType(AudioAttributes.CONTENT_TYPE_UNKNOWN).
                build();
        builder.setAudioAttributes(attributes);
        soundPool = builder.build();
    }

    private void loadSoundPool() {
        int soundId;
        for (int i = 1; i <= lstFiles.size(); i++) {
            soundId = soundPool.load(getOggPath(lstFiles.get(i - 1)), 1);
            mArr.add(soundId);
        }
    }

    public ArrayList<String> getOggFiles() {
        return lstFiles;
    }

    private AssetFileDescriptor getOggPath(String name) {
        AssetFileDescriptor fd = null;
        try {
            fd = mContext.getAssets().openFd(currFolder + "/" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fd;
    }

    public void playNote(int index) {

        Message msg = Message.obtain();
        msg.what = mArr.get(index);
        myHandler.sendMessage(msg);
    }

    public void playNote(int stringNo, int fretNo) {

        int position = startNote[stringNo - 1] + fretNo;
        int soundId = mArr.get(position);

        Message msg = Message.obtain();
        msg.what = soundId;
        myHandler.sendMessage(msg);

    }

    public void stopPlay() {
        //soundPool.
    }

}
