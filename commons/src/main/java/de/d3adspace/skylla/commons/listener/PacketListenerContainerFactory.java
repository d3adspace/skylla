package de.d3adspace.skylla.commons.listener;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.d3adspace.skylla.commons.packet.PacketContext;
import de.d3adspace.skylla.protocol.packet.Packet;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class PacketListenerContainerFactory {

  private PacketListenerContainerFactory() {
  }

  public static PacketListenerContainerFactory create() {
    return new PacketListenerContainerFactory();
  }

  /**
   * Create container by the instance of an object that contains listener methods. This will scan
   * all methods for the {@link PacketListener} annotation with the signature of:
   *
   * <code>void onPacket({@link PacketContext}, {@link Packet})</code>
   *
   * @param listenerInstance The listener instance.
   * @return The container able of receiving events.
   */
  public PacketListenerContainer fromListenerInstance(Object listenerInstance) {
    Preconditions.checkNotNull(listenerInstance);

    Class<?> instanceClass = listenerInstance.getClass();
    Method[] declaredMethods = instanceClass.getDeclaredMethods();

    Map<Class<? extends Packet>, List<Method>> listenerMethods = Maps.newConcurrentMap();

    Arrays.stream(declaredMethods).forEach(method -> {
      boolean annotationPresent = method.isAnnotationPresent(PacketListener.class);
      if (!annotationPresent) {
        return;
      }

      // We can abort if the parameter amount is not 2 or wrong types (PacketContext, Packet)
      Class<?>[] parameterTypes = method.getParameterTypes();
      if (parameterTypes.length != 2) {
        return;
      }

      Class<?> firstParameterType = parameterTypes[0];
      Class<?> secondParameterType = parameterTypes[1];
      if (!PacketContext.class.isAssignableFrom(firstParameterType) || !Packet.class
          .isAssignableFrom(secondParameterType)) {
        return;
      }

      // Add packet listener method
      Class<? extends Packet> packetClass = (Class<? extends Packet>) secondParameterType;
      List<Method> methods = listenerMethods.computeIfAbsent(packetClass, k ->
          Lists.newArrayList());

      methods.add(method);
    });

    return new ReflectionPacketListenerContainer(listenerInstance, listenerMethods);
  }
}
