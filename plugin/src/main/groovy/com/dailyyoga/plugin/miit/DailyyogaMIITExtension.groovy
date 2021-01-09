package com.dailyyoga.plugin.miit

import org.gradle.internal.reflect.Instantiator

class DailyyogaMIITExtension {

    public boolean debug = false
    public DailyyogaMIITSDKExtension sdk

    DailyyogaMIITExtension(Instantiator ins) {
        sdk = ins.newInstance(DailyyogaMIITSDKExtension)
    }
}