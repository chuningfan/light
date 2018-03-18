package com.haiyiyang.light.subscription;

public interface LightSubscriber {

	public String getPath();

	public String getRegistry();

	public void processData(byte[] data, String path);

}
