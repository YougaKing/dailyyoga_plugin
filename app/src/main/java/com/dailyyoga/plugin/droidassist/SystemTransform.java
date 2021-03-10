package com.dailyyoga.plugin.droidassist;

import android.media.AudioTrack;
import android.util.Log;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 2:07 PM
 * @description:
 */
public class SystemTransform {

    private static void println(String args) {
        LogTransform.println(Log.ERROR, SystemTransform.class.getName(), args);
    }

    public static void loadLibrary(String args, String libName) {
        println(args + "_public void java.lang.System.loadLibrary(java.lang.String)");
        try {
            System.loadLibrary(libName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void stop(String args, AudioTrack audioTrack) {
        println(args + "_public void android.media.AudioTrack.stop()");
        try {
            audioTrack.stop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
