package com.haiyiyang.light.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.LightServiceExporter;
import com.haiyiyang.light.service.annotation.LightService;

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
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		Class<?> clazz = null;
		for (String beanDefinitionName : beanDefinitionNames) {
			Object object = context.getBean(beanDefinitionName);
			try {
				clazz = LightServiceExporter.getServiceImpl(object).getClass();
			} catch (Exception e) {
				LOGGER.warn("No class found of bean: {}.", beanDefinitionName);
				continue;
			}
			LightService lightService = (LightService) clazz.getAnnotation(LightService.class);
			if (lightService != null) {
				LightServiceExporter serviceExport = new LightServiceExporter(lightAppMeta.getAppName(), object);
				if (!context.getBeanFactory().containsSingleton(serviceExport.getServiceName())) {
					context.getBeanFactory().registerSingleton(serviceExport.getServiceName(), object);
					// TODO publish
				}
			}
		}
	}

	public static void main(String[] args) {
		LightServiceHandler x = new LightServiceHandler();
		System.out.println(x.getClass().getSimpleName());
	}

}
