package com.dailyyoga.plugin

import com.android.build.gradle.AppExtension

class DailyyogaTransformHelper {

    DailyyogaExtension extension
    AppExtension android

    boolean disableDailyyogaIncremental

    DailyyogaTransformHelper(DailyyogaExtension extension, AppExtension android) {
        this.extension = extension
        this.android = android
    }

}