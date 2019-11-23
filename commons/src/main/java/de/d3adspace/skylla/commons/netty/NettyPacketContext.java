package de.d3adspace.skylla.commons.netty;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.commons.packet.PacketContext;
import de.d3adspace.skylla.protocol.packet.Packet;
import io.netty.channel.Channel;

public final class NettyPacketContext implements PacketContext {

  private final Channel channel;

  private NettyPacketContext(Channel channel) {
    this.channel = channel;
  }

  static NettyPacketContext withChannel(Channel channel) {
    Preconditions.checkNotNull(channel);

    return new NettyPacketContext(channel);
  }

  @Override
  public void resume(Packet packet) {
    Preconditions.checkNotNull(packet);
    channel.writeAndFlush(packet);
  }
}
