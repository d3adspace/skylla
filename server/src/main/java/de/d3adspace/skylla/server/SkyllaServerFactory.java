package de.d3adspace.skylla.server;

import de.d3adspace.skylla.commons.config.SkyllaConfig;

/**
 * @author Nathalie0hneHerz
 */
public class SkyllaServerFactory {
	
	public static SkyllaServer createSkyllaServer(SkyllaConfig config) {
		return new SimpleSkyllaServer(config);
	}
}
