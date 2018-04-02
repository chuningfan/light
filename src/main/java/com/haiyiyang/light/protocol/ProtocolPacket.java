package com.haiyiyang.light.protocol;

import java.nio.ByteBuffer;
import java.util.List;

public class ProtocolPacket {
	public final static int headerLength = 10;

	private int packetId;
	private byte invokeMode;
	private byte serializerType;
	private List<ByteBuffer> arguments;

	public ProtocolPacket(int packetId, byte invokeMode, byte serializerType) {
		super();
		this.packetId = packetId;
		this.invokeMode = invokeMode;
		this.serializerType = serializerType;
	}

	public ProtocolPacket(int packetId, byte invokeMode, byte serializerType, List<ByteBuffer> arguments) {
		super();
		this.packetId = packetId;
		this.invokeMode = invokeMode;
		this.serializerType = serializerType;
		this.arguments = arguments;
	}

	public ByteBuffer encode() {
		int totalLenght = headerLength;
		int datasize = arguments.size();
		for (int i = 0; i < datasize; i++) {
			totalLenght += (4 + arguments.get(i).limit());
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(totalLenght);
		byteBuffer.putInt(packetId).put(invokeMode).put(serializerType).putInt(datasize);
		for (int i = 0; i < datasize; i++) {
			ByteBuffer data = arguments.get(i);
			int lenli = data.limit();
			byteBuffer.putInt(lenli);
			System.arraycopy(data.array(), 0, byteBuffer.array(), byteBuffer.position(), lenli);
			byteBuffer.position(byteBuffer.position() + lenli);
		}
		byteBuffer.flip();
		return byteBuffer;
	}

}
