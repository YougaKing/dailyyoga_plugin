package com.mob.products

class UMSSDKConfig extends MobProductConfig {
	Set getPermission() {
		return [
				"android.permission.READ_CONTACTS",
				"android.permission.RECEIVE_SMS",
				"android.permission.READ_SMS"
		]
	}
}