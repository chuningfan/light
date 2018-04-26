package com.haiyiyang.light.app.props;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;

import jodd.props.Props;

public class SettingsProps {
	private static Logger LOGGER = LoggerFactory.getLogger(SettingsProps.class);

	private final static String FILE_SETTINGS_PROPS = "settings.props";
	private final static String APP_NAME = "appName";
	private final static String SCAN_PACKAGES = "scanPackages";
	private final static String ANNOTATED_CLASSES = "annotatedClasses";

	private Props props;
	private static SettingsProps SETTINGS_PROPS;

	private SettingsProps() throws IOException {
		this.props = new Props();
		initSettingsProps();
	}

	public static SettingsProps SINGLETON() throws IOException {
		if (SETTINGS_PROPS != null) {
			return SETTINGS_PROPS;
		}
		synchronized (SETTINGS_PROPS) {
			if (SETTINGS_PROPS == null) {
				SETTINGS_PROPS = new SettingsProps();
			}
		}
		return SETTINGS_PROPS;
	}

	private void initSettingsProps() throws IOException {
		Enumeration<URL> ps = null;
		try {
			ps = Thread.currentThread().getContextClassLoader().getResources(FILE_SETTINGS_PROPS);
		} catch (IOException e) {
			LOGGER.error("The file [settings.props] was not found.");
			throw e;
		}
		if (ps != null && ps.hasMoreElements()) {
			InputStream in = null;
			try {
				in = ps.nextElement().openStream();
				props.load(in);
				LOGGER.debug("Loaded file [settings.props] successful.");
			} catch (IOException e) {
				LOGGER.error("Loaded file [settings.props] error.");
				throw e;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						LOGGER.error("Close file [settings.props] input stream error.");
						throw e;
					}
				}
			}
		}
	}

	public String getAppName() {
		String appName = props.getValue(APP_NAME);
		if (appName == null || appName.isEmpty()) {
			return null;
		}
		return appName;
	}

	public String getScanPackages() {
		return props.getValue(SCAN_PACKAGES);
	}

	public String getAnnotatedClasses() {
		return props.getValue(ANNOTATED_CLASSES);
	}

	public Class<?>[] getConfigurableClasses() throws ClassNotFoundException {
		String annotatedClasses = getAnnotatedClasses();
		if (annotatedClasses == null || annotatedClasses.isEmpty()) {
			return null;
		}
		String[] classesNames = annotatedClasses.split(LightConstants.COMMA);
		Class<?>[] classes = new Class<?>[classesNames.length];
		for (int i = 0; i < classesNames.length; i++) {
			try {
				classes[i] = Class.forName(classesNames[i]);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Class {} forName error.", classesNames[i]);
				throw e;
			}
		}
		return classes;
	}

}
