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

	private LightSubscription(LightSubscriber lightSubscriber) {
		super(lightSubscriber.getRegistry());
		this.lightSubscriber = lightSubscriber;
		SUBSCRIPTIONS.put(this.lightSubscriber, this);

	}

	public static LightSubscription getSubscription(LightSubscriber lightSubscriber) {
		if (SUBSCRIPTIONS.containsKey(lightSubscriber)) {
			return SUBSCRIPTIONS.get(lightSubscriber);
		}
		synchronized (lightSubscriber) {
			if (SUBSCRIPTIONS.containsKey(lightSubscriber)) {
				return SUBSCRIPTIONS.get(lightSubscriber);
			} else {
				return new LightSubscription(lightSubscriber);
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
