package com.haiyiyang.light.service.subscription;

import java.util.List;

public interface LightSubscriber {

	public String getRegistry();

	public List<String> getPaths();

	public void processData(String path, byte[] data);

}
