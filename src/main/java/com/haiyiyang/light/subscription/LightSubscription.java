package com.haiyiyang.light.subscription;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;

import com.google.common.collect.Maps;
import com.haiyiyang.light.registry.RegistryConnection;
import com.haiyiyang.light.service.entry.LightServiceEntry;

import jodd.props.Props;

public class LightSubscription extends RegistryConnection {

	private LightSubscriber lightSubscriber;
	private static final Map<LightSubscriber, LightSubscription> SUBSCRIPTIONS = Maps.newConcurrentMap();

	private LightSubscription(LightSubscriber subscriber) {
		super(subscriber.getRegistry());
		this.lightSubscriber = subscriber;
		SUBSCRIPTIONS.put(this.lightSubscriber, this);
	}

	public static LightSubscription getSubscription(LightSubscriber subscriber) {
		if (SUBSCRIPTIONS.containsKey(subscriber)) {
			return SUBSCRIPTIONS.get(subscriber);
		}
		synchronized (subscriber) {
			if (SUBSCRIPTIONS.containsKey(subscriber)) {
				return SUBSCRIPTIONS.get(subscriber);
			} else {
				REGISTRY_LEVEL_LOCK.putIfAbsent(subscriber.getRegistry(), subscriber.getRegistry());
				return new LightSubscription(subscriber);
			}
		}
	}

	public byte[] getData(String path) {

		return null;
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

}
