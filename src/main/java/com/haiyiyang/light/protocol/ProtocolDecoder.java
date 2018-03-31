package com.haiyiyang.light.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.haiyiyang.light.constant.LightConstants;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class ProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() > ProtocolPacket.headerLength) {
			return;
		}
		in.markReaderIndex();
		if (in.readByte() != LightConstants.PROTOCOL_MAGIC_NUMBER) {
			throw new CorruptedFrameException("Invalid protocol magic number");
		}
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] decoded = new byte[dataLength];
		in.readBytes(decoded);
		ByteBuffer buffer = ByteBuffer.wrap(decoded);
		int packetId = buffer.getInt();
		byte invokeMode = buffer.get();
		int argumentsSize = buffer.getInt();
		List<ByteBuffer> datas = new ArrayList<ByteBuffer>();
		for (int i = 0; i < argumentsSize; i++) {
			int length = buffer.getInt();
			ByteBuffer buf = ByteBuffer.allocate(length);
			System.arraycopy(buffer.array(), buffer.position(), buf.array(), 0, length);
			buffer.position(buffer.position() + length);
			datas.add(buf);
		}
		out.add(new ProtocolPacket(packetId, invokeMode, datas));
	}

}
