// Liam Wynn, 3/1/2018, Ulysses

/*
	Generates a map that specifies each point's proximity to the latitude.

	TODO: Rename from Latitude to equator.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;
import java.util.Random;

public class EquatorMapGenerator extends MapGenerator {

	private int equator;

	public EquatorMapGenerator(Random rand) {
		super(rand);

		this.equator = 0;
	}

	public int getEquator() {
		return this.equator;
	}

	public void setEquator(int val) {
		if(val < 0)
			val = 0;
		if(val >= this.height)
			val = this.height - 1;

		this.equator = val;
	}

	public PlanetMap generateMap() {
		PlanetMap result = new PlanetMap(this.width, this.height);
		// The distance for each row from the equator.
		int dist;

		for(int y = 0; y < this.height; ++y) {
			dist = (int)Math.abs(y - this.equator);

			for(int x = 0; x < this.width; ++x) {
				result.setData(x, y, dist);
			}
		}

		// Since the furthest rows will have the largest distance,
		// we need to invert every point (such that 0's get max, and max
		// distance gets 0) before normalizing.
		invertMap(result);

		result.normalize();

		return result;
	}

	private void invertMap(PlanetMap equatorMap) {
		int maxDist = (int)equatorMap.getMaxVal();

		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				equatorMap.setData(x, y,
									-equatorMap.getData(x, y) + maxDist);
			}
		}
	}
}
