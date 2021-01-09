package com.dailyyoga.plugin

import com.android.build.gradle.AppExtension

class DailyyogaTransformHelper {

    DailyyogaExtension extension
    AppExtension android

    boolean disableDailyyogaMultiThread
    boolean disableDailyyogaIncremental

    DailyyogaTransformHelper(DailyyogaExtension extension, AppExtension android) {
        this.extension = extension
        this.android = android
    }


    void onTransform() {
        println("sensorsAnalytics {\n" + extension + "\n}")
        ArrayList<String> excludePackages = extension.exclude
        if (excludePackages != null) {
            exclude.addAll(excludePackages)
        }
        ArrayList<String> includePackages = extension.include
        if (includePackages != null) {
            include.addAll(includePackages)
        }
        createSensorsAnalyticsHookConfig()
    }

}