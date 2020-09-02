package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.secverify.SecVerifyDevInfo

class SecVerifyConfig extends MobProductConfig {

	ConfigCreator config
	SecVerifyDevInfo infos
	int versionCode

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	def methodMissing(String name, Object args) {
		if ("disable".equalsIgnoreCase(name) || "exclude".equalsIgnoreCase(name)) {
			infos = new SecVerifyDevInfo()
			infos.configCreator = config
			versionCode = getVersionCode()
			infos.sdkVersion = versionCode
			infos.closure = args[0]
		}
		return super.methodMissing(name, args)
	}

	Set getPermission() {
		return [
				"android.permission.CHANGE_NETWORK_STATE"
		]
	}

	void addActivityDependencies(String version) {//这个地方其实可以不用传版本号进来
		String metaValue = null
		if(infos != null) {
			metaValue = infos.getMetaValue()
		}

		//CUCC login requested activity
		if (version != null && version.trim().startsWith("1.0.0")) {
			config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					tools:node="merge"
					android:name="com.sdk.mobile.manager.login.cucc.OauthActivity"
					android:theme="@android:style/Theme.Holo.NoActionBar">
                </activity>
			""")

			config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.cmic.sso.sdk.activity.LoginAuthActivity"
					android:theme="@android:style/Theme.Holo.NoActionBar"
					android:configChanges="orientation|keyboardHidden|screenSize"
					android:screenOrientation="unspecified"
					android:launchMode="singleTop">
				</activity>
			""")
		} else {
			if (version.trim().startsWith("1.0.1")) {
				config.directToAdd.add("""
					<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="com.cmic.sso.sdk.activity.LoginAuthActivity"
						android:theme="@android:style/Theme.Holo.NoActionBar"
						android:configChanges="orientation|keyboardHidden|screenSize"
						android:screenOrientation="unspecified"
						android:launchMode="singleTop">
					</activity>
				""")
			}
			//1.0之后CUCC换成代理类
			config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.secverify.login.impl.cucc.CuccOAuthProxyActivity"
					android:theme="@android:style/Theme.Translucent.NoTitleBar"
					android:configChanges="keyboardHidden|orientation|screenSize|screenLayout">
				</activity>
			""")
			config.directToAdd.add("""
				<activity-alias
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.sdk.mobile.manager.login.cucc.OauthActivity"
					android:targetActivity="com.mob.secverify.login.impl.cucc.CuccOAuthProxyActivity">
				</activity-alias>
			""")
			if((versionCode == 0 || versionCode >= 20005) && metaValue != null && metaValue.contains("CUCC")){
				config.directToAdd.remove(config.directToAdd.getAt(config.directToAdd.size() - 1))
				config.directToAdd.remove(config.directToAdd.getAt(config.directToAdd.size() - 1))
			}
		}
		//CMCC login requested activity
		config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.mob.secverify.login.impl.cmcc.CmccOAuthProxyActivity"
					android:configChanges="keyboardHidden|orientation|screenSize|screenLayout"
					android:theme="@android:style/Theme.Translucent.NoTitleBar" />
			""")
		if((versionCode == 0 || versionCode >= 20005) &&  metaValue != null && metaValue.contains("CMCC")){
			config.directToAdd.remove(config.directToAdd.getAt(config.directToAdd.size() - 1))
		}
		if (version != null && !version.trim().startsWith("1.0")) {
			config.directToAdd.add("""
				<activity-alias
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.cmic.sso.sdk.activity.LoginAuthActivity"
					android:targetActivity="com.mob.secverify.login.impl.cmcc.CmccOAuthProxyActivity" />
			""")
			if((versionCode == 0 || versionCode >= 20005) &&  metaValue != null && metaValue.contains("CMCC")){
				config.directToAdd.remove(config.directToAdd.getAt(config.directToAdd.size() - 1))
			}
		}
		//CTCC
		config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="cn.com.chinatelecom.account.sdk.ui.AuthActivity"
					android:theme="@android:style/Theme.Translucent.NoTitleBar"
					android:configChanges="keyboardHidden|orientation|screenSize|screenLayout"/>
			""")
		if((versionCode == 0 || versionCode >= 20005) &&  metaValue != null && metaValue.contains("CTCC")){
			config.directToAdd.remove(config.directToAdd.getAt(config.directToAdd.size() - 1))
		}
	}

	private int getVersionCode(){
		if (version == null || "+".equals(version)) {
			return 0
		} else {
			int code = 0
			String[] parts = version.split("\\.")
			for (int i = 0; i < 3; i++) {
				code = code * 100 + Integer.parseInt(parts[i])
			}
			return code
		}
	}
}