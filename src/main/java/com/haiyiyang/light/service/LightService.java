package com.haiyiyang.light.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.rpc.invocation.InvocationFactor;
import com.haiyiyang.light.rpc.invocation.LightInvocationHandler;
import com.haiyiyang.light.rpc.server.IpPort;
import com.haiyiyang.light.service.entry.ServiceEntry;
import com.haiyiyang.light.service.publish.LightPublication;
import com.haiyiyang.light.service.publish.LightPublisher;
import com.haiyiyang.light.service.subscription.LightSubscriber;
import com.haiyiyang.light.service.subscription.LightSubscription;

public class LightService implements LightPublisher, LightSubscriber {

	private static final String LIGHT_SERVICE_SLASH_URL = "/light/service/";
	private static final Set<Object> LOCAL_SERVICE = new HashSet<>();
	private static final Map<String, ServiceInstance> PUBLISHED_SERVICES = new ConcurrentHashMap<>();

	private static LightService LIGHT_SERVICE;

	private LightService() {
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
		ServiceInstance service = PUBLISHED_SERVICES.get(className);
		if (service != null) {
			if (!LOCAL_SERVICE.contains(service.serviceImpl)) {
				LOCAL_SERVICE.add(service.serviceImpl);
			}
			return service.serviceImpl;
		}
		return LightInvocationHandler.getProxyService(factor);
	}

	public void publishService() {
		if (!PUBLISHED_SERVICES.isEmpty()) {
			LightAppMeta lightAppMeta = LightAppMeta.SINGLETON();
			ServiceEntry serviceEntry = new ServiceEntry(
					new IpPort(lightAppMeta.getMachineIp(), lightAppMeta.getAppPort()),
					lightAppMeta.getLightProps().getServerLoadWeight(), lightAppMeta.getZeroOneGrouping());
			serviceEntry.setServiceNames(PUBLISHED_SERVICES.keySet());
			LightPublication.getPublish(this).publishService(lightAppMeta.getAppServicePath(),
					ServiceEntry.encode(serviceEntry));
		}
	}

	public List<ServiceEntry> subscribeService(String serviceName) {
		// TODO add cache.
		String appName = LightAppMeta.SINGLETON().resolveServicePath(serviceName);
		if (appName == null || appName.isEmpty()) {
			appName = serviceName;
		}
		StringBuilder strb = new StringBuilder(LIGHT_SERVICE_SLASH_URL).append(appName);
		List<byte[]> dataList = LightSubscription.getSubscription(this).getChildrenData(strb.toString());
		if (dataList == null || dataList.isEmpty()) {
			return Collections.emptyList();
		}
		List<ServiceEntry> serviceEntryList = new ArrayList<>(dataList.size());
		for (byte[] data : dataList) {
			serviceEntryList.add(ServiceEntry.decode(data));
		}
		return serviceEntryList;
	}

	public void addService(Object object) {
		ServiceInstance service = new ServiceInstance(object);
		PUBLISHED_SERVICES.put(service.serviceName, service);
	}

	public static class ServiceInstance {
		private String serviceName;
		private Object serviceImpl;

		ServiceInstance(Object object) {
			this.serviceImpl = object;
			this.serviceName = getInterfaceName(this.serviceImpl);
		}
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

	public static boolean isLocalService(Object service) {
		return LOCAL_SERVICE.contains(service);
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
