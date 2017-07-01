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
 * @author Nathalie0hneHerz
 */
public class Protocol {
	
	private final Map<Byte, Class<? extends SkyllaPacket>> registeredPackets;
	private final Map<Class<? extends SkyllaPacket>, HandlerContainer> packetHandlers;
	
	public Protocol() {
		this.registeredPackets = new HashMap<>();
		this.packetHandlers = new HashMap<>();
	}
	
	public void registerPacket(Class<? extends SkyllaPacket> packetClazz) {
		SkyllaPacketMeta meta = packetClazz.getAnnotation(SkyllaPacketMeta.class);
		
		registeredPackets.put(meta.id(), packetClazz);
	}
	
	public void unregisterPacket(Class<? extends SkyllaPacket> packetClazz) {
		SkyllaPacketMeta meta = packetClazz.getAnnotation(SkyllaPacketMeta.class);
		
		registeredPackets.remove(meta.id());
	}
	
	public SkyllaPacket createPacket(byte packetId) {
		Class<? extends SkyllaPacket> packetClazz = registeredPackets.get(packetId);
		
		try {
			return packetClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
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
	
	public void handlePacket(SkyllaConnection skyllaConnection, SkyllaPacket packet) {
		HandlerContainer handlerContainer = this.packetHandlers.get(packet.getClass());
		handlerContainer.handlePacket(skyllaConnection, packet);
	}
	
	public byte getPacketId(SkyllaPacket packet) {
		return packet.getClass().getAnnotation(SkyllaPacketMeta.class).id();
	}
}
