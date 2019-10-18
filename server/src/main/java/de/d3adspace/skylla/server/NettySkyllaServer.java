package de.d3adspace.skylla.server;

import com.google.common.base.Preconditions;
import com.google.common.flogger.FluentLogger;
import de.d3adspace.constrictor.netty.NettyUtils;
import de.d3adspace.skylla.protocol.PacketCodec;
import de.d3adspace.skylla.protocol.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public final class NettySkyllaServer implements SkyllaServer {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final String host;
  private final int port;
  private final Protocol protocol;

  private Channel channel;

  private NettySkyllaServer(String host, int port, Protocol protocol) {
    this.host = host;
    this.port = port;
    this.protocol = protocol;
  }

  public static Builder newBuilder() {
    return new Builder("localhost", 8080, Protocol.empty());
  }

  @Override
  public void start() {
    EventLoopGroup bossGroup = NettyUtils.createBossGroup();
    EventLoopGroup workerGroup = NettyUtils.createWorkerGroup();

    ChannelInitializer<ServerSocketChannel> channelInitializer = ServerChannelInitializer
      .forProtocol(protocol);

    Class<? extends ServerSocketChannel> serverSocketChannel = NettyUtils.getServerSocketChannel();
    ServerBootstrap serverBootstrap = new ServerBootstrap()
      .group(bossGroup, workerGroup)
      .channel(serverSocketChannel)
      .childHandler(channelInitializer);

    ChannelFuture channelFuture = serverBootstrap.bind(host, port).syncUninterruptibly();
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
  public boolean isRunning() {
    return channel != null && channel.isActive();
  }

  @Override
  public void stop() {
    if (channel == null) {
      return;
    }

    channel.close();
  }

  /**
   * Channel initializer for the server that will add a length field wrapped packet codec.
   */
  private static class ServerChannelInitializer extends ChannelInitializer<ServerSocketChannel> {
    private final Protocol protocol;

    private ServerChannelInitializer(Protocol protocol) {
      this.protocol = protocol;
    }

    private static ChannelInitializer<ServerSocketChannel> forProtocol(Protocol protocol) {
      Preconditions.checkNotNull(protocol);
      return new ServerChannelInitializer(protocol);
    }

    @Override
    protected void initChannel(ServerSocketChannel serverSocketChannel) throws Exception {
      ChannelPipeline pipeline = serverSocketChannel.pipeline();

      PacketCodec packetCodec = PacketCodec.forProtocol(protocol);

      pipeline.addLast(new LengthFieldBasedFrameDecoder(32768, 4, 4));
      pipeline.addLast(packetCodec);
      pipeline.addLast(new LengthFieldPrepender(4));
    }
  }

  public static class Builder {
    private String host;
    private int port;
    private Protocol protocol;

    private Builder(String host, int port, Protocol protocol) {
      this.host = host;
      this.port = port;
      this.protocol = protocol;
    }

    public Builder withHost(String host) {
      Preconditions.checkNotNull(host);
      this.host = host;
      return this;
    }

    public Builder withPort(int port) {
      this.port = port;
      return this;
    }

    public Builder withProtocol(Protocol protocol) {
      Preconditions.checkNotNull(protocol);
      this.protocol = protocol;
      return this;
    }

    public NettySkyllaServer build() {
      return new NettySkyllaServer(host, port, protocol);
    }
  }
}
