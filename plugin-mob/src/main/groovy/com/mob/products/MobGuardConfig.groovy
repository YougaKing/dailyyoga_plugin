package com.mob.products

import com.mob.ConfigCreator

class MobGuardConfig extends MobProductConfig {
	ConfigCreator config

	void setConfigCreator(ConfigCreator config) {
		this.config = config
		hiddenMembers {
			disableBGuard = false
		}
	}

	def methodMissing(String name, Object args) {
		if ("disableBGuard".equalsIgnoreCase(name)) {
			if (args == null) {
				disableBGuard = false
			} else if (args.length == 0) {
				disableBGuard = false
			} else {
				disableBGuard = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		}
		return super.methodMissing(name, args)
	}

	void addMobGuardService() {
		config.directToAdd.add("""
				<service
						xmlns:android="http://schemas.android.com/apk/res/android"
						xmlns:tools="http://schemas.android.com/tools"
						android:name="com.mob.guard.MobGuardService"
						android:exported="true"
						tools:node="merge">
					<intent-filter>
						<action android:name="com.mob.intent.MOB_GUARD_SERVICE"/>
					</intent-filter>
				</service>
		""")
		config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.mob.MobTranActivity"
					android:excludeFromRecents="true"
					android:configChanges="keyboardHidden|orientation|screenSize"
					android:exported="true"
					android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
					android:screenOrientation="portrait">
					<intent-filter>
						<action android:name="com.mob.open.app" />
						<category android:name="android.intent.category.DEFAULT" />
					</intent-filter>
        		</activity>
		""")

		if (disableBGuard) {
			config.applicationMetaData.put("disable_mob_a_guard", "${disableBGuard}")
		}
	}

	void addNewService() {
		config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.guard.MobGuardService"
					android:exported="true">
					<intent-filter>
						<action android:name="com.mob.intent.MOB_GUARD_SERVICE" />
					</intent-filter>
        		</service>
		""")
		config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					android:name="com.mob.guard.MobGuardBroadCastReceiver"
					android:enabled="true"
					android:exported="true">
					<intent-filter>
						<action android:name="com.mlive.id"/>
            		</intent-filter>
				</receiver>
		""")
		config.directToAdd.add("""
				 <activity
				 	xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
            		android:name="com.mob.MobTranActivity"
            		android:configChanges="keyboardHidden|orientation|screenSize"
            		android:exported="true"
            		android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
					<intent-filter>
						<action android:name="com.mob.open.app" />
						<category android:name="android.intent.category.DEFAULT" />
					</intent-filter>
        		</activity>
		""")
	}
}