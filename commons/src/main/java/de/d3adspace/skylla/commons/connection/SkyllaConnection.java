package de.d3adspace.skylla.commons.connection;

import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.utils.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaConnection extends SimpleChannelInboundHandler<SkyllaPacket> {
	
	private final Channel channel;
	private final Protocol protocol;
	
	public SkyllaConnection(Channel channel, Protocol protocol) {
		this.channel = channel;
		this.protocol = protocol;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, SkyllaPacket packet)
		throws Exception {
		
		this.protocol.handlePacket(packet);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause.hashCode() == IOException.class.hashCode()) {
			NettyUtils.closeWhenFlushed(this.channel);
			return;
		}
		
		cause.printStackTrace();
	}
	
	public void sendPackets(SkyllaPacket... packets) {
		if (packets.length == 1) {
			this.channel.writeAndFlush(packets[0]);
			return;
		}
		
		for (SkyllaPacket packet : packets) {
			this.channel.write(packet);
		}
		
		this.channel.flush();
	}
}
