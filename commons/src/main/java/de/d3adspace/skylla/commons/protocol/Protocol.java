package de.d3adspace.skylla.commons.protocol;

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
				&& method.getParameterCount() == 1
				&& SkyllaPacket.class.isAssignableFrom(method.getParameterTypes()[0])) {
				
				if (!this.packetHandlers.containsKey(method.getParameterTypes()[0])) {
					this.packetHandlers.put(
						(Class<? extends SkyllaPacket>) method.getParameterTypes()[0], new HandlerContainer());
				}
				
				this.packetHandlers.get(method.getParameterTypes()[0]).registerListenerMethod(packetHandler, method);
			}
		}
	}
	
	public void unregisterListener(PacketHandler packetHandler) {
		Method[] declaredMethods = packetHandler.getClass().getDeclaredMethods();
		
		for (Method method : declaredMethods) {
			if (method.isAnnotationPresent(PacketHandlerMethod.class)
				&& method.getParameterCount() == 1
				&& method.getParameterTypes()[0].isAssignableFrom(SkyllaPacket.class)) {
				
				this.packetHandlers.get(method.getParameterTypes()[0]).unregisterHandler(packetHandler);
			}
		}
	}
	
	public void handlePacket(SkyllaPacket packet) {
		HandlerContainer handlerContainer = this.packetHandlers.get(packet.getClass());
		handlerContainer.handlePacket(packet);
	}
	
	public byte getPacketId(SkyllaPacket packet) {
		return packet.getClass().getAnnotation(SkyllaPacketMeta.class).id();
	}
}
