package com.pili.pldroid.playerdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/5/21 1:56 PM
 * @description:
 */
public class YogaLogAdapter implements LogAdapter {

    @NonNull
    private final FormatStrategy formatStrategy;


    public YogaLogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("YogaLog")
                .build();
    }

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        return BuildConfig.DEBUG;
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        formatStrategy.log(priority, tag, message);
    }

}
