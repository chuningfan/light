package com.haiyiyang.light.service.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.client.LightRpcClient;
import com.haiyiyang.light.server.IpPortGroupWeight;
import com.haiyiyang.light.service.ServiceServerResolver;

import io.netty.channel.Channel;

public class LightInvocationHandler implements InvocationHandler {

	private static final Map<InvocationFactor, LightInvocationHandler> INVOCATION_HANDLER = new ConcurrentHashMap<>();

	private Object objectProxy;
	private InvocationFactor invocationFactor;
	private final static String TO_STRING = "toString";
	private LightRpcClient client;

	private LightInvocationHandler(InvocationFactor factor) {
		this.invocationFactor = factor;
		this.objectProxy = Proxy.newProxyInstance(LightInvocationHandler.class.getClassLoader(),
				new Class[] { factor.getClazz() }, this);
		this.client = new LightRpcClient();
	}

	public static Object getProxyService(InvocationFactor factor) {
		if (INVOCATION_HANDLER.containsKey(factor)) {
			return INVOCATION_HANDLER.get(factor).objectProxy;
		}
		return INVOCATION_HANDLER.putIfAbsent(factor, new LightInvocationHandler(factor)).objectProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (TO_STRING.equals(method.getName())) {
			return proxy.getClass().getName();
		}
		IpPortGroupWeight ipgw = ServiceServerResolver.getServer(invocationFactor.getClazz().getName());
		Channel channel = client.getChannel(ipgw);
		while (channel == null) {
			try {
				channel = client.connect(ipgw);
			} catch (Exception e) {
				// TODO
			}
		}
		return channel;

		// ProtocolPacket protocolPacket = new ProtocolPacket();

	}

}
