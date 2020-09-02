package com.mob

class AutoCorrectNode extends DObject {
	private Map fields

	AutoCorrectNode() {
		fields = [:]
	}

	Set getFieldNames() {
		return []
	}

	def methodMissing(String name, def args) {
		String key = fieldNames.find {
			return name.equalsIgnoreCase(it)
		}
		if (key == null) {
			this."${name}" = args[0]
		} else {
			this."${key}" = args[0]
		}
		return null
	}

	def propertyMissing(String name) {
		String key = fieldNames.find {
			return name.equalsIgnoreCase(it)
		}
		if (key == null) {
			return null
		} else {
			return fields[key]
		}
	}

	def propertyMissing(String name, def arg) {
		String key = fieldNames.find {
			return name.equalsIgnoreCase(it)
		}
		if (key == null) {
			return fields.put(name, arg)
		} else {
			return fields.put(key, arg)
		}
	}

	Map getFields() {
		return fields
	}

}