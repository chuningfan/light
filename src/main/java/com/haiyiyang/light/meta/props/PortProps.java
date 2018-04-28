package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

public class PortProps implements LightSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortProps.class);

	private static final int DEFAULT_APP_PORT = 5001;
	private static final String APP_PORT_PROPS_URL = "/light/appPort.props";
	private static final String APP_PORT_PROPS_LOCAL_URL = LightUtils.getLocalPath(APP_PORT_PROPS_URL);

	private Props props = new Props();
	private static PortProps PORT_PROPS;
	private static LightAppMeta LIGHT_APP_META;

	private PortProps(LightAppMeta lightAppMeta) {
		PortProps.LIGHT_APP_META = lightAppMeta;
		initializePortProps();
	}

	public static PortProps SINGLETON(LightAppMeta lightAppMeta) {
		if (PORT_PROPS != null) {
			return PORT_PROPS;
		}
		synchronized (PortProps.class) {
			if (PORT_PROPS == null) {
				PORT_PROPS = new PortProps(lightAppMeta);
			}
		}
		return PORT_PROPS;
	}

	private void initializePortProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(APP_PORT_PROPS_LOCAL_URL);
			if (!file.isFile()) {
				LOGGER.error("The file [{}] does not exists.", APP_PORT_PROPS_LOCAL_URL);
				throw new RuntimeException(LightException.FILE_NOT_FOUND);
			}
			try {
				props.load(file);
			} catch (Exception ex) {
				LOGGER.error("Loading file [{}] failed.", APP_PORT_PROPS_LOCAL_URL);
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
			}
		} else {
			byte[] data = LightSubscription.getSubscription(this).getData(APP_PORT_PROPS_URL);
			if (data == null || data.length == 0) {
				LOGGER.error("The file [{}] does not exists, or is empty.", APP_PORT_PROPS_URL);
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
				LOGGER.error("Loading file [{}] failed.", APP_PORT_PROPS_URL);
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
			}
		}
	}

	public int getAppPort() {
		Integer appPort = props.getIntegerValue(LIGHT_APP_META.getAppName());
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
		updatePropsData(data);
		LOGGER.info("Reloaded file [{}].", path);
	}

}
