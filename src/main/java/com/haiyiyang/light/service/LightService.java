package com.haiyiyang.light.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.context.LightApplicationContext;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.publish.LightPublisher;
import com.haiyiyang.light.service.invocation.InvocationFactor;
import com.haiyiyang.light.service.invocation.LightInvocationHandler;
import com.haiyiyang.light.subscription.LightSubscriber;

public class LightService implements LightPublisher, LightSubscriber {

	private static final Map<String, Service> PUBLISHED_SERVICES = new ConcurrentHashMap<>();

	private static LightService LIGHT_SERVICE;

	private static LightAppMeta LIGHT_APP_META;

	private LightService() {
		LIGHT_APP_META = LightApplicationContext.getLightAppMeta();
	}

	public static LightService SINGLETON() {
		if (LIGHT_SERVICE != null) {
			return LIGHT_SERVICE;
		}
		synchronized (LIGHT_SERVICE) {
			if (LIGHT_SERVICE == null) {
				LIGHT_SERVICE = new LightService();
			}
		}
		return LIGHT_SERVICE;
	}

	public Object getServiceProxy(InvocationFactor factor) {
		String className = factor.getClazz().getName();
		if (PUBLISHED_SERVICES.containsKey(className)) {
			return PUBLISHED_SERVICES.get(className).serviceImpl;
		}
		return LightInvocationHandler.getProxyService(factor);
	}

	public void publishService() {
		// if (service != PUBLISHED_SERVICES.putIfAbsent(service.serviceName, service))
		// {
		// // TODO publish service.
		// }
	}

	public void subscribeService(Object object) {
		// if (service != SUBSCRIBED_SERVICES.putIfAbsent(service.serviceName, service))
		// {
		// // TODO publish service.
		// }
	}

	public void addService(String beanName, Object object) {
		Service service = new Service(beanName, object);
		PUBLISHED_SERVICES.put(service.serviceName, service);
	}

	@SuppressWarnings("unused")
	public static class Service {
		private String beanName;
		private String serviceName;
		private Object serviceImpl;
		private int weight;

		Service(String beanName, Object object) {
			this.beanName = beanName;
			this.serviceImpl = object;
			this.serviceName = getInterfaceName(this.serviceImpl);
			this.weight = LIGHT_APP_META.getLightProps().getServerLoadWeight();
		}
	}

	class ServiceEntry implements Cloneable {
		private String ip;
		private int port;
		private String name;
		private byte group;
		private int weight;

	}

	private static String getInterfaceName(Object serviceImpl) {
		String sampleName = serviceImpl.getClass().getSimpleName();
		Class<?>[] classes = serviceImpl.getClass().getInterfaces();
		if (classes != null && classes.length > 0) {
			for (Class<?> clazz : classes) {
				if (sampleName.indexOf(clazz.getSimpleName()) != -1) {
					return clazz.getName();
				}
			}
		}
		return serviceImpl.getClass().getName();
	}

	@Override
	public String getRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getPaths() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processData(String path, byte[] data) {
		// TODO Auto-generated method stub

	}

}
