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

package de.d3adspace.skylla.commons.protocol.handler;

import de.d3adspace.skylla.commons.connection.SkyllaConnection;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for Handlers.
 *
 * @author Nathalie0hneHerz
 */
public class HandlerContainer {
	
	/**
	 * Registery for all methods to handle.
	 */
	private final Map<PacketHandler, List<Method>> registeredListeners;
	
	/**
	 * Create a new container.
	 */
	public HandlerContainer() {
		this.registeredListeners = new HashMap<>();
	}
	
	/**
	 * register a new method of a given handler.
	 *
	 * @param packetHandler The handler.
	 * @param method The method.
	 */
	public void registerListenerMethod(PacketHandler packetHandler, Method method) {
		if (!this.registeredListeners.containsKey(packetHandler)) {
			this.registeredListeners.put(packetHandler, new ArrayList<>());
		}
		
		this.registeredListeners.get(packetHandler).add(method);
	}
	
	/**
	 * Unregister all methods of a given handler.
	 *
	 * @param packetHandler The handler.
	 */
	public void unregisterHandler(PacketHandler packetHandler) {
		this.registeredListeners.remove(packetHandler);
	}
	
	/**
	 * Handle an incoming packet.
	 *
	 * @param skyllaConnection The connection source.
	 * @param packet The packet.
	 */
	public void handlePacket(SkyllaConnection skyllaConnection, SkyllaPacket packet) {
		this.registeredListeners.forEach((handler, methods) -> methods.forEach(method -> {
			try {
				method.invoke(handler, skyllaConnection, packet);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}));
	}
}
