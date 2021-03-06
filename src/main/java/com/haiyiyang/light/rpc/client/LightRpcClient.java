package com.haiyiyang.light.rpc.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.context.LightContext;
import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.protocol.codec.ProtocolDecoder;
import com.haiyiyang.light.protocol.codec.ProtocolEncoder;
import com.haiyiyang.light.rpc.LightRpcContext;
import com.haiyiyang.light.rpc.client.channel.ClientInboundHandler;
import com.haiyiyang.light.rpc.response.ResponseFuture;
import com.haiyiyang.light.rpc.server.IpPort;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(LightRpcClient.class);

	private ClientInboundHandler clentInboundHandler;
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
			clentInboundHandler = new ClientInboundHandler(this);
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("decoder", new ProtocolDecoder());
							ch.pipeline().addLast("encoder", new ProtocolEncoder());
							ch.pipeline().addLast(new IdleStateHandler(0, 0, 10/** TODO */
							), clentInboundHandler);
						}
					});
			ChannelFuture channelFuture = b.connect(ipPort.getIp(), ipPort.getPort());
			LOGGER.info("Starting Netty server, IP: {}, port: {}.", ipPort.getIp(), ipPort.getPort());
			channelFuture.awaitUninterruptibly(10, TimeUnit.SECONDS); // TODO
			if (channelFuture.isSuccess()) {
				CHANNELS.put(ipPort, channelFuture.channel());
				EVENT_LOOP_GROUPS.put(ipPort, group);
				LOGGER.info("Netty server have already started successfully.");
			} else {
				LOGGER.error("Netty server started failed.");
				throw new Exception();
			}
		} finally {
			REENTRANT_LOCK.unlock();
		}
		return CHANNELS.get(ipPort);
	}

	public Object sendMessage(ProtocolPacket packet, Object classType, Channel channel) throws Exception {
		LightProps lightProps = LightContext.getContext().getLightAppMeta().getLightProps();
		if (packet.getInvokeMode() != LightConstants.BYTE0) {
			LightRpcContext.setResponseFuture(packet.getPacketId(),
					new ResponseFuture<Object>(classType, lightProps.getTimeout(), TimeUnit.MILLISECONDS));
		}
		writeChannel(packet, channel);
		if (packet.getInvokeMode() == LightConstants.BYTE1) {
			return LightRpcContext.getFuture(packet.getPacketId()).get();
		}
		return null;
	}

	private void writeChannel(ProtocolPacket packet, Channel channel) throws Exception {
		if (isChannelActive(channel)) {
			channel.writeAndFlush(packet);
		}
	}

	public boolean isChannelActive(Channel channel) throws Exception {
		return channel != null && channel.isActive() && channel.isOpen();
	}

	public void receiveMessage(Object msg) {
		ProtocolPacket packet = (ProtocolPacket) msg;
		LightRpcContext.getResponseFuture(packet.getPacketId()).receiveProtocolPacket(packet);
	}
}
