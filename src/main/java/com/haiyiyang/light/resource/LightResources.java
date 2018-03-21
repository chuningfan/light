package com.haiyiyang.light.resource;

public enum LightResources {
	MYSQL("mysql"), REDIS("redis");

	private String key;

	LightResources(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

}
