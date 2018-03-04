package com.haiyiyang.light.meta.props;

import java.io.File;

import jodd.props.Props;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.subscriber.LightSubscriber;

public class PortProps {

	protected static Logger logger = LogManager.getLogger(PortProps.class);

	private static Props LOCAL_PORT_PROPS, REMOTE_PORT_PROPS;

	public static String getValue(String key) {
		if (LOCAL_PORT_PROPS != null) {
			return LOCAL_PORT_PROPS.getValue(key);
		}
		return REMOTE_PORT_PROPS.getValue(key);
	}

	public static void initPortProps(LightAppMeta lightAppMeta) {
		if (LOCAL_PORT_PROPS == null) {
			synchronized (LOCAL_PORT_PROPS) {
				if (LOCAL_PORT_PROPS == null) {
					LOCAL_PORT_PROPS = new Props();
					File file = new File(lightAppMeta.getLightPropsLocalURL());
					if (file.isFile()) {
						try {
							LOCAL_PORT_PROPS.load(file);
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
						}
					}
					refreshRemotePortProps(lightAppMeta);
				}
			}
		}
	}

	public static void refreshRemotePortProps(LightAppMeta lightAppMeta) {
		LightSubscriber lightSubscriber = LightSubscriber.getLightConfigSubscriber();
		REMOTE_PORT_PROPS = lightSubscriber.subscribeLightConfig(lightAppMeta.getPortPropsRemoteURL());
	}

}
