package com.dailyyoga.cn;

import android.content.Context;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 1:59 PM
 * @description:
 */
public class YogaContext {

    private static Context CONTEXT;

    public static void setContext(Context context) {
        YogaContext.CONTEXT = context;
    }

    public static Context getContext() {
        return CONTEXT;
    }
}
