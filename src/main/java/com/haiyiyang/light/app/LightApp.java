package com.haiyiyang.light.app;

import com.haiyiyang.light.context.LightApplicationContext;

public class LightApp {

	private static LightApplicationContext lightApplicationContext;

	public static LightApplicationContext getContext() {
		return lightApplicationContext;
	}

	public static void main(String[] args) {
		if (lightApplicationContext == null) {
			synchronized (lightApplicationContext) {
				if (lightApplicationContext == null) {
					lightApplicationContext = new LightApplicationContext();
					lightApplicationContext.start();
				}
			}
		}
	}

}
