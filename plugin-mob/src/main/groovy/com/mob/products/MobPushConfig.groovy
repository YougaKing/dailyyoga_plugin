package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.mobpush.PluginsDevInfo

class MobPushConfig extends MobProductConfig {
	ConfigCreator config

	void badge(boolean badge) {
		this.badge = badge
	}

	void debugLevel(int debugLevel) {
		this.debugLevel = debugLevel
	}

	void defaultActivityName(String defaultActivityName) {
		this.defaultActivityName = defaultActivityName
	}

	void setConfigCreator(ConfigCreator config) {
		this.config = config
		hiddenMembers {
			awk = false
			badge = true
			adPush = false
			mGuard = true
			beta = false
			debugLevel = 0
			defaultActivityName = null
			foregroundNotification = false
		}
	}

	void addMobService() {
		config.directToAdd.add("""
				<service
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.mob.pushsdk.MobService"
						android:exported="true"
						tools:node="merge"
						android:process=":mobservice">
					<intent-filter>
						<action android:name="com.mob.intent.MOB_SERVICE"/>
					</intent-filter>
				</service>
		""")

		int code  = getVersionCode()
		if(code == 0 || code >= 10600){
			config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.pushsdk.impl.MobPushReceiver">
					<intent-filter>
						<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
					</intent-filter>
				</receiver>
		""")
			config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.pushsdk.impl.PushJobService"
					android:permission="android.permission.BIND_JOB_SERVICE" />
		""")
		}

		if (debugLevel > 0) {
			config.applicationMetaData.put("com.mob.mobpush.debugLevel", "${debugLevel}")
		}

		if(defaultActivityName != null && defaultActivityName.length() > 0) {
			config.setMobPushDefaultOpenActivityName(defaultActivityName)
		}
	}

	void addTCPService() {
		config.directToAdd.add("""
				<service
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.mob.socketservice.MobService"
						android:exported="true"
						tools:node="merge"
						android:process=":mobservice">
					<intent-filter>
						<action android:name="com.mob.intent.MOB_SERVICE"/>
					</intent-filter>
				</service>
		""")
	}

	def methodMissing(String name, Object args) {
		if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
				|| "factory".equalsIgnoreCase(name) || "factories".equalsIgnoreCase(name)
				|| "fac".equalsIgnoreCase(name) || "facs".equalsIgnoreCase(name)
				|| "info".equalsIgnoreCase(name) || "infos".equalsIgnoreCase(name)) {
			PluginsDevInfo infos = new PluginsDevInfo()
			infos.configCreator = config
			infos.sdkVersion = getVersionCode()
			infos.closure = args[0]
		} else if ("awk".equalsIgnoreCase(name)) {
			if (args == null) {
				awk = false
			} else if (args.length == 0) {
				awk = false
			} else {
				awk = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("adPush".equalsIgnoreCase(name)) {
			if (args == null) {
				adPush = false
			} else if (args.length == 0) {
				adPush = false
			} else {
				adPush = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("mGuard".equalsIgnoreCase(name)) {
			if (args == null) {
				mGuard = true
			} else if (args.length == 0) {
				mGuard = true
			} else {
				mGuard = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("foregroundNotification".equalsIgnoreCase(name)) {
			if (args != null && args.length == 1) {
				foregroundNotification = "true".equalsIgnoreCase(String.valueOf(args[0]))
				if (foregroundNotification) {
					config.applicationMetaData.put("mob_foreground_notification", "yes")
				}
			}
		}
		return super.methodMissing(name, args)
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

	Set getPermission() {
		Set permissions = []
		if (badge) {
			permissions = [
					"android.permission.VIBRATE",
					"com.huawei.android.launcher.permission.CHANGE_BADGE",
					"android.permission.READ_APP_BADGE",
					"com.sonymobile.home.permission.PROVIDER_INSERT_BADGE",
					"com.sonyericsson.home.permission.BROADCAST_BADGE",
					"com.sonyericsson.home.action.UPDATE_BADGE",
					"com.sec.android.provider.badge.permission.READ",
					"com.sec.android.provider.badge.permission.WRITE",
					"com.htc.launcher.permission.READ_SETTINGS",
					"com.htc.launcher.permission.UPDATE_SHORTCUT"
			]
		} else {
			permissions = [
					"android.permission.VIBRATE"
			]
		}
		if (foregroundNotification) {
			permissions.add("android.permission.FOREGROUND_SERVICE")
		}
		return permissions
	}
}