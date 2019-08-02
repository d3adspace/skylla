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

package de.d3adspace.example;

import de.d3adspace.skylla.client.SkyllaClient;
import de.d3adspace.skylla.client.SkyllaClientFactory;
import de.d3adspace.skylla.commons.config.SkyllaConfig;
import de.d3adspace.skylla.commons.protocol.Protocol;
import de.d3adspace.skylla.commons.protocol.context.SkyllaPacketContext;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandler;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandlerMethod;

/**
 * @author Nathalie O'Neill (nathalie@d3adspace.de)
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SkyllaClientExample {

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
