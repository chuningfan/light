package com.haiyiyang.light.service.entry;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.rpc.server.IpPort;

public class ServiceEntry {
	private IpPort ipPort;
	private byte group;
	private byte weight;
	private Set<String> serviceNames;

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

	public static byte[] encode(ServiceEntry serviceEntry) {
		byte[] data;
		data = serviceEntry.getIpPort().getIp().getBytes(LightConstants.CHARSET_UTF8);
		int totalLenght = 4 + data.length + 4 + 1 + 1;
		if (serviceEntry.getServiceNames() != null) {
			for (String serviceName : serviceEntry.getServiceNames()) {
				totalLenght += 4 + serviceName.length();
			}
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(totalLenght);
		byteBuffer.putInt(data.length).put(data).putInt(serviceEntry.getIpPort().getPort()).put(serviceEntry.getGroup())
				.put(serviceEntry.getWeight());
		if (serviceEntry.getServiceNames() != null) {
			for (String serviceName : serviceEntry.getServiceNames()) {
				data = serviceName.getBytes(LightConstants.CHARSET_UTF8);
				byteBuffer.putInt(data.length).put(data);
			}
		}
		byteBuffer.flip();
		return byteBuffer.array();
	}

	public static ServiceEntry decode(byte[] data) {
		byte[] byteArray;
		ByteBuffer bb = ByteBuffer.wrap(data);
		byteArray = new byte[bb.getInt()];
		bb.get(byteArray);
		IpPort ipPort = new IpPort(new String(byteArray, LightConstants.CHARSET_UTF8), bb.getInt());
		ServiceEntry serviceEntry = new ServiceEntry(ipPort, bb.get(), bb.get());
		Set<String> serviceNameSet = new HashSet<>();
		while (bb.hasRemaining()) {
			byteArray = new byte[bb.getInt()];
			bb.get(byteArray);
			serviceNameSet.add(new String(byteArray, LightConstants.CHARSET_UTF8));
		}
		serviceEntry.setServiceNames(serviceNameSet);
		return serviceEntry;
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

	@Override
	public String toString() {
		return "ServiceEntry [ipPort=" + ipPort + ", group=" + group + ", weight=" + weight + ", serviceNames="
				+ serviceNames + "]";
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

	public Set<String> getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(Set<String> serviceNames) {
		this.serviceNames = serviceNames;
	}

	public static void main(String[] args) {
		ServiceEntry se = new ServiceEntry(new IpPort("192.168.1.1", 8080), (byte) 1, (byte) 1);
		Set<String> serviceNames = new HashSet<>();
		serviceNames.add("123");
		serviceNames.add("com.demo.a.b");
		serviceNames.add("com.demo.a.b.c");
		se.setServiceNames(serviceNames);
		byte[] code = ServiceEntry.encode(se);
		ServiceEntry newSe = ServiceEntry.decode(code);
		System.out.println(newSe);
	}

}
