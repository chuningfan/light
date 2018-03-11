package com.haiyiyang.light.service.proxy;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.service.invocation.InvocationFactor;

public class LightServiceFactory {

	public static <T> T getService(Class<T> clazz, String priorityChannel) throws LightException {
		return getServiceProxy(new InvocationFactor(clazz.getName(), priorityChannel, LightConstants.STR1));
	}

	public static <T> T getAsyncService(Class<T> clazz, String priorityChannel) throws LightException {
		return getServiceProxy(new InvocationFactor(clazz.getName(), priorityChannel, LightConstants.STR2));
	}

	public static <T> T getAsyncNoResultService(Class<T> clazz, String priorityChannel) throws LightException {
		return getServiceProxy(new InvocationFactor(clazz.getName(), priorityChannel, LightConstants.STR3));
	}

	private static <T> T getServiceProxy(InvocationFactor invocationFactor) throws LightException {

		return null;
	}

}
