package com.haiyiyang.light.service.publish;

import java.util.List;

public interface LightPublisher {

	public String getRegistry();

	public List<String> getPaths();

	public byte[] getData(String path);

}
