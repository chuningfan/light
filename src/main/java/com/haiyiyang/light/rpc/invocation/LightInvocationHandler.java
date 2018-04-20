package com.haiyiyang.light.rpc.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.protocol.PacketIdGenerator;
import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.rpc.client.LightRpcClient;
import com.haiyiyang.light.rpc.request.RequestMeta;
import com.haiyiyang.light.rpc.server.IpPortGroupWeight;
import com.haiyiyang.light.serialization.SerializerContext;
import com.haiyiyang.light.serialization.SerializerFactory;
import com.haiyiyang.light.serialization.SerializerType;
import com.haiyiyang.light.service.LightService;
import com.haiyiyang.light.service.ServiceServerResolver;
import com.haiyiyang.light.service.proxy.ProxyType;
import com.haiyiyang.light.utils.RequestUtil;

import io.netty.channel.Channel;

public class LightInvocationHandler implements InvocationHandler, MethodInterceptor {

	private static final Map<InvocationFactor, LightInvocationHandler> INVOCATION_HANDLER = new ConcurrentHashMap<>();

	private Object objectProxy;
	private InvocationFactor invocationFactor;
	private final static String TO_STRING = "toString";
	private LightRpcClient client;
	private ProxyType proxyType;

	private LightInvocationHandler(InvocationFactor factor) {
		this.invocationFactor = factor;
		this.client = new LightRpcClient();
		this.proxyType = ProxyType.valueOf(LightAppMeta.SINGLETON().getLightProps().getProxyType());
		if (ProxyType.CGLIB == this.proxyType) {
			Enhancer en = new Enhancer();
			en.setSuperclass(factor.getClazz());
			en.setCallback(this);
			this.objectProxy = en.create();
		} else {
			this.objectProxy = Proxy.newProxyInstance(LightInvocationHandler.class.getClassLoader(),
					new Class[] { factor.getClazz() }, this);
		}
	}

	public static Object getProxyService(InvocationFactor factor) {
		if (INVOCATION_HANDLER.containsKey(factor)) {
			return INVOCATION_HANDLER.get(factor).objectProxy;
		}
		return INVOCATION_HANDLER.putIfAbsent(factor, new LightInvocationHandler(factor)).objectProxy;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return doInvoke(proxy, method, args, null);
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return doInvoke(proxy, method, args, methodProxy);
	}

	private Object doInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		if (TO_STRING.equals(method.getName())) {
			return proxy.getClass().getName();
		} else if (LightService.isLocalService(proxy)) {
			return method.invoke(proxy, args);
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
		LightProps lightProps = LightAppMeta.SINGLETON().getLightProps();
		SerializerType serializerType = SerializerType.valueOf(lightProps.getSerializer());

		RequestMeta message = new RequestMeta();
		String requestId = RequestUtil.getThreadLocalUUID();
		if (requestId != null) {
			message.setRequestId(requestId);
		} else {
			message.setRequestId(RequestUtil.getRequestUUID());
		}
		message.setServiceName(invocationFactor.getClazz().getName());
		message.setMethod(method.getName());
		message.setParamsTypes(method.getParameterTypes());
		message.setClientAppName(LightAppMeta.SINGLETON().getAppName());
		message.setClientIP(LightAppMeta.SINGLETON().getMachineIp());
		message.setDatetime(String.valueOf(System.currentTimeMillis()));

		List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
		buffers.add(message.serialize());
		if (args != null && args.length > 0) {
			buffers.add(SerializerFactory.getSerializer(serializerType.getValue()).serialize(args, null));
		}
		ProtocolPacket protocolPacket = new ProtocolPacket(PacketIdGenerator.getPacketId(),
				invocationFactor.getInvokeMode(), serializerType.getValue(), System.currentTimeMillis(), buffers);
		SerializerContext context = null;
		if (invocationFactor.getInvokeMode() == LightConstants.BYTE1) {
			context = new SerializerContext(method.getReturnType());
		} else if (invocationFactor.getInvokeMode() == LightConstants.BYTE2) {
			context = new SerializerContext(method.getReturnType());
		} else if (invocationFactor.getInvokeMode() == LightConstants.BYTE0) {
			context = new SerializerContext();
		}
		return client.sendMessage(protocolPacket, context, channel);
	}

}
