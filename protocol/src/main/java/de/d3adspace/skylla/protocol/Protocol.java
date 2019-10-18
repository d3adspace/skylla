package de.d3adspace.skylla.protocol;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.flogger.FluentLogger;
import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;
import de.d3adspace.skylla.protocol.exception.InvalidPacketException;
import de.d3adspace.skylla.protocol.packet.*;

import java.util.List;
import java.util.Set;
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

  PacketContainer decodePacket(SkyllaBuffer buffer) throws InvalidPacketException {
    Preconditions.checkNotNull(buffer);
    return packetContainerFactory.decode(buffer);
  }

  public static class Builder {
    private final List<Class<? extends Packet>> packetClasses;

    private Builder(List<Class<? extends Packet>> packetClasses) {
      this.packetClasses = packetClasses;
    }

    public Builder withPacket(Class<? extends Packet> packetClass) {
      Preconditions.checkNotNull(packetClass);
      packetClasses.add(packetClass);
      return this;
    }

    public Protocol build() {
      Set<PacketDefinition> packetDefinitions = packetClasses.stream().map(packetClass -> {
        try {
          return PacketDefinition.fromClass(packetClass);
        } catch (InvalidPacketException e) {
          logger.atWarning()
            .withCause(e)
            .log("Invalid packet {0}. Ignoring packet", packetClass);
          return null;
        }
      }).collect(Collectors.toSet());

      PacketDefinitionRegistry packetDefinitionRegistry = PacketDefinitionRegistry.withDefinitions(packetDefinitions);
      PacketContainerFactory packetContainerFactory = PacketContainerFactory.withDefinitionRegistry(packetDefinitionRegistry);
      return new Protocol(packetContainerFactory);
    }
  }
}
