package com.haiyiyang.light.protocol;

import java.nio.ByteBuffer;
import java.util.List;

public class ProtocolPacket {
	public final static int headerLength = 18;

	private int packetId;
	private byte invokeMode;
	private byte serializerType;
	private long startTime;
	private List<ByteBuffer> requestMeta;

	public ProtocolPacket(int packetId, byte invokeMode, byte serializerType, List<ByteBuffer> requestMeta) {
		this.packetId = packetId;
		this.invokeMode = invokeMode;
		this.serializerType = serializerType;
		this.startTime = System.currentTimeMillis();
		this.requestMeta = requestMeta;
	}

	public ByteBuffer encode() {
		int totalLenght = headerLength;
		int datasize = requestMeta.size();
		for (int i = 0; i < datasize; i++) {
			totalLenght += (4 + requestMeta.get(i).limit());
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(totalLenght);
		byteBuffer.putInt(packetId).put(invokeMode).put(serializerType).putLong(startTime).putInt(datasize);
		for (int i = 0; i < datasize; i++) {
			ByteBuffer data = requestMeta.get(i);
			int lenli = data.limit();
			byteBuffer.putInt(lenli);
			System.arraycopy(data.array(), 0, byteBuffer.array(), byteBuffer.position(), lenli);
			byteBuffer.position(byteBuffer.position() + lenli);
		}
		byteBuffer.flip();
		return byteBuffer;
	}

	public int getPacketId() {
		return packetId;
	}

	public byte getInvokeMode() {
		return invokeMode;
	}

	public byte getSerializerType() {
		return serializerType;
	}

	public long getStartTime() {
		return startTime;
	}

	public List<ByteBuffer> getRequestMeta() {
		return requestMeta;
	}

}
