package com.haiyiyang.light.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.context.LightContext;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.rpc.invocation.InvocationFactor;
import com.haiyiyang.light.rpc.invocation.LightInvocationHandler;
import com.haiyiyang.light.rpc.server.LightRpcServer;
import com.haiyiyang.light.rpc.server.task.handler.RequestHandler;
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

	private String path;
	private String registry;
	private List<ServiceEntry> data;

	private LightService(String registry, String path) {
		this.path = path;
		this.registry = registry;
	}

	private LightService(String registry, String path, ServiceEntry serviceEntries) {
		this.path = path;
		this.registry = registry;
		this.data = Lists.newArrayList(serviceEntries);
	}

	public static Object getServiceProxy(InvocationFactor factor) {
		String className = factor.getClazz().getName();
		Object service = LOCAL_SERVICE.get(className);
		if (service != null) {
			return service;
		}
		return LightInvocationHandler.getProxyService(factor);
	}

	public static void doPublishLightService() {
		if (!PUBLISHED_SERVICES.isEmpty()) {
			Entry<String, LightService> entry;
			for (Iterator<Entry<String, LightService>> ite = PUBLISHED_SERVICES.entrySet().iterator(); ite.hasNext();) {
				entry = ite.next();
				LightPublication.getPublish(entry.getValue()).publishService(entry.getValue().getPath(),
						entry.getValue().getPublishedData());
			}
		}
	}

	public static void publishLightService(Collection<Object> objects) {
		LightAppMeta lightAppMeta = LightContext.getContext().getLightAppMeta();
		String publishRegistry = lightAppMeta.getLightProps().getPublishRegistry();
		for (Object object : objects) {
			String interfaceName = getInterfaceName(object);
			LOCAL_SERVICE.put(interfaceName, object);
			String servicePath = lightAppMeta.resolveServicePath(interfaceName);
			LightService lightService = PUBLISHED_SERVICES.get(servicePath);
			if (lightService != null) {
				lightService.data.get(0).getServiceNames().add(interfaceName);
			} else {
				ServiceEntry serviceEntry = new ServiceEntry(lightAppMeta, interfaceName);
				lightService = new LightService(publishRegistry, getPublishPath(servicePath), serviceEntry);
				PUBLISHED_SERVICES.put(servicePath, lightService);
			}
		}
		if (!PUBLISHED_SERVICES.isEmpty()) {
			RequestHandler.handle();
			LightRpcServer.SINGLETON().start();
			doPublishLightService();
		}
	}

	public List<ServiceEntry> doSubscribeLightService() {
		List<byte[]> dataList = LightSubscription.getSubscription(this).getChildrenData(this.path);
		if (dataList != null && !dataList.isEmpty()) {
			List<ServiceEntry> serviceEntryList = new ArrayList<>(dataList.size());
			for (byte[] data : dataList) {
				serviceEntryList.add(ServiceEntry.decode(data));
			}
			this.setSubscribedData(serviceEntryList);
		}
		return this.getSubscribedData();
	}

	public static List<ServiceEntry> subscribeLightService(String serviceName) {
		LightAppMeta lightAppMeta = LightContext.getContext().getLightAppMeta();
		String servicePath = lightAppMeta.resolveServicePath(serviceName);
		LightService lightService = SUBSCRIBED_SERVICES.get(servicePath);
		if (lightService != null) {
			return lightService.getSubscribedData();
		}
		String registry = lightAppMeta.getLightProps().getSubscriptionRegistry(servicePath);
		lightService = new LightService(registry, getSubscriptionPath(servicePath));
		SUBSCRIBED_SERVICES.put(servicePath, lightService);
		return lightService.doSubscribeLightService();
	}

	private static String getPublishPath(String path) {
		return new StringBuilder(LIGHT_SERVICE_SLASH_URL).append(path).append(LightConstants.SLASH)
				.append(LightContext.getContext().getLightAppMeta().getMachineIp()).toString();
	}

	private static String getSubscriptionPath(String path) {
		return new StringBuilder(LIGHT_SERVICE_SLASH_URL).append(path).toString();
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

	public byte[] getPublishedData() {
		return ServiceEntry.encode(this.data.get(0));
	}

	public List<ServiceEntry> getSubscribedData() {
		return this.data;
	}

	public void setSubscribedData(List<ServiceEntry> data) {
		this.data = data;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getRegistry() {
		return this.registry;
	}

	@Override
	public void subscribe() {
		doSubscribeLightService();
	}

	@Override
	public void publish() {
		LightService.doPublishLightService();
	}

}
