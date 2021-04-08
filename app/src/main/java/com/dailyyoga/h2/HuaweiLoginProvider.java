package com.dailyyoga.h2;

import android.app.Activity;
import android.content.Intent;

import com.dailyyoga.cn.model.bean.User;
import com.dailyyoga.cn.utils.AnalyticsUtil;
import com.dailyyoga.cn.utils.BuglyUtil;
import com.dailyyoga.cn.utils.CommonUtil;
import com.dailyyoga.cn.utils.HuaWei;
import com.dailyyoga.h2.components.analytics.LoginClickSource;
import com.dailyyoga.h2.components.toast.CustomToast;
import com.dailyyoga.h2.ui.sign.ILoginView;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.hwid.HuaweiId;
import com.huawei.hms.support.api.hwid.HuaweiIdSignInOptions;
import com.huawei.hms.support.api.hwid.HuaweiIdStatusCodes;
import com.huawei.hms.support.api.hwid.SignInHuaweiId;
import com.huawei.hms.support.api.hwid.SignInResult;
import com.orhanobut.logger.Logger;
import com.pili.pldroid.playerdemo.R;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2019/02/28 11:58
 * @description:
 */
public class HuaweiLoginProvider implements LoginProvider {

    public static final int REQUEST_SIGN_IN_UNLOGIN = 999;
    public static final int REQUEST_SIGN_IN_AUTH = 998;
    private static final String HUAWEI = "huawei";
    private HuaweiApiClient mClient;

    @Override
    public Platform platform() {
        return new HuaWei();
    }

    @Override
    public int icon(int iconType) {
        switch (iconType) {
            case QUICK_LOGIN_ICON:
                return R.drawable.retry_btn_default;
            case OCCASION_LOGIN_ICON:
                return R.drawable.retry_btn_default;
            default:
                return 0;
        }
    }

    @Override
    public String name() {
        return "华为登录";
    }

    @Override
    public String loginClickSource() {
        return LoginClickSource.HUAWEI;
    }

    @Override
    public boolean isAvailable() {
        String manufacturer = CommonUtil.getManufacturer();
        return HUAWEI.equalsIgnoreCase(manufacturer);
    }

