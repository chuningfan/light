package com.haiyiyang.light.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.context.LightContext;
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

	private static Logger LOGGER = LoggerFactory.getLogger(LightService.class);

	private static final String LIGHT_SERVICE_SLASH_URL = "/light/service/";
	private static final Set<Object> LOCAL_SERVICE = new HashSet<>();
	private static final Map<String, Object> PUBLISHED_SERVICES = new ConcurrentHashMap<>();
	private static final Map<String, ServiceEntry> PUBLISHED_SERVICE_ENTRIES = new ConcurrentHashMap<>();

	private LightAppMeta lightAppMeta;
	private static LightService LIGHT_SERVICE;

	private LightService() {
		this.lightAppMeta = LightContext.getContext().getLightAppMeta();
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
		Object service = PUBLISHED_SERVICES.get(className);
		if (service != null) {
			if (!LOCAL_SERVICE.contains(service)) {
				LOCAL_SERVICE.add(service);
			}
			return service;
		}
		return LightInvocationHandler.getProxyService(factor);
	}

	public void publishService() {
		if (!PUBLISHED_SERVICE_ENTRIES.isEmpty()) {
			Entry<String, ServiceEntry> entry;
			for (Iterator<Entry<String, ServiceEntry>> ite = PUBLISHED_SERVICE_ENTRIES.entrySet().iterator(); ite
					.hasNext();) {
				entry = ite.next();
				LightPublication.getPublish(this).publishService(entry.getKey(), ServiceEntry.encode(entry.getValue()));
			}
		}
	}

	public List<ServiceEntry> subscribeService(String serviceName) {
		// TODO add cache.
		String appName = lightAppMeta.resolveServicePath(serviceName);
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
		String interfaceName = getInterfaceName(object);
		PUBLISHED_SERVICES.put(interfaceName, object);
		String servicePath = lightAppMeta.resolveServicePath(interfaceName);
		ServiceEntry serviceEntry = PUBLISHED_SERVICE_ENTRIES.get(servicePath);
		if (serviceEntry != null) {
			serviceEntry.getServiceNames().add(interfaceName);
		} else {
			serviceEntry = new ServiceEntry(new IpPort(lightAppMeta.getMachineIp(), lightAppMeta.getAppPort()),
					lightAppMeta.getLightProps().getServerLoadWeight(), lightAppMeta.getZeroOneGrouping());
			serviceEntry.getServiceNames().add(interfaceName);
			PUBLISHED_SERVICE_ENTRIES.put(servicePath, serviceEntry);
		}
	}

	private static String getInterfaceName(Object serviceImpl) {
		Class<?>[] classes = serviceImpl.getClass().getInterfaces();
		if (classes == null || classes.length == 0) {
			LOGGER.error("The service Class [{}] must implements an interface.", serviceImpl.getClass().getName());
			throw new RuntimeException("As a service Class must implements an interface.");
		}
		if (classes != null && classes.length > 0) {
			String sampleName = serviceImpl.getClass().getSimpleName();
			for (Class<?> clazz : classes) {
				if (sampleName.indexOf(clazz.getSimpleName()) == 0) {
					return clazz.getName();
				}
			}
		}
		LOGGER.error("The simple name of the Class [{}] must be prefixed with its interface simple name.",
				serviceImpl.getClass().getName());
		throw new RuntimeException(
				"The simple name of an implementation class must be prefixed with its interface simple name.");
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
