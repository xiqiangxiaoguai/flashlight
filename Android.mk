# Copyright 2007-2008 The Android Open Source Project

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := FlashLight

LOCAL_CERTIFICATE := platform

# Builds against the public SDK
#LOCAL_SDK_VERSION := current

LOCAL_STATIC_JAVA_LIBRARIES += android-common jsr305

LOCAL_REQUIRED_MODULES := SoundRecorder

LOCAL_MODULE_PATH := $(TARGET_OUT_CUSTOM_APPS)

#LOCAL_PROGUARD_FLAG_FILES := proguard.flags

include $(BUILD_PACKAGE)

# This finds and builds the test apk as well, so a single make does both.
include $(call all-makefiles-under,$(LOCAL_PATH))
