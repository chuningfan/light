package com.haiyiyang.light.service.meta;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

public class LightServiceMeta {
	private static final String LIGHT_DOT = "light.";

	private String appName;
	private String serviceName;

	private Object serviceImpl;
	private String serviceImplName;

	private Byte group;
	private Byte invokeMode;

	public LightServiceMeta(String appName, Object object) {
		this.appName = appName;
		this.serviceImpl = getServiceImpl(object);
		this.serviceName = getServiceName(this.serviceImpl);
		this.serviceImplName = serviceImpl.getClass().getName();
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

	public Byte getGroup() {
		return group;
	}

	public void setGroup(Byte group) {
		this.group = group;
	}

	public Byte getInvokeMode() {
		return invokeMode;
	}

	public void setInvokeMode(Byte invokeMode) {
		this.invokeMode = invokeMode;
	}

	private static String getServiceName(Object serviceImpl) {
		String sampleName = serviceImpl.getClass().getSimpleName();
		Class<?>[] classes = serviceImpl.getClass().getInterfaces();
		if (classes != null && classes.length > 0) {
			for (Class<?> clazz : classes) {
				if (sampleName.indexOf(clazz.getSimpleName()) != -1) {
					return new StringBuilder(LIGHT_DOT).append(clazz.getName()).toString();
				}
			}
		}
		return serviceImpl.getClass().getName();
	}

	public static Object getServiceImpl(Object object) {
		if (AopUtils.isAopProxy(object) && object instanceof Advised) {
			Advised advised = (Advised) object;
			try {
				object = advised.getTargetSource().getTarget();
			} catch (Exception e) {
				return object;
			}
			return getServiceImpl(object);
		}
		return object;
	}

}
