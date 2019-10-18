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

# Example

## Server

```java
public class ServerExample {

  public static void main(String[] args) {

    Protocol protocol = Protocol.newBuilder()
      .withPacket(ChatPacket.class)
      .build();

    SkyllaServer server = NettySkyllaServer.newBuilder()
      .withHost("localhost")
      .withPort(8080)
      .withProtocol(protocol)
      .build();

    server.start();
  }

  @PacketMeta(id = 1)
  public static final class ChatPacket implements Packet {
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
}
```