    @Override
    public void initializeLogin(Activity activity) {
        if (activity == null) return;
        //创建基础权限的登录参数options
        HuaweiIdSignInOptions signInOptions = new HuaweiIdSignInOptions
                .Builder(HuaweiIdSignInOptions.DEFAULT_SIGN_IN)
                .requestOpenId()
                .build();
        //创建华为移动服务client实例用以登录华为帐号
        //需要指定api为HUAWEIId.SIGN_IN_API
        //scope为HUAWEIId.HUAEWEIID_BASE_SCOPE,可以不指定，HUAWEIIdSignInOptions.DEFAULT_SIGN_IN默认使用该scope
        //连接回调以及连接失败监听
        mClient = new HuaweiApiClient.Builder(activity)
                .addApi(HuaweiId.SIGN_IN_API, signInOptions)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected() {

                    }

                    @Override
                    public void onConnectionSuspended(int cause) {


                    }
                })
                .addOnConnectionFailedListener(result -> {
                    Logger.d(result.getErrorCode());
                    BuglyUtil.postCatchedException("华为连接异常" + result.getErrorCode());
                })
                .build();

        //建议在onCreate的时候连接华为移动服务
        //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
        mClient.connect(activity);
    }

    @Override
    public void login(Activity activity, PlatformProvider.PlatformLoginListener listener) {
        if (activity == null) return;
        if (mClient == null) {
            initializeLogin(activity);
            listener.showToast(activity.getString(R.string.connect_huawei_error));
            return;
        }
        if (!mClient.isConnected()) {
            mClient.connect(activity);
            listener.showToast(activity.getString(R.string.connect_huawei_error_try));
            return;
        }
        PendingResult<SignInResult> signInResult = HuaweiId.HuaweiIdApi.signIn(activity, mClient);
        signInResult.setResultCallback(result -> {
            if (result.isSuccess()) {
                listener.resultCode(AnalyticsUtil.AUTHORIZATION_SUCCESS);
                onComplete(result, listener);
            } else {
                //当未登录或者未授权，回调的result中包含处理该种异常的intent，开发者只需要通过getData将对应异常的intent获取出来
                //并通过startActivityForResult启动对应的异常处理界面。再相应的页面处理完毕后返回结果后，开发者需要做相应的处理
                if (result.getStatus().getStatusCode() == HuaweiIdStatusCodes.SIGN_IN_UNLOGIN) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        activity.startActivityForResult(intent, REQUEST_SIGN_IN_UNLOGIN);
                    } else {
                        // 华为帐号登录异常
                        listener.showToast(activity.getString(R.string.huawei_login_error));
                    }
                } else if (result.getStatus().getStatusCode() == HuaweiIdStatusCodes.SIGN_IN_AUTH) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        activity.startActivityForResult(intent, REQUEST_SIGN_IN_AUTH);
                    } else {
                        // 授权异常
                        listener.showToast(activity.getString(R.string.huawei_authorization_error));
                    }
                } else {
                    // 其它异常
                    listener.resultCode(AnalyticsUtil.AUTHORIZATION_FAIL);
                    listener.showToast(activity.getString(R.string.huawei_login_other_error));
                }
            }
        });
    }

    private void onComplete(SignInResult result, PlatformProvider.PlatformLoginListener listener) {
        //可以获取帐号的 openid，昵称，头像 at信息
        SignInHuaweiId account = result.getSignInHuaweiId();
        Map<String, String> map = new HashMap<>();
        map.put(ILoginView.ACCOUNTTYPE, String.valueOf(User.HUAWEI));
        map.put("gender", String.valueOf(account.getGender()));
        map.put("logo", account.getPhotoUrl());
        map.put("nickname", account.getDisplayName());
        map.put("openId", account.getOpenId());
        map.put("unionId", account.getUnionId());
        map.put("accessToken", account.getAccessToken());
        listener.onComplete(map);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data, PlatformProvider.PlatformLoginListener listener) {
        if (listener != null) {
            if (requestCode == HuaweiLoginProvider.REQUEST_SIGN_IN_UNLOGIN && resultCode != -1) {
                // 登陆取消
                listener.resultCode(AnalyticsUtil.AUTHORIZATION_CANCEL);
            } else if (requestCode == HuaweiLoginProvider.REQUEST_SIGN_IN_AUTH && resultCode == HuaweiIdStatusCodes.SIGN_IN_AUTH) {
                // 授权取消
                listener.resultCode(AnalyticsUtil.AUTHORIZATION_CANCEL);
            } else if (requestCode == HuaweiLoginProvider.REQUEST_SIGN_IN_AUTH && resultCode == -1) {
                // 授权成功
                listener.resultCode(AnalyticsUtil.AUTHORIZATION_SUCCESS);
            } else {
                // 授权失败
                listener.resultCode(AnalyticsUtil.AUTHORIZATION_FAIL);
            }
        }
        switch (requestCode) {
            case REQUEST_SIGN_IN_UNLOGIN:
                //当返回值是-1的时候表明用户登录成功，需要开发者再次调用signIn
                if (resultCode == Activity.RESULT_OK) {
                    listener.onComplete(null);
                } else {
                    //当resultCode 为0的时候表明用户未登录，则开发者可以处理用户不登录事件
                    CustomToast.showToast(R.string.cancel_long);
                }
                return true;
            case REQUEST_SIGN_IN_AUTH:
                //当返回值是-1的时候表明用户确认授权，
                if (resultCode == Activity.RESULT_OK) {
                    SignInResult result = HuaweiId.HuaweiIdApi.getSignInResultFromIntent(data);
                    if (result.isSuccess()) {
                        onComplete(result, listener);
                    } else {
                        // 授权失败，result.getStatus()获取错误原因
                        CustomToast.showToast("授权失败:" + result.getStatus().toString());
                    }
                } else {
                    //当resultCode 为0的时候表明用户未授权，则开发者可以处理用户未授权事件
                    CustomToast.showToast(R.string.cancel_authorization);
                }
                return true;
            default:
                return false;
        }
    }
}
