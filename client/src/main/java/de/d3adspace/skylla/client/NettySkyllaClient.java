package de.d3adspace.skylla.client;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import de.d3adspace.constrictor.netty.NettyUtils;
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

public final class NettySkyllaClient implements SkyllaClient {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final String serverHost;
  private final int serverPort;
  private final Protocol protocol;

  private Channel channel;

  private NettySkyllaClient(String serverHost, int serverPort, Protocol protocol) {
    this.serverHost = serverHost;
    this.serverPort = serverPort;
    this.protocol = protocol;
  }

  @Override
  public void connect() {
    EventLoopGroup workerGroup = NettyUtils.createWorkerGroup();

    ChannelInitializer<SocketChannel> channelInitializer = ClientChannelInitializer
      .forProtocol(protocol);

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

    private ClientChannelInitializer(Protocol protocol) {
      this.protocol = protocol;
    }

    private static ChannelInitializer<SocketChannel> forProtocol(Protocol protocol) {
      Preconditions.checkNotNull(protocol);
      return new ClientChannelInitializer(protocol);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      ChannelPipeline pipeline = socketChannel.pipeline();

      PacketCodec packetCodec = PacketCodec.forProtocol(protocol);

      pipeline.addLast(new LengthFieldBasedFrameDecoder(32768, 4, 4));
      pipeline.addLast(packetCodec);
      pipeline.addLast(new LengthFieldPrepender(4));
    }
  }

  public static class Builder {
    private String serverHost;
    private int serverPort;
    private Protocol protocol;

    private Builder(String serverHost, int serverPort, Protocol protocol) {
      this.serverHost = serverHost;
      this.serverPort = serverPort;
      this.protocol = protocol;
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

    public NettySkyllaClient build() {
      return new NettySkyllaClient(serverHost, serverPort, protocol);
    }
  }
}
