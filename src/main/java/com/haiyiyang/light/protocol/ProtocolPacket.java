package com.haiyiyang.light.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;

public class ProtocolPacket {
	public static final int headerLength = 18;

	private int packetId;
	private byte invokeMode;
	private byte serializerType;
	private long startTime;
	private List<ByteBuffer> requestMeta;
	private ChannelHandlerContext chContext;

	public ProtocolPacket(int packetId, byte invokeMode, byte serializerType, long startTime,
			List<ByteBuffer> requestMeta) {
		this.packetId = packetId;
		this.invokeMode = invokeMode;
		this.serializerType = serializerType;
		this.startTime = startTime;
		this.requestMeta = requestMeta;
	}

	public static ByteBuffer encode(ProtocolPacket packet) {
		int totalLenght = headerLength;
		int datasize = packet.getRequestMeta().size();
		for (int i = 0; i < datasize; i++) {
			totalLenght += (4 + packet.getRequestMeta().get(i).limit());
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(totalLenght);
		byteBuffer.putInt(packet.getPacketId()).put(packet.getInvokeMode()).put(packet.getSerializerType())
				.putLong(packet.getStartTime()).putInt(datasize);
		for (int i = 0; i < datasize; i++) {
			ByteBuffer data = packet.getRequestMeta().get(i);
			int lenli = data.limit();
			byteBuffer.putInt(lenli);
			System.arraycopy(data.array(), 0, byteBuffer.array(), byteBuffer.position(), lenli);
			byteBuffer.position(byteBuffer.position() + lenli);
		}
		byteBuffer.flip();
		return byteBuffer;
	}

	public static ProtocolPacket decode(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int packetId = buffer.getInt();
		byte invokeMode = buffer.get();
		byte serializerType = buffer.get();
		long startTime = buffer.getLong();
		int argumentsSize = buffer.getInt();
		List<ByteBuffer> datas = new ArrayList<ByteBuffer>();
		for (int i = 0; i < argumentsSize; i++) {
			int length = buffer.getInt();
			ByteBuffer buf = ByteBuffer.allocate(length);
			System.arraycopy(buffer.array(), buffer.position(), buf.array(), 0, length);
			buffer.position(buffer.position() + length);
			datas.add(buf);
		}
		return new ProtocolPacket(packetId, invokeMode, serializerType, startTime, datas);
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

	public ChannelHandlerContext getChContext() {
		return chContext;
	}

	public void setChContext(ChannelHandlerContext chContext) {
		this.chContext = chContext;
	}

}
