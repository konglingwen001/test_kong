package com.example.rmd2k.guitarstudio_android.Tools.Tuner;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rmd2k.guitarstudio_android.R;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_2D;

public class TunerActivity extends AppCompatActivity {

    private final static double PI = 3.141592654;

    InstrumentPanelView instrumentPanelView;
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
        instrumentPanelView = findViewById(R.id.instrumentPanelView);
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
        instrumentPanelView.setCurrStandardFrequency(currStandardFrequency);
/*
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
*/
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

                while (isRecording) {
                    int N = audioRecord.read(buffer, 0, audioRecordMinBufferSize);

                    double real;
                    double image;
                    double fftData[] = new double[N * 2];
                    double magnitude[] = new double[N / 2];
                    for (int i = 0; i < N - 1; i++) {
                        fftData[2 * i] = buffer[i];
                        fftData[2 * i + 1] = 0;
                    }

                    DoubleFFT_1D fft = new DoubleFFT_1D(N);
                    fft.complexForward(fftData);

                    for (int i = 0; i < N / 2 - 1; i++) {
                        real = fftData[2 * i];
                        image = fftData[2 * i + 1];
                        magnitude[i] = Math.sqrt(real * real + image * image);
                    }

                    double max_magnitude = -999999999;
                    int max_index = -1;
                    for (int i = 0; i< N / 2 - 1; i++) {
                        if (magnitude[i] > max_magnitude) {
                            max_magnitude = magnitude[i];
                            max_index = i;
                        }
                    }

                    double dominantFreq = max_index * 44100 / N;
                    Log.e("KONG", "Frequency:" + dominantFreq);

                    instrumentPanelView.setCurrFrequency(dominantFreq);
                    instrumentPanelView.invalidate();

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
