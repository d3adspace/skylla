package de.d3adspace.skylla.protocol.packet;

import com.google.common.base.Preconditions;
import de.d3adspace.skylla.protocol.exception.InvalidPacketException;

public final class PacketDefinition {

  private final Class<? extends Packet> packetClass;
  private final int id;

  private PacketDefinition(Class<? extends Packet> packetClass, int id) {
    this.packetClass = packetClass;
    this.id = id;
  }

  /**
   * Generate a packet definition from its packet class.
   *
   * @param packetClass The packet class.
   * @return The packet definition.
   * @throws InvalidPacketException If the given class is not a valid packet definition.
   */
  public static PacketDefinition fromClass(Class<? extends Packet> packetClass)
      throws InvalidPacketException {
    Preconditions.checkNotNull(packetClass);

    boolean annotationPresent = packetClass.isAnnotationPresent(PacketMeta.class);
    if (!annotationPresent) {
      throw InvalidPacketException.withMessage("Invalid packet, no @PacketMeta annotation");
    }

    PacketMeta packetMeta = packetClass.getAnnotation(PacketMeta.class);
    int id = packetMeta.id();

    return new PacketDefinition(packetClass, id);
  }

  public int id() {
    return id;
  }

  public Class<? extends Packet> packetClass() {
    return packetClass;
  }
}
