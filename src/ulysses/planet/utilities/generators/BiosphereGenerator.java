// Liam Wynn, 4/15/2018, Ulysses

/*
	TODO: Finish documentation!
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.Biosphere;
import ulysses.planet.holdridge.*;
import ulysses.planet.holdridge.HoldridgeSystem.*;
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
		double altitude, biotemp, precip;

		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				if(this.heightMap.getData(x, y) <= 0.37f) {
					result.setHoldridgeData(x, y, null);
					continue;
				}

				altitude = convertValue(this.heightMap.getData(x, y), this.params.minAltitude, this.params.maxAltitude);
				biotemp = convertValue(this.tempMap.getData(x, y), this.params.minTemp, this.params.maxTemp);
				precip = convertValue(this.precipMap.getData(x, y), this.params.minPrecip, this.params.maxPrecip);

				next = HoldridgeSystem.computeData(biotemp, precip, altitude);
				result.setHoldridgeData(x, y, next);
			}
		}

		return result;
	}

	/*
		Given a value from 0.0 to 1.0, we convert it to a corresponding
		value on the scale of min to max. We will assume val is between 0 and 1.
		However, if max is the same as the min, we will return min.

		ARGUMENTS:
			val - a value from 0.0 to 1.0.
			min, max - describe the scale to convert val to.

		RETURNS:
			A value between min and max (inclusive). If min equals max, we
			return min.
	*/
	private double convertValue(double val, double min, double max) {
		if(max == min)
			return min;

		return (max - min) * val + min;
	}
}
