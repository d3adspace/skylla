package de.d3adspace.skylla.client;

import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;

/**
 * @author Nathalie0hneHerz
 */
public interface SkyllaClient {
	
	void connect();
	
	void disconnect();
	
	void sendPacket(SkyllaPacket packet);
}
