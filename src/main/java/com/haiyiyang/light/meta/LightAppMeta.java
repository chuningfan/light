package com.haiyiyang.light.meta;

import java.util.Map;
import java.util.Set;

import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.server.LightConfigServer;

public class LightAppMeta {

	private String appName;
	private String appPort;

	private static String CONFIG_REGISTRY;
	private static Set<String> PUBLISH_REGISTRY_SET;
	private static Map<String, String> SUBSCRIBE_REGISTRY_MAP;

	private static volatile LightAppMeta LIGHT_APP_META;

	private LightAppMeta(String appName) throws LightException{
		this.appName = appName;
		CONFIG_REGISTRY = LightConfigServer.getLightConfigServer();
	}

	public static LightAppMeta SINGLETON(String appName) {
		if (LIGHT_APP_META == null) {
			synchronized (LIGHT_APP_META) {
				if (LIGHT_APP_META == null) {
					LIGHT_APP_META = new LightAppMeta(appName);
				}
			}
		}
		return LIGHT_APP_META;
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