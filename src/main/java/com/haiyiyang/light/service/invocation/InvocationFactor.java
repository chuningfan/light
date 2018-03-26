package com.haiyiyang.light.service.invocation;

import java.io.Serializable;
import java.util.StringTokenizer;

import com.haiyiyang.light.constant.LightConstants;

public class InvocationFactor implements Serializable {

	private static final long serialVersionUID = 8727789951151988418L;

	private String appName;
	private String invokeType;
	private String serviceName;
	private String clientABGroup;

	public InvocationFactor(String serviceName, String invokeType) {
		this.setAppNameByServiceName(serviceName);
		this.serviceName = serviceName;
		this.invokeType = invokeType;
	}

	private void setAppNameByServiceName(String serviceName) {
		StringBuilder strb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(serviceName, LightConstants.DOT);
		int dotCount = LightConstants.INT0;
		while (dotCount <= LightConstants.INT3 && st.hasMoreTokens()) {
			strb.append(st.nextToken()).append(LightConstants.DOT);
			dotCount++;
		}
		this.setAppName(strb.substring(0, strb.length() - 1).toString());
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getInvokeType() {
		return invokeType;
	}

	public void setInvokeType(String invokeType) {
		this.invokeType = invokeType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getClientABGroup() {
		return clientABGroup;
	}

	public void setClientABGroup(String clientABGroup) {
		this.clientABGroup = clientABGroup;
	}

}
