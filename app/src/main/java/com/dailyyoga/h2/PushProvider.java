package com.dailyyoga.h2;

import android.app.Application;

import com.dailyyoga.h2.PlatformProvider.PlatformPushListener;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 11:53
 * @description:
 */
public interface PushProvider {

    default void initialize(Application application, PlatformPushListener listener) {}

    default String setOppoAliases() {
        return "";
    }

    default void setVivoAliases(Application application, String uid) {}
}
