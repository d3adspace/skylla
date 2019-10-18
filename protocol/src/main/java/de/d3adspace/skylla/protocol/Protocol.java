package de.d3adspace.skylla.protocol;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.exception.InvalidPacketException;
import de.d3adspace.skylla.protocol.packet.PacketContainer;
import de.d3adspace.skylla.protocol.packet.PacketContainerFactory;

public final class Protocol {
  private final String version;

  private final PacketContainerFactory packetContainerFactory;

  private Protocol(String version, PacketContainerFactory packetContainerFactory) {
    this.version = version;
    this.packetContainerFactory = packetContainerFactory;
  }

  public PacketContainer decodePacket(SkyllaBuffer buffer) throws InvalidPacketException {
    Preconditions.checkNotNull(buffer);
    return packetContainerFactory.decode(buffer);
  }
}
