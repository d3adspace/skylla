# skylla

A simple network framework based on packet events. Example: 

# Installation / Usage

- Install [Maven](http://maven.apache.org/download.cgi)
- Clone this repo
- Installh: ```mvn clean install```

**Maven dependencies**

_Client:_
```xml
<dependency>
    <groupId>de.d3adspace</groupId>
    <artifactId>skylla-client</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

_Server:_
```xml
<dependency>
    <groupId>de.d3adspace</groupId>
    <artifactId>skylla-server</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

_Commons:_
```xml
<dependency>
    <groupId>de.d3adspace</groupId>
    <artifactId>skylla-commons</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

# Example

```java
package de.d3adspace.example;

import de.d3adspace.skylla.commons.buffer.SkyllaBuffer;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacketMeta;

/**
 * @author Nathalie0hneHerz
 */
@SkyllaPacketMeta(id = 0)
public class ChatPacket extends SkyllaPacket {
	
	private String sender;
	private String message;
	
	public ChatPacket(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	
	public ChatPacket() {
	}
	
	@Override
	public void write(SkyllaBuffer buffer) {
		buffer.writeString(sender);
		buffer.writeString(message);
	}
	
	@Override
	public void read(SkyllaBuffer buffer) {
		sender = buffer.readString();
		message = buffer.readString();
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	@Override
	public String toString() {
		return "ChatPacket{" +
			"sender='" + sender + '\'' +
			", message='" + message + '\'' +
			'}';
	}
}

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
		public void onPacketChat(SkyllaConnection skyllaConnection, ChatPacket chatPacket) {
			System.out.println("[Server] received: " + chatPacket);
			
			skyllaConnection.sendPackets(chatPacket);
		}
	}
}

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
		public void onPacketChat(SkyllaConnection skyllaConnection, ChatPacket chatPacket) {
			System.out.println("[Client] received: " + chatPacket);
		}
	}
}


```

