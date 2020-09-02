package com.mob.products.mobpush

import com.mob.AutoCorrectNode

class PluginsInfoNode extends AutoCorrectNode {
	Set getFieldNames() {
		return [
				"AppId",
				"AppKey",
				"AppSecret",
				"IconRes",
				"version"
		]
	}
}