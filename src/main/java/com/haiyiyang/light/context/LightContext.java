package com.haiyiyang.light.context;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.haiyiyang.light.app.props.SettingsProps;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.LightService;
import com.haiyiyang.light.service.annotation.IAmALightService;

public class LightContext extends AnnotationConfigApplicationContext implements ApplicationListener<ApplicationEvent> {

	private static Logger LOGGER = LoggerFactory.getLogger(LightContext.class);

	private LightAppMeta lightAppMeta;

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

	public LightAppMeta getLightAppMeta() {
		return lightAppMeta;
	}

	private LightContext() {
		try {
			SettingsProps settingsProps = SettingsProps.SINGLETON();
			LOGGER.info("System Property [useLocalProps] is {}", LightConstants.USE_LOCAL_PROPS);
			lightAppMeta = LightAppMeta.SINGLETON(settingsProps.getAppName());
			String scanPackages = settingsProps.getScanPackages();
			if (scanPackages != null && !scanPackages.isEmpty()) {
				this.scan(scanPackages.split(LightConstants.COMMA));
				LOGGER.info("Scanned packages: {}.", scanPackages);
			}
			Class<?>[] classes;
			classes = settingsProps.getConfigurableClasses();
			if (classes != null && classes.length > 0) {
				this.register(classes);
				LOGGER.info("Registered packages: {}.", settingsProps.getAnnotatedClasses());
			}
			this.refresh();
			LOGGER.info("Refreshed the light context.");
		} catch (IOException e) {
			LOGGER.info("Initialization configuration[SettingsProps] failed.");
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Registered annotatedClasses in configuration[SettingsProps] failed.");
			throw new RuntimeException(e);
		}
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
			Entry<String, Object> entry;
			for (Iterator<Entry<String, Object>> ite = objectMap.entrySet().iterator(); ite.hasNext();) {
				entry = ite.next();
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
