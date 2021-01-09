package com.dailyyoga.plugin.miit

import com.android.build.gradle.AppExtension

class DailyyogaMIITTransformHelper {

    DailyyogaMIITExtension extension
    AppExtension android

    boolean disableDailyyogaMultiThread
    boolean disableDailyyogaIncremental

    DailyyogaMIITTransformHelper(DailyyogaMIITExtension extension, AppExtension android) {
        this.extension = extension
        this.android = android
    }


    void onTransform() {
        println("DailyyogaMIIT {\n" + extension + "\n}")
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