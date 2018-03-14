package com.haiyiyang.light.subscription;

public interface LightSubscriber {

	public String getRegistry();

	public String getSubscriptionPath();

	public void processData(byte[] data, String path);

}
