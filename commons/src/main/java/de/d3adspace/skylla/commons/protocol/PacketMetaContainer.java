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

package de.d3adspace.skylla.commons.protocol;

import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacketMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for meta to prevent lazy loading of meta.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
class PacketMetaContainer {

    /**
     * The underlying Map
     */
    private final Map<Class<?>, SkyllaPacketMeta> metadata;

    /**
     * Create a new container by a map of known meta data.
     *
     * @param metadata The metadata.
     */
    private PacketMetaContainer(Map<Class<?>, SkyllaPacketMeta> metadata) {

        this.metadata = metadata;
    }

    /**
     * Create an empty container
     */
    PacketMetaContainer() {

        this(new HashMap<>());
    }

    /**
     * Retrieve packet meta by a packet instance.
     *
     * @param skyllaPacket The packet.
     * @return The meta.
     */
    SkyllaPacketMeta getPacketMeta(SkyllaPacket skyllaPacket) {

        return getPacketMeta(skyllaPacket.getClass());
    }

    /**
     * Get meta by a packet class.
     *
     * @param packetClazz The packet class.
     * @return The meta.
     */
    SkyllaPacketMeta getPacketMeta(Class<?> packetClazz) {

        return metadata.computeIfAbsent(packetClazz, k -> packetClazz.getAnnotation(SkyllaPacketMeta.class));
    }

    /**
     * Presave a meta to prevent lazy loading.
     *
     * @param packetClazz The packet class.
     * @param meta        The meta.
     */
    void registerPacketMeta(Class<?> packetClazz, SkyllaPacketMeta meta) {

        metadata.put(packetClazz, meta);
    }
}
