package com.dailyyoga.plugin.droidassist;

import android.util.Log;

import com.dailyyoga.h2.util.PersistencePreferencesUtil;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/5/21 9:09 AM
 * @description:
 */
public class LogTransform {

    public static boolean isLoggable(String tag, int level) {
        boolean open = PersistencePreferencesUtil.getBoolean(LogTransform.class.getName(), false);
        return open;
    }

    public static void println(int priority, String tag, String msg) {
        Log.println(priority, tag, msg);
    }

    public static int v(String args, String tag, String msg) {
        if (!isLoggable(tag, Log.VERBOSE)) return 0;
        return Log.v(tag, msg);
    }

    public static int v(String args, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, Log.VERBOSE)) return 0;
        return Log.v(tag, msg, tr);
    }

    public static int d(String args, String tag, String msg) {
        if (!isLoggable(tag, Log.DEBUG)) return 0;
        return Log.d(tag, msg);
    }

    public static int d(String args, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, Log.DEBUG)) return 0;
        return Log.d(tag, msg, tr);
    }

    public static int i(String args, String tag, String msg) {
        if (!isLoggable(tag, Log.INFO)) return 0;
        return Log.i(tag, msg);
    }

    public static int i(String args, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, Log.INFO)) return 0;
        return Log.i(tag, msg, tr);
    }

    public static int w(String args, String tag, String msg) {
        if (!isLoggable(tag, Log.WARN)) return 0;
        return Log.w(tag, msg);
    }

    public static int w(String args, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, Log.WARN)) return 0;
        return Log.w(tag, msg, tr);
    }

    public static int w(String args, String tag, Throwable tr) {
        if (!isLoggable(tag, Log.WARN)) return 0;
        return Log.w(tag, tr);
    }

    public static int e(String args, String tag, String msg) {
        if (!isLoggable(tag, Log.ERROR)) return 0;
        return Log.e(tag, msg);
    }

    public static int e(String args, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, Log.ERROR)) return 0;
        return Log.e(tag, msg, tr);
    }

    public static int wtf(String args, String tag, String msg) {
        if (!isLoggable(tag, Log.ERROR)) return 0;
        return Log.wtf(tag, msg);
    }

    public static int wtf(String args, String tag, String msg, Throwable tr) {
        if (!isLoggable(tag, Log.ERROR)) return 0;
        return Log.wtf(tag, msg, tr);
    }

    public static int wtf(String args, String tag, Throwable tr) {
        if (!isLoggable(tag, Log.ERROR)) return 0;
        return Log.wtf(tag, tr);
    }

    public static int println(String args, int priority, String tag, String msg) {
        if (!isLoggable(tag, priority)) return 0;
        return Log.println(priority, tag, msg);
    }
}
