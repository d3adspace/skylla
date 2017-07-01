/*
 * Copyright (c) 2017 D3adspace
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.d3adspace.skylla.server;

import de.d3adspace.skylla.commons.config.SkyllaConfig;
import de.d3adspace.skylla.commons.initializer.SkyllaChannelInitializer;
import de.d3adspace.skylla.commons.utils.NettyUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

/**
 * @author Nathalie0hneHerz
 */
public class SimpleSkyllaServer implements SkyllaServer {
	
	/**
	 * The config for the server containing the address and the protocol
	 */
	private final SkyllaConfig config;
	
	/**
	 * Netty boss group
	 */
	private EventLoopGroup bossGroup;
	
	/**
	 * Netty worker group
	 */
	private EventLoopGroup workerGroup;
	
	/**
	 * Server channel
	 */
	private Channel channel;
	
	/**
	 * Create a new server based on the given config.
	 *
	 * @param config The config.
	 */
	SimpleSkyllaServer(SkyllaConfig config) {
		this.config = config;
	}
	
	@Override
	public void start() {
		this.bossGroup = NettyUtils.createEventLoopGroup(1);
		this.workerGroup = NettyUtils.createEventLoopGroup(4);
		
		Class<? extends ServerChannel> serverChannelClazz = NettyUtils.getServerChannelClass();
		ChannelHandler channelInitializer = new SkyllaChannelInitializer(config.getProtocol());
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		
		try {
			channel = serverBootstrap
				.group(this.bossGroup, this.workerGroup)
				.channel(serverChannelClazz)
				.childHandler(channelInitializer)
				.option(ChannelOption.SO_BACKLOG, 4)
				.bind(this.config.getServerHost(), this.config.getServerPort())
				.sync().channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		this.channel.close();
		this.bossGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
	}
	
	@Override
	public boolean isActive() {
		return this.channel != null && this.channel.isActive();
		
	}
}
