package de.d3adspace.skylla.commons.config;

import de.d3adspace.skylla.commons.protocol.Protocol;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaConfig {
	
	private String serverHost;
	private int serverPort;
	private Protocol protocol;
	
	protected SkyllaConfig(String serverHost, int serverPort,
		Protocol protocol) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.protocol = protocol;
	}
	
	public String getServerHost() {
		return serverHost;
	}
	
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public Protocol getProtocol() {
		return protocol;
	}
	
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public String toString() {
		return "SkyllaConfig{" +
			"serverHost='" + serverHost + '\'' +
			", serverPort=" + serverPort +
			", protocol=" + protocol +
			'}';
	}
}
