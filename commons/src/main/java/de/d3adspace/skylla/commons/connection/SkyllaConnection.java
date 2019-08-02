/*
 * Copyright (c) 2017 - 2019 D3adspace
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.d3adspace.skylla.commons.connection;

import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.context.SkyllaPacketContext;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.util.Arrays;

/**
 * Handling Netty connections.
 *
 * @author Nathalie O'Neill (nathalie@d3adspace.de)
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SkyllaConnection extends SimpleChannelInboundHandler<SkyllaPacket> {

  /**
   * The underlying netty channel.
   */
  private final Channel channel;

  /**
   * The protocol for communication.
   */
  private final Protocol protocol;

  /**
   * Create a new connection wrapper.
   *
   * @param channel The channel.
   * @param protocol The protocol.
   */
  public SkyllaConnection(Channel channel, Protocol protocol) {

    this.channel = channel;
    this.protocol = protocol;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, SkyllaPacket packet) {

    SkyllaPacketContext packetContext = new SkyllaPacketContext(this);
    protocol.handlePacket(packetContext, packet);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    /*
     * Handle IO Exception on disconnect.
     */
    if (cause instanceof IOException) {
      channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
      return;
    }

    /*
     * Print stacktrace if it isnt an IO Exception
     */
    cause.printStackTrace();
  }

  /**
   * Send one or more packets to the server.
   *
   * @param packets The packets.
   */
  public void sendPackets(SkyllaPacket... packets) {

    if (packets.length == 1) {
      channel.writeAndFlush(packets[0]);
      return;
    }

    Arrays.stream(packets).forEach(channel::write);

    channel.flush();
  }
}
