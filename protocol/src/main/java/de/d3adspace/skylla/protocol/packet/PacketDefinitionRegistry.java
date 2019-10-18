package de.d3adspace.skylla.protocol.packet;

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PacketDefinitionRegistry {

  private final Map<Class<? extends Packet>, PacketDefinition> packetDefinitions;

  private PacketDefinitionRegistry(Map<Class<? extends Packet>, PacketDefinition> packetDefinitions) {
    this.packetDefinitions = packetDefinitions;
  }

  public static PacketDefinitionRegistry withDefinitions(Set<PacketDefinition> packetDefinitions) {
    Preconditions.checkNotNull(packetDefinitions);

    Map<Class<? extends Packet>, PacketDefinition> effectivePacketDefinitions = packetDefinitions
      .stream()
      .collect(Collectors.toMap(PacketDefinition::packetClass, Function.identity()));
    return new PacketDefinitionRegistry(effectivePacketDefinitions);
  }

  Optional<PacketDefinition> findByPacketClass(Class<? extends Packet> packetClass) {
    Preconditions.checkNotNull(packetClass);

    return Optional.ofNullable(packetDefinitions.get(packetClass));
  }

  Optional<PacketDefinition> findByPacketId(int id) {
    return packetDefinitions.values().stream()
      .filter(packetDefinition -> packetDefinition.id() == id)
      .findFirst();
  }
}
