package com.haiyiyang.light.app.props;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;

import jodd.props.Props;

public class SettingsProps {

	private static Logger logger = LoggerFactory.getLogger(SettingsProps.class);

	private final static String FILE_SETTINGS_PROPS = "settings.props";
	private final static String KEY_APP_NAME = "appName";
	private final static String KEY_APP_SERVICE_PACKAGES = "appServicePackages";
	private final static String KEY_APP_CONFIGURABLE_CLASSES = "appConfigurableClasses";

	private static Props SETTINGS_PROPS = null;

	private static Props getSettingsProps() {
		if (SETTINGS_PROPS == null) {
			synchronized (SETTINGS_PROPS) {
				if (SETTINGS_PROPS == null) {
					initSettingsProps();
				}
			}
		}
		return SETTINGS_PROPS;
	}

	private static void initSettingsProps() {
		Enumeration<URL> ps = null;
		try {
			ps = Thread.currentThread().getContextClassLoader().getResources(FILE_SETTINGS_PROPS);
		} catch (IOException e) {
			logger.error("The file [settings.props] was not found.");
		}
		SETTINGS_PROPS = new Props();
		if (ps != null && ps.hasMoreElements()) {
			InputStream in = null;
			try {
				in = ps.nextElement().openStream();
				SETTINGS_PROPS.load(in);
			} catch (IOException e) {
				logger.error("Load file [settings.props] error.");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error("Close file [settings.props] input stream error.");
					}
				}
			}
		}
	}

	public static String getAppNameValue() {
		return getSettingsProps().getValue(KEY_APP_NAME);
	}

	public static String getAppServicePackagesValue() {
		return getSettingsProps().getValue(KEY_APP_SERVICE_PACKAGES);
	}

	private static String getAppConfigurableClassesValue() {
		return getSettingsProps().getValue(KEY_APP_CONFIGURABLE_CLASSES);
	}

	public static Class<?>[] getAppConfigurableClasses() throws LightException {
		String[] classesNames = StringUtils.tokenizeToStringArray(getAppConfigurableClassesValue(),
				LightConstants.COMMA);
		Class<?>[] classes = new Class<?>[classesNames.length];
		for (int i = 0; i < classesNames.length; i++) {
			try {
				classes[i] = Class.forName(classesNames[i]);
			} catch (ClassNotFoundException e) {
				logger.error("Class {} forName error.", classesNames[i]);
				throw new LightException(LightException.Code.SETTINGS_ERROR);
			}
		}
		return classes;
	}

}
