// Liam Wynn, 4/15/2018, Ulysses

/*
	TODO: Finish documentation!
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.Biosphere;
import ulysses.planet.utilities.PlanetMap;

public class BiosphereGenerator {
	// Dimensions of the world.
	private int width, height;

	// Neccessary maps for computing the biosphere.
	private PlanetMap heightMap, tempMap, precipMap;

	// Stores values used to translate raw PlanetMap data to.
	private BiosphereGeneratorParams params;

	public BiosphereGenerator() {
		this.width = 256;
		this.height = 128;

		this.heightMap = null;
		this.tempMap = null;
		this.precipMap = null;

		this.params = null;
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

	public BiosphereGeneratorParams getParams() {
		return this.params;
	}

	public void setParams(BiosphereGeneratorParams val) {
		this.params = val;
	}

	/*
		Generates a biosphere by using the Holdridge Climate model.
		The classification system requires temperature, precipitation, and
		altitude data. The data is given as normalized values from 0 to 1.
		See the HoldridgeSystem class for more information on how this is done.

		RETURNS:
			A biosphere, or null if the params or maps (temp, precip, height) are null.
	*/
	public Biosphere generateMap() {
		if(this.params == null || this.tempMap == null || this.precipMap == null || this.heightMap == null)
			return null;

		Biosphere result = new Biosphere(this.width, this.height);
		HoldridgeData next;

		for(int i = 0; i < this.width * this.height; ++i) {
			// TODO: Finish me!
		}

		return result;
	}
}
