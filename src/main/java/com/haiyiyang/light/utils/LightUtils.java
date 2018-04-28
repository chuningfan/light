package com.haiyiyang.light.utils;

import com.haiyiyang.light.constant.LightConstants;

public class LightUtils {

	public static String getLocalPath(String path) {
		return LightConstants.USER_HOME + path.replace('/', LightConstants.FS_CHAR);
	}

}
