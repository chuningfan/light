package org.haiyiyang.demo.client;

import org.haiyiyang.demo.service.DemoService;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.service.proxy.LightServiceFactory;

public class Client {
	
	public static void main(String[] args) {
		System.setProperty("useLocalProps", LightConstants.STR1);
		DemoService ds = LightServiceFactory.getService(DemoService.class);
		String result = ds.helloWord("Agent");
		System.out.println(result);
	}
}
