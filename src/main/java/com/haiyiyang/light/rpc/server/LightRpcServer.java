package com.haiyiyang.light.rpc.server;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.context.LightContext;
import com.haiyiyang.light.meta.LightAppMeta;
import com.haiyiyang.light.protocol.codec.ProtocolDecoder;
import com.haiyiyang.light.protocol.codec.ProtocolEncoder;
import com.haiyiyang.light.rpc.server.channel.ServerInboundHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class LightRpcServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(LightRpcServer.class);

	private static LightRpcServer LIGHT_RPC_SERVER = new LightRpcServer();

	public static LightRpcServer SINGLETON() {
		return LIGHT_RPC_SERVER;
	}

	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private ServerBootstrap serverBootstrap = null;

	public synchronized void start() {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		LightAppMeta lightAppMeta = LightContext.getContext().getLightAppMeta();
		try {
			serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {
							ch.pipeline().addLast("decoder", new ProtocolDecoder());
							ch.pipeline().addLast("encoder", new ProtocolEncoder());
							ch.pipeline().addLast("handler", new ServerInboundHandler(this));
						}
					});
			String serverIp = lightAppMeta.getMachineIp();
			int serverPort = lightAppMeta.getAppPort();
			ChannelFuture channelFuture = serverBootstrap.bind(serverIp, serverPort);

			channelFuture.awaitUninterruptibly(lightAppMeta.getLightProps().getTimeout(), TimeUnit.SECONDS);
			if (!channelFuture.isSuccess()) {
				LOGGER.debug("starting Netty  successfully");
			}

		} catch (Exception e) {
		}
	}

}
