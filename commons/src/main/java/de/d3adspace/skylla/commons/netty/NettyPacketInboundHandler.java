package de.d3adspace.skylla.commons.netty;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.commons.listener.PacketListenerContainer;
import de.d3adspace.skylla.protocol.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public final class NettyPacketInboundHandler extends SimpleChannelInboundHandler<Packet> {

  private final PacketListenerContainer packetListenerContainer;

  private NettyPacketInboundHandler(PacketListenerContainer packetListenerContainer) {
    this.packetListenerContainer = packetListenerContainer;
  }

  public static NettyPacketInboundHandler withListenerContainer(
      PacketListenerContainer listenerContainer
  ) {
    Preconditions.checkNotNull(listenerContainer);
    return new NettyPacketInboundHandler(listenerContainer);
  }

  @Override
  protected void channelRead0(
      ChannelHandlerContext channelHandlerContext,
      Packet packet
  ) throws Exception {
    var channel = channelHandlerContext.channel();
    var packetContext = NettyPacketContext.withChannel(channel);

    packetListenerContainer.callEvent(packetContext, packet);
  }
}
