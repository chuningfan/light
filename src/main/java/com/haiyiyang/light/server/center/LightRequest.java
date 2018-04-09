package com.haiyiyang.light.server.center;

import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.server.LightRpcServer;

import io.netty.channel.ChannelHandlerContext;

public class LightRequest {

	private ProtocolPacket protocolPacket;
	private ChannelHandlerContext context;
	private LightRpcServer lightRpcServer;

	public LightRequest(ChannelHandlerContext context, Object msg, LightRpcServer lightRpcServer) {
		this.protocolPacket = (ProtocolPacket) msg;
		this.context = context;
		this.lightRpcServer = lightRpcServer;
	}

}
