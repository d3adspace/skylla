package de.d3adspace.skylla.client;

import de.d3adspace.skylla.protocol.packet.Packet;

public interface SkyllaClient {

  void connect();

  boolean isConnected();

  void disconnect();

  void sendPacket(Packet packet);
}
