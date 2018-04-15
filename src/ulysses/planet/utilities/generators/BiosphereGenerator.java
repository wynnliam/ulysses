// Liam Wynn, 4/15/2018, Ulysses

/*
	TODO: Finish documentation!
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;

public class BiosphereGenerator {
	// Dimensions of the world.
	private int width, height;

	// Neccessary maps for computing the biosphere.
	private PlanetMap heightMap, tempMap, precipMap;

	public BiosphereGenerator() {
		this.width = 256;
		this.height = 128;

		this.heightMap = null;
		this.tempMap = null;
		this.precipMap = null;
	}
}
