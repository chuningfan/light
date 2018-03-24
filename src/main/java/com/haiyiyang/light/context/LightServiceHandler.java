package com.haiyiyang.light.context;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.LightService;
import com.haiyiyang.light.service.annotation.IAmALightService;

public class LightServiceHandler implements ApplicationListener<ApplicationEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(LightServiceHandler.class);

	private static AbstractApplicationContext context = null;

	private LightAppMeta lightAppMeta;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			if (context == null) {
				context = (AbstractApplicationContext) event.getSource();
				lightAppMeta = LightApplicationContext.getLightAppMeta();
				publishLightService();
			}
		}
	}

	private void publishLightService() {
		Map<String, Object> objectMap = context.getBeansWithAnnotation(IAmALightService.class);
		if (objectMap != null && !objectMap.isEmpty()) {
			LightService lightService = LightService.SINGLETON(lightAppMeta);
			for (Iterator<Entry<String, Object>> ite = objectMap.entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = ite.next();
//				LightServiceExporter serviceExport = new LightServiceExporter(lightAppMeta.getAppName(),
//						entry.getValue());
//				lightService.addServices(serviceExport);
				
			}
		}
	}

	public static void main(String[] args) {
		LightServiceHandler x = new LightServiceHandler();
		System.out.println(x.getClass().getSimpleName());
	}

}
