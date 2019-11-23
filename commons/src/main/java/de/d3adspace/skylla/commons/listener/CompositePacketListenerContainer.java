package de.d3adspace.skylla.commons.listener;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.d3adspace.skylla.commons.packet.PacketContext;
import de.d3adspace.skylla.protocol.packet.Packet;
import java.util.List;

public final class CompositePacketListenerContainer implements PacketListenerContainer {

  private final List<PacketListenerContainer> packetListenerContainers;

  private CompositePacketListenerContainer(List<PacketListenerContainer> packetListenerContainers) {
    super();
    this.packetListenerContainers = packetListenerContainers;
  }

  public static CompositePacketListenerContainer withListeners(
      List<PacketListenerContainer> packetListenerContainers
  ) {
    Preconditions.checkNotNull(packetListenerContainers);
    return new CompositePacketListenerContainer(packetListenerContainers);
  }

  public static CompositePacketListenerContainer empty() {
    return new CompositePacketListenerContainer(Lists.newArrayList());
  }

  /**
   * Call the event of a received packet.
   *
   * @param packetContext The packet context.
   * @param packet        The packet.
   */
  public void callEvent(PacketContext packetContext, Packet packet) {
    Preconditions.checkNotNull(packetContext);
    Preconditions.checkNotNull(packet);

    packetListenerContainers.forEach(packetListenerContainer ->
        packetListenerContainer.callEvent(packetContext, packet));
  }
}
