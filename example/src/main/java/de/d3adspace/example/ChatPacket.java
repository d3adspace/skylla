/*
 * Copyright (c) 2017 D3adspace
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

import de.d3adspace.skylla.commons.buffer.SkyllaBuffer;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacketMeta;

/**
 * Representing a chat message.
 *
 * @author Nathalie O'Neill <nathalie@d3adspace.de>
 */
@SkyllaPacketMeta(id = 0)
public class ChatPacket implements SkyllaPacket {

    /**
     * The sender of the message.
     */
    private String sender;

    /**
     * The message sent by the sender.
     */
    private String message;

    /**
     * Create a new packet.
     *
     * @param sender  The sender.
     * @param message The message.
     */
    public ChatPacket(String sender, String message) {

        this.sender = sender;
        this.message = message;
    }

    /**
     * Packet Constructor.
     */
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

    /**
     * Get the message by the sender.
     *
     * @return The message.
     */
    public String getMessage() {

        return message;
    }

    /**
     * Set the message by the sender.
     *
     * @param message The message.
     */
    public void setMessage(String message) {

        this.message = message;
    }

    /**
     * Get the sender of the message.
     *
     * @return The sender.
     */
    public String getSender() {

        return sender;
    }

    /**
     * Set the sender of the message.
     *
     * @param sender The sender.
     */
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
