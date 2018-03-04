package com.haiyiyang.light.context;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LightAppContext extends ClassPathXmlApplicationContext {
	@Override
	protected void onClose() {
		// TODO
	}

	@Override
	public void registerShutdownHook() {
		// TODO
	}

}
