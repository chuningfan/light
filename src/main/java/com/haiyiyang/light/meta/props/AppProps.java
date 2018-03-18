package com.haiyiyang.light.meta.props;

import com.haiyiyang.light.subscription.LightSubscriber;

public class AppProps implements LightSubscriber {

	private String registry;

	public AppProps(String registry) {
		this.registry = registry;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRegistry() {
		return registry;
	}

	@Override
	public void processData(byte[] data, String path) {
		// TODO Auto-generated method stub

	}

}
