package com.haiyiyang.light.context;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.haiyiyang.light.app.props.SettingsProps;
import com.haiyiyang.light.meta.LightAppMeta;

public class LightApplicationContext extends AnnotationConfigApplicationContext {

	private static volatile LightAppMeta LIGHT_APP_META = null;

	public LightApplicationContext() {
		if (LIGHT_APP_META == null) {
			synchronized (LightApplicationContext.class) {
				if (LIGHT_APP_META == null) {
					LIGHT_APP_META = LightAppMeta.SINGLETON();
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
			}
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

	public static LightAppMeta getLightAppMeta() {
		return LIGHT_APP_META;
	}

}
