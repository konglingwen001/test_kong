LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := FFT

LOCAL_SRC_FILES := FFT.cpp

include $(BUILD_SHARED_LIBRARY)