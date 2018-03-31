package com.haiyiyang.light.protocol;

public class PacketIdFacotry {
	private static int packetId = 0;

	public synchronized static int getPacketId() {
		if (packetId >= Integer.MAX_VALUE) {
			packetId = 0;
		}
		return packetId++;
	}
}
