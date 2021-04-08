package com.dailyyoga.h2;

import android.app.Application;

import com.dailyyoga.h2.PlatformProvider.PlatformPushListener;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNormalMsgHandler;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 11:58
 * @description:
 */
public class HuaweiPushProvider implements PushProvider {

    @Override
    public void initialize(Application application, PlatformPushListener listener) {
        if (!HMSAgent.init(application)) {
            listener.onFail();
        }
        HMSAgent.Push.getToken(rst -> {
            if (rst == 0) {
                // 成功 token下发给receiver onToken方法
                // 开启透传消息接收
                HMSAgent.Push.enableReceiveNormalMsg(true, rst1 -> {
                    if (rst1 == 0) {
                        // 成功
                        // 开启通知消息接收
                        listener.onSuccess();
                        HMSAgent.Push.enableReceiveNotifyMsg(true, null);
                    } else {
                        // 开启通知消息接收
                        HMSAgent.Push.enableReceiveNotifyMsg(true, rst11 -> {
                            if (rst11 != 0) {
                                listener.onFail();
                            } else {
                                listener.onSuccess();
                            }
                        });
                    }
                });
            } else {
                // 失败
                listener.onFail();
            }
        });
    }
}
