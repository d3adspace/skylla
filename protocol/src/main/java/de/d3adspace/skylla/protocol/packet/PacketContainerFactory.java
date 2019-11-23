package de.d3adspace.skylla.protocol.packet;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.exception.InvalidPacketException;
import java.lang.reflect.InvocationTargetException;

public final class PacketContainerFactory {

  private final PacketDefinitionRegistry definitionRegistry;

  private PacketContainerFactory(PacketDefinitionRegistry definitionRegistry) {
    this.definitionRegistry = definitionRegistry;
  }

  /**
   * Create a factory based on a registry of packet definitions.
   *
   * @param packetDefinitionRegistry The definition registry.
   * @return The container factory.
   */
  public static PacketContainerFactory withDefinitionRegistry(
      PacketDefinitionRegistry packetDefinitionRegistry
  ) {
    Preconditions.checkNotNull(packetDefinitionRegistry);
    return new PacketContainerFactory(packetDefinitionRegistry);
  }

  /**
   * Decode a full packet container from a skylla buffer. This will also read the packet content
   * from the buffer.
   *
   * @param buffer The buffer.
   * @return The packet container.
   * @throws InvalidPacketException If there wasn't a suitable valid packet.
   */
  public PacketContainer decode(SkyllaBuffer buffer) throws InvalidPacketException {
    Preconditions.checkNotNull(buffer);

    var packetId = buffer.readInt();
    var packetContainer = createPacketContainer(packetId);

    packetContainer.decode(buffer);

    return packetContainer;
  }

  private PacketContainer createPacketContainer(int id) throws InvalidPacketException {
    var packetDefinitionOptional = definitionRegistry.findByPacketId(id);
    var packetDefinition = packetDefinitionOptional.orElseThrow();

    var packetClass = packetDefinition.packetClass();

    Packet packet;

    try {
      packet = packetClass.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw InvalidPacketException.withMessageAndCause("Couldn't construct packet instance", e);
    }

    return new PacketContainer(packetDefinition, packet);
  }

  private PacketContainer createPacketContainer(Packet packet) {
    Preconditions.checkNotNull(packet);

    var packetClass = packet.getClass();
    var packetDefinitionOptional =
        definitionRegistry.findByPacketClass(packetClass);
    var packetDefinition = packetDefinitionOptional.orElseThrow();

    return new PacketContainer(packetDefinition, packet);
  }

  /**
   * Create a packet container by its packet handle and the buffer.
   *
   * @param packet The packet handle.
   * @param buffer The buffer.
   * @return The packet container.
   */
  public PacketContainer encode(Packet packet, SkyllaBuffer buffer) {
    Preconditions.checkNotNull(packet);
    Preconditions.checkNotNull(buffer);

    var packetContainer = createPacketContainer(packet);
    packetContainer.encode(buffer);
    return packetContainer;
  }
}
