package com.mob.products

class MobVerifyConfig extends MobProductConfig {
	Set getPermission() {
		return [
				"android.permission.WRITE_SETTINGS",
				"android.permission.CHANGE_NETWORK_STATE"
		]
	}
}