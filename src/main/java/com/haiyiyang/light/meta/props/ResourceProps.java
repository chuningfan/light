package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.resource.LightResources;
import com.haiyiyang.light.subscription.LightSubscriber;
import com.haiyiyang.light.subscription.LightSubscription;

import jodd.props.Props;

public class ResourceProps implements LightSubscriber {
	protected static Logger LOGGER = LoggerFactory.getLogger(ResourceProps.class);

	public static final String RESOURCE_PROPS_PATH = "/light/resource/";
	public static final String RESOURCE_PROPS_LOCAL_PATH = LightConstants.USER_HOME
			+ RESOURCE_PROPS_PATH.replaceAll("/", LightConstants.FS);

	private LightAppMeta lightAppMeta;
	private static ResourceProps RESOURCE_PROPS;
	private static Map<String, LightResources> PATH_RESOURCES = new ConcurrentHashMap<>();
	private static Map<LightResources, Props> RESOURCES_PROPS = new ConcurrentHashMap<>();

	private ResourceProps(LightAppMeta lightAppMeta, Map<String, String> resourcesMap) {
		this.lightAppMeta = lightAppMeta;
		initResourceProps(resourcesMap);
	}

	public static ResourceProps SINGLETON(LightAppMeta lightAppMeta, Map<String, String> resourcesMap) {
		if (RESOURCE_PROPS != null) {
			return RESOURCE_PROPS;
		}
		synchronized (RESOURCE_PROPS) {
			if (RESOURCE_PROPS == null) {
				RESOURCE_PROPS = new ResourceProps(lightAppMeta, resourcesMap);
			}
		}
		return RESOURCE_PROPS;
	}

	private void initResourceProps(Map<String, String> resourcesMap) {
		Set<Map.Entry<String, String>> entrySet = resourcesMap.entrySet();
		Iterator<Map.Entry<String, String>> iter = entrySet.iterator();
		Map.Entry<String, String> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
				String filePath = RESOURCE_PROPS_LOCAL_PATH + entry.getValue();
				File file = new File(filePath);
				if (file.isFile()) {
					try {
						LightResources lightResource = LightResources.valueOf(entry.getKey());
						RESOURCES_PROPS.put(lightResource, new Props());
						RESOURCES_PROPS.get(lightResource).load(file);
					} catch (Exception ex) {
						LOGGER.error(ex.getMessage(), ex);
					}
				}
			} else {
				String urlPath = RESOURCE_PROPS_PATH + entry.getValue();
				LightResources lightResource = LightResources.valueOf(entry.getKey());
				PATH_RESOURCES.put(urlPath, lightResource);
				updatePropsData(lightResource, LightSubscription.getSubscription(this).getData(urlPath));
			}
		}
	}

	private void updatePropsData(LightResources lightResource, byte[] data) {
		synchronized (this) {
			try {
				RESOURCES_PROPS.put(lightResource, new Props());
				RESOURCES_PROPS.get(lightResource).load(new ByteArrayInputStream(data));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	public String getPropsValue(LightResources resource, String key) {
		if (RESOURCES_PROPS.get(resource) != null) {
			return RESOURCES_PROPS.get(resource).getValue(key);
		}
		return null;
	}

	public String getPropsValue(LightResources resource, String key, String profile) {
		if (RESOURCES_PROPS.get(resource) != null) {
			return RESOURCES_PROPS.get(resource).getValue(key, profile);
		}
		return null;
	}

	@Override
	public String getRegistry() {
		return lightAppMeta.getConfigRegistry();
	}

	@Override
	public List<String> getPaths() {
		return new ArrayList<>(PATH_RESOURCES.keySet());
	}

	@Override
	public void processData(String path, byte[] data) {
		LOGGER.info("ResourceProps>>>> PATH: {}, data", path, data);
	}

}
