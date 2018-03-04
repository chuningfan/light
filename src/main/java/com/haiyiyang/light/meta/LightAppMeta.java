package com.haiyiyang.light.meta;

import java.util.Map;
import java.util.Set;

import com.haiyiyang.light.server.LightConfigServer;

public class LightAppMeta {

	private String appName;
	private String appPort;

	private static String CONFIG_REGISTRY;
	private static Set<String> PUBLISH_REGISTRY_SET;
	private static Map<String, String> SUBSCRIBE_REGISTRY_MAP;

	protected LightAppMeta(String appName) {
		CONFIG_REGISTRY = LightConfigServer.getLightConfigServer();
	}

	public String getLightPropsLocalURL() {
		return null;
	}

	public String getLightPropsRemoteURL() {
		return null;
	}

	public String getPortPropsLocalURL() {
		return null;
	}

	public String getPortPropsRemoteURL() {
		return null;
	}

	public String getSubscribeRegistry() {
		return null;
	}

}