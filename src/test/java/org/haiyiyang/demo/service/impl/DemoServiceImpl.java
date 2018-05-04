package org.haiyiyang.demo.service.impl;

import org.haiyiyang.demo.service.DemoService;

import com.haiyiyang.light.service.annotation.IAmALightService;

@IAmALightService
public class DemoServiceImpl implements DemoService {

	@Override
	public String helloWord(String name) {
		return "Hello " + name + ", to the World.";
	}

	@Override
	public String welcome(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return "Welcome " + firstName + ", " + lastName;
	}

}
