package com.haiyiyang.light.test.demo.service.impl;

import org.springframework.stereotype.Service;

import com.haiyiyang.light.service.annotation.IAmALightService;
import com.haiyiyang.light.test.demo.service.DemoService;

@Service
@IAmALightService
public class DemoServiceImpl implements DemoService {

	@Override
	public String helloWord(String name) {
		return "Hello " + name + ", to the World.";
	}

}
