package com.haiyiyang.light.subscription;

import java.util.Map;

import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.haiyiyang.light.registry.RegistryConnection;

public class LightSubscription extends RegistryConnection {

	private static Logger LOGGER = LoggerFactory.getLogger(LightSubscription.class);

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
		if (path == null) {
			path = this.lightSubscriber.getSubscriptionPath();
		}
		Stat stat = existsPath(path, false);
		if (stat == null) {
			LOGGER.debug("The path {} is not exists.", path);
			return null;
		}
		return getData(path, this);
	}
	
	public byte[] getChildrenDatas(String path) {
		if (path == null) {
			path = this.lightSubscriber.getSubscriptionPath();
		}
		Stat stat = existsPath(path, false);
		if (stat == null) {
			LOGGER.debug("The path {} is not exists.", path);
			return null;
		}
		return getData(path, this);
	}

}
