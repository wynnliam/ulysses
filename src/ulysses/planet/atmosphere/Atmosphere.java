// Liam Wynn, 4/7/2018, Ulysses

/*
	The Atmosphere is responsible for a Planet's
	temperature data. The temperature data is represented
	in the form of the Temperature Map - which is the
	temperatures of every land point. The Temperature map
	is a combination of several submaps as follows:

	Altitude - The height of each point.
	Equator - The distance each point is from a specified
	equator.
	Wind - Essentially a smooth noise map that represents wind
	patterns and their effects on the temperature.
	Distance to sea - TODO: Finish me!

	We choose these because scientifically these factors relate
	to the temperature behavior in a climate system. For altitude,
	the temperature decreases as altitude increases. For distance from
	the equator, the temperature decreases as the distance increases.
	Wind patterns affect temperature in numerous and complicated ways.
	As such, we simply use smooth noise to represent those factors.

	TODO: Finish me!
*/

package ulysses.planet;

import ulysses.planet.utilities.PlanetMap;

public class Atmosphere {
	// Dimensions of the world.
	private int width, height;

	// Represents distance from the equator.
	private PlanetMap equatorMap;
	// A smooth noise map to represent how wind affects temperature.
	private PlanetMap windMap;

	// higher altitudes (starting with sea level) yield cooler
	// temperatures.
	private PlanetMap heightMap;
	// Use this to make a modified heightMap (see getTemperatureMap).
	private float seaLevel;

	public Atmosphere(int width, int height) {
		setWidth(width);
		setHeight(height);

		this.equatorMap = null;
		this.windMap = null;

		this.heightMap = null;
		this.seaLevel = 0;
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

	public PlanetMap getEquatorMap() {
		return this.equatorMap;
	}

	public void setEquatorMap(PlanetMap val) {
		this.equatorMap = val;
	}

	public PlanetMap getWindMap() {
		return this.windMap;
	}

	public void setWindMap(PlanetMap val) {
		this.windMap = val;
	}

	public PlanetMap getHeightMap() {
		return this.heightMap;
	}

	public void setHeightMap(PlanetMap val) {
		this.heightMap = val;
	}

	public float getSeaLevel() {
		return this.seaLevel;
	}

	public void setSeaLevel(float val) {
		this.seaLevel = val;
	}

	public PlanetMap getTemperatureMap() {
		PlanetMap result;
		PlanetMap[] maps = new PlanetMap[2];
		float[] tempBounds;

		maps[0] = this.windMap;
		maps[1] = this.heightMap.getCopy();

		invertHeightMap(maps[1]);

		result = equatorMap.combineWith(maps);
		result.sqrt();

		tempBounds = getMinMaxLandVals(result);
		normalizeByLandPrecips(tempBounds, result);

		return result;
	}

	private void invertHeightMap(PlanetMap heightMap) {
		float max = heightMap.getMaxVal();
		float val;

		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				val = -heightMap.getData(x, y) + max;
				heightMap.setData(x, y, val);
			}
		}
	}

	/*
		Determines the maximum and minimum precipitation values for all
		precipitation values that are corresponding to land (height values
		above sea level).

		ARGUMENTS:
			precipitation - access the precipitation map.

		RETURNS:
			a 2 dimensional vector where the first entry is the minimum precipitation
			value on land, and the second is the maximum precipitation on land.
	*/
	private float[] getMinMaxLandVals(PlanetMap precipitation) {
		// What we will return.
		float[] result = new float[2];
		// Use this for comparisons.
		float currH, currP;
		// Used to make iteration cleaner.
		int len = this.width * this.height;

		// Garantees these will be set.
		result[0] = precipitation.getMaxVal();
		result[1] = precipitation.getMinVal();

		for(int i = 0; i < len; ++i) {
			currH = this.heightMap.getData(i);
			currP = precipitation.getData(i);
			if(currH > this.seaLevel) {
				if(currP < result[0])
					result[0] = currP;
				if(currP > result[1])
					result[1] = currP;
			}
		}

		return result;
	}

	/*
		Normalizes every point according to the max and min precipitation values found on land.
		Oceanic points are given the value of the cloud frequency map so as to simulate rainfall in
		oceanic regions.

		ARGUMENTS:
			landPrecips - stores the min and max precipitation values of land points.
			precipition - the map to modify.
	*/
	private void normalizeByLandPrecips(float[] landPrecips, PlanetMap precipitation) {
		float curr;
		int len = this.width * this.height;

		for(int i = 0; i < len; ++i) {
			if(this.heightMap.getData(i) > this.seaLevel)
				curr = (precipitation.getData(i) - landPrecips[0]) / (landPrecips[1] - landPrecips[0]);
			else
				curr = 0;

			precipitation.setData(i, curr);
		}
	}
}
