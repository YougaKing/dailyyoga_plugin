package com.mob.products

class SMSSDKConfig extends MobProductConfig {
	//自动填写短信验证码
	boolean autoSMS = false

	void autoSMS(boolean autoSMS) {
		this.autoSMS = autoSMS
	}

	Set getPermission() {
		if (autoSMS) {
			return [
					"android.permission.RECEIVE_SMS",
					"android.permission.READ_CONTACTS"
			]
		} else {
			return [
					"android.permission.READ_CONTACTS"
			]
		}
	}
}