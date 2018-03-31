package com.haiyiyang.light.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.haiyiyang.light.protocol.ProtocolDecoder;
import com.haiyiyang.light.protocol.ProtocolEncoder;
import com.haiyiyang.light.server.IpPort;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class LightRpcClient {
	private LightChannelHandler lightClienHandler;
	private static Map<IpPort, Channel> CHANNELS = new ConcurrentHashMap<>();
	private static Map<IpPort, EventLoopGroup> EVENT_LOOP_GROUPS = new ConcurrentHashMap<>();

	private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

	public Channel getChannel(IpPort ipPort) {
		return CHANNELS.get(ipPort);
	}

	public Channel connect(IpPort ipPort) throws Exception {
		if (CHANNELS.containsKey(ipPort)) {
			return CHANNELS.get(ipPort);
		}
		REENTRANT_LOCK.lock();
		try {
			Bootstrap b = new Bootstrap();
			EventLoopGroup group = new NioEventLoopGroup();
			lightClienHandler = new LightChannelHandler(this);
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("decoder", new ProtocolDecoder());
							ch.pipeline().addLast("encoder", new ProtocolEncoder());
							ch.pipeline().addLast(new IdleStateHandler(0, 0, 10/** TODO */
							), lightClienHandler);
						}
					});
			ChannelFuture channelFuture = b.connect(ipPort.getIp(), ipPort.getPort());
			channelFuture.awaitUninterruptibly(10, TimeUnit.SECONDS); // TODO
			if (channelFuture.isSuccess()) {
				CHANNELS.put(ipPort, channelFuture.channel());
				EVENT_LOOP_GROUPS.put(ipPort, group);
			} else {
				throw new Exception();
			}
		} finally {
			REENTRANT_LOCK.unlock();
		}
		return CHANNELS.get(ipPort);
	}
}
