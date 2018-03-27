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
import com.haiyiyang.light.subscription.LightSubscriber;
import com.haiyiyang.light.subscription.LightSubscription;

import jodd.props.Props;

public class LightProps implements LightSubscriber {

	private static Logger LOGGER = LoggerFactory.getLogger(LightProps.class);

	private static final String OPEN_GROUP = "openGroup";
	public static final String LIGHT_PROPS_URL = "/light/light.props";
	public static final String LIGHT_PROPS_LOCAL_URL = LightConstants.USER_HOME
			+ LIGHT_PROPS_URL.replaceAll("/", LightConstants.FS);

	private Props props;
	private static LightProps LIGHT_PROPS;
	private static LightAppMeta LIGHT_APP_META;

	private LightProps(LightAppMeta lightAppMeta) {
		this.props = new Props();
		LightProps.LIGHT_APP_META = lightAppMeta;
		initLightProps();
	}

	public static LightProps SINGLETON(LightAppMeta lightAppMeta) {
		if (LIGHT_PROPS != null) {
			return LIGHT_PROPS;
		}
		synchronized (LIGHT_PROPS) {
			if (LIGHT_PROPS == null) {
				LIGHT_PROPS = new LightProps(lightAppMeta);
			}
		}
		return LIGHT_PROPS;
	}

	private void initLightProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(LIGHT_PROPS_LOCAL_URL);
			if (file.isFile()) {
				try {
					props.load(file);
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		} else {
			updatePropsData(LightSubscription.getSubscription(this).getData(LIGHT_PROPS_URL));
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

	public boolean isOpenGroup() {
		return LightConstants.STR1.equals(props.getValue(OPEN_GROUP));
	}

	public String getPropsValue(String key) {
		return props.getValue(key);
	}

	public String getPropsValue(final String key, String profiles) {
		return props.getValue(key, profiles);
	}

	@Override
	public String getRegistry() {
		return LIGHT_APP_META.getConfigRegistry();
	}

	@Override
	public List<String> getPaths() {
		return Lists.newArrayList(LIGHT_PROPS_URL);
	}

	@Override
	public void processData(String path, byte[] data) {
		LOGGER.info("LightProps>>>> PATH: {}, data", path, data);
	}

}
