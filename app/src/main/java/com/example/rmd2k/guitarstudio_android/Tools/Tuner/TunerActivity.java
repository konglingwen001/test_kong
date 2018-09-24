package com.example.rmd2k.guitarstudio_android.Tools.Tuner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.DataModel.NotesModel;
import com.example.rmd2k.guitarstudio_android.R;
import com.example.rmd2k.guitarstudio_android.Utils.NotePlayUtils;
import com.example.rmd2k.guitarstudio_android.Utils.TunerUtils;

import java.lang.ref.WeakReference;

public class TunerActivity extends AppCompatActivity {

    private static final double PI = 3.141592654;
    private static final int PERMISSION_RECORD_AUDIO = 0;

    NotesModel notesModel;
    NotePlayUtils notePlayUtils;
    TunerUtils tunerUtils;
    Context mContext;

    InstrumentPanelView instrumentPanelView;
    TextView tvCurrNote;
    Button btnE;
    Button btnA;
    Button btnD;
    Button btnG;
    Button btnB;
    Button btnE1;
    Switch swAudoTuner;

    boolean isRecording = false;
    Handler myHandler;

    int currStringNo = 6;
    int preStringNo = 6;
    String[] tunerNotes = { "E1", "B", "G", "D", "A", "E" };
    double currStandardFrequency;
    double[] standardFrequencies = { 329.6276, 246.9517, 195.9977, 146.8324, 110.0000, 82.4069 };
    String[] permissions = { Manifest.permission.RECORD_AUDIO };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        mContext = getApplicationContext();
        notesModel = NotesModel.getInstance(mContext);
        notePlayUtils = NotePlayUtils.getInstance(mContext);

        initView();

        myHandler = new MyHandler(this);

        tunerUtils = new TunerUtils();

        if (checkPermission() == true) {
            startTuner();
        }

    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTuner();
            } else {
                finish();
            }
        }
    }

    class MyHandler extends Handler {

        WeakReference<TunerActivity> mWeakReference;
        public MyHandler(TunerActivity tunerActivity) {
            super();
            mWeakReference = new WeakReference<>(tunerActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final TunerActivity tunerActivity = mWeakReference.get();

            float frequency = (float)msg.obj;

            for (int i = 0; i < standardFrequencies.length; i++) {
                if (frequency >= standardFrequencies[i] - 25 && frequency <= standardFrequencies[i] + 25) {
                    currStringNo = i + 1;
                    break;
                }
            }
            if (preStringNo != currStringNo && swAudoTuner.isChecked()) {
                preStringNo = currStringNo;
                tvCurrNote.setText(tunerNotes[currStringNo - 1]);
                setSelected(currStringNo);
                instrumentPanelView.setCurrStringNo(currStringNo);
            }

            instrumentPanelView.setCurrFrequency(frequency);
            tunerActivity.instrumentPanelView.invalidate();
        }
    }

    private void initView() {
        instrumentPanelView = findViewById(R.id.instrumentPanelView);
        tvCurrNote = findViewById(R.id.tvCurrNote);
        btnE = findViewById(R.id.btnE);
        btnA = findViewById(R.id.btnA);
        btnD = findViewById(R.id.btnD);
        btnG = findViewById(R.id.btnG);
        btnB = findViewById(R.id.btnB);
        btnE1 = findViewById(R.id.btnE1);
        swAudoTuner = findViewById(R.id.swAutoTuner);

        btnE.setBackgroundColor(Color.RED);
        btnA.setBackgroundColor(Color.GRAY);
        btnD.setBackgroundColor(Color.GRAY);
        btnG.setBackgroundColor(Color.GRAY);
        btnB.setBackgroundColor(Color.GRAY);
        btnE1.setBackgroundColor(Color.GRAY);
    }

    private void setSelected(int stringNo) {

        btnE.setBackgroundColor(Color.GRAY);
        btnA.setBackgroundColor(Color.GRAY);
        btnD.setBackgroundColor(Color.GRAY);
        btnG.setBackgroundColor(Color.GRAY);
        btnB.setBackgroundColor(Color.GRAY);
        btnE1.setBackgroundColor(Color.GRAY);

        switch (stringNo) {
            case 1:
                btnE1.setBackgroundColor(Color.RED);
                break;
            case 2:
                btnB.setBackgroundColor(Color.RED);
                break;
            case 3:
                btnG.setBackgroundColor(Color.RED);
                break;
            case 4:
                btnD.setBackgroundColor(Color.RED);
                break;
            case 5:
                btnA.setBackgroundColor(Color.RED);
                break;
            case 6:
                btnE.setBackgroundColor(Color.RED);
                break;
        }
    }

    public void selectTunerNote(View view) {

        int stringNo = Integer.parseInt(view.getTag().toString());

        notePlayUtils.playNote(stringNo, 0);

        setSelected(stringNo);

        tvCurrNote.setText(tunerNotes[stringNo - 1]);
        instrumentPanelView.setCurrStringNo(stringNo);
    }

    private void startTuner() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final int audioRecordMinBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, audioRecordMinBufferSize);

                audioRecord.startRecording();
                isRecording = true;

                int bufSize = 8192;
                short[] buffer = new short[bufSize];
                while (isRecording) {
                    int read = audioRecord.read(buffer, 0, bufSize);

                    float frequency = tunerUtils.handleFFT(buffer, read);
                    if (frequency == 0) {
                        continue;
                    }

                    // invalidate instrumentPanelView
                    Message msg = Message.obtain();
                    msg.obj = frequency;
                    myHandler.sendMessage(msg);
                }
                audioRecord.stop();
            }
        }).start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRecording = false;
    }


}
