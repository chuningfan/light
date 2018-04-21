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

	private static Logger LOGGER = LoggerFactory.getLogger(SettingsProps.class);

	private static Props SETTINGS_PROPS = null;
	private final static String FILE_SETTINGS_PROPS = "settings.props";

	private final static String ROOT_PACKAGE = "rootPackage";
	private final static String DOMAIN_PACKAGE = "domainPackage";
	private final static String ANNOTATED_CLASSES = "annotatedClasses";

	private static Props getSettingsProps() {
		if (SETTINGS_PROPS != null) {
			return SETTINGS_PROPS;
		}
		synchronized (SETTINGS_PROPS) {
			if (SETTINGS_PROPS == null) {
				initSettingsProps();
			}
		}
		return SETTINGS_PROPS;
	}

	private static void initSettingsProps() {
		Enumeration<URL> ps = null;
		try {
			ps = Thread.currentThread().getContextClassLoader().getResources(FILE_SETTINGS_PROPS);
		} catch (IOException e) {
			LOGGER.error("The file [settings.props] was not found.");
		}
		SETTINGS_PROPS = new Props();
		if (ps != null && ps.hasMoreElements()) {
			InputStream in = null;
			try {
				in = ps.nextElement().openStream();
				SETTINGS_PROPS.load(in);
			} catch (IOException e) {
				LOGGER.error("Load file [settings.props] error.");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						LOGGER.error("Close file [settings.props] input stream error.");
					}
				}
			}
		}
	}

	public static String getRootPackage() {
		return getSettingsProps().getValue(ROOT_PACKAGE);
	}

	public static String getDomainPackage() {
		return getSettingsProps().getValue(DOMAIN_PACKAGE);
	}

	private static String getAnnotatedClasses() {
		return getSettingsProps().getValue(ANNOTATED_CLASSES);
	}

	public static Class<?>[] getConfigurableClasses() throws LightException {
		String[] classesNames = StringUtils.tokenizeToStringArray(getAnnotatedClasses(), LightConstants.COMMA);
		Class<?>[] classes = new Class<?>[classesNames.length];
		for (int i = 0; i < classesNames.length; i++) {
			try {
				classes[i] = Class.forName(classesNames[i]);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Class {} forName error.", classesNames[i]);
				throw new LightException(LightException.Code.SETTINGS_ERROR);
			}
		}
		return classes;
	}

}
