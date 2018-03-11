package com.haiyiyang.light.constant;

import java.io.File;

public class LightConstants {

	public static final String DOT = ".";
	public static final String SLASH = "/";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";

	public static final String FS = File.separator;
	public static final String LIGHT_SERVICE_PREFIX = "light:";
	public static final String DOMAIN_PACKAGE = "com.haiyiyang";

	public static final int INT0 = 0, INT1 = 1, INT2 = 2, INT3 = 3;
	public static final byte BYTE0 = 0, BYTE1 = 1, BYTE2 = 2, BYTE3 = 3;
	public static final String STR0 = "0", STR1 = "1", STR2 = "2", STR3 = "3";
	
	public static final String UH = System.getProperty("user.home");

	public static final String LIGHT_PROPS_REMOTE_URL = "/config/light/light.props";
	public static final String LIGHT_PROPS_LOCAL_URL = UH + LIGHT_PROPS_REMOTE_URL.replaceAll("/", LightConstants.FS);

	public static final String APP_PORT_PROPS_REMOTE_URL = "/config/light/appPort.props";
	public static final String APP_PORT_PROPS_LOCAL_URL = UH + APP_PORT_PROPS_REMOTE_URL.replaceAll("/", LightConstants.FS);

	public static final String RESOURCE_PROPS_REMOTE_PATH = "/config/light/resource/";
	public static final String RESOURCE_PROPS_LOCAL_PATH = UH + RESOURCE_PROPS_REMOTE_PATH.replaceAll("/", LightConstants.FS);

	public static final String APP_PROPS_REMOTE_PATH = "/config/light/app/";
	public static final String APP_PROPS_LOCAL_PATH = UH + APP_PROPS_REMOTE_PATH.replaceAll("/", LightConstants.FS);


}