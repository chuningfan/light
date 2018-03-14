package com.haiyiyang.light.service.meta;

public class LightServiceMeta {
	private String appName;
	private String serviceName;
	private Object serviceImpl;
	private String serviceImplName;
	private byte invokeMode;
	private byte group;

	public LightServiceMeta(Object serviceImpl) {
		super();
		this.serviceImpl = serviceImpl;
	}

	public LightServiceMeta(String appName, Object serviceImpl) {
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

}
