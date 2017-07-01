package de.d3adspace.skylla.commons.codec;

import de.d3adspace.skylla.commons.buffer.SkyllaBuffer;
import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaPacketDecoder extends MessageToMessageDecoder<ByteBuf> {
	
	private final Protocol protocol;
	
	public SkyllaPacketDecoder(Protocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
		List<Object> list) throws Exception {
		
		if (byteBuf.readInt() > 0) {
			byte packetId = byteBuf.readByte();
			
			SkyllaPacket packet = this.protocol.createPacket(packetId);
			packet.read(new SkyllaBuffer(byteBuf));
			
			list.add(packet);
		}
	}
}
