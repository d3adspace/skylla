package de.d3adspace.skylla.client;

import de.d3adspace.skylla.commons.config.SkyllaConfig;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaClientFactory {
	
	public static SkyllaClient createSkyllaClient(SkyllaConfig config) {
		return new SimpleSkyllaClient(config);
	}
}
