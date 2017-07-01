package de.d3adspace.skylla.commons.codec;

import de.d3adspace.skylla.commons.buffer.SkyllaBuffer;
import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaPacketEncoder extends MessageToMessageEncoder<SkyllaPacket> {
	
	private final Protocol protocol;
	
	public SkyllaPacketEncoder(Protocol protocol) {
		this.protocol = protocol;
	}
	
	
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, SkyllaPacket packet,
		List<Object> list) throws Exception {
		
		byte packetId = this.protocol.getPacketId(packet);
		ByteBuf byteBuf = channelHandlerContext.alloc().buffer();
		
		// write packet id
		byteBuf.writeByte(packetId);
		
		// Encode Packet
		packet.write(new SkyllaBuffer(byteBuf));
		
		list.add(byteBuf);
	}
}
