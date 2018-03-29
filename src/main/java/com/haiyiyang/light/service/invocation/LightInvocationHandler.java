package com.haiyiyang.light.service.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LightInvocationHandler implements InvocationHandler {

	private static final Map<InvocationFactor, LightInvocationHandler> INVOCATION_HANDLER = new ConcurrentHashMap<>();

	private Object objectProxy;
	private InvocationFactor invocationFactor;

	private LightInvocationHandler(InvocationFactor factor) {
		this.invocationFactor = factor;
		this.objectProxy = Proxy.newProxyInstance(LightInvocationHandler.class.getClassLoader(),
				new Class[] { factor.getClazz() }, this);
	}

	public static Object getProxyService(InvocationFactor factor) {
		if (INVOCATION_HANDLER.containsKey(factor)) {
			return INVOCATION_HANDLER.get(factor).objectProxy;
		}
		return INVOCATION_HANDLER.putIfAbsent(factor, new LightInvocationHandler(factor)).objectProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
