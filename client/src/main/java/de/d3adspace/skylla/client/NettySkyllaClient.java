package de.d3adspace.skylla.client;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.flogger.FluentLogger;
import de.d3adspace.constrictor.netty.NettyUtils;
import de.d3adspace.skylla.commons.listener.CompositePacketListenerContainer;
import de.d3adspace.skylla.commons.listener.PacketListenerContainer;
import de.d3adspace.skylla.commons.listener.PacketListenerContainerFactory;
import de.d3adspace.skylla.commons.netty.NettyPacketInboundHandler;
import de.d3adspace.skylla.protocol.PacketCodec;
import de.d3adspace.skylla.protocol.Protocol;
import de.d3adspace.skylla.protocol.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.util.List;
import java.util.stream.Collectors;

public final class NettySkyllaClient implements SkyllaClient {

  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final PacketListenerContainer packetListenerContainer;
  private final String serverHost;
  private final int serverPort;
  private final Protocol protocol;

  private Channel channel;

  private NettySkyllaClient(
      PacketListenerContainer packetListenerContainer,
      String serverHost,
      int serverPort,
      Protocol protocol
  ) {
    this.packetListenerContainer = packetListenerContainer;
    this.serverHost = serverHost;
    this.serverPort = serverPort;
    this.protocol = protocol;
  }

  public static Builder newBuilder() {
    return new Builder("localhost", 8080, Protocol.empty(), Lists.newArrayList());
  }

  @Override
  public void connect() {
    var bootstrap = createBootstrap();
    var channelFuture = bootstrap
        .connect(serverHost, serverPort)
        .syncUninterruptibly();
    addFutureListener(channelFuture);
    channel = channelFuture.channel();
  }

  private void addFutureListener(ChannelFuture channelFuture) {
    channelFuture.addListener((ChannelFutureListener) future -> {
      boolean success = future.isSuccess();
      if (success) {
        logger.atInfo()
            .log("Server started successfully.");
      } else {
        logger.atSevere()
            .withCause(future.cause())
            .log("Server couldn't start");
      }
    });
  }

  private Bootstrap createBootstrap() {
    var workerGroup = NettyUtils.createWorkerGroup();

    var channelInitializer = ClientChannelInitializer
        .forProtocolAndListener(protocol, packetListenerContainer);

    return new Bootstrap()
        .group(workerGroup)
        .remoteAddress(serverHost, serverPort)
        .handler(channelInitializer);
  }

  @Override
  public boolean isConnected() {
    return channel != null && channel.isActive();
  }

  @Override
  public void disconnect() {
    if (channel == null) {
      return;
    }

    channel.close();
  }

  @Override
  public void sendPacket(Packet packet) {
    Preconditions.checkNotNull(packet);

    channel.writeAndFlush(packet);
  }

  private static final class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Protocol protocol;
    private final PacketListenerContainer packetListenerContainer;

    private ClientChannelInitializer(
        Protocol protocol,
        PacketListenerContainer packetListenerContainer
    ) {
      this.protocol = protocol;
      this.packetListenerContainer = packetListenerContainer;
    }

    private static ChannelInitializer<SocketChannel> forProtocolAndListener(
        Protocol protocol,
        PacketListenerContainer packetListenerContainer
    ) {
      Preconditions.checkNotNull(protocol);
      Preconditions.checkNotNull(packetListenerContainer);
      return new ClientChannelInitializer(protocol, packetListenerContainer);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      var pipeline = socketChannel.pipeline();

      var packetCodec = PacketCodec.forProtocol(protocol);

      pipeline.addLast(new LengthFieldBasedFrameDecoder(32768, 4, 4));
      pipeline.addLast(packetCodec);
      pipeline.addLast(new LengthFieldPrepender(4));

      var inboundHandler = NettyPacketInboundHandler.withListenerContainer(packetListenerContainer);
      pipeline.addLast(inboundHandler);
    }
  }

  public static class Builder {

    private final List<Object> packetListenerInstances;
    private String serverHost;
    private int serverPort;
    private Protocol protocol;

    private Builder(
        String serverHost,
        int serverPort,
        Protocol protocol,
        List<Object> packetListenerInstances
    ) {
      this.serverHost = serverHost;
      this.serverPort = serverPort;
      this.protocol = protocol;
      this.packetListenerInstances = packetListenerInstances;
    }

    /**
     * Set the server host to a certain domain or ip.
     *
     * @param serverHost The server host.
     * @return The builder instance.
     */
    public Builder withServerHost(String serverHost) {
      Preconditions.checkNotNull(serverHost);
      this.serverHost = serverHost;
      return this;
    }

    /**
     * Set the server port.
     *
     * @param serverPort The server port.
     * @return The builder instance.
     */
    public Builder withServerPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }

    /**
     * Set the server protocol.
     *
     * @param protocol The protocol.
     * @return The protocol.
     */
    public Builder withProtocol(Protocol protocol) {
      Preconditions.checkNotNull(protocol);
      this.protocol = protocol;
      return this;
    }

    /**
     * Add a listener object.
     *
     * @param listenerInstance The listener instancen.
     * @return The builder instance.
     */
    public Builder withListener(Object listenerInstance) {
      packetListenerInstances.add(listenerInstance);
      return this;
    }

    /**
     * Build the server instance. This will first scan the given listener instances for appropriate
     * listener methods.
     *
     * @return The server instance.
     */
    public NettySkyllaClient build() {
      var packetListenerContainerFactory = PacketListenerContainerFactory.create();
      var packetListenerContainers = packetListenerInstances.stream()
          .map(packetListenerContainerFactory::fromListenerInstance)
          .collect(Collectors.toList());

      var packetListenerContainer = CompositePacketListenerContainer
          .withListeners(packetListenerContainers);
      return new NettySkyllaClient(packetListenerContainer, serverHost, serverPort, protocol);
    }
  }
}
