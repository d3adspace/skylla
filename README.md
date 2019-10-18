# skylla

A simple network framework based on packet events. First taken as a PROC we decided to publish this and develop some more features in the future in order to provider a lightweight but powerful framework for little network applications that want to outsource their network handling parts. 

Skylla uses [constrictor](https://github.com/d3adspace/constrictor) networking utils.

# Installation / Usage

## Build from Source

- Install [Maven](http://maven.apache.org/download.cgi)
- Clone this repo
- Instal: ```mvn clean install```

## Maven Repository

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
</repositories>
```

_Dependencies_:

```xml
<dependency>
  <groupId>de.d3adspace.skylla</groupId>
  <artifactId>skylla-client</artifactId>
  <version>4.0.0</version>
  <scope>compile</scope>
</dependency>
```

```xml
<dependency>
  <groupId>de.d3adspace.skylla</groupId>
  <artifactId>skylla-server</artifactId>
  <version>4.0.0</version>
  <scope>compile</scope>
</dependency>
```

# Example

## Server and Client instantiation

```java
public class ServerExample {
  public static void main(String[] args) {
    
    // The shared protocol
    Protocol protocol = Protocol.newBuilder()
      .withPacket(ChatPacket.class)
      .build();
    
    
    // Server
    ExampleServerListener exampleServerListener = ExampleServerListener.create();
    SkyllaServer server = NettySkyllaServer.newBuilder()
      .withServerHost("localhost")
      .withServerPort(8080)
      .withProtocol(protocol)
      .withListener(exampleServerListener)
      .build();

    server.start();

    // Client
    ExampleClientListener exampleClientListener = ExampleClientListener.create();
    SkyllaClient client = NettySkyllaClient.newBuilder()
      .withServerHost("localhost")
      .withServerPort(8080)
      .withProtocol(protocol)
      .withListener(exampleClientListener)
      .build();

    client.connect();
      
    ChatPacket chatPacket = ChatPacket.of("Client", "Hey Server!");
    client.sendPacket(chatPacket);
  }
  
  public static final class ExampleClientListener {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  
    private ExampleClientListener() {
    }

    private ExampleClientListener create() {
      return new ExampleClientListener();
    }   

    @PacketListener
    public void onChatPacket(PacketContext packetContext, ChatPacket chatPacket) {
      logger.atInfo().log("%s: %s", chatPacket.getName(), chatPacket.getMessage());
    }
  }

  public static final class ExampleServerListener {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  
    private ExampleServerListener() {
    }

    private ExampleServerListener create() {
      return new ExampleServerListener();
    }   

    @PacketListener
    public void onChatPacket(PacketContext packetContext, ChatPacket chatPacket) {
      ChatPacket answer = ChatPacket.of("Server", "Hey Client!");
      packetContext.resume(answer);
    }
  }
}
```

## Packet definition

```java
@PacketMeta(id = 1)
public final class ChatPacket implements Packet {
  private String name;
  private String message;

  public ChatPacket() {
  }

  private ChatPacket(String name, String message) {
    this.name = name;
    this.message = message;
  }

  public static ChatPacket of(String name, String message) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(message);

    return new ChatPacket(name, message);
  }

  @Override
  public void read(SkyllaBuffer buffer) {
    name = buffer.readString();
    message = buffer.readString();
  }

  @Override
  public void write(SkyllaBuffer buffer) {
    buffer.writeString(name);
    buffer.writeString(message);
  }

  public String getMessage() {
    return message;
  }

  public String getName() {
    return name;
  }
}
```
