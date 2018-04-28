package com.haiyiyang.light.meta;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haiyiyang.light.utils.LightUtils;

public enum ResourceEnum {

	mysql("mysql.props"), redis("redis.props");

	public static final String RESOURCE_PATH = "/light/resource/";
	public static final String RESOURCE_LOCAL_PATH = LightUtils.getLocalPath(RESOURCE_PATH);

	private static final BiMap<ResourceEnum, String> RESOURCE_TYPE_PATH = HashBiMap.create(4);
	private static final Map<ResourceEnum, String> RESOURCE_TYPE_LOCAL_PATH = new HashMap<>(4);

	static {
		RESOURCE_TYPE_PATH.put(mysql, RESOURCE_PATH + mysql.key);
		RESOURCE_TYPE_PATH.put(redis, RESOURCE_PATH + redis.key);
		RESOURCE_TYPE_PATH.inverse(); // Filling the cache.
		RESOURCE_TYPE_LOCAL_PATH.put(mysql, RESOURCE_LOCAL_PATH + mysql.key);
		RESOURCE_TYPE_LOCAL_PATH.put(redis, RESOURCE_LOCAL_PATH + redis.key);
	}	

	private String key;

	ResourceEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public String getPath() {
		return RESOURCE_TYPE_PATH.get(this);
	}

	public String getLocalPath() {
		return RESOURCE_TYPE_LOCAL_PATH.get(this);
	}

	public static ResourceEnum pathVauleOf(String path) {
		return RESOURCE_TYPE_PATH.inverse().get(path);
	}

	public static void main(String[] args) {
		System.out.println(RESOURCE_TYPE_PATH);
		System.out.println(RESOURCE_TYPE_PATH.inverse());
	}
}
