package com.dailyyoga.h2;

import android.app.Activity;
import android.content.Intent;

import com.dailyyoga.h2.components.analytics.LoginClickSource;

import cn.sharesdk.framework.Platform;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 11:53
 * @description:
 */
public interface LoginProvider {

    int QUICK_LOGIN_ICON = 111;
    int OCCASION_LOGIN_ICON = 112;

    default Platform platform() {
        return null;
    }

    default int icon(int iconType) {
        return 0;
    }

    default String name() {
        return null;
    }

    @LoginClickSource
    default String loginClickSource() {
        return null;
    }

    default boolean isAvailable() {
        return false;
    }

    default void initializeLogin(Activity activity) {}

    default void login(Activity activity, PlatformProvider.PlatformLoginListener listener) {}

    default boolean onActivityResult(int requestCode, int resultCode, Intent data, PlatformProvider.PlatformLoginListener listener) {
        return false;
    }
}
