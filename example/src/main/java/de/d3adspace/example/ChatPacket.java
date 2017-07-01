package de.d3adspace.example;

import de.d3adspace.skylla.commons.buffer.SkyllaBuffer;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacket;
import de.d3adspace.skylla.commons.protocol.packet.SkyllaPacketMeta;

/**
 * @author Nathalie0hneHerz
 */
@SkyllaPacketMeta(id = 0)
public class ChatPacket extends SkyllaPacket {
	
	private String sender;
	private String message;
	
	public ChatPacket(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	
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
	
	public String getMessage() {
		return message;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
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
