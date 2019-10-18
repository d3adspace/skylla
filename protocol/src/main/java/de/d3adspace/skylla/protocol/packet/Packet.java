package de.d3adspace.skylla.protocol.packet;

import de.d3adspace.skylla.protocol.buffer.SkyllaBuffer;

public interface Packet {

  void read(SkyllaBuffer buffer);

  void write(SkyllaBuffer buffer);
}
