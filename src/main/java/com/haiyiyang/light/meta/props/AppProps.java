package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.subscription.LightSubscriber;
import com.haiyiyang.light.service.subscription.LightSubscription;

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

	private static AppProps APP_PROPS;
	private static LightAppMeta LIGHT_APP_META;

	private AppProps(LightAppMeta lightAppMeta) {
		props = new Props();
		AppProps.LIGHT_APP_META = lightAppMeta;
		this.appPropsPath = new StringBuilder(APP_PROPS_PATH).append(lightAppMeta.getAppName())
				.append(LightConstants.DOT_PROPS).toString();
		this.appPropsLocalPath = new StringBuilder(APP_PROPS_LOCAL_PATH).append(lightAppMeta.getAppName())
				.append(LightConstants.DOT_PROPS).toString();
		initializeAppProps();
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

	private void initializeAppProps() {
		if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
			File file = new File(appPropsLocalPath);
			if (!file.isFile()) {
				LOGGER.error("The file[{}] does not exists.", appPropsLocalPath);
				throw new RuntimeException(LightException.FILE_NOT_FOUND);
			}
			try {
				props.load(file);
			} catch (Exception ex) {
				LOGGER.error("Loading file[{}] failed.", appPropsLocalPath);
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
			}
		} else {
			byte[] data = LightSubscription.getSubscription(this).getData(appPropsPath);
			if (data == null || data.length == 0) {
				LOGGER.error("The file[{}] does not exists, or is empty.", appPropsPath);
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
				LOGGER.error("Loading file[{}] failed.", appPropsPath);
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
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
	public String getRegistry() {
		return LIGHT_APP_META.getConfigRegistry();
	}

	@Override
	public List<String> getPaths() {
		return Lists.newArrayList(this.appPropsPath);
	}

	@Override
	public void processData(String path, byte[] data) {
		updatePropsData(data);
		LOGGER.info("Reloading file[{}].", path);
	}

}
