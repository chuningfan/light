package com.haiyiyang.light.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.context.LightContext;

public class LightApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightApp.class);

	public static void main(String[] args) {
		LightContext.getContext().start();
		LOGGER.info("The light app have already started.");
	}

}
