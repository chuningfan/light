package org.haiyiyang.demo.service.impl;

import org.haiyiyang.demo.service.DemoService;
import org.springframework.stereotype.Service;

import com.haiyiyang.light.service.annotation.IAmALightService;

@Service
@IAmALightService
public class DemoServiceImpl implements DemoService {

	@Override
	public String helloWord(String name) {
		return "Hello " + name + ", to the World.";
	}

}
