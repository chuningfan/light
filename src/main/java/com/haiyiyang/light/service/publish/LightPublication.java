package com.haiyiyang.light.service.publish;

import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;
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

	public void publishService(String path, byte[] data) {
		createPath(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
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
