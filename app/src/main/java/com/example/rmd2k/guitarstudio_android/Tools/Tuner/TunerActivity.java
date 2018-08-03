package com.example.rmd2k.guitarstudio_android.Tools.Tuner;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.R;

public class TunerActivity extends AppCompatActivity {

    TextView tvCurrNote;
    Button btnE;
    Button btnA;
    Button btnD;
    Button btnG;
    Button btnB;
    Button btnE1;

    boolean isRecording = false;


    String[] tunerNotes = { "E1", "B", "G", "D", "A", "E" };
    double currStandardFrequency;
    double[] standardFrequencies = { 329.6276, 246.9517, 195.9977, 146.8324, 110.0000, 82.4069 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        initView();

        // 开始监测频率
        startTuner();

    }

    private void initView() {
        tvCurrNote = findViewById(R.id.tvCurrNote);
        btnE = findViewById(R.id.btnE);
        btnA = findViewById(R.id.btnA);
        btnD = findViewById(R.id.btnD);
        btnG = findViewById(R.id.btnG);
        btnB = findViewById(R.id.btnB);
        btnE1 = findViewById(R.id.btnE1);

        btnE.setBackgroundColor(Color.RED);
        btnA.setBackgroundColor(Color.GRAY);
        btnD.setBackgroundColor(Color.GRAY);
        btnG.setBackgroundColor(Color.GRAY);
        btnB.setBackgroundColor(Color.GRAY);
        btnE1.setBackgroundColor(Color.GRAY);
    }

    public void selectTunerNote(View view) {
        btnE.setBackgroundColor(Color.GRAY);
        btnA.setBackgroundColor(Color.GRAY);
        btnD.setBackgroundColor(Color.GRAY);
        btnG.setBackgroundColor(Color.GRAY);
        btnB.setBackgroundColor(Color.GRAY);
        btnE1.setBackgroundColor(Color.GRAY);
        view.setBackgroundColor(Color.RED);

        int stringNo = Integer.parseInt(view.getTag().toString());
        tvCurrNote.setText(tunerNotes[stringNo - 1]);
        currStandardFrequency = standardFrequencies[stringNo - 1];
//        switch (stringNo) {
//            case 1:
//                btnE1.setBackgroundColor(Color.RED);
//                break;
//            case 2:
//                btnB.setBackgroundColor(Color.RED);
//                break;
//            case 3:
//                btnG.setBackgroundColor(Color.RED);
//                break;
//            case 4:
//                btnD.setBackgroundColor(Color.RED);
//                break;
//            case 5:
//                btnA.setBackgroundColor(Color.RED);
//                break;
//            case 6:
//                btnE.setBackgroundColor(Color.RED);
//                break;
//        }
    }

    private void startTuner() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final int audioRecordMinBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, audioRecordMinBufferSize);
                short[] buffer = new short[audioRecordMinBufferSize];
                audioRecord.startRecording();
                isRecording = true;

                int audioTrackMinBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, audioTrackMinBufferSize, AudioTrack.MODE_STREAM);
                audioTrack.play();

                final int maxCR = Visualizer.getMaxCaptureRate();
                Visualizer visualizer = new Visualizer(audioTrack.getAudioSessionId());
                visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
                visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                    @Override
                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                        //Log.e("KONG", "aaa");
                    }

                    @Override
                    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                        //Log.e("KONG", "aaa:" + fft.length);
                    }
                }, maxCR / 2, true, true);
                visualizer.setEnabled(true);

                while (isRecording) {
                    int bufferReadReault = audioRecord.read(buffer, 0, audioRecordMinBufferSize);
                    audioTrack.write(buffer, 0, bufferReadReault);

                    for (int i = 0; i < bufferReadReault; i++) {
                        //Log.i("KONG", String.valueOf(buffer[i]));
                    }
                }
                audioRecord.stop();
                audioTrack.stop();
                visualizer.setEnabled(false);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRecording = false;
    }


}
