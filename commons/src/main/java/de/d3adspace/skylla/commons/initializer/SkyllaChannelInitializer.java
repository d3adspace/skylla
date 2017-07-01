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
 * @author Nathalie0hneHerz
 */
public class SkyllaChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private final Protocol protocol;
	
	public SkyllaChannelInitializer(Protocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		
		ChannelHandler lengthFieldBasedFrameDecoder = NettyUtils.createLengthFieldBasedFrameDecoder(32768, 0, 4);
		pipeline.addLast(lengthFieldBasedFrameDecoder);
		
		ChannelHandler packetDecoder = new SkyllaPacketDecoder(this.protocol);
		pipeline.addLast(packetDecoder);
		
		ChannelHandler lengthFieldPrepender = NettyUtils.createLengthFieldPrepender(4);
		pipeline.addLast(lengthFieldPrepender);
		
		ChannelHandler packetEncoder = new SkyllaPacketEncoder(this.protocol);
		pipeline.addLast(packetEncoder);
		
		ChannelHandler packetHandler = new SkyllaConnection(socketChannel, this.protocol);
		pipeline.addLast(packetHandler);
	}
}
