package de.d3adspace.skylla.commons.protocol.handler;

import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nathalie0hneHerz
 */
public class HandlerContainer {
	
	private final Map<PacketHandler, List<Method>> registeredListeners;
	
	public HandlerContainer() {
		this.registeredListeners = new HashMap<>();
	}
	
	public void registerListenerMethod(PacketHandler packetHandler, Method method) {
		if (!this.registeredListeners.containsKey(packetHandler)) {
			this.registeredListeners.put(packetHandler, new ArrayList<>());
		}
		
		this.registeredListeners.get(packetHandler).add(method);
	}
	
	public void unregisterHandler(PacketHandler packetHandler) {
		this.registeredListeners.remove(packetHandler);
	}
	
	public void handlePacket(SkyllaPacket packet) {
		this.registeredListeners.forEach((handler, methods) -> methods.forEach(method -> {
			try {
				method.invoke(handler, packet);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}));
	}
}
