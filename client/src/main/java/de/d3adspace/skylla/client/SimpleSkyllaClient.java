package de.d3adspace.skylla.client;

import de.d3adspace.skylla.commons.config.SkyllaConfig;
import de.d3adspace.skylla.commons.initializer.SkyllaChannelInitializer;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.utils.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;

/**
 * @author Nathalie0hneHerz
 */
public class SimpleSkyllaClient implements SkyllaClient {
	
	private final SkyllaConfig config;
	private EventLoopGroup workerGroup;
	private Channel channel;
	
	SimpleSkyllaClient(SkyllaConfig config) {
		this.config = config;
	}
	
	@Override
	public void connect() {
		this.workerGroup = NettyUtils.createEventLoopGroup(4);
		
		Class<? extends Channel> channelClazz = NettyUtils.getChannel();
		ChannelHandler channelInitializer = new SkyllaChannelInitializer(this.config.getProtocol());
		
		Bootstrap bootstrap = new Bootstrap();
		
		try {
			channel = bootstrap
				.channel(channelClazz)
				.group(this.workerGroup)
				.handler(channelInitializer)
				.connect(this.config.getServerHost(), this.config.getServerPort())
				.sync().channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void disconnect() {
		this.channel.close();
		this.workerGroup.shutdownGracefully();
	}
	
	@Override
	public void sendPacket(SkyllaPacket packet) {
		this.channel.writeAndFlush(packet);
	}
}
