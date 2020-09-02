package com.mob

class PermissionConfig extends DObject {
	ConfigCreator config

	void setConfigCreator(ConfigCreator config) {
		this.config = config
	}

	void exclude(String... permissions) {
		HashSet<String> set = new HashSet<String>(Arrays.asList(permissions))
		exclude(set)
	}

	void exclude(Set<String> permissions) {
		config.excludePermissions.addAll(permissions)
	}

}