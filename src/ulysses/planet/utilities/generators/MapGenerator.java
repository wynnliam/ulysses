// Liam Wynn, 1/6/2017, Ulysses

/*
	This is a simple abstract base class for every
	PlanetMap generation algorithm. By having this,
	we can experiment with different generators for
	generating worlds more easily.
*/

package ulysses.planet.utilities.generators;

// For generating maps.
import ulysses.planet.utilities.PlanetMap;
// For choosing random noise values.
import java.util.Random;

public abstract class MapGenerator {
	// The dimensions for the map to generate
	protected int width, height;
	// Used for choosing random numbers.
	protected Random rand;

	public MapGenerator(Random rand) {
		if(rand == null)
			rand = new Random();

		this.rand = rand;

		this.width = 256;
		this.height = 128;
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

	public Random getRandom() {
		return this.rand;
	}

	public void setRandom(Random val) {
		if(val == null)
			val = new Random();

		this.rand = val;
	}

	public abstract PlanetMap generateMap();
}
