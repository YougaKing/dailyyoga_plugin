package com.pili.pldroid.playerdemo;

import android.app.Application;

import com.dailyyoga.cn.YogaContext;

/**
 * author: YougaKingWu@gmail.com
 * created on: 2018/04/27 12:13
 * description:
 */
public class PldroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YogaContext.setContext(this);
//        NewRelic.enableFeature(FeatureFlag.DistributedTracing);
//
//        NewRelic.withApplicationToken("GENERATED_TOKEN")
//                .usingSsl(true)
//                .withLoggingEnabled(true)
//                .withHttpResponseBodyCaptureEnabled(true)
//                .withInteractionTracing(true)
//                .start(this);
    }
}
