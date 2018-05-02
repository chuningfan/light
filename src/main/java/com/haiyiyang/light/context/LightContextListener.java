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

	private static AbstractApplicationContext CONTEXT;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			if (CONTEXT == null) {
				CONTEXT = (AbstractApplicationContext) event.getSource();
				Map<String, Object> objectMap = CONTEXT.getBeansWithAnnotation(IAmALightService.class);
				if (objectMap != null && !objectMap.isEmpty()) {
					LightService.publishLightService(objectMap.values());
					LOGGER.debug("Published Light services.");
				}
			}
		}
	}

}
