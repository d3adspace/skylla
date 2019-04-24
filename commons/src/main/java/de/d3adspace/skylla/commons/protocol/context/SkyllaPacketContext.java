package de.d3adspace.skylla.commons.protocol.context;

import de.d3adspace.skylla.commons.connection.SkyllaConnection;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;

public class SkyllaPacketContext {

    private final SkyllaConnection connection;

    public SkyllaPacketContext(SkyllaConnection connection) {

        this.connection = connection;
    }

    /**
     * Send a packet as an answer.
     *
     * @param skyllaPacket The packet.
     */
    public void answer(SkyllaPacket skyllaPacket) {

        connection.sendPackets(skyllaPacket);
    }
}
