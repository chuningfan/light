package com.haiyiyang.light.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;

public class LightRpcServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(LightRpcServer.class);

	private static LightRpcServer LIGHT_RPC_SERVER = new LightRpcServer();

	public static LightRpcServer SINGLETON() {
		return LIGHT_RPC_SERVER;
	}

	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private ServerBootstrap serverBootstrap = null;

}
