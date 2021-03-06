package com.haiyiyang.light.utils;

import com.haiyiyang.light.constant.LightConstants;

public class LightUtils {

	public static boolean useLocalProps() {
		return LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS);
	}
	
	public static String getLocalPath(String path) {
		return LightConstants.USER_HOME + path.replace('/', LightConstants.FS_CHAR);
	}
}
