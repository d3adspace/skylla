package de.d3adspace.skylla.protocol.packet;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.Optional;

public final class PacketDefinitionRegistry {

  private final Map<Class<? extends Packet>, PacketDefinition> packetDefinitions;

  private PacketDefinitionRegistry(Map<Class<? extends Packet>, PacketDefinition> packetDefinitions) {
    this.packetDefinitions = packetDefinitions;
  }

  public Optional<PacketDefinition> findByPacketClass(Class<? extends Packet> packetClass) {
    Preconditions.checkNotNull(packetClass);

    return Optional.ofNullable(packetDefinitions.get(packetClass));
  }

  public Optional<PacketDefinition> findByPacketId(int id) {
    return packetDefinitions.values().stream()
      .filter(packetDefinition -> packetDefinition.id() == id)
      .findFirst();
  }
}
