package de.d3adspace.skylla.commons.packet;

import de.d3adspace.skylla.protocol.packet.Packet;

public interface PacketContext {
  /**
   * Answer the context with the given packet.
   *
   * @param packet The packet.
   */
  void resume(Packet packet);
}
