// Liam Wynn, 1/26/2018, Ulysses

/*
	Generates a map where all of the values are 0.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;
import java.util.Random;

public class EmptyMapGenerator extends MapGenerator
{
	public EmptyMapGenerator(Random rand)
	{
		super(rand);
	}

	public PlanetMap generateMap()
	{
		PlanetMap result = new PlanetMap(this.width, this.height);

		for(int i = 0; i < this.width * this.height; ++i)
			result.setData(i, 0.0f);

		return result;
	}
}
