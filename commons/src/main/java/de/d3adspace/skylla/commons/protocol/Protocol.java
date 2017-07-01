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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Structuring network communication.
 *
 * @author Nathalie0hneHerz
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
	 * Create a new Protocol.
	 */
	public Protocol() {
		this.registeredPackets = new HashMap<>();
		this.packetHandlers = new HashMap<>();
	}
	
	/**
	 * Register a new packet.
	 *
	 * @param packetClazz The packet clazz.
	 */
	public void registerPacket(Class<? extends SkyllaPacket> packetClazz) {
		SkyllaPacketMeta meta = packetClazz.getAnnotation(SkyllaPacketMeta.class);
		
		registeredPackets.put(meta.id(), packetClazz);
	}
	
	/**
	 * Unregister a new packet.
	 *
	 * @param packetClazz The packet clazz.
	 */
	public void unregisterPacket(Class<? extends SkyllaPacket> packetClazz) {
		SkyllaPacketMeta meta = packetClazz.getAnnotation(SkyllaPacketMeta.class);
		
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
		
		try {
			return packetClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Register a listener.
	 *
	 * @param packetHandler The handler.
	 */
	public void registerListener(PacketHandler packetHandler) {
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
		Method[] declaredMethods = packetHandler.getClass().getDeclaredMethods();
		
		for (Method method : declaredMethods) {
			if (method.isAnnotationPresent(PacketHandlerMethod.class)
				&& method.getParameterCount() == 1
				&& method.getParameterTypes()[1].isAssignableFrom(SkyllaPacket.class)) {
				
				this.packetHandlers.get(method.getParameterTypes()[1])
					.unregisterHandler(packetHandler);
			}
		}
	}
	
	/**
	 * Handle an incoming packet.
	 *
	 * @param skyllaConnection The connection.
	 * @param packet The packet.
	 */
	public void handlePacket(SkyllaConnection skyllaConnection, SkyllaPacket packet) {
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
		return packet.getClass().getAnnotation(SkyllaPacketMeta.class).id();
	}
}
