package com.haiyiyang.light.context;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.haiyiyang.light.app.ShutdownHook;
import com.haiyiyang.light.app.props.SettingsProps;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.service.LightService;

public class LightContext extends AnnotationConfigApplicationContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightContext.class);

	private LightAppMeta lightAppMeta;

	private AbstractApplicationContext ctx;

	private static volatile LightContext LIGHT_CONTEXT;

	public static LightContext getContext() {
		return getContext(null);
	}

	public static LightContext getContext(AbstractApplicationContext ctx) {
		if (LIGHT_CONTEXT != null) {
			return LIGHT_CONTEXT;
		}
		synchronized (LightContext.class) {
			if (LIGHT_CONTEXT == null) {
				LIGHT_CONTEXT = new LightContext(ctx);
				LightContext.getContext(null).refresh();
				LOGGER.info("Refreshed the light context.");
			}
		}
		return LIGHT_CONTEXT;
	}

	public LightAppMeta getLightAppMeta() {
		return lightAppMeta;
	}

	private LightContext(AbstractApplicationContext ctx) {
		this.ctx = ctx != null ? ctx : this;
		initializeLightContext();
	}

	private void initializeLightContext() {
		if (this.ctx != this) {
			this.ctx.addApplicationListener(LightContextListener.SINGLETON());
			this.ctx.refresh();
		}

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
		} catch (IOException e) {
			LOGGER.info("Initialization configuration[SettingsProps] failed.");
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			LOGGER.info("Registered annotatedClasses in configuration[SettingsProps] failed.");
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onClose() {
		LightService.doUnpublishLightService();
	}

	@Override
	public void registerShutdownHook() {
		ShutdownHook.hook();
	}
}
