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

public class LightProps implements LightSubscriber {

	private static Logger logger = LoggerFactory.getLogger(LightProps.class);

	private static final String OPEN_GROUP = "openGroup";

	private Props props;

	private LightAppMeta lightAppMeta;

	private static LightProps LIGHT_PROPS;

	private LightProps(LightAppMeta lightAppMeta) {
		this.props = new Props();
		this.lightAppMeta = lightAppMeta;
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
			File file = new File(lightAppMeta.getLightPropsLocalURL());
			if (file.isFile()) {
				try {
					props.load(file);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}
			}
		} else {
			updatePropsData(LightSubscription.getSubscription(this).getData(null));
		}
	}

	private void updatePropsData(byte[] data) {
		try {
			props.load(new ByteArrayInputStream(data));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public boolean isOpenGroup() {
		return LightConstants.STR1.equals(props.getValue(OPEN_GROUP));
	}

	@Override
	public String getRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubscriptionPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processData(byte[] data, String path) {
		// TODO Auto-generated method stub

	}

}
