package com.haiyiyang.light.app;

import com.haiyiyang.light.meta.LightAppMeta;

public class LightApp extends LightAppMeta {

	private static volatile LightApp LIGHT_APP;

	private LightApp(String appName) {
		super(appName);
	}

	public static LightApp SINGLETON(String appName) {
		if (LIGHT_APP == null) {
			synchronized (LIGHT_APP) {
				if (LIGHT_APP == null) {
					LIGHT_APP = new LightApp(appName);
				}
			}
		}
		return LIGHT_APP;
	}

	public String getAppName() {
		return this.getAppName();
	}

}
