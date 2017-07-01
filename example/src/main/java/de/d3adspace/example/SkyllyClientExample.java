package de.d3adspace.example;

import de.d3adspace.skylla.client.SimpleSkyllaClient;
import de.d3adspace.skylla.client.SkyllaClient;
import de.d3adspace.skylla.client.SkyllaClientFactory;
import de.d3adspace.skylla.commons.config.SkyllaConfig;
import de.d3adspace.skylla.commons.config.SkyllaConfigBuilder;
import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandler;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandlerMethod;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllyClientExample {
	
	public static void main(String[] args) {
		Protocol protocol = new Protocol();
		protocol.registerPacket(ChatPacket.class);
		protocol.registerListener(new ClientPacketHandlerExample());
		
		SkyllaConfig config = new SkyllaConfigBuilder()
			.setServerHost("localhost")
			.setServerPort(1337)
			.setProtocol(protocol)
			.createSkyllaConfig();
		
		SkyllaClient skyllaClient = SkyllaClientFactory.createSkyllaClient(config);
		skyllaClient.connect();
		
		skyllaClient.sendPacket(new ChatPacket("[Sender #1]", "Hallo Welt!"));
	}
	
	public static class ClientPacketHandlerExample implements PacketHandler {
		
		@PacketHandlerMethod
		public void onPacketChat(ChatPacket chatPacket) {
			System.out.println("[Client] received: " + chatPacket);
		}
	}
}
