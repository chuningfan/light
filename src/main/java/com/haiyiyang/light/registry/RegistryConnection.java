package com.haiyiyang.light.registry;

import java.io.IOException;
import java.util.List;
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
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RegistryConnection implements Watcher {

	private static Logger logger = LoggerFactory.getLogger(RegistryConnection.class);

	protected final static String PATH_LIGHT = "/light";

	protected String registry = null;
	protected ZooKeeper zooKeeper = null;

	private CountDownLatch countDownLatch = null;
	protected static final int SESSION_TIMEOUT = 50;

	protected volatile static ConcurrentHashMap<String, String> REGISTRY_LEVEL_LOCK = new ConcurrentHashMap<>(8);

	protected static ConcurrentHashMap<String, ZooKeeper> REGISTRIES = new ConcurrentHashMap<>(8);

	protected RegistryConnection(String registry) {
		synchronized (REGISTRY_LEVEL_LOCK.get(registry)) {
			this.registry = registry;
			this.getRegistry();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			if (countDownLatch != null) {
				countDownLatch.countDown();
				countDownLatch = null;
				logger.info("CountDownLatch has counted down the latch.");
			}
			if (event.getPath() != null) {
				doWatcherProcess(false, event);
			}
		} else if (event.getState() == KeeperState.Expired) {
			getRegistry();
			doWatcherProcess(true, event);
			logger.info("KeeperState Expired. zooKeeper: {}", zooKeeper);
		}
	}

	abstract public void doWatcherProcess(boolean sessionExpired, WatchedEvent event);

	protected ZooKeeper getRegistry() {
		synchronized (this.registry) {
			zooKeeper = REGISTRIES.get(this.registry);
			if (zooKeeper == null) {
				this.connect();
			}
			if (zooKeeper != null) {
				if (!zooKeeper.getState().isAlive()) {
					this.connect();
				} else if (!zooKeeper.getState().isConnected()) {
					this.close();
					this.connect();
				}
			}
			return zooKeeper;
		}
	}

	private void connect() {
		countDownLatch = new CountDownLatch(1);
		try {
			zooKeeper = new ZooKeeper(registry, SESSION_TIMEOUT, this);
			REGISTRIES.put(registry, zooKeeper);
			countDownLatch.await(SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
			logger.info("Zookeeper connection {} have been establish.", registry);
		} catch (IOException e) {
			logger.error("Zookeeper connection {} error: {}.", registry, e.getMessage());
		} catch (InterruptedException e) {
			logger.error("Zookeeper connection {} got interrupted exception: {}.", registry, e.getMessage());
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

	protected void createLightPath() {
		createPath(PATH_LIGHT, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	protected Stat existsPath(String path, boolean watch) {
		try {
			return getRegistry().exists(path, watch);
		} catch (KeeperException ke) {
			logger.error("Execute existsPath caused keeper exception, code : {}", ke.code());
			return null;
		} catch (InterruptedException ie) {
			logger.error("Execute existsPath caused interrupted exception : {}", ie.getMessage());
			return null;
		}
	}

	protected void createPath(String path, byte data[], List<ACL> acl, CreateMode createMode) {
		synchronized (this.registry) {
			if (existsPath(path, false) == null) {
				try {
					getRegistry().create(path, null, acl, createMode);
					logger.info("ZookKeeper created the path {}.", path);
				} catch (KeeperException ke) {
					logger.error("Execute createPath {} caused KeeperException error : {}", path, ke.getMessage());
				} catch (InterruptedException ie) {
					logger.error("Execute createPath {} caused InterruptedException error : {}", path, ie.getMessage());
				}
			}
		}
	}

	public byte[] getData(String path, Watcher watcher) {
		if (existsPath(path, false) != null) {
			try {
				return getRegistry().getData(path, watcher, null);
			} catch (KeeperException ke) {
				logger.error("Execute getData caused KeeperException error : {}", ke.getMessage());
			} catch (InterruptedException ie) {
				logger.error("Execute getData caused InterruptedException error : {}", ie.getMessage());
			}
		}
		return null;
	}

	public List<String> getChildren(String path, Watcher watcher) {
		if (existsPath(path, false) != null) {
			try {
				return getRegistry().getChildren(path, watcher);
			} catch (KeeperException ke) {
				logger.error("Execute getChildren of path {} caused KeeperException error : {}", path, ke.getMessage());
			} catch (InterruptedException ie) {
				logger.error("Execute getChildren of path {} caused InterruptedException error : {}", path,
						ie.getMessage());
			}
		}
		return null;
	}

}
