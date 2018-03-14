package com.haiyiyang.light.registry;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public abstract class RegistryConnection implements Watcher {

	private static Logger logger = LogManager.getLogger(RegistryConnection.class);

	protected final static String PATH_LIGHT = "/light";

	protected String registry = null;
	protected ZooKeeper zooKeeper = null;

	private CountDownLatch countDownLatch = null;
	protected static final int SESSION_TIMEOUT = 60 * 1000;

	protected static ConcurrentHashMap<String, ZooKeeper> REGISTRIES = new ConcurrentHashMap<String, ZooKeeper>(4);

	protected RegistryConnection(String registry) {
		this.registry = registry;
		getRegistry();
	}

	@Override
	public void process(WatchedEvent arg0) {
		// TODO Auto-generated method stub

	}

	private ZooKeeper getRegistry() {
		synchronized (RegistryConnection.class) {
			if (zooKeeper == null) {
				this.connect();
				logger.info("Zookeeper connection [{}] have been establish.", registry);
			}
			if (zooKeeper.getState() == null || !zooKeeper.getState().isAlive()) {
				this.close();
				this.connect();
				logger.info("Zookeeper connection [{}] have been reestablish.", registry);
			}
		}
		return zooKeeper;
	}

	private void connect() {
		countDownLatch = new CountDownLatch(1);
		try {
			zooKeeper = new ZooKeeper(registry, SESSION_TIMEOUT, this);
			REGISTRIES.put(registry, zooKeeper);
			countDownLatch.await(SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("Closed zookeeper connection error.");
		}
	}

	public void close() {
		if (zooKeeper != null) {
			try {
				zooKeeper.close();
				logger.info("Closed zookeeper connection.");
			} catch (InterruptedException e) {
				logger.error("Closed zookeeper connection error.");
			}
		}
	}

	protected void createLightPath() throws IOException, KeeperException, InterruptedException {
		if (getRegistry().exists(PATH_LIGHT, false) == null) {
			String createdPath = getRegistry().create(PATH_LIGHT, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("Created root: " + createdPath);
		}
	}

}
