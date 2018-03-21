package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.subscription.LightSubscriber;
import com.haiyiyang.light.subscription.LightSubscription;

import jodd.props.Props;
import jodd.props.PropsEntry;

public class AppProps implements LightSubscriber {
	protected static Logger LOGGER = LoggerFactory.getLogger(AppProps.class);

	private static final String SECTION_RESOURCE = "resource";
	private static final String APP_PROPS_PATH = "/light/app/";
	private static final String APP_PROPS_LOCAL_PATH = LightConstants.USER_HOME
			+ APP_PROPS_PATH.replaceAll("/", LightConstants.FS);

	private Props props;
	private String appPropsPath;
	private String appPropsLocalPath;

	private LightAppMeta lightAppMeta;
	private static AppProps APP_PROPS;

	private AppProps(LightAppMeta lightAppMeta) {
		this.props = new Props();
		this.lightAppMeta = lightAppMeta;
		this.appPropsPath = new StringBuilder(APP_PROPS_PATH).append(lightAppMeta.getAppName())
				.append(LightConstants.DOT_PROPS).toString();
		this.appPropsLocalPath = new StringBuilder(APP_PROPS_LOCAL_PATH).append(lightAppMeta.getAppName())
				.append(LightConstants.DOT_PROPS).toString();
		initAppProps();
	}

	public static AppProps SINGLETON(LightAppMeta lightAppMeta) {
		if (APP_PROPS != null) {
			return APP_PROPS;
		}
		synchronized (APP_PROPS) {
			if (APP_PROPS == null) {
				APP_PROPS = new AppProps(lightAppMeta);
			}
		}
		return APP_PROPS;
	}

	private void initAppProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(appPropsLocalPath);
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

	public Map<String, String> getResources() {
		Map<String, String> map = new HashMap<>();
		Iterator<PropsEntry> it = props.entries().section(SECTION_RESOURCE).iterator();
		PropsEntry pe;
		while (it.hasNext()) {
			pe = it.next();
			map.put(pe.getKey(), pe.getValue());
		}
		return map;
	}

	public String getPropsValue(String key) {
		return props.getValue(key);
	}

	public String getPropsValue(final String key, String profiles) {
		return props.getValue(key, profiles);
	}

	@Override
	public String getPath() {
		return this.appPropsPath;
	}

	@Override
	public String getRegistry() {
		return lightAppMeta.getConfigRegistry();
	}

	@Override
	public void processData(byte[] data, String path) {
		LOGGER.info("AppProps>>>> PATH: {}, data", path, data);
	}

}
