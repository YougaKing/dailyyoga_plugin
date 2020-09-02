package com.mob.products.sharesdk

import com.mob.ConfigCreator
import com.mob.DObject
import com.mob.products.ShareSDKConfig

import java.util.regex.Matcher
import java.util.regex.Pattern

class DevInfo extends DObject {
	static Set platforms
	static Map jarNames
	ConfigCreator config
	ShareSDKConfig sharesdk
	Set deleteQQConfigInfoSet = []
	boolean hasQZone = false
	boolean hasQQ = false

	static {
		platforms = [
				"SinaWeibo", "TencentWeibo", "Douban", "QZone", "Renren", "KaiXin",
				"Facebook", "Twitter", "Evernote", "FourSquare", "GooglePlus",
				"Instagram", "LinkedIn", "Tumblr", "Email", "ShortMessage", "Wechat",
				"WechatMoments", "QQ", "Instapaper", "Pocket", "YouDao", "Pinterest",
				"Flickr", "Dropbox", "VKontakte", "WechatFavorite", "Yixin",
				"YixinMoments", "Mingdao", "Line", "WhatsApp", "KakaoTalk",
				"KakaoStory", "FacebookMessenger", "Alipay", "AlipayMoments",
				"Dingding", "Youtube", "Meipai", "Telegram", "Cmcc", "Reddit",
				"Telecom", "Accountkit", "Douyin", "Wework", "HWAccount", "Oasis", "XMAccount",
				"SnapChat", "Kuaishou", "Littleredbook", "Watermelonvideo", "Tiktok"
		]

		jarNames = [:]
		def names = [
				null, null, null, null, null, null,
				null, null, null, null, null,
				null, null, null, null, null, ["Wechat", "Wechat-Core"],
				["Wechat-Moments", "Wechat-Core"], null, null, null, null, null,
				null, null, null, ["Wechat-Favorite", "Wechat-Core"], ["Yixin", "Yixin-Core"],
				["Yixin-Moments", "Yixin-Core"], null, null, null, ["KakaoTalk", "Kakao-Core"],
				["KakaoStory", "Kakao-Core"], null, ["Alipay", "Alipay-Core"], ["Alipay-Moments", "Alipay-Core"],
				null, null, null, null, null, null,
				null, null, ["Douyin", "Douyin-Core"], ["Wework", "Wework-Core"], ["HWAccount", "HWAccount-Core"], null, ["XMAccount", "XMAccount-Core"],
				null, null, null, null, ["Tiktok", "Tiktok-Common", "Tiktok-External"]
		]
		for (int i = 0; i < platforms.size(); i++) {
			if (names[i] == null) {
				jarNames.put(platforms[i], [platforms[i]])
			} else {
				jarNames.put(platforms[i], names[i])
			}
		}
	}

	void setShareSDKConfig(ShareSDKConfig sharesdk){
		this.sharesdk = sharesdk
		this.config = sharesdk.config
		platforms.each { p ->
			if (!config.sharesdkInfoMap.containsKey(p)) {
				config.sharesdkInfoMap.put(p, null)
			}
		}
	}

	def methodMissing(String name, def args) {
		String plat = platforms.find {
			return name.equalsIgnoreCase(it)
		}
		if (plat != null) {
			InfoNode info = new InfoNode()
			info.closure = args[0]
			addInfo(plat, info)
			addPermission(plat)
			addShareSDKFileProvider(plat)
		}
		return null
	}

