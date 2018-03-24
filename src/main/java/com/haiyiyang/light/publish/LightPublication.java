package com.haiyiyang.light.publish;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.haiyiyang.light.registry.RegistryConnection;

public class LightPublication extends RegistryConnection {
	private static Logger LOGGER = LoggerFactory.getLogger(LightPublication.class);

	private LightPublisher lightPublisher;
	private static final Map<LightPublisher, LightPublication> PUBLICATIONS = Maps.newConcurrentMap();

	private LightPublication(LightPublisher publisher) {
		super(publisher.getRegistry());
		this.lightPublisher = publisher;
		PUBLICATIONS.put(this.lightPublisher, this);
	}

	public static LightPublication getPublish(LightPublisher publisher) {
		if (PUBLICATIONS.containsKey(publisher)) {
			return PUBLICATIONS.get(publisher);
		}
		synchronized (publisher) {
			if (PUBLICATIONS.containsKey(publisher)) {
				return PUBLICATIONS.get(publisher);
			} else {
				REGISTRY_LEVEL_LOCK.putIfAbsent(publisher.getRegistry(), publisher.getRegistry());
				return new LightPublication(publisher);
			}
		}
	}

	public byte[] getData(String path) {
		return getData(path, this);
	}

	public List<String> getChildren(String path) {
		return getChildren(path, this);
	}

	protected LightPublication(String registry) {
		super(registry);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doWatcherProcess(boolean sessionExpired, WatchedEvent event) {
		// TODO Auto-generated method stub

	}

}
