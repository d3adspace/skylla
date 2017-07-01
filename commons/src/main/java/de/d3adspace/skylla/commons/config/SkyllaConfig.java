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

package de.d3adspace.skylla.commons.config;

import de.d3adspace.skylla.commons.protocol.Protocol;

/**
 * Basic config for all skylla connections.
 *
 * @author Nathalie0hneHerz
 */
public class SkyllaConfig {
	
	/**
	 * The host of the skylla server.
	 */
	private String serverHost;
	
	/**
	 * The port of the skylla server.
	 */
	private int serverPort;
	
	/**
	 * The protocol for the communication.
	 */
	private Protocol protocol;
	
	/**
	 * Create a new config.
	 *
	 * @param serverHost The host.
	 * @param serverPort The port.
	 * @param protocol The protocol.
	 */
	SkyllaConfig(String serverHost, int serverPort, Protocol protocol) {
		if (serverHost == null || serverHost.isEmpty()) {
			throw new IllegalArgumentException("host cannot be null or empty");
		}
		
		if (serverPort < 0) {
			throw new IllegalArgumentException("severport cannot be under zero.");
		}
		
		if (protocol == null) {
			throw new IllegalArgumentException("protocol cannot be null");
		}
		
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.protocol = protocol;
	}
	
	public static SkyllaConfigBuilder newBuilder() {
		return new SkyllaConfigBuilder();
	}
	
	/**
	 * Get the host of the skylla server.
	 *
	 * @return The host.
	 */
	public String getServerHost() {
		return serverHost;
	}
	
	/**
	 * Set the host of the server.
	 *
	 * @param serverHost The host.
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	
	/**
	 * Get the port of the server.
	 *
	 * @return The port.
	 */
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * Set the port of the server.
	 *
	 * @param serverPort The port.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * Get the protocol for communication.
	 *
	 * @return The protocol.
	 */
	public Protocol getProtocol() {
		return protocol;
	}
	
	/**
	 * Set the protocol
	 *
	 * @param protocol The protocol.
	 */
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
