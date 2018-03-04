package com.haiyiyang.light.service.invocation;

import java.io.Serializable;
import java.util.StringTokenizer;

import com.haiyiyang.light.constant.LightConstant;

public class InvocationFactor implements Serializable {

	private static final long serialVersionUID = 8727789951151988418L;

	private String appName;
	private String invokeType;
	private String serviceName;
	private String clientABGroup;
	private String priorityChannel;

	public InvocationFactor(String serviceName, String priorityChannel, String invokeType) {
		this.setAppNameByServiceName(serviceName);
		this.serviceName = serviceName;
		this.priorityChannel = priorityChannel;
		this.invokeType = invokeType;
	}

	private void setAppNameByServiceName(String serviceName) {
		StringBuilder strb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(serviceName, LightConstant.DOT);
		int dotCount = LightConstant.INT0;
		while (dotCount <= LightConstant.INT3 && st.hasMoreTokens()) {
			strb.append(st.nextToken()).append(LightConstant.DOT);
			dotCount++;
		}
		this.setAppName(strb.substring(0, strb.length() - 1).toString());
	}

	@Override
	// TODO 更换成1.7 Strinbuffer
	public String toString() {
		return "InvocationFactor [appName=" + appName + ", invokeType=" + invokeType + ", serviceName=" + serviceName + ", clientABGroup=" + clientABGroup + ", priorityChannel=" + priorityChannel
				+ "]";
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

	public String getPriorityChannel() {
		return priorityChannel;
	}

	public void setPriorityChannel(String priorityChannel) {
		this.priorityChannel = priorityChannel;
	}

}
