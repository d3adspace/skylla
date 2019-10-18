package de.d3adspace.skylla.commons.listener;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import de.d3adspace.skylla.commons.packet.PacketContext;
import de.d3adspace.skylla.protocol.packet.Packet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public final class ReflectionPacketListenerContainer implements PacketListenerContainer {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final Object listenerHandle;
  private final Map<Class<? extends Packet>, List<Method>> listenerMethods;

  ReflectionPacketListenerContainer(Object listenerHandle,
    Map<Class<? extends Packet>, List<Method>> listenerMethods) {
    this.listenerHandle = listenerHandle;
    this.listenerMethods = listenerMethods;
  }

  @Override
  public void callEvent(PacketContext packetContext, Packet packet) {
    Preconditions.checkNotNull(packetContext);
    Preconditions.checkNotNull(packet);

    Class<? extends Packet> packetClass = packet.getClass();
    List<Method> methods = listenerMethods.get(packetClass);

    if (methods == null || methods.isEmpty()) {
      return;
    }

    methods.forEach(method -> {
      try {
        method.invoke(listenerHandle, packetContext, packet);
      } catch (IllegalAccessException | InvocationTargetException e) {
        logger.atSevere()
          .withCause(e)
          .log("Couldn't call callback method");
      }
    });
  }
}
