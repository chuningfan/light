package com.haiyiyang.light.meta;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import com.haiyiyang.light.context.LightContextListener;

@Configurable
public class Configration {

	@Bean()
	LightContextListener getLightContextListener() {
		return new LightContextListener();
	}
}
