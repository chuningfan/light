package com.haiyiyang.light.meta;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.server.LightConfigServer;

public class LightAppMeta {

	private String appName;
	private String appPort;

	private static String CONFIG_REGISTRY;
	private static List<String> PUBLISH_REGISTRIES = Lists.newArrayListWithCapacity(3);
	private static Multimap<String, String> SUBSCRIBER_REGISTRIES_MAP = ArrayListMultimap.create();;
	private static final String DEFAULT_SUBSCRIBER_REGISTRY = "DEFAULT_SUBSCRIBER_REGISTRY";

	private static volatile LightAppMeta LIGHT_APP_META;

	private LightAppMeta(String appName) throws LightException {
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