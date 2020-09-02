package com.mob.products

class MobCommonsConfig extends CoreProductConfig {
	Set getPermission() {
		return [
				"android.permission.INTERNET",
				"android.permission.ACCESS_WIFI_STATE",
				"android.permission.CHANGE_WIFI_STATE",
				"android.permission.ACCESS_NETWORK_STATE",
				"android.permission.READ_EXTERNAL_STORAGE",
				"android.permission.WRITE_EXTERNAL_STORAGE"
		]
	}
}

//"android.permission.READ_PHONE_STATE"
//"android.permission.ACCESS_COARSE_LOCATION",
//"android.permission.ACCESS_FINE_LOCATION"
