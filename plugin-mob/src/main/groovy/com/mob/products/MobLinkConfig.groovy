package com.mob.products

import com.mob.ConfigCreator

class MobLinkConfig extends MobProductConfig {
	ConfigCreator config
	String scheme
	String host
	String applinkHost

	void setConfigCreator(ConfigCreator config) {
		this.config = config
		hiddenMembers {
			forceAutoVerify = false
			preferClassicalEdition = false
		}
	}

	def methodMissing(String name, def args) {
		if (name.equalsIgnoreCase("uriScheme") || name.equalsIgnoreCase("scheme")
				|| name.equalsIgnoreCase("uri") || name.equalsIgnoreCase("url")) {
			uriScheme(args[0])
		} else if (name.equalsIgnoreCase("applinkHost") || name.equalsIgnoreCase("host")
				|| name.equalsIgnoreCase("applink") || name.equalsIgnoreCase("link")) {
			appLinkHost(args[0])
		} else if (name.equalsIgnoreCase("preferClassicalEdition") || name.equalsIgnoreCase("classicalEdition")
				|| name.equalsIgnoreCase("classical") || name.equalsIgnoreCase("classic")) {
			preferClassicalEdition(args[0])
		} else if (name.equalsIgnoreCase("forceAutoVerify") || name.equalsIgnoreCase("autoVerify")) {
			if (args != null && args.length > 0) {
				forceAutoVerify = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		}
	}

	void uriScheme(String uri) {
		if (uri.contains("://")) {
			URI u = new URI(uri)
			scheme = u.getScheme()
			host = u.getHost()
		} else {
			scheme = "http"
			host = uri
		}
	}

	void appLinkHost(String host) {
		if (host.contains("://")) {
			applinkHost = new URI(host).getHost()
		} else {
			applinkHost = host
		}
	}

	void addHosts() {
		if (scheme != null && host != null) {
			config.mobLinkUri = "${scheme}://${host}"
		}

		if (applinkHost != null) {
			config.mobLinkAppLinkHost = applinkHost
			config.mobLinkAppLinkAutoVerify = forceAutoVerify
		}
	}

	void preferClassicalEdition(def arg) {
		this.preferClassicalEdition = "true".equalsIgnoreCase(String.valueOf(arg))
	}

	boolean isPreferClassicalEdition() {
		return this.preferClassicalEdition
	}

}