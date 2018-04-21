package com.haiyiyang.light.service.entry;

import com.haiyiyang.light.rpc.server.IpPort;

public class ServiceEntry {
	private IpPort ipPort;
	private byte group;
	private byte weight;

	public ServiceEntry(IpPort ipPort) {
		this.ipPort = ipPort;
	}

	public ServiceEntry(IpPort ipPort, byte weight) {
		this.ipPort = ipPort;
		this.weight = weight;
	}

	public ServiceEntry(IpPort ipPort, byte group, byte weight) {
		this.ipPort = ipPort;
		this.group = group;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipPort == null) ? 0 : ipPort.hashCode());
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
		ServiceEntry other = (ServiceEntry) obj;
		if (ipPort == null) {
			if (other.ipPort != null)
				return false;
		} else if (!ipPort.equals(other.ipPort))
			return false;
		return true;
	}

	public IpPort getIpPort() {
		return ipPort;
	}

	public void setIpPort(IpPort ipPort) {
		this.ipPort = ipPort;
	}

	public byte getGroup() {
		return group;
	}

	public void setGroup(byte group) {
		this.group = group;
	}

	public byte getWeight() {
		return weight;
	}

	public void setWeight(byte weight) {
		this.weight = weight;
	}

}
