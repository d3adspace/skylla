package de.d3adspace.skylla.protocol.codec;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.protocol.Protocol;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.packet.PacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<PacketContainer> {

  private final Protocol protocol;

  private PacketCodec(Protocol protocol) {
    this.protocol = protocol;
  }

  public static PacketCodec forProtocol(Protocol protocol) {
    Preconditions.checkNotNull(protocol);
    return new PacketCodec(protocol);
  }

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, PacketContainer packetContainer, ByteBuf byteBuf) throws Exception {
    packetContainer.encode(byteBuf);
  }

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
    SkyllaBuffer buffer = SkyllaBuffer.withBuffer(byteBuf);
    PacketContainer packetContainer = protocol.decodePacket(buffer);
    list.add(packetContainer);
  }
}
