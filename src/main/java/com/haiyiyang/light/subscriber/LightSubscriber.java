package com.haiyiyang.light.subscriber;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jodd.props.Props;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.haiyiyang.light.service.entry.LightServiceEntry;

public class LightSubscriber implements Watcher {

	private String registry;

	public String getZookeeperServer() {
		return registry;
	}

	public static ConcurrentHashMap<String, LightSubscriber> LIGHT_SUBSCRIBER_CACHE = new ConcurrentHashMap<String, LightSubscriber>();

	private LightSubscriber(String registry) {
		this.registry = registry;
	}

	public static LightSubscriber getLightConfigSubscriber() {
		return getLightSubscriber(null);
	}

	public LightSubscriber getLightServiceSubscriber(String appName) {
		return getLightSubscriber(null);
	}

	private static LightSubscriber getLightSubscriber(String appName) {

		String registryHost = null; // TODO

		LightSubscriber lightSubscriber = LIGHT_SUBSCRIBER_CACHE.get(registryHost);
		if (lightSubscriber != null) {
			return lightSubscriber;
		}

		synchronized (LIGHT_SUBSCRIBER_CACHE) {
			lightSubscriber = LIGHT_SUBSCRIBER_CACHE.get(registryHost);
			if (lightSubscriber == null) {
				lightSubscriber = new LightSubscriber(registryHost);
				LIGHT_SUBSCRIBER_CACHE.put(registryHost, lightSubscriber);
			}
		}

		return lightSubscriber;
	}

	public Props subscribeLightConfig(String configPropsPath) {
		return null;
	}

	public Map<String, Props> subscribeLightConfigs(String configPath) {
		return null;
	}

	public List<LightServiceEntry> subscribeLightService(String serviceName) {

		return null;
	}

	@Override
	public void process(WatchedEvent arg0) {
		// TODO Auto-generated method stub

	}

	private Props doSubscribeLightProps(String propertiesName) {
		return null;
	}

	public List<LightServiceEntry> doSubscribeLightService(String serviceName) {

		return null;
	}

}
