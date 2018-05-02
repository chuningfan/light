package com.haiyiyang.light.test.demo;

import com.haiyiyang.light.app.LightApp;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.service.proxy.LightServiceFactory;
import com.haiyiyang.light.test.demo.service.DemoService;

public class Demo {

	public static void main(String[] args) {
		System.setProperty("useLocalProps", LightConstants.STR1);
		LightApp.main(null);
		DemoService ds = LightServiceFactory.getService(DemoService.class);
		String result = ds.helloWord("Agent");
		System.out.println(result);
	}

}
