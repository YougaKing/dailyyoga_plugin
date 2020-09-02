package com.mob

import com.mob.solutions.GrowSolutionConfig
import org.gradle.api.Action

class MobSDKSolution extends DObject {
	MobSDKConfig mobSDK

	void setMobSDK(MobSDKConfig mobSDK) {
		this.mobSDK = mobSDK
	}

	void appKey(String appkey) {
		mobSDK.appKey(appkey)
	}

	void appSecret(String appSecret) {
		mobSDK.appSecret(appSecret)
	}

	def methodMissing(String name, def args) {
		if ("appKey".equalsIgnoreCase(name)) {
			appKey(args[0])
		} else if ("appSecret".equalsIgnoreCase(name)) {
			appSecret(args[0])
		} else if ("autoConfig".equalsIgnoreCase(name)) {
			mobSDK.autoConfig(args[0])
		} else if ("updateCacheDynamicVersionsFor".equalsIgnoreCase(name)) {
			mobSDK.updateCacheDynamicVersionsFor(args[0])
		} else if ("GrowSolution".equalsIgnoreCase(name) || "grow".equalsIgnoreCase(name)) {
			hmGrowSolution(args[0])
		}
		return null
	}

	void hmGrowSolution(Object arg) {
		GrowSolutionConfig solution = new GrowSolutionConfig()
		solution.mobSDK = mobSDK
		if (arg instanceof Action) {
			arg.execute(solution)
		} else {
			solution.closure = arg
		}
		solution.process()
	}

	void GrowSolution(Action<GrowSolutionConfig> config) {
		hmGrowSolution(config)
	}

}