# skylla

A simple network framework based on packet events. First taken as a PROC we decided to publish this and develop some more features in the future in order to provider a lightweight but powerful framework for little network applications that want to outsource their network handling parts. 

# Installation / Usage

- Install [Maven](http://maven.apache.org/download.cgi)
- Clone this repo
- Instal: ```mvn clean install```

**Maven dependencies**

_Repositories_:
```xml
<repositories>

    <!-- Klauke Enterprises Releases -->
    <repository>
        <id>klauke-enterprises-maven-releases</id>
        <name>Klauke Enterprises Maven Releases</name>
        <url>https://repository.klauke-enterprises.com/repository/maven-releases/</url>
    </repository>
	
    <!-- Klauke Enterprises Snapshots -->
    <repository>
        <id>klauke-enterprises-maven-snapshots</id>
        <name>Klauke Enterprises Maven Snapshots</name>
        <url>https://repository.klauke-enterprises.com/repository/maven-snapshots/</url>
    </repository>
</repositories>
```

_Client:_
```xml
<dependency>
    <groupId>de.d3adspace.skylla</groupId>
    <artifactId>skylla-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

_Server:_
```xml
<dependency>
    <groupId>de.d3adspace.skylla</groupId>
    <artifactId>skylla-server</artifactId>
    <version>1.0.0</version>
</dependency>
```

_Commons:_
```xml
<dependency>
    <groupId>de.d3adspace.skylla</groupId>
    <artifactId>skylla-commons</artifactId>
    <version>1.0.0</version>
</dependency>
```

# Example

```java
/**
 * @author Nathalie0hneHerz
 */
@SkyllaPacketMeta(id = 0)
public class ChatPacket implements SkyllaPacket {

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

/**
 * @author Nathalie0hneHerz
 */
public class ChatProtocol extends Protocol {
	
	public ChatProtocol() {
		
		this.registerPacket(ChatPacket.class);
	}
}

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaSeverExample {
	
	public static void main(String[] args) {
		Protocol protocol = new Protocol();
		protocol.registerPacket(ChatPacket.class);
		protocol.registerListener(new ServerPacketHandlerExample());
		
		SkyllaConfig config = SkyllaConfig.newBuilder()
			.setServerHost("localhost")
			.setServerPort(1337)
			.setProtocol(protocol)
			.createSkyllaConfig();
		
		SkyllaServer skyllaServer = SkyllaServerFactory.createSkyllaServer(config);
		skyllaServer.start();
	}
	
	public static class ServerPacketHandlerExample implements PacketHandler {
		
		@PacketHandlerMethod
		public void onPacketChat(SkyllaPacketContext packetContext, ChatPacket chatPacket) {
			System.out.println("[Server] received: " + chatPacket);
			
			skyllaConnection.sendPackets(chatPacket);
		}
	}
}

/**
 * @author Nathalie0hneHerz
 */
public class SkyllyClientExample {
	
	public static void main(String[] args) {
		Protocol protocol = new Protocol();
		protocol.registerPacket(ChatPacket.class);
		protocol.registerListener(new ClientPacketHandlerExample());
		
		SkyllaConfig config = SkyllaConfig.newBuilder()
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
		public void onPacketChat(SkyllaPacketContext packetContext, ChatPacket chatPacket) {
			System.out.println("[Client] received: " + chatPacket);
		}
	}
}
```

