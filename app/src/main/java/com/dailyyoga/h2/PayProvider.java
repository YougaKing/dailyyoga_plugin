package com.dailyyoga.h2;

import android.app.Activity;
import android.content.Intent;

import com.dailyyoga.cn.model.bean.PrePayInfo;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 11:53
 * @description:
 */
public interface PayProvider {

    default void connect(Activity activity) {}

    default void pay(PrePayInfo prePayInfo, PlatformProvider.PlatformPayListener listener) {}

    default void autoBuyProductPay(Activity activity, PrePayInfo prePayInfo, PlatformProvider.PlatformPayListener listener) {}

    default boolean onActivityResult(int requestCode, Intent data, PlatformProvider.PlatformPayListener listener) {return false;}
}
