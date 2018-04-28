package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.subscription.LightSubscriber;
import com.haiyiyang.light.service.subscription.LightSubscription;
import com.haiyiyang.light.utils.LightUtils;

import jodd.props.Props;

public class LightProps implements LightSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightProps.class);

	private static final String TIMEOUT = "timeout";
	private static final long DEFAULT_TIMEOUT = 10000;

	private static final String MIN_THREAD = "minThread";
	private static final int DEFAULT_MIN_THREAD = 10;
	private static final String MAX_THREAD = "maxThread";
	private static final int DEFAULT_MAX_THREAD = 100;

	private static final String PROXY_TYPE = "proxyType";
	private static final String DEFAULT_PROXY_TYPE = "JDK";

	private static final String SERIALIZER = "serializer";
	private static final String DEFAULT_SERIALIZER = "PROTOBUF";

	private static final String IP_SEGMENT_PREFIX = "ipSegmentPrefix";

	private static final String PUBLISH_REGISTRY = "publishRegistry";
	private static final String SUBSCRIPTION_REGISTRY = "subscriptionRegistry";
	private static final String DEFAULT_REGISTRY = "127.0.0.1:2181";

	private static final String OPEN_GROUP = "openGroup";
	private static final String NEED_SIGNATURE = "needSignature";

	private static final byte DEFAULT_SERVER_LOAD_WEIGHT = 3;
	private static final String SERVER_LOAD_WEIGHT = "serverLoadWeight";

	private static final String LIGHT_PROPS_URL = "/light/light.props";
	private static final String LIGHT_PROPS_LOCAL_URL = LightUtils.getLocalPath(LIGHT_PROPS_URL);

	public static final String DOMAIN_PACKAGES = "domainPackages";

	private Props props = new Props();;
	private static LightProps LIGHT_PROPS;

	private LightAppMeta lightAppMeta;
	private List<String> domainPackageList;

	private LightProps(LightAppMeta lightAppMeta) {
		this.lightAppMeta = lightAppMeta;
		initializeLightProps();
	}

	public static LightProps SINGLETON(LightAppMeta lightAppMeta) {
		if (LIGHT_PROPS != null) {
			return LIGHT_PROPS;
		}
		synchronized (LightProps.class) {
			if (LIGHT_PROPS == null) {
				LIGHT_PROPS = new LightProps(lightAppMeta);
			}
		}
		return LIGHT_PROPS;
	}

	private void initializeLightProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(LIGHT_PROPS_LOCAL_URL);
			if (!file.isFile()) {
				LOGGER.error("The file [{}] does not exists.", LIGHT_PROPS_LOCAL_URL);
				throw new RuntimeException(LightException.FILE_NOT_FOUND);
			}
			try {
				props.load(file);
			} catch (Exception ex) {
				LOGGER.error("Loading file [{}] failed.", LIGHT_PROPS_LOCAL_URL);
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
			}
		} else {
			byte[] data = LightSubscription.getSubscription(this).getData(LIGHT_PROPS_URL);
			if (data == null || data.length == 0) {
				LOGGER.error("The file [{}] does not exists, or is empty.", LIGHT_PROPS_URL);
				throw new RuntimeException(LightException.FILE_NOT_FOUND_OR_EMPTY);
			}
			updatePropsData(data);
		}
	}

	private void updatePropsData(byte[] data) {
		synchronized (this) {
			try {
				props.load(new ByteArrayInputStream(data));
			} catch (IOException e) {
				LOGGER.error("Loading file [{}] failed.", LIGHT_PROPS_URL);
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
			}
		}
	}

	public List<String> getDomainPackages() {
		if (domainPackageList != null) {
			return domainPackageList;
		}
		String domainPackages = props.getValue(DOMAIN_PACKAGES);
		if (domainPackages == null) {
			domainPackageList = Collections.emptyList();
		} else {
			domainPackageList = Lists.newArrayList(domainPackages.split(LightConstants.COMMA));
			domainPackageList.sort((a, b) -> (a.length() > b.length()) ? -1 : 1);
		}
		return domainPackageList;
	}

	public int getMinThread() {
		Integer minThread = props.getIntegerValue(MIN_THREAD, lightAppMeta.getAppName());
		if (minThread != null) {
			return minThread.intValue();
		}
		return DEFAULT_MIN_THREAD;
	}

	public int getMaxThread() {
		Integer maxThread = props.getIntegerValue(MAX_THREAD, lightAppMeta.getAppName());
		if (maxThread != null) {
			return maxThread.intValue();
		}
		return DEFAULT_MAX_THREAD;
	}

	public long getTimeout() {
		Long timeout = props.getLongValue(TIMEOUT, lightAppMeta.getAppName());
		if (timeout != null) {
			return timeout.longValue();
		}
		return DEFAULT_TIMEOUT;
	}

	public String getPublishRegistry() {
		String publishRegistry = props.getValue(PUBLISH_REGISTRY, lightAppMeta.getAppName());
		if (publishRegistry != null && !publishRegistry.isEmpty()) {
			return publishRegistry;
		}
		return DEFAULT_REGISTRY;
	}

	public String getSubscriptionRegistry(String subscriptionAppName) {
		String subscriptionRegistry = props.getValue(SUBSCRIPTION_REGISTRY, subscriptionAppName);
		if (subscriptionRegistry != null) {
			return subscriptionRegistry;
		}
		return DEFAULT_REGISTRY;
	}

	public byte getServerLoadWeight() {
		Integer weight = props.getIntegerValue(SERVER_LOAD_WEIGHT, lightAppMeta.getMachineIp());
		if (weight != null) {
			return weight.byteValue();
		}
		return DEFAULT_SERVER_LOAD_WEIGHT;
	}

	public String getProxyType() {
		String proxyType = props.getValue(PROXY_TYPE, lightAppMeta.getAppName());
		if (proxyType != null) {
			return proxyType;
		}
		return DEFAULT_PROXY_TYPE;
	}

	public String getSerializer() {
		String serializer = props.getValue(SERIALIZER, lightAppMeta.getAppName());
		if (serializer != null) {
			return serializer;
		}
		return DEFAULT_SERIALIZER;
	}

	public boolean isOpenGroup() {
		return LightConstants.STR1.equals(props.getValue(OPEN_GROUP));
	}

	public boolean needSignature() {
		return LightConstants.STR1.equals(props.getValue(NEED_SIGNATURE, lightAppMeta.getAppName()));
	}

	public String getIpSegmentPrefix() {
		return props.getValue(IP_SEGMENT_PREFIX);
	}

	public String getPropsValue(String key) {
		return props.getValue(key);
	}

	public String getPropsValue(final String key, String profiles) {
		return props.getValue(key, profiles);
	}

	@Override
	public String getRegistry() {
		return lightAppMeta.getConfigRegistry();
	}

	@Override
	public List<String> getPaths() {
		return Lists.newArrayList(LIGHT_PROPS_URL);
	}

	@Override
	public void processData(String path, byte[] data) {
		updatePropsData(data);
		LOGGER.info("Reloaded file [{}].", path);
	}

}
