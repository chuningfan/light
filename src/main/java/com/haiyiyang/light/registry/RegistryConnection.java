package com.haiyiyang.light.registry;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public abstract class RegistryConnection implements Watcher {

	protected static Logger logger = LogManager.getLogger(RegistryConnection.class);

	protected ZooKeeper registry = null;

	protected String registryHost = null;

	private CountDownLatch connectedSignal = null;

	protected static final int SESSION_TIMEOUT = 60 * 1000;

	protected static ConcurrentHashMap<String, ZooKeeper> registryPool = new ConcurrentHashMap<String, ZooKeeper>(4);

	@Override
	public void process(WatchedEvent arg0) {
		// TODO Auto-generated method stub

	}

	private ZooKeeper getRegistry(String registryHost) throws IOException, InterruptedException {
		if (registryHost == null || "".equals(registryHost)) {
			throw new IOException("registryHost is null or empty");
		}
		synchronized (RegistryConnection.class) {
			registry = registryPool.get(registryHost);
			if (registry == null) {
				logger.info("connection have not establish by " + registryHost);
				connect(registryHost);
				registryPool.put(registryHost, registry);
				logger.info("new zookeeper connection");
			}
			if (registry.getState() == null || !registry.getState().isAlive()) {
				logger.info("this zookeeper was died , just reconnect zookeeper");
				this.close();
				connect(registryHost);
				registryPool.put(registryHost, registry);
				logger.info("reconnect zookeeper finish");
			}
		}
		return registry;
	}

	private void connect(String registryHost) throws IOException, InterruptedException {
		connectedSignal = new CountDownLatch(1);
		registry = new ZooKeeper(registryHost, SESSION_TIMEOUT, this);
		connectedSignal.await(SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
	}

	public void close() throws InterruptedException {
		if (registry != null) {
			registry.close();
			registry = null;
			logger.info("close zookeeper connection");
		}
	}

}
