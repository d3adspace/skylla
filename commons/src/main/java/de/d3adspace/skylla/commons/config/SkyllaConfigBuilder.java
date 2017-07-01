package de.d3adspace.skylla.commons.config;

import de.d3adspace.skylla.commons.protocol.Protocol;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaConfigBuilder {
	
	private String serverHost;
	private int serverPort;
	private Protocol protocol;
	
	public SkyllaConfigBuilder setServerHost(String serverHost) {
		this.serverHost = serverHost;
		return this;
	}
	
	public SkyllaConfigBuilder setServerPort(int serverPort) {
		this.serverPort = serverPort;
		return this;
	}
	
	public SkyllaConfigBuilder setProtocol(Protocol protocol) {
		this.protocol = protocol;
		return this;
	}
	
	public SkyllaConfig createSkyllaConfig() {
		return new SkyllaConfig(serverHost, serverPort, protocol);
	}
}