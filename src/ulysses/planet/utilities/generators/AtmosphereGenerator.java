// Liam Wynn, 4/7/2018, Ulysses

/*
	Manages the logic of generating an Atmosphere.

	TODO: Finish me!
*/

package ulysses.planet.utilities.generators;

import java.util.Random;
import ulysses.planet.utilities.PlanetMap;

public class AtmosphereGenerator {
	// Dimensions of the atmosphere we wish to generate.
	private int width, height;
	// For generating maps.
	private Random random;

	// Used to compute map of distance to equator.
	private MapGenerator equatorMapGenerator;
	// Generates wind pattern data.
	private MapGenerator windMapGenerator;

	// Used to get modified height map.
	private PlanetMap heightMap;
	private float seaLevel;

	public AtmosphereGenerator(Random rand) {
		this.width = 256;
		this.height = 128;

		this.random = rand;

		this.equatorMapGenerator = null;
		this.windMapGenerator = null;

		this.heightMap = null;
		this.seaLevel = 0.0f;
	}
}
