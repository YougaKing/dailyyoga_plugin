package com.dailyyoga.h2;

import android.app.Activity;
import android.app.Application;

import com.dailyyoga.cn.detection.Lib;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.hianalytics.v2.HiAnalytics;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.hwid.HuaweiIdApi;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.updatesdk.UpdateSdkAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 10:42
 * @description:
 */
public class HuaWeiProvider extends PlatformProvider {

    private LoginProvider mLoginProvider;
    private PushProvider mPushProvider;

    @Override
    public void initialize(Application application) {
        HMSAgent.init(application);
    }

    @Override
    public void connect(Activity activity) {
        HMSAgent.connect(activity, rst -> {
            if (rst == 0) {
                HMSAgent.checkUpdate(activity, rst1 -> {
                });
            }
        });
    }

    @Override
    public LoginProvider loginProvider() {
        return mLoginProvider == null ? mLoginProvider = new HuaweiLoginProvider() : mLoginProvider;
    }

    @Override
    public PushProvider pushProvider() {
        return mPushProvider == null ? mPushProvider = new HuaweiPushProvider() : mPushProvider;
    }

    @Override
    public PayProvider payProvider() {
        return new PayProvider() {};
    }

    @Override
    public List<Lib> necessaryLib() {
        List<Lib> libList = new ArrayList<>();
        libList.add(new Lib("华为", HuaweiApiClient.class, HiAnalytics.class, UpdateSdkAPI.class, HuaweiIdApi.class, HuaweiPush.class));
        return libList;
    }
}
