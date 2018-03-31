package com.haiyiyang.light.protocol;

import java.nio.ByteBuffer;
import java.util.List;

public class ProtocolPacket {
	public final static int headerLength = 9;

	private int packetId;
	private byte invokeMode;
	private List<ByteBuffer> arguments;

	public ProtocolPacket(byte invokeMode, List<ByteBuffer> arguments) {
		super();
		this.invokeMode = invokeMode;
		this.arguments = arguments;
	}

	public ProtocolPacket(int packetId, byte invokeMode, List<ByteBuffer> arguments) {
		super();
		this.packetId = packetId;
		this.invokeMode = invokeMode;
		this.arguments = arguments;
	}

	public ByteBuffer encode() {
		int totalLenght = headerLength;
		int datasize = this.arguments.size();
		for (int i = 0; i < datasize; i++) {
			totalLenght += (4 + this.arguments.get(i).limit());
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(totalLenght);
		byteBuffer.putInt(this.packetId).put(this.invokeMode).putInt(datasize);
		for (int i = 0; i < datasize; i++) {
			ByteBuffer data = this.arguments.get(i);
			int lenli = data.limit();
			byteBuffer.putInt(lenli);
			System.arraycopy(data.array(), 0, byteBuffer.array(), byteBuffer.position(), lenli);
			byteBuffer.position(byteBuffer.position() + lenli);
		}
		byteBuffer.flip();
		return byteBuffer;
	}

}
