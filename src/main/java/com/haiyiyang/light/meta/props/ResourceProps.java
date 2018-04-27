package com.haiyiyang.light.meta.props;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.meta.LightResourceType;
import com.haiyiyang.light.service.subscription.LightSubscriber;
import com.haiyiyang.light.service.subscription.LightSubscription;

import jodd.props.Props;

public class ResourceProps implements LightSubscriber {
	protected static Logger LOGGER = LoggerFactory.getLogger(ResourceProps.class);

	private static LightAppMeta LIGHT_APP_META;
	private static ResourceProps RESOURCE_PROPS;
	private static Map<String, LightResourceType> PATH_RESOURCES = new ConcurrentHashMap<>();
	private static Map<LightResourceType, Props> RESOURCES_PROPS = new ConcurrentHashMap<>();

	private ResourceProps(LightAppMeta lightAppMeta, Map<String, String> resourcesMap) {
		ResourceProps.LIGHT_APP_META = lightAppMeta;
		if (resourcesMap != null && !resourcesMap.isEmpty()) {
			initializeResourceProps(resourcesMap);
		}
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

	private void initializeResourceProps(Map<String, String> resourcesMap) {
		Iterator<Map.Entry<String, String>> iter = resourcesMap.entrySet().iterator();
		Map.Entry<String, String> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (LightConstants.STR1.equals(LightConstants.USE_LOCAL_PROPS)) {
				LightResourceType lightResourceType = LightResourceType.valueOf(entry.getValue());
				String filePath = lightResourceType.getLocalPath();
				File file = new File(filePath);
				if (!file.isFile()) {
					LOGGER.error("The file[{}] does not exists.", filePath);
					throw new RuntimeException(LightException.FILE_NOT_FOUND);
				}
				try {
					RESOURCES_PROPS.put(lightResourceType, new Props());
					RESOURCES_PROPS.get(lightResourceType).load(file);
				} catch (Exception ex) {
					LOGGER.error("Loading file[{}] failed.", filePath);
					throw new RuntimeException(LightException.LOADING_FILE_FAILED);
				}
			} else {
				LightResourceType lightResourceType = LightResourceType.valueOf(entry.getValue());
				String urlPath = lightResourceType.getPath();
				PATH_RESOURCES.put(urlPath, lightResourceType);
				byte[] data = LightSubscription.getSubscription(this).getData(urlPath);
				if (data == null || data.length == 0) {
					LOGGER.error("The file[{}] does not exists, or is empty.", urlPath);
					throw new RuntimeException(LightException.FILE_NOT_FOUND_OR_EMPTY);
				}
				updatePropsData(lightResourceType, data);
			}
		}
	}

	private void updatePropsData(LightResourceType lightResource, byte[] data) {
		synchronized (this) {
			try {
				RESOURCES_PROPS.put(lightResource, new Props());
				RESOURCES_PROPS.get(lightResource).load(new ByteArrayInputStream(data));
			} catch (IOException e) {
				LOGGER.error("Loading file[{}] failed.", lightResource.getPath());
				throw new RuntimeException(LightException.LOADING_FILE_FAILED);
			}
		}
	}

	public String getPropsValue(LightResourceType resource, String key) {
		if (RESOURCES_PROPS.get(resource) != null) {
			return RESOURCES_PROPS.get(resource).getValue(key);
		}
		return null;
	}

	public String getPropsValue(LightResourceType resource, String key, String profile) {
		if (RESOURCES_PROPS.get(resource) != null) {
			return RESOURCES_PROPS.get(resource).getValue(key, profile);
		}
		return null;
	}

	@Override
	public String getRegistry() {
		return LIGHT_APP_META.getConfigRegistry();
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
