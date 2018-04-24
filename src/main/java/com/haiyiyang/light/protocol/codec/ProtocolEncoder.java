package com.haiyiyang.light.protocol.codec;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.protocol.ProtocolPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<ProtocolPacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolPacket msg, ByteBuf out) throws Exception {
		byte[] data = ProtocolPacket.encode(msg).array();
		out.writeByte(LightConstants.PROTOCOL_MAGIC_NUMBER).writeInt(data.length).writeBytes(data);
	}
}
