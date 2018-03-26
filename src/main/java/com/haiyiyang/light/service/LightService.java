package com.haiyiyang.light.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.publish.LightPublisher;
import com.haiyiyang.light.subscription.LightSubscriber;

public class LightService implements LightPublisher, LightSubscriber {

	private static final Map<String, Service> PUBLISHED_SERVICES = new ConcurrentHashMap<>();
	private static final Map<String, Service> SUBSCRIBED_SERVICES = new ConcurrentHashMap<>();

	private static LightService LIGHT_SERVICE;

	private LightAppMeta lightAppMeta;

	private LightService(LightAppMeta lightAppMeta) {
		if (lightAppMeta != null) {
			this.lightAppMeta = lightAppMeta;
		}
	}

	public static LightService SINGLETON(LightAppMeta lightAppMeta) {
		if (LIGHT_SERVICE != null) {
			return LIGHT_SERVICE;
		}
		synchronized (LIGHT_SERVICE) {
			if (LIGHT_SERVICE == null) {
				LIGHT_SERVICE = new LightService(lightAppMeta);
			}
		}
		return LIGHT_SERVICE;
	}

	public <T> T getServiceProxy(Class<T> clazz, byte invokeMode) {
		return null;
	}

	public void publishService(Object object) {
		Service service = new Service(object);
		if (service != PUBLISHED_SERVICES.putIfAbsent(service.serviceName, service)) {
			// TODO publish service.
		}
	}

	public void subscribeService(Object object) {
		Service service = new Service(object);
		if (service != SUBSCRIBED_SERVICES.putIfAbsent(service.serviceName, service)) {
			// TODO publish service.
		}
	}

	static class Service {
		private String serviceName;

		private Object serviceImpl;
		private String serviceImplName;

		private byte group;
		private byte invokeMode;

		public Service(Object object) {
			this.serviceImpl = object;
			this.serviceName = getServiceName(this.serviceImpl);
			this.serviceImplName = serviceImpl.getClass().getName();
		}
	}

	static class InvokeFactor {
		private Class<?> clazz;
		private byte invokeMode;

		public InvokeFactor(Class<?> clazz, byte invokeMode) {
			this.clazz = clazz;
			this.invokeMode = invokeMode;
		}
	}

	class ServiceEntry implements Cloneable {
		private String IP;
		private int port;
		private int weight = 1;
		private String serviceName;
		private String appName;
		private byte group;
	}

	private static String getServiceName(Object serviceImpl) {
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
	
	public static void main (String[] args) throws ClassNotFoundException {
		Class a = Class.forName("java.lang.Object");
		Class b = Class.forName("java.lang.Object");
		System.out.println(a);
		System.out.println(b);
		System.out.println(a==b);
		System.out.println(a.equals(b));
	}

}
