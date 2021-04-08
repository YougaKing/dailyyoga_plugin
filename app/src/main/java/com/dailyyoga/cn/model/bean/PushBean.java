package com.dailyyoga.cn.model.bean;

import java.io.Serializable;

/**
 * @author: zhaojiaxing@dailyyoga.com
 * @created on: 2018/12/05 17:14
 * @description: 华为push 推送相关
 */
public class PushBean implements Serializable {
    public String type;
    public String id;
    public String image;
    public String image_pad;
    public String logo;
    public String only_push;
    public String message_type;
    public String link_url;
    public String channel_type;
    public String channel_type_id;
    public int notice_push_log_id;
    public String title;
}
