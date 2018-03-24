package com.haiyiyang.light.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.publish.LightPublisher;

public class LightService implements LightPublisher {
	
	private static final Map<String, LightService> SERVICES = new ConcurrentHashMap<>();

	private static LightService LIGHT_SERVICE;

	private LightAppMeta lightAppMeta;

	private LightService(LightAppMeta lightAppMeta) {
		this.lightAppMeta = lightAppMeta;
	}

	public static LightService SINGLETON(LightAppMeta lightAppMeta) {
		if (LIGHT_SERVICE != null) {
			return LIGHT_SERVICE;
		}
		synchronized (LIGHT_SERVICE) {
			if (LIGHT_SERVICE == null) {
				LIGHT_SERVICE = new LightService(lightAppMeta);
			}
		}
		return LIGHT_SERVICE;
	}

	@Override
	public String getRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getPaths() {
		// TODO Auto-generated method stub
		return null;
	}

}
