package com.haiyiyang.light.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.haiyiyang.light.service.meta.LightServiceMeta;

public class LightServiceExporter extends LightServiceMeta implements InitializingBean, DisposableBean {

	public LightServiceExporter(String appName, Object serviceImpl) {
		super(appName, serviceImpl);
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
