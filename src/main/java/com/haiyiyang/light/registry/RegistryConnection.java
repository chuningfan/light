package com.haiyiyang.light.registry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RegistryConnection implements Watcher {

	private static Logger logger = LoggerFactory.getLogger(RegistryConnection.class);

	protected final static String PATH_LIGHT = "/light";

	protected String registry = null;
	protected ZooKeeper zooKeeper = null;
	protected PathLevelLock pathLevelLock = null;

	private CountDownLatch countDownLatch = null;
	protected static final int SESSION_TIMEOUT = 60 * 1000;

	protected volatile static ConcurrentHashMap<String, String> REGISTRY_LEVEL_LOCK = new ConcurrentHashMap<>(8);
	protected volatile static ConcurrentHashMap<String, PathLevelLock> PATH_LEVEL_LOCK = new ConcurrentHashMap<>(8);

	protected static ConcurrentHashMap<String, ZooKeeper> REGISTRIES = new ConcurrentHashMap<>(8);

	protected RegistryConnection(String registry) {
		synchronized (REGISTRY_LEVEL_LOCK.get(registry)) {
			this.registry = registry;
			this.pathLevelLock = PATH_LEVEL_LOCK.putIfAbsent(registry, new PathLevelLock());
			this.getRegistry();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			countDownLatch.countDown();
			logger.info("CountDownLatch has counted down the latch.");
		}
	}

	private ZooKeeper getRegistry() {
		zooKeeper = REGISTRIES.get(this.registry);
		if (zooKeeper != null && zooKeeper.getState() != null && zooKeeper.getState().isAlive()) {
			return zooKeeper;
		}
		synchronized (this.registry) {
			if (zooKeeper == null) {
				this.connect();
				logger.info("Zookeeper connection {} have been establish.", registry);
			}
			if (zooKeeper.getState() == null || !zooKeeper.getState().isAlive()) {
				this.close();
				this.connect();
				logger.info("Zookeeper connection {} have been reestablish.", registry);
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

	private void close() {
		if (zooKeeper != null) {
			try {
				zooKeeper.close();
				logger.info("Closed zookeeper connection.");
			} catch (InterruptedException e) {
				logger.error("Closed zookeeper connection error.");
			}
		}
	}

	public void createLightPath() throws KeeperException, InterruptedException {
		if (existsPath(PATH_LIGHT, false) == null) {
			getRegistry().create(PATH_LIGHT, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("Created root path PATH_LIGHT.");
		}
	}

	public Stat existsPath(String path, boolean watch) throws KeeperException, InterruptedException {
		try {
			return getRegistry().exists(path, watch);
		} catch (KeeperException.ConnectionLossException e) {
			close();
			return getRegistry().exists(path, watch);
		}
	}

	public void createPath(String path) throws KeeperException, InterruptedException {
		this.pathLevelLock.addLock(path);
		synchronized (this.pathLevelLock.getLock(path)) {
			if (existsPath(path, false) == null) {
				getRegistry().create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				logger.info("Created root path PATH_LIGHT.");
			}
		}
	}

	class PathLevelLock {

		private ConcurrentHashMap<String, String> PATH_LEVEL_LOCK = new ConcurrentHashMap<>();

		void addLock(String path) {
			PATH_LEVEL_LOCK.putIfAbsent(path, path);
		}

		protected String getLock(String path) {
			return PATH_LEVEL_LOCK.get(path);
		}
	}

}
