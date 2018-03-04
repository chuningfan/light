package com.haiyiyang.light.context;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

public class LightServiceHandler implements ApplicationListener<ApplicationEvent> {

	private static AbstractApplicationContext context = null;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		if (event instanceof ContextRefreshedEvent) {
			AbstractApplicationContext context = (AbstractApplicationContext) event.getSource();
			if (null == LightServiceHandler.context) {
				LightServiceHandler.context = context;
				publishLightService();
			}
		}
	}

	public static void publishLightService() {
		String[] beannames = context.getBeanDefinitionNames();
		for (int x = 0; x < beannames.length; x++) {
			if (beannames[x] != null && beannames[x].startsWith("light.")) {

			}
		}
	}

	private String getServiceName(Object service) {
		String serviceName = "";
		Class<?>[] interfaceClasss = service.getClass().getInterfaces();
		if (interfaceClasss.length > 0) {
			for (int x = 0; x < interfaceClasss.length; x++) {
				if (interfaceClasss[x].getName().startsWith("light.")) {
					serviceName = interfaceClasss[x].getName();
					break;
				}
			}
		}
		if ("".equals(serviceName)) {
			Class<?>[] interfaceSuperClasss = service.getClass().getSuperclass().getInterfaces();
			for (int x = 0; x < interfaceSuperClasss.length; x++) {
				if (interfaceSuperClasss[x].getName().startsWith("light.")) {
					serviceName = interfaceSuperClasss[x].getName();
					break;
				}
			}
		}
		return serviceName;
	}

}
