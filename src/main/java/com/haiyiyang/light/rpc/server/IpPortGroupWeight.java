package com.haiyiyang.light.rpc.server;

public class IpPortGroupWeight extends IpPort {

	private byte group;
	private byte weight;

	public IpPortGroupWeight(String ip, int port) {
		super(ip, port);
	}

	public IpPortGroupWeight(String ip, int port, byte weight) {
		super(ip, port);
		this.weight = weight;
	}

	public IpPortGroupWeight(String ip, int port, byte group, byte weight) {
		super(ip, port);
		this.group = group;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
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
