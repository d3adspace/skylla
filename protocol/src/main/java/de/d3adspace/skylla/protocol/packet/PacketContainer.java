package de.d3adspace.skylla.protocol.packet;

import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import io.netty.buffer.ByteBuf;

public final class PacketContainer {
  private final PacketDefinition definition;
  private final Packet packet;

  PacketContainer(PacketDefinition definition, Packet packet) {
    this.definition = definition;
    this.packet = packet;
  }

  public int id() {
    return definition.id();
  }

  public SkyllaBuffer encode(ByteBuf byteBuf) {
    SkyllaBuffer buffer = SkyllaBuffer.withBuffer(byteBuf);
    int id = definition.id();

    buffer.writeInt(id);
    packet.write(buffer);

    return buffer;
  }

  public void decode(SkyllaBuffer buffer) {
    packet.read(buffer);
  }
}
