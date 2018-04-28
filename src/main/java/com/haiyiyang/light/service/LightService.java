package com.haiyiyang.light.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.haiyiyang.light.context.LightContext;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.rpc.invocation.InvocationFactor;
import com.haiyiyang.light.rpc.invocation.LightInvocationHandler;
import com.haiyiyang.light.service.entry.ServiceEntry;
import com.haiyiyang.light.service.publish.LightPublication;
import com.haiyiyang.light.service.publish.LightPublisher;
import com.haiyiyang.light.service.subscription.LightSubscriber;
import com.haiyiyang.light.service.subscription.LightSubscription;

public class LightService implements LightPublisher, LightSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightService.class);

	private static final String LIGHT_SERVICE_SLASH_URL = "/light/service/";

	private static final Map<String, Object> LOCAL_SERVICE = new HashMap<>(8);
	private static final Map<String, LightService> PUBLISHED_SERVICES = new ConcurrentHashMap<>(8);
	private static final Map<String, LightService> SUBSCRIBED_SERVICES = new ConcurrentHashMap<>();

	private String registry;
	private String path;
	private List<ServiceEntry> serviceEntries;

	// private static LightService LIGHT_SERVICE;

	private LightService(String registry, String path) {
		this.path = path;
		this.registry = registry;
	}

	private LightService(String registry, String path, List<ServiceEntry> serviceEntries) {
		this.path = path;
		this.registry = registry;
		this.serviceEntries = serviceEntries;
	}

	public static Object getServiceProxy(InvocationFactor factor) {
		String className = factor.getClazz().getName();
		Object service = LOCAL_SERVICE.get(className);
		if (service != null) {
			return service;
		}
		return LightInvocationHandler.getProxyService(factor);
	}

	public static void publishService() {
		if (!PUBLISHED_SERVICES.isEmpty()) {
			Entry<String, LightService> entry;
			for (Iterator<Entry<String, LightService>> ite = PUBLISHED_SERVICES.entrySet().iterator(); ite.hasNext();) {
				entry = ite.next();
				LightPublication.getPublish(entry.getValue()).publishService(entry.getValue().getPath(),
						entry.getValue().getData());
			}
		}
	}

	public static List<ServiceEntry> subscribeService(String serviceName) {
		LightAppMeta lightAppMeta = LightContext.getContext().getLightAppMeta();
		String servicePath = lightAppMeta.resolveServicePath(serviceName);
		String fullPath = new StringBuilder(LIGHT_SERVICE_SLASH_URL).append(servicePath).toString();
		LightService lightService = SUBSCRIBED_SERVICES.get(fullPath);
		if (lightService != null) {
			return lightService.getServiceEntries();
		} else {
			String registry = lightAppMeta.getLightProps().getSubscriptionRegistry(servicePath);
			lightService = new LightService(registry, fullPath);
			SUBSCRIBED_SERVICES.put(fullPath, lightService);
			List<byte[]> dataList = LightSubscription.getSubscription(lightService).getChildrenData(fullPath);
			if (dataList == null || dataList.isEmpty()) {
				lightService.setServiceEntries(Collections.emptyList());
				return Collections.emptyList();
			}
			List<ServiceEntry> serviceEntryList = new ArrayList<>(dataList.size());
			for (byte[] data : dataList) {
				serviceEntryList.add(ServiceEntry.decode(data));
			}
			lightService.setServiceEntries(serviceEntryList);
			return serviceEntryList;
		}
	}

	public static void publishServices(String registry, Collection<Object> objects) {
		LightAppMeta lightAppMeta = LightContext.getContext().getLightAppMeta();
		for (Object object : objects) {
			String interfaceName = getInterfaceName(object);
			LOCAL_SERVICE.put(interfaceName, object);
			String servicePath = lightAppMeta.resolveServicePath(interfaceName);
			LightService lightService = PUBLISHED_SERVICES.get(servicePath);
			if (lightService != null) {
				lightService.serviceEntries.get(0).getServiceNames().add(interfaceName);
			} else {
				ServiceEntry serviceEntry = new ServiceEntry(lightAppMeta, interfaceName);
				lightService = new LightService(registry, servicePath, Lists.newArrayList(serviceEntry));
				PUBLISHED_SERVICES.put(servicePath, lightService);
			}
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
		return LOCAL_SERVICE.containsValue(service);
	}

	public String getPath() {
		return path;
	}

	public byte[] getData() {
		return ServiceEntry.encode(this.serviceEntries.get(0));
	}

	public List<ServiceEntry> getServiceEntries() {
		return this.serviceEntries;
	}

	public void setServiceEntries(List<ServiceEntry> serviceEntries) {
		this.serviceEntries = serviceEntries;
	}

	@Override
	public String getRegistry() {
		return this.registry;
	}

	@Override
	public List<String> getPaths() {
		return Lists.newArrayList(path);
	}

	@Override
	public void processData(String path, byte[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getData(String path) {
		return PUBLISHED_SERVICES.get(path).getData();
	}

}
