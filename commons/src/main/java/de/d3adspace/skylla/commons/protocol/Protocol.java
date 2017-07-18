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

package de.d3adspace.skylla.commons.protocol;

import de.d3adspace.skylla.commons.connection.SkyllaConnection;
import de.d3adspace.skylla.commons.protocol.handler.HandlerContainer;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandler;
import de.d3adspace.skylla.commons.protocol.handler.PacketHandlerMethod;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacketMeta;
import de.d3adspace.skylla.commons.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Structuring network communication.
 *
 * @author Nathalie0hneHerz, Felix 'SasukeKawaii' Klauke
 */
public class Protocol {

    /**
     * Registry for all known packets.
     */
    private final Map<Byte, Class<? extends SkyllaPacket>> registeredPackets;

    /**
     * Registry for all known Handlers.
     */
    private final Map<Class<? extends SkyllaPacket>, HandlerContainer> packetHandlers;

    /**
     * The container for all the packet meta
     */
    private final PacketMetaContainer metaContainer;

    /**
     * Logger for all protocol actions.
     */
    private final Logger logger;

    /**
     * Create a new Protocol.
     */
    public Protocol() {
        this.registeredPackets = new HashMap<>();
        this.packetHandlers = new HashMap<>();
        this.metaContainer = new PacketMetaContainer();
        this.logger = LoggerFactory.getLogger(Protocol.class);
    }

    /**
     * Register a new packet.
     *
     * @param packetClazz The packet clazz.
     */
    public void registerPacket(Class<? extends SkyllaPacket> packetClazz) {
        if (packetClazz == null) {
            throw new IllegalArgumentException("packetClazz cannot be null");
        }

        if (!ClassUtils.hasNoArgsConstructor(packetClazz)) {
            throw new IllegalArgumentException(
                    packetClazz + " does not have a no args constructor.");
        }

        SkyllaPacketMeta meta = packetClazz.getAnnotation(SkyllaPacketMeta.class);

        if (meta == null) {
            throw new IllegalArgumentException(
                    packetClazz + " does not have @SkyllaPacketMeta Annotation.");
        }

        if (this.registeredPackets.containsKey(meta.id())) {
            throw new IllegalArgumentException("I already know a packet with the id " + meta.id());
        }

        this.metaContainer.registerPacketMeta(packetClazz, meta);

        this.logger.info(
                "Registering new Packet: " + packetClazz.getSimpleName() + " with id " + meta.id());

        this.registeredPackets.put(meta.id(), packetClazz);
    }

    /**
     * Unregister a new packet.
     *
     * @param packetClazz The packet clazz.
     */
    public void unregisterPacket(Class<? extends SkyllaPacket> packetClazz) {
        if (packetClazz == null) {
            throw new IllegalArgumentException("packetClazz cannot be null");
        }

        SkyllaPacketMeta meta = this.metaContainer.getPacketMeta(packetClazz);

        if (meta == null) {
            throw new IllegalArgumentException(
                    packetClazz + " does not have @SkyllaPacketMeta Annotation.");
        }

        registeredPackets.remove(meta.id());
    }

    /**
     * Create a new packet by id.
     *
     * @param packetId The id.
     * @return The packet.
     */
    public SkyllaPacket createPacket(byte packetId) {
        Class<? extends SkyllaPacket> packetClazz = registeredPackets.get(packetId);

        if (packetClazz == null) {
            throw new IllegalStateException("No packet with id " + packetId);
        }

        SkyllaPacket packet = null;

        try {
            packet = packetClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            this.logger.error("Unable to create packet with id " + packetId, e);
        }

        return packet;
    }

    /**
     * Register a listener.
     *
     * @param packetHandler The handler.
     */
    public void registerListener(PacketHandler packetHandler) {
        if (packetHandler == null) {
            throw new IllegalArgumentException("packetHandler cannot be null");
        }

        Method[] declaredMethods = packetHandler.getClass().getDeclaredMethods();

        for (Method method : declaredMethods) {

            if (method.isAnnotationPresent(PacketHandlerMethod.class)
                    && method.getParameterCount() == 2
                    && SkyllaPacket.class.isAssignableFrom(method.getParameterTypes()[1])) {

                Class<? extends SkyllaPacket> packetClazz = (Class<? extends SkyllaPacket>) method
                        .getParameterTypes()[1];

                if (!this.packetHandlers.containsKey(packetClazz)) {
                    this.packetHandlers.put(packetClazz, new HandlerContainer());
                }

                this.packetHandlers.get(packetClazz).registerListenerMethod(packetHandler, method);
            }
        }
    }

    /**
     * Unregister a listener.
     *
     * @param packetHandler The handler.
     */
    public void unregisterListener(PacketHandler packetHandler) {
        if (packetHandler == null) {
            throw new IllegalArgumentException("packetHandler cannot be null");
        }

        Method[] declaredMethods = packetHandler.getClass().getDeclaredMethods();

        for (Method method : declaredMethods) {
            int parameterCount = method.getParameterCount();
            Class<?> parameterClazz = method.getParameterTypes()[1];

            if (method.isAnnotationPresent(PacketHandlerMethod.class)
                    && parameterCount == 1
                    && parameterClazz.isAssignableFrom(SkyllaPacket.class)) {

                this.packetHandlers.get(parameterClazz)
                        .unregisterHandler(packetHandler);
            }
        }
    }

    /**
     * Handle an incoming packet.
     *
     * @param skyllaConnection The connection.
     * @param packet           The packet.
     */
    public void handlePacket(SkyllaConnection skyllaConnection, SkyllaPacket packet) {
        if (skyllaConnection == null) {
            throw new IllegalArgumentException("skyllaConnection cannot be null");
        }

        if (packet == null) {
            throw new IllegalArgumentException("packet cannot be null");
        }

        HandlerContainer handlerContainer = this.packetHandlers.get(packet.getClass());
        handlerContainer.handlePacket(skyllaConnection, packet);
    }

    /**
     * Get id by packet.
     *
     * @param packet The packet.
     * @return The id.
     */
    public byte getPacketId(SkyllaPacket packet) {
        if (packet == null) {
            throw new IllegalArgumentException("packet cannot be null");
        }

        return this.metaContainer.getPacketMeta(packet).id();
    }
}
