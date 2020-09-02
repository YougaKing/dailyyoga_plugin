package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.sharesdk.DevInfo

class ShareSDKConfig extends MobProductConfig {
	ConfigCreator config

	void setConfigCreator(ConfigCreator config) {
		this.config = config
		hiddenMembers {
			loopShare = false
		}
	}

	def methodMissing(String name, Object args) {
		if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
				|| "platform".equalsIgnoreCase(name) || "platforms".equalsIgnoreCase(name)
				|| "plat".equalsIgnoreCase(name) || "plats".equalsIgnoreCase(name)
				|| "info".equalsIgnoreCase(name) || "infos".equalsIgnoreCase(name)) {
			DevInfo infos = new DevInfo()
			infos.shareSDKConfig = this
			infos.closure = args[0]
		} else if ("disableConfig".equalsIgnoreCase(name)) {
			config.disableShareSDKConfig = true
		} else if ("placeConfigHere".equalsIgnoreCase(name)) {
			String path = null
			try {
				if (args[0] instanceof String) {
					path = args[0]
				}
			} catch(Throwable t) {}
			config.setPlaceShareSDKConfigHere(path)
		} else if ("loopShare".equalsIgnoreCase(name)) {
			if (args != null && args.length > 0) {
				loopShare(args[0])
			}
        }
        return super.methodMissing(name, args)
	}

    void loopShare(def enable){
		this.loopShare = "true".equalsIgnoreCase(String.valueOf(enable))
		config.setLoopShare(enable)
    }

//	Set getPermission() {
//		return [
//				"android.permission.BLUETOOTH"
//		]
//	}
}