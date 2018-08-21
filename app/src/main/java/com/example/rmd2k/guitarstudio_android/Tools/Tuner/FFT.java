package com.example.rmd2k.guitarstudio_android.Tools.Tuner;

public class FFT {
    static {
        System.loadLibrary("FFT");
    }

    public static native short[] fft(short[] fftData);
}
