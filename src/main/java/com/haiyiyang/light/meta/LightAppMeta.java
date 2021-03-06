package com.haiyiyang.light.meta;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.exception.LightException;
import com.haiyiyang.light.meta.props.AppProps;
import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.meta.props.PortProps;
import com.haiyiyang.light.meta.props.ResourceProps;
import com.haiyiyang.light.rpc.server.LightConfig;
import com.haiyiyang.light.utils.NetworkUtils;

public class LightAppMeta {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightAppMeta.class);

	private String appName;
	private String configRegistry;
	private LightProps lightProps;
	private PortProps portProps;
	private AppProps appProps;
	private ResourceProps resourceProps;

	private static byte ZERO_ONE_GROUPING;
	private static String MACHINE_IP = LightConstants.IP_127_0_0_1;

	private static volatile LightAppMeta LIGHT_APP_META;

	private LightAppMeta(String appName) throws LightException {
		this.appName = appName;
		this.configRegistry = LightConfig.getConfigRegistry();
		this.lightProps = LightProps.SINGLETON(this);
		this.portProps = PortProps.SINGLETON(this);
		this.appProps = AppProps.SINGLETON(this);
		ResourceProps.publishResourceProps(appProps.getResources());
		this.setMachineIPAndZeroOneGrouping();
		LOGGER.info("Initialized LightAppMeta.");
	}

	public static LightAppMeta SINGLETON(String appName) {
		Assert.notNull(appName, "[appName] cannot be empty.");
		if (LIGHT_APP_META == null) {
			synchronized (LightAppMeta.class) {
				if (LIGHT_APP_META == null) {
					LIGHT_APP_META = new LightAppMeta(appName);
				}
			}
		}
		return LIGHT_APP_META;
	}

	private void setMachineIPAndZeroOneGrouping() {
		Set<String> ips = NetworkUtils.getLocalIps();
		String ipSegmentPrefix = lightProps.getIpSegmentPrefix();
		for (String ip : ips) {
			if (ipSegmentPrefix == null || ip.startsWith(ipSegmentPrefix)) {
				MACHINE_IP = ip;
				break;
			}
		}
		ZERO_ONE_GROUPING = Byte.parseByte(MACHINE_IP.substring(MACHINE_IP.length() - 1, MACHINE_IP.length()));
	}

	public String resolveServicePath(String serviceName) {
		List<String> domainPackageList = lightProps.getDomainPackages();
		if (!domainPackageList.isEmpty()) {
			for (String domainPackage : domainPackageList) {
				if (serviceName.indexOf(domainPackage) == 0) {
					int index = serviceName.indexOf(LightConstants.DOT, domainPackage.length() + 1);
					return serviceName.substring(domainPackage.length() + 1,
							index == -1 ? serviceName.length() : index);
				}
			}
		}
		return serviceName;
	}

	public String getMatchedDomainPackage(String serviceName) {
		List<String> domainPackageList = lightProps.getDomainPackages();
		if (!domainPackageList.isEmpty()) {
			for (String domainPackage : domainPackageList) {
				if (serviceName.indexOf(domainPackage) == 0) {
					return domainPackage;
				}
			}
		}
		return null;
	}

	public int getAppPort() {
		return portProps.getAppPort();
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

	public byte getZeroOneGrouping() {
		return ZERO_ONE_GROUPING;
	}

}