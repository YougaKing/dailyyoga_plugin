package com.mob.solutions

import com.mob.DObject
import com.mob.MobSDKConfig

class SolutionConfig extends DObject {
	MobSDKConfig mobSDK
	boolean gui

	void setMobSDK(MobSDKConfig mobSDK) {
		this.mobSDK = mobSDK
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

	void gui(boolean gui) {
		this.gui = gui
	}

	void process() {

	}

}