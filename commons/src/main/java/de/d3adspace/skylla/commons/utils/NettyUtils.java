package de.d3adspace.skylla.commons.utils;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;

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
	
	public static ChannelHandler createLengthFieldBasedFrameDecoder(int maxFrameLength, int offset,
		int lengthFieldLength) {
		return new LengthFieldBasedFrameDecoder(maxFrameLength, offset, lengthFieldLength);
	}
	
	public static ChannelHandler createLengthFieldPrepender(int lengthFieldLength) {
		return new LengthFieldPrepender(lengthFieldLength);
	}
	
	public static void closeWhenFlushed(Channel channel) {
		channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
}
