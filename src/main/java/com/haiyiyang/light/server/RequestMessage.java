package com.haiyiyang.light.server;

import com.haiyiyang.light.protocol.ProtocolPacket;

import io.netty.channel.ChannelHandlerContext;

public class RequestMessage {

	private ProtocolPacket message;
	private ChannelHandlerContext context;
	private LightRpcServer lightRpcServer;

	public RequestMessage(ChannelHandlerContext context, Object msg, LightRpcServer lightRpcServer) {
		this.message = (ProtocolPacket) msg;
		this.context = context;
		this.lightRpcServer = lightRpcServer;
	}

}
