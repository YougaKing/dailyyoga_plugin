package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.bbssdk.DevInfo

class BBSSDKConfig extends MobProductConfig {
	ConfigCreator config
	String theme
	String newsTheme

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	def methodMissing(String name, def args) {
		if ("newsTheme".equals(name)) {
			this.newsTheme = args[0]
		} else if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
					|| "info".equalsIgnoreCase(name) ||"infos".equalsIgnoreCase(name)
					|| "map".equalsIgnoreCase(name) || "maps".equalsIgnoreCase(name)) {
			DevInfo infos = new DevInfo()
			infos.configCreator = config
			infos.closure = args[0]
		}
		return super.methodMissing(name, args)
	}

	void theme(String theme) {
		this.theme = theme
	}

}