package com.haiyiyang.light.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.haiyiyang.light.app.props.SettingsProps;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.meta.props.AppProps;
import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.meta.props.PortProps;
import com.haiyiyang.light.meta.props.ResourceProps;
import com.haiyiyang.light.server.LightConfig;
import com.haiyiyang.light.utils.NetworkUtil;

public class LightAppMeta {

	private String appName;
	private String configRegistry;
	private LightProps lightProps;
	private PortProps portProps;
	private AppProps appProps;
	private ResourceProps resourceProps;

	private static Integer ZERO_ONE_GROUPING;
	private static List<String> PUBLISH_REGISTRIES = new ArrayList<>(3);
	private static String MACHINE_IP = LightConstants.IP_127_0_0_1;

	private static volatile LightAppMeta LIGHT_APP_META;

	private LightAppMeta() throws LightException {
		this.configRegistry = LightConfig.getConfigServer();
		this.lightProps = LightProps.SINGLETON(this);
		this.initAppName();
		this.initPublishRegistries();
		this.portProps = PortProps.SINGLETON(this);
		this.appProps = AppProps.SINGLETON(this);
		this.resourceProps = ResourceProps.SINGLETON(this, this.appProps.getResources());
		this.setMachineIPAndZeroOneGrouping();
	}

	private void initAppName() {
		String domainPackage = SettingsProps.getDomainPackage();
		if (domainPackage == null || domainPackage.isEmpty()) {
			domainPackage = lightProps.getPropsValue(LightConstants.DOMAIN_PACKAGE);
		}
		this.appName = SettingsProps.getRootPackage().substring(domainPackage.length() + 1);
	}

	private void initPublishRegistries() {
		String publishRegistry = lightProps.getPublishRegistry();
		String[] registrys = StringUtils.tokenizeToStringArray(publishRegistry, LightConstants.SEMICOLON);
		for (String registry : registrys) {
			PUBLISH_REGISTRIES.add(registry);
		}
	}

	private void setMachineIPAndZeroOneGrouping() {
		Set<String> ips = NetworkUtil.getLocalIps();
		String ipSegmentPrefix = lightProps.getIpSegmentPrefix();
		for (String ip : ips) {
			if (ipSegmentPrefix == null) {
				MACHINE_IP = ip;
				break;
			} else if (ip.startsWith(ipSegmentPrefix)) {
				MACHINE_IP = ip;
				break;
			}
		}
		if (!LightConstants.IP_127_0_0_1.equals(MACHINE_IP)) {
			ZERO_ONE_GROUPING = Integer.parseInt(MACHINE_IP.substring(MACHINE_IP.length() - 1, MACHINE_IP.length()));
		}
	}

	public static LightAppMeta SINGLETON() {
		if (LIGHT_APP_META == null) {
			synchronized (LIGHT_APP_META) {
				if (LIGHT_APP_META == null) {
					LIGHT_APP_META = new LightAppMeta();
				}
			}
		}
		return LIGHT_APP_META;
	}

	public LightProps getLightProps() {
		return lightProps;
	}

	public PortProps getPortProps() {
		return portProps;
	}

	public AppProps getAppProps() {
		return appProps;
	}

	public ResourceProps getResourceProps() {
		return resourceProps;
	}

	public String getAppName() {
		return appName;
	}

	public String getConfigRegistry() {
		return configRegistry;
	}

	public String getMachineIp() {
		return MACHINE_IP;
	}

	public Integer getZeroOneGrouping() {
		return ZERO_ONE_GROUPING;
	}

}