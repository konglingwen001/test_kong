package com.example.rmd2k.guitarstudio_android.Utils;

import android.util.Log;

public class TunerUtils {

    private float mLastComputedFreq = 0;

    public float handleFFT(short[] buffer, int length) {
        if (length > 0) {
            final double intensity = averageIntensity(buffer, length);
            int maxZeroCrossing = (int) (250 * (length / 8192.0));

            if (intensity >= 50 && zeroCrossingCount(buffer) <= maxZeroCrossing) {

                float freq = getPitch(buffer, length / 4, length, 44100, 50, 500);
                if (Math.abs(freq - mLastComputedFreq) <= 5f) {
                    //mPitchDetectionListener.onPitchDetected(freq, intensity);
                    Log.e("KONG", "freq:" + freq);
                    return freq;
                }
                mLastComputedFreq = freq;
            }
        }
        return 0;
    }

    private float getPitch(short[] data, int windowSize, int frames, float sampleRate, float minFreq, float maxFreq) {

        float maxOffset = sampleRate / minFreq;
        float minOffset = sampleRate / maxFreq;


        int minSum = Integer.MAX_VALUE;
        int minSumLag = 0;
        int[] sums = new int[Math.round(maxOffset) + 2];

        for (int lag = (int) minOffset; lag <= maxOffset; lag++) {
            int sum = 0;
            for (int i = 0; i < windowSize; i++) {

                int oldIndex = i - lag;

                int sample = ((oldIndex < 0) ? data[frames + oldIndex] : data[oldIndex]);

                sum += Math.abs(sample - data[i]);
            }

            sums[lag] = sum;

            if (sum < minSum) {
                minSum = sum;
                minSumLag = lag;
            }
        }

        // quadratic interpolation
        float delta = (float) (sums[minSumLag + 1] - sums[minSumLag - 1]) / ((float)
                (2 * (2 * sums[minSumLag] - sums[minSumLag + 1] - sums[minSumLag - 1])));
        return sampleRate / (minSumLag + delta);
    }

    private int zeroCrossingCount(short[] data) {
        int len = data.length;
        int count = 0;
        boolean prevValPositive = data[0] >= 0;
        for (int i = 1; i < len; i++) {
            boolean positive = data[i] >= 0;
            if (prevValPositive == !positive)
                count++;

            prevValPositive = positive;
        }
        return count;
    }

    private double averageIntensity(short[] data, int frames) {

        double sum = 0;
        for (int i = 0; i < frames; i++) {
            sum += Math.abs(data[i]);
        }
        return sum / frames;

    }
}
