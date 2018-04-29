package com.haiyiyang.light.service.subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.haiyiyang.light.registry.RegistryConnection;

public class LightSubscription extends RegistryConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightSubscription.class);

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
		return getData(path, this);
	}

	public List<byte[]> getChildrenData(String path) {
		List<String> childrenPaths = getChildren(path, this);
		if (childrenPaths != null && !childrenPaths.isEmpty()) {
			List<byte[]> result = new ArrayList<>(childrenPaths.size());
			for (String childrenPath : childrenPaths) {
				result.add(getData(childrenPath));
			}
			return result;
		}
		return Collections.emptyList();
	}

	@Override
	public void doWatcherProcess(boolean sessionExpired, WatchedEvent event) {
		LOGGER.info("Received [WatchedEvent], sessionExpired: {}, event: {}.", sessionExpired, event);
		if (sessionExpired) {
			lightSubscriber.processData(getData(lightSubscriber.getPath()));
		} else {
			lightSubscriber.processData(getData(event.getPath()));
		}
	}

}
