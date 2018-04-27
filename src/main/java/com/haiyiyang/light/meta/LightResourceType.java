package com.haiyiyang.light.meta;

import java.util.HashMap;
import java.util.Map;

import com.haiyiyang.light.constant.LightConstants;

public enum LightResourceType {

	MYSQL("MYSQL"), REDIS("REDIS");

	public static final String RESOURCE_PATH = "/light/resource/";
	public static final String RESOURCE_LOCAL_PATH = LightConstants.USER_HOME
			+ RESOURCE_PATH.replaceAll("/", LightConstants.FS);

	private static final Map<LightResourceType, String> RESOURCE_TYPE_PATH = new HashMap<>(8);
	private static final Map<LightResourceType, String> RESOURCE_TYPE_LOCAL_PATH = new HashMap<>(8);
	static {
		RESOURCE_TYPE_PATH.put(LightResourceType.MYSQL, RESOURCE_PATH + "mysql.props");
		RESOURCE_TYPE_LOCAL_PATH.put(LightResourceType.MYSQL, RESOURCE_LOCAL_PATH + "mysql.props");
		RESOURCE_TYPE_PATH.put(LightResourceType.REDIS, RESOURCE_PATH + "redis.props");
		RESOURCE_TYPE_LOCAL_PATH.put(LightResourceType.REDIS, RESOURCE_LOCAL_PATH + "redis.props");
	}

	private String key;

	LightResourceType(String key) {
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

}
