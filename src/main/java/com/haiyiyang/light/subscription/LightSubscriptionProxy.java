package com.haiyiyang.light.subscription;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.google.common.collect.Lists;
import com.haiyiyang.light.service.entry.LightServiceEntry;

import jodd.props.Props;

public class LightSubscriptionProxy implements Watcher {

	private LightSubscriber lightSubscriber;

	private List<LightSubscriptionProxy> lightSubscribers = Lists.newArrayListWithCapacity(3);

	private String registry;

	public String getZookeeperServer() {
		return registry;
	}

	public static ConcurrentHashMap<String, LightSubscriptionProxy> SUBSCRIBERS = new ConcurrentHashMap<String, LightSubscriptionProxy>();

	private LightSubscriptionProxy(String registry) {
		this.registry = registry;
	}

	public static LightSubscriptionProxy getLightConfigSubscriber() {
		return getLightSubscriber(null);
	}

	public LightSubscriptionProxy getLightServiceSubscriber(String appName) {
		return getLightSubscriber(null);
	}

	private static LightSubscriptionProxy getLightSubscriber(String appName) {

		String registryHost = null; // TODO

		LightSubscriptionProxy lightSubscriber = SUBSCRIBERS.get(registryHost);
		if (lightSubscriber != null) {
			return lightSubscriber;
		}

		synchronized (SUBSCRIBERS) {
			lightSubscriber = SUBSCRIBERS.get(registryHost);
			if (lightSubscriber == null) {
				lightSubscriber = new LightSubscriptionProxy(registryHost);
				SUBSCRIBERS.put(registryHost, lightSubscriber);
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
