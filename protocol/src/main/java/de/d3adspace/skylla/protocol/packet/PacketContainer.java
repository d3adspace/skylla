package de.d3adspace.skylla.protocol.packet;

import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;

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

  /**
   * Encode the packet into the given buffer.
   *
   * @param buffer The skylla buffer.
   * @return The skylla buffer.
   */
  SkyllaBuffer encode(SkyllaBuffer buffer) {
    var id = definition.id();

    buffer.writeInt(id);
    packet.write(buffer);

    return buffer;
  }

  void decode(SkyllaBuffer buffer) {
    packet.read(buffer);
  }
}
