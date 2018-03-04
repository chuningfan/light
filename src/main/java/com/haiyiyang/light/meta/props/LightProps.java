package com.haiyiyang.light.meta.props;

import java.io.File;

import jodd.props.Props;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.subscriber.LightSubscriber;

public class LightProps {

	private static Logger logger = LogManager.getLogger(LightProps.class);

	private static Props LOCAL_LIGHT_PROPS;
	private static Props REMOTE_LIGHT_PROPS;

	public static void initLightProps(LightAppMeta lightAppMeta) {

		File file = new File(lightAppMeta.getLightPropsLocalURL());
		if (file.isFile() && LOCAL_LIGHT_PROPS == null) {
			synchronized (LOCAL_LIGHT_PROPS) {
				if (LOCAL_LIGHT_PROPS == null) {
					LOCAL_LIGHT_PROPS = new Props();
					try {
						LOCAL_LIGHT_PROPS.load(file);
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
					}
					refreshRemoteLightProps(lightAppMeta);
				}
			}
		}
	}

	public static void refreshRemoteLightProps(LightAppMeta lightAppMeta) {
		LightSubscriber lightSubscriber = LightSubscriber.getLightConfigSubscriber();
		REMOTE_LIGHT_PROPS = lightSubscriber.subscribeLightConfig(lightAppMeta.getLightPropsRemoteURL());
	}

	public static String getLightProps(String key) {
		if (LOCAL_LIGHT_PROPS != null) {
			return LOCAL_LIGHT_PROPS.getValue(key);
		}
		return REMOTE_LIGHT_PROPS.getValue(key);
	}

}
