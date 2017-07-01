package de.d3adspace.example;

import de.d3adspace.skylla.commons.config.SkyllaConfig;
import de.d3adspace.skylla.commons.config.SkyllaConfigBuilder;
import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandler;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandlerMethod;
import de.d3adspace.skylla.server.SkyllaServer;
import de.d3adspace.skylla.server.SkyllaServerFactory;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaSeverExample {
	
	public static void main(String[] args) {
		Protocol protocol = new Protocol();
		protocol.registerPacket(ChatPacket.class);
		protocol.registerListener(new ServerPacketHandlerExample());
		
		SkyllaConfig config = new SkyllaConfigBuilder()
			.setServerHost("localhost")
			.setServerPort(1337)
			.setProtocol(protocol)
			.createSkyllaConfig();
		
		SkyllaServer skyllaServer = SkyllaServerFactory.createSkyllaServer(config);
		skyllaServer.start();
	}
	
	public static class ServerPacketHandlerExample implements PacketHandler {
		
		@PacketHandlerMethod
		public void onPacketChat(ChatPacket chatPacket) {
			System.out.println("[Server] received: " + chatPacket);
		}
	}
}
