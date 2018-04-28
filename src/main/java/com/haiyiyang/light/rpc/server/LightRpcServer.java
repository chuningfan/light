package com.haiyiyang.light.rpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;

public class LightRpcServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightRpcServer.class);

	private static LightRpcServer LIGHT_RPC_SERVER = new LightRpcServer();

	private static Map<String, Object> map = new ConcurrentHashMap<>();

	public static LightRpcServer SINGLETON() {
		return LIGHT_RPC_SERVER;
	}

	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private ServerBootstrap serverBootstrap = null;

}