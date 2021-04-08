package com.dailyyoga.cn.receiver;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.dailyyoga.cn.model.bean.PushBean;
import com.dailyyoga.cn.utils.CommonUtil;
import com.dailyyoga.cn.utils.PushUtil;
import com.huawei.hms.support.api.push.PushReceiver;
import com.yoga.http.utils.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: zhaojiaxing@dailyyoga.com
 * @created on: 2018/11/29 19:23
 * @description: 华为push消息入口
 */
public class HuaWeiPushReceiver extends PushReceiver {

    /**
     * Push事件回调方法入口，供子类继承实现，目前支持的回调事件有通知栏消息点击事件回调、通知栏扩展消息按钮点击事件回调 click
     *
     * @param context 上下文信息
     * @param event   {@link Event}
     * @param extras  附加信息
     */
    @Override
    public void onEvent(Context context, Event event, Bundle extras) {
        String json = formatData(extras);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            String title = jsonObject.optString("push");
            int pushId = jsonObject.optInt("notice_push_log_id");
            if (!TextUtils.isEmpty(json)) {
                PushUtil.getInstance().sendNotification(context, json, title, pushId, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 供子类继承，用来接收token
     *
     * @param context 上下文信息
     * @param token   应用的token
     */
    @Override
    public void onToken(Context context, String token) {
        // 获取token 上传给服务端
        PushUtil.getInstance().mHuaWeiToken = token;
        PushUtil.getInstance().associatedUser();
    }

    /** 数据处理 */
    private String formatData(Bundle extras) {
        String strList = extras.getString("pushMsg"); // 华为给的key 不可更改
        PushBean bean = new PushBean();
        try {
            JSONArray jsonArray = new JSONArray(strList);
            for (int i = 0; i < jsonArray.length(); i++) {
                String str = (String) jsonArray.get(i);
                switch (i) {
                    case 0:
                        bean.type = str;
                        break;
                    case 1:
                        bean.id = str;
                        break;
                    case 2:
                        bean.image = str;
                        break;
                    case 3:
                        bean.image_pad = str;
                        break;
                    case 4:
                        bean.logo = str;
                        break;
                    case 5:
                        bean.only_push = str;
                        break;
                    case 6:
                        bean.message_type = str;
                        break;
                    case 7:
                        bean.link_url = str;
                        break;
                    case 8:
                        bean.channel_type = str;
                        break;
                    case 9:
                        bean.channel_type_id = str;
                        break;
                    case 10:
                        bean.notice_push_log_id = CommonUtil.stringToInt(str);
                        break;
                    case 11:
                        bean.title = str;
                        break;
                }
            }
            if (!TextUtils.isEmpty(bean.message_type)) {
                return GsonUtil.toJson(bean);
            } else {
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}