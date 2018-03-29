package com.haiyiyang.light.service.entry;

public class LightServiceEntry implements Cloneable {
	private String ip;
	private int port;
	private String serviceName;
	private Integer grouping;
	private int weight = 5;

	public LightServiceEntry(String ip, int port, String serviceName, Integer grouping, int weight) {
		super();
		this.ip = ip;
		this.port = port;
		this.serviceName = serviceName;
		this.grouping = grouping;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grouping == null) ? 0 : grouping.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LightServiceEntry other = (LightServiceEntry) obj;
		if (grouping == null) {
			if (other.grouping != null)
				return false;
		} else if (!grouping.equals(other.grouping))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		return true;
	}

	public LightServiceEntry clone() {
		LightServiceEntry o = null;
		try {
			o = (LightServiceEntry) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return o;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Integer getGrouping() {
		return grouping;
	}

	public int getWeight() {
		return weight;
	}

}
