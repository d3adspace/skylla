package de.d3adspace.skylla.protocol;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.flogger.FluentLogger;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.exception.InvalidPacketException;
import de.d3adspace.skylla.protocol.packet.Packet;
import de.d3adspace.skylla.protocol.packet.PacketContainer;
import de.d3adspace.skylla.protocol.packet.PacketContainerFactory;
import de.d3adspace.skylla.protocol.packet.PacketDefinition;
import de.d3adspace.skylla.protocol.packet.PacketDefinitionRegistry;
import java.util.List;
import java.util.stream.Collectors;

public final class Protocol {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PacketContainerFactory packetContainerFactory;

  private Protocol(PacketContainerFactory packetContainerFactory) {
    this.packetContainerFactory = packetContainerFactory;
  }

  public static Builder newBuilder() {
    return new Builder(Lists.newArrayList());
  }

  public static Protocol empty() {
    return newBuilder().build();
  }

  PacketContainer decodePacket(SkyllaBuffer buffer) throws InvalidPacketException {
    Preconditions.checkNotNull(buffer);
    return packetContainerFactory.decode(buffer);
  }

  PacketContainer encodePacket(Packet packet, SkyllaBuffer buffer) {
    Preconditions.checkNotNull(packet);
    Preconditions.checkNotNull(buffer);
    return packetContainerFactory.encode(packet, buffer);
  }

  public static class Builder {

    private final List<Class<? extends Packet>> packetClasses;

    private Builder(List<Class<? extends Packet>> packetClasses) {
      this.packetClasses = packetClasses;
    }

    /**
     * Add a packet definition class to the protocol packets.
     *
     * @param packetClass The class of the packet.
     * @return The builder instance.
     */
    public Builder withPacket(Class<? extends Packet> packetClass) {
      Preconditions.checkNotNull(packetClass);
      packetClasses.add(packetClass);
      return this;
    }

    /**
     * Construct the protocol instance.
     *
     * @return The protocol instance.
     */
    public Protocol build() {
      var packetDefinitions =
          packetClasses.stream()
              .map(this::definePacket)
              .collect(Collectors.toSet());

      var packetDefinitionRegistry = PacketDefinitionRegistry.withDefinitions(packetDefinitions);
      var packetContainerFactory = PacketContainerFactory
          .withDefinitionRegistry(packetDefinitionRegistry);
      return new Protocol(packetContainerFactory);
    }

    private PacketDefinition definePacket(Class<? extends Packet> packetClass) {
      try {
        return PacketDefinition.fromClass(packetClass);
      } catch (InvalidPacketException e) {
        logger.atWarning()
            .withCause(e)
            .log("Invalid packet {0}. Ignoring packet", packetClass);
        return null;
      }
    }
  }
}
