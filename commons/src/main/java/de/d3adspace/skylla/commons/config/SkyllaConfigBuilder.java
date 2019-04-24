/*
 * Copyright (c) 2017 - 2019 D3adspace
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
 * Basic Builder class for {@link SkyllaConfig}
 *
 * @author Nathalie O'Neill <nathalie@d3adspace.de>
 */
public class SkyllaConfigBuilder {

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
     * Prevent creation from outside package.
     */
    SkyllaConfigBuilder() {

    }

    /**
     * Set the server host of skylla.
     *
     * @param serverHost The host.
     * @return The builder.
     */
    public SkyllaConfigBuilder setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    /**
     * Set the server port of skylla.
     *
     * @param serverPort The port.
     * @return The builder.
     */
    public SkyllaConfigBuilder setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    /**
     * Set the protocol for communication.
     *
     * @param protocol The protocol.
     * @return The builder.
     */
    public SkyllaConfigBuilder setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * Build the resulting skylla config.
     *
     * @return The config.
     */
    public SkyllaConfig createSkyllaConfig() {
        return new SkyllaConfig(serverHost, serverPort, protocol);
    }
}
