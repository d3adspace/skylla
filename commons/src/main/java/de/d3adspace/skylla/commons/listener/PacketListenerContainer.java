package de.d3adspace.skylla.commons.listener;

import de.d3adspace.skylla.commons.packet.PacketContext;
import de.d3adspace.skylla.protocol.packet.Packet;

public interface PacketListenerContainer {

  void callEvent(PacketContext packetContext, Packet packet);
}
