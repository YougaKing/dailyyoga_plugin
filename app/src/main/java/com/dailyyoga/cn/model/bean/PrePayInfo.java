package com.dailyyoga.cn.model.bean;

import java.io.Serializable;

/**
 * 支付预订单信息
 * Created by zjx on 2017/12/26
 */
public class PrePayInfo implements Serializable {
    // 支付宝
    public String pay_info;
    public String payment_type;
    public String notify_url;
    public String subject;
    public String total_fee;
    public String body;

    // 微信
    public String partnerid;
    public String prepayid;
    public String packageValue;
    public String noncestr;
    public String timestamp;

    // 华为
    public String product_name;
    public String product_desc;
    public String application_id;
    public String amount;
    public String merchant_id;
    public String service_catalog;
    public String merchant_name;
    public int sdk_channel;
    public String trade_type;
    public String currency;
    public String country;
    public String urlver;
    public String url;

    // 云闪付
    public String tn;

    // 小米
    public String product_code;
    public int order_index;

    // 公共字段
    public String out_trade_no;
    public String sign; // 微信、华为公用
    //无需第三方支付
    public boolean order_complete;
}
