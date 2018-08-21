//
// Created by CHT1HTSH3236 on 2018/8/14.
//
#include "FFT.h"
#include <complex>
#include <cstdio>

using namespace std;

void fft2 (complex<double>* X, int N);
void separate (complex<double>* a, int n);

JNIEXPORT jshortArray JNICALL Java_com_example_rmd2k_guitarstudio_1android_Tools_Tuner_FFT_fft
  (JNIEnv *env, jclass jObj, jshortArray array) {
    jshort *shortArray = env->GetShortArrayElements(array, NULL);
    jint length = env->GetArrayLength(array);

    complex<double>* b = new complex<double>[length / 2];
    for (int i = 0; i < length; i++) {

        complex<double> tmp(shortArray[i], 0);
        b[i] = tmp;
    }
    fft2(b, length);

    env->ReleaseShortArrayElements(array, shortArray, 0);

    short *fftResult = new short[length];
    for (int i = 0; i < length; i++) {
        fftResult[i] = (short) b[i].real();
    }
    jshortArray resultArray = env->NewShortArray(length);
    env->SetShortArrayRegion(resultArray, 0, length, fftResult);
    delete b;
    return resultArray;
  }

  void separate (complex<double>* a, int n) {
      complex<double>* b = new complex<double>[n/2];  // get temp heap storage
      for(int i=0; i<n/2; i++)    // copy all odd elements to heap storage
          b[i] = a[i*2+1];
      for(int i=0; i<n/2; i++)    // copy all even elements to lower-half of a[]
          a[i] = a[i*2];
      for(int i=0; i<n/2; i++)    // copy all odd (from heap) to upper-half of a[]
          a[i+n/2] = b[i];
      delete[] b;                 // delete heap storage
  }

  void fft2 (complex<double>* X, int N) {
      if(N < 2) {
          // bottom of recursion.
          // Do nothing here, because already X[0] = x[0]
      } else {
          separate(X,N);      // all evens to lower half, all odds to upper half
          fft2(X,     N/2);   // recurse even items
          fft2(X+N/2, N/2);   // recurse odd  items
          // combine results of two half recursions
          for(int k=0; k<N/2; k++) {
              complex<double> e = X[k    ];   // even
              complex<double> o = X[k+N/2];   // odd
                           // w is the "twiddle-factor"
              complex<double> w = exp( complex<double>(0,-2.*M_PI*k/N) );
              X[k    ] = e + w * o;
              X[k+N/2] = e - w * o;
          }
      }
  }
