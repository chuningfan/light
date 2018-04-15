package com.haiyiyang.light.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class LightClientChannelHandler extends ChannelInboundHandlerAdapter {

	private LightRpcClient lightRpcClient;

	public LightClientChannelHandler(LightRpcClient lightRpcClient) {
		this.lightRpcClient = lightRpcClient;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		lightRpcClient.receiveMessage(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (!(evt instanceof IdleStateEvent)) {
			return;
		}
		IdleStateEvent e = (IdleStateEvent) evt;
		if (e.state() == IdleState.ALL_IDLE) {
			ctx.close();
		}
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		// lightRpcClient.disconnect(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
