package com.haiyiyang.light.rpc.server.channel;

import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.rpc.server.task.TaskQueue;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ServerInboundHandler extends SimpleChannelInboundHandler<ProtocolPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ProtocolPacket msg) throws Exception {
		TaskQueue.SINGLETON().add((ProtocolPacket) msg);
	}

}
