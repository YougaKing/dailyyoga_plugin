package com.mob.products

class ShareRECConfig extends MobProductConfig {
	boolean editor = true
	boolean uploader = true
	boolean community = true

	void editor(boolean editor) {
		this.editor = editor
	}

	void uploader(boolean uploader) {
		this.uploader = uploader
	}

	void community(boolean community) {
		this.community = community
	}

	Set getPermission() {
		return [
				"android.permission.WAKE_LOCK",
//				"android.permission.READ_CONTACTS",
				"android.permission.RECORD_AUDIO"
		]
	}
}