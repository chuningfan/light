package com.haiyiyang.light.test.demo;

import com.haiyiyang.light.app.LightApp;
import com.haiyiyang.light.constant.LightConstants;

public class Demo {

	public static void main(String[] args) {
		System.setProperty("useLocalProps", LightConstants.STR1);
		LightApp.main(null);
	}

}
