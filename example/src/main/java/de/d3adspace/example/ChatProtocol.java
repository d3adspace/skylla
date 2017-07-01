package de.d3adspace.example;

import de.d3adspace.skylla.commons.protocol.Protocol;

/**
 * @author Nathalie0hneHerz
 */
public class ChatProtocol extends Protocol {
	
	public ChatProtocol() {
		
		this.registerPacket(ChatPacket.class);
	}
}
