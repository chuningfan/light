package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.subscription.LightSubscriber;
import com.haiyiyang.light.service.subscription.LightSubscription;

import jodd.props.Props;

public class PortProps implements LightSubscriber {

	private static Logger LOGGER = LoggerFactory.getLogger(PortProps.class);

	private static final int DEFAULT_APP_PORT = 5001;
	private static final String APP_PORT_PROPS_URL = "/light/appPort.props";
	private static final String APP_PORT_PROPS_LOCAL_URL = LightConstants.USER_HOME
			+ APP_PORT_PROPS_URL.replaceAll("/", LightConstants.FS);

	private Props props;
	private static PortProps PORT_PROPS;
	private static LightAppMeta LIGHT_APP_META;

	private PortProps(LightAppMeta lightAppMeta) {
		props = new Props();
		PortProps.LIGHT_APP_META = lightAppMeta;
		initPortProps();
	}

	public static PortProps SINGLETON(LightAppMeta lightAppMeta) {
		if (PORT_PROPS != null) {
			return PORT_PROPS;
		}
		synchronized (PORT_PROPS) {
			if (PORT_PROPS == null) {
				PORT_PROPS = new PortProps(lightAppMeta);
			}
		}
		return PORT_PROPS;
	}

	private void initPortProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(APP_PORT_PROPS_LOCAL_URL);
			if (file.isFile()) {
				try {
					props.load(file);
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		} else {
			updatePropsData(LightSubscription.getSubscription(this).getData(APP_PORT_PROPS_URL));
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

	public int getAppPort(String appName) {
		Integer appPort = props.getIntegerValue(appName);
		if (appPort != null) {
			return appPort.intValue();
		}
		return DEFAULT_APP_PORT;
	}

	@Override
	public String getRegistry() {
		return LIGHT_APP_META.getConfigRegistry();
	}

	@Override
	public List<String> getPaths() {
		return Lists.newArrayList(APP_PORT_PROPS_URL);
	}

	@Override
	public void processData(String path, byte[] data) {
		LOGGER.info("PortProps>>>> PATH: {}, data", path, data);
	}

}
