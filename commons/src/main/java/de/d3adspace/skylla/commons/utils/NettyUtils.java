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

package de.d3adspace.skylla.commons.utils;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.internal.PlatformDependent;

/**
 * @author Nathalie0hneHerz
 */
public class NettyUtils {

    private static final boolean EPOLL = !PlatformDependent.isWindows() && Epoll.isAvailable();

    public static EventLoopGroup createEventLoopGroup(int threadAmount) {

        return EPOLL ? new EpollEventLoopGroup(threadAmount) : new NioEventLoopGroup(threadAmount);
    }

    public static Class<? extends ServerChannel> getServerChannelClass() {

        return EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    public static Class<? extends Channel> getChannel() {

        return EPOLL ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    public static boolean isEpoll() {

        return EPOLL;
    }

    public static ChannelHandler createLengthFieldBasedFrameDecoder(int maxFrameLength, int offset, int lengthFieldLength) {

        return new LengthFieldBasedFrameDecoder(maxFrameLength, offset, lengthFieldLength);
    }

    public static ChannelHandler createLengthFieldPrepender(int lengthFieldLength) {

        return new LengthFieldPrepender(lengthFieldLength);
    }

    public static void closeWhenFlushed(Channel channel) {

        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
