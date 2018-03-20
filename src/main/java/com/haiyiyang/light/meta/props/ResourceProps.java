package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.subscription.LightSubscriber;
import com.haiyiyang.light.subscription.LightSubscription;

import jodd.props.Props;

public class ResourceProps implements LightSubscriber {
	protected static Logger LOGGER = LoggerFactory.getLogger(ResourceProps.class);

	public static final String RESOURCE_PROPS_PATH = "/light/resource/";
	public static final String RESOURCE_PROPS_LOCAL_PATH = LightConstants.USER_HOME
			+ RESOURCE_PROPS_PATH.replaceAll("/", LightConstants.FS);

	private Props props;
	private LightAppMeta lightAppMeta;
	private static ResourceProps RESOURCE_PROPS;

	private ResourceProps(LightAppMeta lightAppMeta) {
		this.props = new Props();
		this.lightAppMeta = lightAppMeta;
		initResourceProps();
	}

	public static ResourceProps SINGLETON(LightAppMeta lightAppMeta) {
		if (RESOURCE_PROPS != null) {
			return RESOURCE_PROPS;
		}
		synchronized (RESOURCE_PROPS) {
			if (RESOURCE_PROPS == null) {
				RESOURCE_PROPS = new ResourceProps(lightAppMeta);
			}
		}
		return RESOURCE_PROPS;
	}

	private void initResourceProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(RESOURCE_PROPS_LOCAL_PATH);
			if (file.isFile()) {
				try {
					props.load(file);
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		} else {
			updatePropsData(LightSubscription.getSubscription(this).getData());
		}
	}

	private void updatePropsData(byte[] data) {
		synchronized (this) {
			try {
				props.load(new ByteArrayInputStream(data));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public String getPropsValue(String key) {
		return props.getValue(key);
	}

	public String getPropsValue(final String key, String profiles) {
		return props.getValue(key, profiles);
	}

	@Override
	public String getPath() {
		return RESOURCE_PROPS_PATH;
	}

	@Override
	public String getRegistry() {
		return lightAppMeta.getConfigRegistry();
	}

	@Override
	public void processData(byte[] data, String path) {
		LOGGER.info("ResourceProps>>>> PATH: {}, data", path, data);
	}

}
