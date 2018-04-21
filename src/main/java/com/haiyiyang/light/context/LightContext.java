package com.haiyiyang.light.context;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.haiyiyang.light.app.props.SettingsProps;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.LightService;
import com.haiyiyang.light.service.annotation.IAmALightService;

public class LightContext extends AnnotationConfigApplicationContext implements ApplicationListener<ApplicationEvent> {

	private static LightContext LIGHT_CONTEXT;

	private static AbstractApplicationContext CONTEXT;

	public static LightContext getContext() {
		if (LIGHT_CONTEXT != null) {
			return LIGHT_CONTEXT;
		}
		synchronized (LightContext.class) {
			if (LIGHT_CONTEXT == null) {
				LIGHT_CONTEXT = new LightContext();
			}
		}
		return LIGHT_CONTEXT;
	}

	private LightContext() {
		LightAppMeta.SINGLETON();
		String rootPackage = SettingsProps.getRootPackage();
		if (rootPackage != null && !rootPackage.isEmpty()) {
			this.scan(SettingsProps.getRootPackage());
		}
		Class<?>[] classes = SettingsProps.getConfigurableClasses();
		if (classes.length > 0) {
			this.register(classes);
		}
		this.refresh();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			if (CONTEXT == null) {
				CONTEXT = (AbstractApplicationContext) event.getSource();
				publishLightService();
			}
		}
	}

	private void publishLightService() {
		Map<String, Object> objectMap = CONTEXT.getBeansWithAnnotation(IAmALightService.class);
		if (objectMap != null && !objectMap.isEmpty()) {
			LightService lightService = LightService.SINGLETON();
			for (Iterator<Entry<String, Object>> ite = objectMap.entrySet().iterator(); ite.hasNext();) {
				Entry<String, Object> entry = ite.next();
				lightService.addService(entry.getValue());
			}
			lightService.publishService();
		}
	}

	@Override
	protected void onClose() {
		// TODO
	}

	@Override
	public void registerShutdownHook() {
		// TODO
	}

}
