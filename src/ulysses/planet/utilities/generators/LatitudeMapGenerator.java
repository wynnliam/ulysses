// Liam Wynn, 3/1/2018, Ulysses

/*
	Generates a map that specifies each point's proximity to the latitude.

	TODO: Rename from Latitude to equator.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;
import java.util.Random;

public class LatitudeMapGenerator extends MapGenerator {

	private int latitude;

	public LatitudeMapGenerator(Random rand) {
		super(rand);

		this.latitude = 0;
	}

	public int getLatitude() {
		return this.latitude;
	}

	public void setLatitude(int val) {
		if(val < 0)
			val = 0;
		if(val >= this.height)
			val = this.height - 1;

		this.latitude = val;
	}

	public PlanetMap generateMap() {
		PlanetMap result = new PlanetMap(this.width, this.height);
		// The distance for each row from the latitude.
		int dist;

		for(int y = 0; y < this.height; ++y) {
			dist = (int)Math.abs(y - this.latitude);

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

	private void invertMap(PlanetMap latitudeMap) {
		int maxDist = (int)latitudeMap.getMaxVal();

		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				latitudeMap.setData(x, y,
									-latitudeMap.getData(x, y) + maxDist);
			}
		}
	}
}
