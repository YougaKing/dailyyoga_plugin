package com.dailyyoga.plugin

import org.gradle.internal.reflect.Instantiator

class DailyyogaExtension {

    public boolean debug = false
    public DailyyogaSDKExtension sdk

    DailyyogaExtension(Instantiator ins) {
        sdk = ins.newInstance(DailyyogaSDKExtension)
    }
}