	private void addInfo(String name, InfoNode info) {
		addActivity(name, info)
		Map raw = [:]
		raw.putAll(info.fields)
		if ("SinaWeibo".equals(name) || "XMAccount".equals(name)) {
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("TencentWeibo".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Facebook".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
			config.setFacebookOfficialVersion(raw.get("OfficialVersion"))
		} else if ("Twitter".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
			raw.put("CallbackUrl", raw.remove("CallbackUri"))
		} else if ("Renren".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("SecretKey", raw.remove("AppSecret"))
		} else if ("KaiXin".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Douban".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("Secret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("YouDao".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Evernote".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
		} else if ("LinkedIn".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("SecretKey", raw.remove("AppSecret"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("GooglePlus".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
			config.setGooglePlusOfficialVersion(raw.get("OfficialVersion"));
		} else if ("FourSquare".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
			raw.put("ClientSecret", raw.remove("AppSecret"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Pinterest".equals(name)) {
			raw.put("ClientId", raw.remove("AppId"))
		} else if ("Flickr".equals(name)) {
			raw.put("ApiKey", raw.remove("AppKey"))
			raw.put("ApiSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Tumblr".equals(name)) {
			raw.put("OAuthConsumerKey", raw.remove("AppKey"))
			raw.put("SecretKey", raw.remove("AppSecret"))
			raw.put("CallbackUrl", raw.remove("CallbackUri"))
		} else if ("Dropbox".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("VKontakte".equals(name)) {
			raw.put("ApplicationId", raw.remove("AppId"))
		} else if ("Instagram".equals(name)) {
			raw.put("ClientId", raw.remove("AppId"))
			raw.put("ClientSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Dingding".equals(name)) {
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Mingdao".equals(name)) {
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Line".equals(name)) {
			raw.put("ChannelID", raw.remove("AppId"))
			raw.put("ChannelSecret", raw.remove("AppSecret"))
			raw.put("RedirectUri", raw.remove("CallbackUri"))
		} else if ("Pocket".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
		} else if ("Instapaper".equals(name)) {
			raw.put("ConsumerKey", raw.remove("AppKey"))
			raw.put("ConsumerSecret", raw.remove("AppSecret"))
		} else if ("Youtube".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Meipai".equals(name)) {
			raw.put("ClientID", raw.remove("AppId"))
		} else if("Reddit".equals(name)){
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if("Telecom".equals(name)){
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if("Accountkit".equals(name)){
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Telegram".equals(name)) {
			raw.put("RedirectUrl", raw.remove("CallbackUri"))
		} else if ("Douyin".equals(name)) {
			config.setDouyinCallbackAct(raw.remove("callbackAct"));
		}

		if (!raw.isEmpty()) {
			Map m = [:]
			raw.each { k, v ->
				if (k != null && v != null) {
					m.put(k, v)
				}
			}
			raw = m
		}

		// 下面平台的字段是标准的
		// Wechat、WechatMoments、WechatFavorite、QZone、QQ、Email、ShortMessage、
		// Yixin、YixinMoments、KakaoTalk、KakaoStory、WhatsApp、Bluetooth、
		// FacebookMessenger、Alipay、AlipayMoments、Dingding、Telegram
		config.sharesdkInfoMap.put(name, raw)
	}

	private void addActivity(String name, InfoNode info) {
		if ("Wechat".equals(name) || "WechatMoments".equals(name) || "WechatFavorite".equals(name)) {
			config.activitiesToAdd.add(".wxapi.WXEntryActivity")
		} else if ("Yixin".equals(name) || "YixinMoments".equals(name)) {
			config.activitiesToAdd.add(".yxapi.YXEntryActivity")
		} else if ("Alipay".equals(name) || "AlipayMoments".equals(name)) {
            config.activitiesToAdd.add(".apshare.ShareEntryActivity")
		} else if ("Dingding".equals(name)) {
			config.activitiesToAdd.add(".ddshare.DDShareActivity")
		} else if ("Tiktok".equals(name)) {
			config.activitiesToAdd.add(".tiktokapi.TikTokEntryActivity")
		} else if ("Dropbox".equals(name)) {
			config.intents.add("""
					<intent-filter
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:priority="1000">
						<data android:scheme="db-7janx53ilz11gbs" />
						<action android:name="android.intent.action.VIEW" />
						<category android:name="android.intent.category.BROWSABLE"/>
						<category android:name="android.intent.category.DEFAULT" />
					</intent-filter>
            """)
		} else if ("Line".equals(name) && info.AppId != null) {
			config.directToAdd.add("""
					<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="cn.sharesdk.line.LineHandlerActivity"
						android:configChanges="orientation|screenSize|keyboardHidden"
						android:exported="true">
						<intent-filter>
							<action android:name="android.intent.action.VIEW" />
			
							<category android:name="android.intent.category.DEFAULT" />
							<category android:name="android.intent.category.BROWSABLE" />
			
							<data android:scheme="${info.callbackscheme}" />
						</intent-filter>
					</activity>
			""")
			config.directToAdd.add("""
				   <activity-alias
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name=".lineapi.LineAuthenticationCallbackActivity"
						android:exported="true"
						android:targetActivity="cn.sharesdk.line.LineHandlerActivity" />
			""")
		} else if ("SinaWeibo".equals(name)) {
			config.intents.add("""
					<intent-filter xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools">
						<action android:name="com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY" />
						<category android:name="android.intent.category.DEFAULT" />
					</intent-filter>
            """)
		} else if ("QQ".equals(name) && info.AppId != null) {
			hasQQ = true
			//当QQ空间配置在QQ之前，已经知道注册了QQ空间的回调，但是依然要注册QQ的回调，则此时需要移除QQ空间的回调
			if(hasQQ && hasQZone && deleteQQConfigInfoSet.size() > 0){
				deleteQQConfigInfoSet.each { deleteItem ->
					config.directToAdd.remove(deleteItem)
				}
			}
			config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="cn.sharesdk.tencent.qq.ReceiveActivity"
						android:launchMode="singleTask"
						android:noHistory="true">
					<intent-filter>
						<data android:scheme="tencent${info.AppId}" />
						<action android:name="android.intent.action.VIEW" />
						<category android:name="android.intent.category.DEFAULT" />
						<category android:name="android.intent.category.BROWSABLE" />
					</intent-filter>
				</activity>
			""")

			boolean qqUpdate = true
			try {
				qqUpdate = sharesdk.version == null || sharesdk.version.trim().equals("+") ||
						Integer.parseInt(sharesdk.version.trim().replace(".", "")) > 363
			} catch (Throwable t) {}
			if (qqUpdate) {
				config.directToAdd.add("""
                        <activity
                            xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:tools="http://schemas.android.com/tools"
                            android:name="com.tencent.tauth.AuthActivity"
                            android:launchMode="singleTask"
                            android:noHistory="true" >
                        </activity>
                    """)
				config.directToAdd.add("""
                        <activity
                            xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:tools="http://schemas.android.com/tools"
                            android:name="com.tencent.connect.common.AssistActivity"
                            android:configChanges="orientation|keyboardHidden|screenSize"
                            android:screenOrientation="behind"
                            android:theme="@android:style/Theme.Translucent.NoTitleBar">
                        </activity>
                    """)
			}
		} else if ("QZone".equals(name) && info.AppId != null) {
			hasQZone = true
			//当QQ配置在QQ空间之前，已经知道注册了QQ的回调，则此时不需要注册QQ空间的回调
			if(!hasQQ){
				config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="cn.sharesdk.tencent.qzone.ReceiveActivity"
						android:launchMode="singleTask"
						android:noHistory="true">
					<intent-filter>
						<data android:scheme="tencent${info.AppId}" />
						<action android:name="android.intent.action.VIEW" />
						<category android:name="android.intent.category.DEFAULT" />
						<category android:name="android.intent.category.BROWSABLE" />
					</intent-filter>
				</activity>
			""")
				deleteQQConfigInfoSet.add(config.directToAdd.getAt(config.directToAdd.size() - 1))
				boolean qqUpdate = false
				try {
					qqUpdate = sharesdk.version != null && !sharesdk.version.trim().equals("+") &&
							Integer.parseInt(sharesdk.version.trim().replace(",", "")) > 363 &&
							Integer.parseInt(sharesdk.version.trim().replace(",", "")) < 374
				} catch (Throwable t) {}
				if (qqUpdate) {
					config.directToAdd.add("""
                        <activity
                            xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:tools="http://schemas.android.com/tools"
                            android:name="com.tencent.tauth.AuthActivity"
                            android:launchMode="singleTask"
                            android:noHistory="true" >
                        </activity>
                    """)
					deleteQQConfigInfoSet.add(config.directToAdd.getAt(config.directToAdd.size() - 1))
					config.directToAdd.add("""
                        <activity
                            xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:tools="http://schemas.android.com/tools"
                            android:name="com.tencent.connect.common.AssistActivity"
                            android:configChanges="orientation|keyboardHidden|screenSize"
                            android:screenOrientation="behind"
                            android:theme="@android:style/Theme.Translucent.NoTitleBar">
                        </activity>
                    """)
					deleteQQConfigInfoSet.add(config.directToAdd.getAt(config.directToAdd.size() - 1))
				}
			}
		} else if ("KakaoTalk".equals(name) && info.AppKey != null) {
			config.directToAdd.add("""
					<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:theme="@android:style/Theme.Translucent.NoTitleBar"
							android:name="cn.sharesdk.kakao.talk.ReceiveActivity"
							android:exported="true">
						<intent-filter>
							<action android:name="android.intent.action.VIEW"/>
							<category android:name="android.intent.category.DEFAULT"/>
							<category android:name="android.intent.category.BROWSABLE"/>
							<data android:scheme="kakao${info.AppKey}" android:host="kakaolink" />
						</intent-filter>
					</activity>
			""")
			//kakao demo卡片分享需要的appkey
			config.applicationMetaData.put("com.kakao.sdk.AppKey", "${info.AppKey}")
		} else if ("FacebookMessenger".equals(name)) {
			config.directToAdd.add("""
					<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:theme="@android:style/Theme.Translucent.NoTitleBar"
							android:name="cn.sharesdk.facebookmessenger.ReceiveActivity"
							android:exported="true">
							<intent-filter>
								<action android:name="android.intent.action.PICK"/>
								<category android:name="android.intent.category.DEFAULT" />
								<category android:name="com.facebook.orca.category.PLATFORM_THREAD_20150314" />
							</intent-filter>
					</activity>
			""")
		} else if ("Facebook".equals(name) && info.OfficialVersion != null) {
			//OfficialVersion不为空，则走官方版本，需要做如下配置
			String tmpPre = ""
			try {
				//如果facebook的AppKey是纯数字，则需要在前面添加\\来转成string
				Pattern pattern = Pattern.compile("^-?[0-9]+")
				Matcher isNum = pattern.matcher(info.AppKey);
				tmpPre = isNum.matches() ? "\\" : ""
			} catch (Throwable t) {}
			config.applicationMetaData.put("com.facebook.sdk.ApplicationId", "${tmpPre}${info.AppKey}")
			config.directToAdd.add("""
					<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							android:name="com.facebook.FacebookActivity"
							android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation">
					</activity>
			""")
			config.directToAdd.add("""
					<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							android:name="com.facebook.CustomTabActivity"
							android:exported="true">
							<intent-filter>
								<action android:name="android.intent.action.VIEW"/>
								<category android:name="android.intent.category.DEFAULT" />
								<category android:name="android.intent.category.BROWSABLE" />
								<data android:scheme="${info.FaceBookLoginProtocolScheme}" />
							</intent-filter>
					</activity>
			""")
			config.directToAdd.add("""
					<provider
							xmlns:android="http://schemas.android.com/apk/res/android"
							android:name="com.facebook.FacebookContentProvider"
							android:authorities="com.facebook.app.FacebookContentProvider${info.AppKey}"
							android:exported="true">
					</provider>
			""")
		} else if ("Douyin".equals(name)) {
			config.directToAdd.add("""
					<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="cn.sharesdk.douyin.bdopen.DouYinHandlerActivity"
						android:configChanges="orientation|screenSize|keyboardHidden"
						android:exported="true">
					</activity>
			""")
			config.directToAdd.add("""
					<activity-alias
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name=".bdopen.BdEntryActivity"
						android:exported="true"
						android:targetActivity="cn.sharesdk.douyin.bdopen.DouYinHandlerActivity" >
					</activity-alias>
			""")
		} else if ("HWAccount".equals(name)) {
			config.directToAdd.add("""
					<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="cn.sharesdk.hwaccount.hwid.HMSSignInAgentActivity"
						android:configChanges="orientation|locale|screenSize|layoutDirection|fontScale"
						android:excludeFromRecents="true"
						android:exported="false"
						android:hardwareAccelerated="true">
					</activity>
			""")
			config.applicationMetaData.put("com.huawei.hms.client.appid", "appid=${info.AppId}")
		} else if ("Oasis".equals(name)) {
			config.directToAdd.add("""    
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="cn.sharesdk.oasis.inner.ShareActivity">
				</activity>
			""")
		} else if ("XMAccount".equals(name)) {
			config.directToAdd.add("""    
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.xiaomi.account.openauth.AuthorizeActivity">
				</activity>
			""")
		}
	}

	private void addPermission(String name) {
		if ("Cmcc".equals(name)) {
			config.permissions.addAll([
					"android.permission.ACCESS_COARSE_LOCATION",
					"android.permission.CHANGE_NETWORK_STATE",
					"android.permission.SEND_SMS"
			])
		} else if ("XMAccount".equals(name)) {
			config.permissions.addAll([
					"com.xiaomi.permission.AUTH_SERVICE"
			])
		}
	}

	private void addShareSDKFileProvider(String name) {
		if ("Instagram".equalsIgnoreCase(name) || "Wechat".equalsIgnoreCase(name) || "WechatMoments".equalsIgnoreCase(name)) {
			try {
				if (sharesdk.version == null || sharesdk.version.trim().equals("+") || Integer.parseInt(sharesdk.version.trim().replace(".", "")) > 366) {
					//sharesdk 3.6.7版本才有ShareSDKFileProvider
					config.directToAdd.add("""
						<provider
							xmlns:android="http://schemas.android.com/apk/res/android"
							android:name="cn.sharesdk.framework.utils.ShareSDKFileProvider"
							android:authorities="#MobApplicationId#.cn.sharesdk.ShareSDKFileProvider"
							android:exported="false"
							android:grantUriPermissions="true" >
						</provider>
					""")
				}
			} catch (Throwable t) {}
		}
	}

}