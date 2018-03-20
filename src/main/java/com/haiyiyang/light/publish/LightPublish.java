package com.haiyiyang.light.publish;

import org.apache.zookeeper.WatchedEvent;

import com.haiyiyang.light.registry.RegistryConnection;

public class LightPublish extends RegistryConnection {

	protected LightPublish(String registry) {
		super(registry);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doWatcherProcess(boolean sessionExpired, WatchedEvent event) {
		// TODO Auto-generated method stub

	}

}
