package com.mob.products.pay

import com.mob.ConfigCreator
import com.mob.DObject

class DevInfo extends DObject {
	static Set channels
	ConfigCreator config

	static {
		channels = [
				"AliPay",
				"Wechat",
				"UnionPay"
		]
	}

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	def methodMissing(String name, def args) {
		String ch = channels.find {
			return name.equalsIgnoreCase(it)
		}
		if (ch != null) {
			InfoNode info = new InfoNode()
			info.closure = args[0]
			addActivity(ch, info)
			addDependency(ch)
			addPermission(ch)
		}
	}

	private void addActivity(String name, InfoNode info) {
		if ("AliPay".equals(name)) {
			config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.alipay.sdk.app.H5PayActivity"
						android:configChanges="orientation|keyboardHidden|navigation|screenSize"
						android:exported="false"
						android:screenOrientation="behind" />
			""")
			config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.alipay.sdk.auth.AuthActivity"
						android:configChanges="orientation|keyboardHidden|navigation|screenSize"
						android:exported="false"
						android:screenOrientation="behind" />
			""")
		} else if ("Wechat".equals(name)) {
			config.activitiesToAdd.add(".wxapi.WXPayEntryActivity")
		} else if ("UnionPay".equals(name)) {
			config.directToAdd.add("""
				<uses-library
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="org.simalliance.openmobileapi"
						android:required="false" />
			""")
			config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.unionpay.uppay.PayActivity"
						android:configChanges="orientation|keyboardHidden"
						android:excludeFromRecents="true"
						android:screenOrientation="portrait"
						android:windowSoftInputMode="adjustResize" />
			""")
			config.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.unionpay.UPPayWapActivity"
						android:configChanges="orientation|keyboardHidden"
						android:screenOrientation="portrait"
						android:windowSoftInputMode="adjustResize" />
			""")
		}
	}

	private void addDependency(String name) {
		String version = config.gradlePluginVersion
		if ("AliPay".equals(name)) {
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', 'com.mob.pay.plugins:AliPay:+@aar')
				}
			} else{
				config.project.dependencies {
					add('compile', 'com.mob.pay.plugins:AliPay:+@aar')
				}
			}
		} else if ("Wechat".equals(name)) {
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:5.1.4')
				}
			} else{
				config.project.dependencies {
					add('compile', 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:5.1.4')
				}
			}
		} else if ("UnionPay".equals(name)) {
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', 'com.mob.pay.plugins:UnionPay:+@aar')
				}
			} else{
				config.project.dependencies {
					add('compile', 'com.mob.pay.plugins:UnionPay:+@aar')
				}
			}
		}
	}

	private void addPermission(String name) {
		if ("Wechat".equals(name)) {
			config.permissions.add('android.permission.MODIFY_AUDIO_SETTINGS')
		} else if ("UnionPay".equals(name)) {
			config.permissions.add('org.simalliance.openmobileapi.SMARTCARD')
			config.permissions.add('android.permission.NFC')
			config.features.add('android.hardware.nfc.hce')
		}
	}
}