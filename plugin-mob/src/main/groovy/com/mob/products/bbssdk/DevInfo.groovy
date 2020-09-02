package com.mob.products.bbssdk

import com.mob.ConfigCreator
import com.mob.DObject

class DevInfo extends DObject {
	static Set maps

	static {
		maps = ["GadMap"]
	}

	ConfigCreator config

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	def methodMissing(String name, def args) {
		String map = maps.find {
			return name.equalsIgnoreCase(it)
		}
		if (map != null) {
			InfoNode info = new InfoNode()
			info.closure = args[0]
			addActivity(map, info)
			addPermission(map)
		}
		return null
	}

	private void addActivity(String name, InfoNode info) {
		if ("GadMap".equals(name)) {
			config.directToAdd.add("""
				<service
				xmlns:android="http://schemas.android.com/apk/res/android"
				xmlns:tools="http://schemas.android.com/tools"
				tools:node="merge"
				android:name="com.amap.api.location.APSService" />
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.amap.api.v2.apikey"
            		android:value="${info.ApiKey}"/>
			""")
		}
	}

	private void addPermission(String name) {
		String version = config.gradlePluginVersion
		if ("GadMap".equals(name)) {
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', 'com.amap.api:3dmap:6.1.0')
				}
				config.project.dependencies {
					add('implementation', 'com.amap.api:location:3.8.0')
				}
				config.project.dependencies {
					add('implementation', 'com.amap.api:search:6.1.0')
				}
			} else{
				config.project.dependencies {
					add('compile', 'com.amap.api:3dmap:6.1.0')
				}
				config.project.dependencies {
					add('compile', 'com.amap.api:location:3.8.0')
				}
				config.project.dependencies {
					add('compile', 'com.amap.api:search:6.1.0')
				}
			}
			config.permissions.addAll([
					"android.permission.CHANGE_WIFI_STATE",
					"android.permission.READ_PHONE_STATE",
					"android.permission.ACCESS_COARSE_LOCATION",
					"android.permission.ACCESS_FINE_LOCATION",
					"android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"
			])
		}
	}
}