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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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
    String serverHost, int serverPort, Protocol protocol) {
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
    EventLoopGroup workerGroup = NettyUtils.createWorkerGroup();

    ChannelInitializer<SocketChannel> channelInitializer = ClientChannelInitializer
      .forProtocolAndListener(protocol, packetListenerContainer);

    Bootstrap bootstrap = new Bootstrap()
      .group(workerGroup)
      .remoteAddress(serverHost, serverPort)
      .handler(channelInitializer);

    ChannelFuture channelFuture = bootstrap.connect(serverHost, serverPort)
      .syncUninterruptibly();

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

    channel = channelFuture.channel();
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

  private static final class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final Protocol protocol;
    private final PacketListenerContainer packetListenerContainer;

    private ClientChannelInitializer(Protocol protocol,
      PacketListenerContainer packetListenerContainer) {
      this.protocol = protocol;
      this.packetListenerContainer = packetListenerContainer;
    }

    private static ChannelInitializer<SocketChannel> forProtocolAndListener(Protocol protocol, PacketListenerContainer packetListenerContainer) {
      Preconditions.checkNotNull(protocol);
      Preconditions.checkNotNull(packetListenerContainer);
      return new ClientChannelInitializer(protocol, packetListenerContainer);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      ChannelPipeline pipeline = socketChannel.pipeline();

      PacketCodec packetCodec = PacketCodec.forProtocol(protocol);

      pipeline.addLast(new LengthFieldBasedFrameDecoder(32768, 4, 4));
      pipeline.addLast(packetCodec);
      pipeline.addLast(new LengthFieldPrepender(4));

      NettyPacketInboundHandler inboundHandler = NettyPacketInboundHandler.withListenerContainer(packetListenerContainer);
      pipeline.addLast(inboundHandler);
    }
  }

  public static class Builder {
    private String serverHost;
    private int serverPort;
    private Protocol protocol;
    private final List<Object> packetListenerInstances;

    private Builder(String serverHost, int serverPort, Protocol protocol,
      List<Object> packetListenerInstances) {
      this.serverHost = serverHost;
      this.serverPort = serverPort;
      this.protocol = protocol;
      this.packetListenerInstances = packetListenerInstances;
    }

    public Builder withServerHost(String serverHost) {
      Preconditions.checkNotNull(serverHost);
      this.serverHost = serverHost;
      return this;
    }

    public Builder withServerPort(int serverPort) {
      this.serverPort = serverPort;
      return this;
    }

    public Builder withProtocol(Protocol protocol) {
      Preconditions.checkNotNull(protocol);
      this.protocol = protocol;
      return this;
    }

    public Builder withListener(Object listenerInstance) {
      packetListenerInstances.add(listenerInstance);
      return this;
    }

    public NettySkyllaClient build() {
      PacketListenerContainerFactory packetListenerContainerFactory = PacketListenerContainerFactory
        .create();
      List<PacketListenerContainer> packetListenerContainers = packetListenerInstances.stream()
        .map(packetListenerContainerFactory::fromListenerInstance).collect(
          Collectors.toList());

      PacketListenerContainer packetListenerContainer = CompositePacketListenerContainer.withListeners(packetListenerContainers);
      return new NettySkyllaClient(packetListenerContainer, serverHost, serverPort, protocol);
    }
  }
}
