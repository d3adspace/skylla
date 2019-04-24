/*
 * Copyright (c) 2017 - 2019 D3adspace
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

package de.d3adspace.skylla.commons.initializer;

import de.d3adspace.skylla.commons.codec.SkyllaPacketDecoder;
import de.d3adspace.skylla.commons.codec.SkyllaPacketEncoder;
import de.d3adspace.skylla.commons.connection.SkyllaConnection;
import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.utils.NettyUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Nathalie O'Neill <nathalie@d3adspace.de>
 */
public class SkyllaChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * The protocol for communication.
     */
    private final Protocol protocol;

    /**
     * Create a new initializer.
     *
     * @param protocol The protocol.
     */
    public SkyllaChannelInitializer(Protocol protocol) {

        this.protocol = protocol;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {

        ChannelPipeline pipeline = socketChannel.pipeline();

        ChannelHandler lengthFieldBasedFrameDecoder = NettyUtils.createLengthFieldBasedFrameDecoder(32768, 0, 4);
        pipeline.addLast(lengthFieldBasedFrameDecoder);

        ChannelHandler packetDecoder = new SkyllaPacketDecoder(protocol);
        pipeline.addLast(packetDecoder);

        ChannelHandler lengthFieldPrepender = NettyUtils.createLengthFieldPrepender(4);
        pipeline.addLast(lengthFieldPrepender);

        ChannelHandler packetEncoder = new SkyllaPacketEncoder(protocol);
        pipeline.addLast(packetEncoder);

        ChannelHandler packetHandler = new SkyllaConnection(socketChannel, protocol);
        pipeline.addLast(packetHandler);
    }
}
