package com.haiyiyang.light.service;

public class LightService {
	private String appName;
	private String serviceName;
	private Object serviceImpl;
	private String serviceImplName;
	private byte invokeMode;
	private byte group;
	private byte serviceChannel;

	public LightService(Object serviceImpl) {
		super();
		this.serviceImpl = serviceImpl;
	}

	public LightService(String appName, Object serviceImpl) {
		super();
		this.appName = appName;
		this.serviceImpl = serviceImpl;
	}

	public String getAppName() {
		return appName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Object getServiceImpl() {
		return serviceImpl;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public byte getInvokeMode() {
		return invokeMode;
	}

	public byte getGroup() {
		return group;
	}

	public byte getServiceChannel() {
		return serviceChannel;
	}

}
