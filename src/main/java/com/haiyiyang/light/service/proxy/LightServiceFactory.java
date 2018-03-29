package com.haiyiyang.light.service.proxy;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.service.LightService;
import com.haiyiyang.light.service.invocation.InvocationFactor;

public class LightServiceFactory {

	public static <T> T getService(Class<T> clazz) throws LightException {
		return getServiceProxy(clazz, LightConstants.BYTE1);
	}

	public static <T> T getAsyncService(Class<T> clazz) throws LightException {
		return getServiceProxy(clazz, LightConstants.BYTE2);
	}

	public static <T> T getAsyncNoResultService(Class<T> clazz) throws LightException {
		return getServiceProxy(clazz, LightConstants.BYTE3);
	}

	private static <T> T getServiceProxy(Class<T> clazz, byte invokeMode) throws LightException {
		return LightService.SINGLETON().getServiceProxy(new InvocationFactor(clazz, invokeMode));
	}

}
