package de.d3adspace.skylla.protocol.packet;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.exception.InvalidPacketException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public final class PacketContainerFactory {

  private final PacketDefinitionRegistry definitionRegistry;

  PacketContainerFactory(PacketDefinitionRegistry definitionRegistry) {
    this.definitionRegistry = definitionRegistry;
  }

  public PacketContainer decode(SkyllaBuffer buffer) throws InvalidPacketException {
    Preconditions.checkNotNull(buffer);

    int packetId = buffer.readInt();
    PacketContainer packetContainer = createPacketContainer(packetId);

    packetContainer.decode(buffer);

    return packetContainer;
  }

  public PacketContainer createPacketContainer(int id) throws InvalidPacketException {
    Optional<PacketDefinition> packetDefinitionOptional = definitionRegistry.findByPacketId(id);
    PacketDefinition packetDefinition = packetDefinitionOptional.orElseThrow();

    Class<? extends Packet> packetClass = packetDefinition.packetClass();

    Packet packet;

    try {
      packet = packetClass.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw InvalidPacketException.withMessageAndCause("Couldn't construct packet instance", e);
    }

    return new PacketContainer(packetDefinition, packet);
  }

  public PacketContainer createPacketContainer(Packet packet) {
    Preconditions.checkNotNull(packet);

    Class<? extends Packet> packetClass = packet.getClass();
    Optional<PacketDefinition> packetDefinitionOptional = definitionRegistry.findByPacketClass(packetClass);
    PacketDefinition packetDefinition = packetDefinitionOptional.orElseThrow();

    return new PacketContainer(packetDefinition, packet);
  }
}
