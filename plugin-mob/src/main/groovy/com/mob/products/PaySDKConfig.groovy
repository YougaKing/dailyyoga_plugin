package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.pay.DevInfo

class PaySDKConfig extends MobProductConfig {
	ConfigCreator config

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	def methodMissing(String name, Object args) {
		if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
				|| "info".equalsIgnoreCase(name) || "infos".equalsIgnoreCase(name)
				|| "channel".equalsIgnoreCase(name) || "channels".equalsIgnoreCase(name)) {
			DevInfo infos = new DevInfo()
			infos.configCreator = config
			infos.closure = args[0]
		}
		return super.methodMissing(name, args)
	}

}