package de.d3adspace.skylla.protocol.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketMeta {

  /**
   * The id of the packet used to identify it.
   *
   * @return The id.
   */
  int id();
}
