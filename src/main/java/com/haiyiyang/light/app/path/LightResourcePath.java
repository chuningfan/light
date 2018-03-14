package com.haiyiyang.light.app.path;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;

public class LightResourcePath {

	private final static String APP = "app";
	private final static String LIGHT = "light";
	private final static String PROPS = ".props";

	private final static String RESOURCE = "resource";
	private final static String LIGHT_PROPS = "light.props";
	private final static String APP_PORT_PROPS = "appPort.props";

	private final static String[] LIGHT_PATH_MEMBER = new String[] { LIGHT, LIGHT_PROPS };
	private final static String[] APP_PORT_PATH_MEMBER = new String[] { LIGHT, APP_PORT_PROPS };

	private final String[] APP_PATH_MEMBER = new String[] { LIGHT, APP };
	private final String[] RESOURCE_PATH_MEMBER = new String[] { LIGHT, RESOURCE };

	public static final String APP_PROPS_PATH = "/light/app/";
	public static final String APP_PROPS_LOCAL_PATH = LightConstants.USER_HOME
			+ APP_PROPS_PATH.replaceAll("/", LightConstants.FS);

	public static final String LIGHT_PROPS_URL = "/light/light.props";
	public static final String LIGHT_PROPS_LOCAL_URL = LightConstants.USER_HOME
			+ LIGHT_PROPS_URL.replaceAll("/", LightConstants.FS);

	public static final String APP_PORT_PROPS_URL = "/light/appPort.props";
	public static final String APP_PORT_PROPS_LOCAL_URL = LightConstants.USER_HOME
			+ APP_PORT_PROPS_URL.replaceAll("/", LightConstants.FS);

	public static final String RESOURCE_PROPS_PATH = "/light/resource/";
	public static final String RESOURCE_PROPS_LOCAL_PATH = LightConstants.USER_HOME
			+ RESOURCE_PROPS_PATH.replaceAll("/", LightConstants.FS);

	private LightAppMeta lightAppMeta;
	private static LightResourcePath LIGHT_RESOURCE_PATH;

	private LightResourcePath(LightAppMeta lightAppMeta) {
		this.lightAppMeta = lightAppMeta;
		this.initLightResourcePath();
	}

	public static LightResourcePath SINGLETON(LightAppMeta lightAppMeta) {
		if (LIGHT_RESOURCE_PATH != null) {
			return LIGHT_RESOURCE_PATH;
		}
		synchronized (LIGHT_RESOURCE_PATH) {
			if (LIGHT_RESOURCE_PATH == null) {
				LIGHT_RESOURCE_PATH = new LightResourcePath(lightAppMeta);
			}
		}
		return LIGHT_RESOURCE_PATH;
	}

	private void initLightResourcePath() {
		
		
	}

	private String appName;

}
