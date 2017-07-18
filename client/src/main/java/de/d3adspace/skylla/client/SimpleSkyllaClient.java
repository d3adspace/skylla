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

package de.d3adspace.skylla.client;

import de.d3adspace.skylla.commons.config.SkyllaConfig;
import de.d3adspace.skylla.commons.initializer.SkyllaChannelInitializer;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.utils.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

/**
 * Basic client.
 *
 * @author Nathalie0hneHerz
 */
public class SimpleSkyllaClient implements SkyllaClient {

    /**
     * Config providing server adress
     */
    private final SkyllaConfig config;

    /**
     * Worker group for netty.
     */
    private EventLoopGroup workerGroup;

    /**
     * Channel to communicate with the server
     */
    private Channel channel;

    /**
     * Create a new client
     *
     * @param config The config.
     */
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
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 50)
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
