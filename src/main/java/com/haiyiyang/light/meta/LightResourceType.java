package com.haiyiyang.light.meta;

public enum LightResourceType {
	MYSQL("mysql"), REDIS("redis");

	private String key;

	LightResourceType(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
