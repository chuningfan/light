package com.haiyiyang.light.context;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.haiyiyang.light.app.props.SettingsProps;
import com.haiyiyang.light.meta.LightAppMeta;

public class LightApplicationContext extends AnnotationConfigApplicationContext {

	private static LightAppMeta LIGHT_APP_META = null;

	public LightApplicationContext() {
		if (LIGHT_APP_META == null) {
			LIGHT_APP_META = LightAppMeta.SINGLETON(SettingsProps.getAppNameValue());
			this.scan(SettingsProps.getAppServicePackagesValue());
			this.register(SettingsProps.getAppConfigurableClasses());
			this.refresh();
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
