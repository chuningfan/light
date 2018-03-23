package com.haiyiyang.light.meta;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.meta.props.AppProps;
import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.meta.props.PortProps;
import com.haiyiyang.light.meta.props.ResourceProps;
import com.haiyiyang.light.server.LightConfig;

public class LightAppMeta {

	private String appName;
	private String appPort;
	private String configRegistry;
	private LightProps lightProps;
	private PortProps portProps;
	private AppProps appProps;
	private ResourceProps resourceProps;

	private static List<String> PUBLISH_REGISTRIES = Lists.newArrayListWithCapacity(3);
	private static Multimap<String, String> SUBSCRIBER_REGISTRIES_MAP = ArrayListMultimap.create();;
	private static final String DEFAULT_SUBSCRIBER_REGISTRY = "DEFAULT_SUBSCRIBER_REGISTRY";

	private static volatile LightAppMeta LIGHT_APP_META;

	private LightAppMeta(String appName) throws LightException {
		this.appName = appName;
		this.configRegistry = LightConfig.getLightConfigServer();
		this.lightProps = LightProps.SINGLETON(this);
		this.portProps = PortProps.SINGLETON(this);
		this.appProps = AppProps.SINGLETON(this);
		this.resourceProps = ResourceProps.SINGLETON(this, this.appProps.getResources());
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

	public String getAppName() {
		return appName;
	}

	public String getAppPort() {
		return appPort;
	}

	public String getConfigRegistry() {
		return configRegistry;
	}

}