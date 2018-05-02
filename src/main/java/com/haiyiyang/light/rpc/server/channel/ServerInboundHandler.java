package com.haiyiyang.light.rpc.server.channel;

import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.rpc.server.task.TaskQueue;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

@Sharable
public class ServerInboundHandler extends ChannelInboundHandlerAdapter {

	public ServerInboundHandler() {
	}

	public ServerInboundHandler(ChannelInitializer<SocketChannel> channelInitializer) {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg == null) {
			return;
		}
		if (msg instanceof ProtocolPacket) {
			TaskQueue.SINGLETON().add((ProtocolPacket) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
