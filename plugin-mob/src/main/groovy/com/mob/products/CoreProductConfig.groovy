package com.mob.products

import com.mob.DObject

class CoreProductConfig extends DObject {

	CoreProductConfig() {
		hiddenMembers {
			version = null
		}
	}

	def methodMissing(String name, def args) {
		if ("version".equalsIgnoreCase(name)) {
			if (args == null) {
				version = null
			} else if (args.length == 0) {
				version = null
			} else {
				version = args[0] == null ? null : String.valueOf(args[0])
			}
		}
		return null
	}

	Set getPermission() {
		return []
	}
}