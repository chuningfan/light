package com.haiyiyang.light.meta.props;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.meta.LightAppMeta;

import jodd.props.Props;

public class PortProps {

	protected static Logger logger = LoggerFactory.getLogger(PortProps.class);

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
				}
			}
		}
	}

}
