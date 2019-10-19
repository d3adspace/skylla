package de.d3adspace.skylla.protocol;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.packet.Packet;
import de.d3adspace.skylla.protocol.packet.PacketContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import java.util.List;

public final class PacketCodec extends ByteToMessageCodec<Packet> {

  private final Protocol protocol;

  private PacketCodec(Protocol protocol) {
    this.protocol = protocol;
  }

  public static PacketCodec forProtocol(Protocol protocol) {
    Preconditions.checkNotNull(protocol);
    return new PacketCodec(protocol);
  }

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf)
      throws Exception {

    SkyllaBuffer buffer = SkyllaBuffer.withBuffer(byteBuf);
    protocol.encodePacket(packet, buffer);
  }

  @Override
  protected void decode(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
      throws Exception {
    SkyllaBuffer buffer = SkyllaBuffer.withBuffer(byteBuf);
    PacketContainer packetContainer = protocol.decodePacket(buffer);
    list.add(packetContainer);
  }
}
