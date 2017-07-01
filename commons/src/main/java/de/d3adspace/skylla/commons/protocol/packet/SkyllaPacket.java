package de.d3adspace.skylla.commons.protocol.packet;

import de.d3adspace.skylla.commons.buffer.SkyllaBuffer;

/**
 * @author Nathalie0hneHerz
 */
public abstract class SkyllaPacket {
	
	public abstract void write(SkyllaBuffer buffer);
	
	public abstract void read(SkyllaBuffer buffer);
}
