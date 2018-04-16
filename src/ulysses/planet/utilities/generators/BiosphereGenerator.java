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

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int val) {
		if(val <= 0)
			val = 256;

		this.width = val;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int val) {
		if(val <= 0)
			val = 128;

		this.height = val;
	}

	public PlanetMap getHeightMap() {
		return this.heightMap;
	}

	public void setHeightMap(PlanetMap val) {
		this.heightMap = val;
	}

	public PlanetMap getTemperatureMap() {
		return this.tempMap;
	}

	public void setTemperatureMap(PlanetMap val) {
		this.tempMap = val;
	}

	public PlanetMap getPrecipitationMap() {
		return this.precipMap;
	}

	public void setPrecipitationMap(PlanetMap val) {
		this.precipMap = val;
	}
}
