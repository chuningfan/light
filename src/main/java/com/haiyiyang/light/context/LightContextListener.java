package com.haiyiyang.light.context;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.haiyiyang.light.service.LightService;
import com.haiyiyang.light.service.annotation.IAmALightService;

public class LightContextListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightContextListener.class);

	private static volatile AbstractApplicationContext CONTEXT;
	private static volatile LightContextListener LIGHT_CONTEXT_LISTENER;

	private LightContextListener() {
	}

	public static LightContextListener SINGLETON() {
		if (LIGHT_CONTEXT_LISTENER != null) {
			return LIGHT_CONTEXT_LISTENER;
		}
		synchronized (LightContextListener.class) {
			if (LIGHT_CONTEXT_LISTENER == null) {
				LIGHT_CONTEXT_LISTENER = new LightContextListener();
			}
		}
		return LIGHT_CONTEXT_LISTENER;
	}

	@Override
	public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
		if (CONTEXT == null && event instanceof ContextRefreshedEvent) {
			CONTEXT = (AbstractApplicationContext) event.getSource();
			Map<String, Object> objectMap = CONTEXT.getBeansWithAnnotation(IAmALightService.class);
			if (objectMap != null && !objectMap.isEmpty()) {
				LightService.publishLightService(objectMap.values());
				LOGGER.info("Published Light services.");
			}
		}
	}

}
