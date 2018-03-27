// Liam Wynn, 2/1/2018, Ulysses

/*
	The HydrosphereGenerator manages the entire process of
	Hydrosphere creation. It does so by following this algorithm:

	1. Get the heightmap from the Lithosphere
	2. Create a cloud frequency map
	3. Use the height map and cloud frequency map to determine
	   where rivers will likely form (river source map)
	4. Use the river source map to choose where our rivers will
	   begin
	5. Create rivers
	6. Create a equator map, which tells the distance each point
	   is from the equator.
	7. Use river map + equator map + cloud frequency map to create
	   precitipitation map.

	In this system, we assume that the amount of precipitation in
	a given area is determined by the presence of clouds and proximity
	to sources of water. Thus, we use that to make a precipitation map.
*/

package ulysses.planet.utilities.generators;

// Used in generateHydrosphere method.
import ulysses.planet.Hydrosphere;
import ulysses.planet.River;
// Used to store the height map.
import ulysses.planet.utilities.PlanetMap;
// Used in generating rivers.
import java.awt.geom.Point2D;
// Used in river generation.
// Used to shuffle the neighbors that we check. This way
// we encourage more random rivers.
import java.util.Random;

public class HydrosphereGenerator {

	// The dimensions of the resulting hydrosphere.
	// Used to keep all maps consistent in size.
	private int width, height;

	// Used to compute the river source map. Which
	// Tells us the most likely places rivers will form.
	private PlanetMap heightMap;

	// Used to generate the cloud frequency map.
	private MapGenerator cloudFreqMapGenerator;
	// Makes the river spawning more arbitrary.
	private MapGenerator riverSourceModiferMap;
	// Used to compute each point's distance from the
	// equator.
	private MapGenerator equatorMapGenerator;

	// The number of rivers we want in our map.
	private int numRivers;
	// Used in the riverBuilder to mark points that terminate
	// the algorithm.
	private float seaLevel;

	private RiverBuilder riverBuilder;

	// Used to shuffle the order we check neighbors in getNeighbors
	private Random rand;

	public HydrosphereGenerator(long shuffleSeed) {
		this.width = 256;
		this.height = 128;

		this.heightMap = null;
		this.cloudFreqMapGenerator = null;
		this.riverSourceModiferMap = null;

		this.numRivers = 0;
		this.seaLevel = 0;
		this.riverBuilder = new RiverBuilder(shuffleSeed);

		this.rand = new Random(shuffleSeed);
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

	public MapGenerator getCloudFreqMapGenerator() {
		return this.cloudFreqMapGenerator;
	}

	public void setCloudFreqMapGenerator(MapGenerator val) {
		this.cloudFreqMapGenerator = val;
	}

	public MapGenerator getRiverSourceModifierMap() {
		return this.riverSourceModiferMap;
	}

	public void setRiverSourceModifierMap(MapGenerator val) {
		this.riverSourceModiferMap = val;
	}

	public MapGenerator getEquatorMapGenerator() {
		return this.equatorMapGenerator;
	}

	public void setEquatorMapGenerator(MapGenerator val) {
		this.equatorMapGenerator = val;
	}

	public int getNumRivers() {
		return this.numRivers;
	}

	public void setNumRivers(int val) {
		if(val < 0)
			val = 0;

		this.numRivers = val;
	}

	public float getSeaLevel() {
		return this.seaLevel;
	}

	public void setSeaLevel(float val) {
		this.seaLevel = val;
	}

	public Hydrosphere generateHydrosphere() {
		if(this.heightMap == null ||
		   this.cloudFreqMapGenerator == null)
		{
			return null;
		}

		// What we will return.
		Hydrosphere result = new Hydrosphere(this.width, this.height);
		// Use this to generate the river source map.
		PlanetMap cloudFreqMap;
		PlanetMap riverSourceModMap;
		// Use this to generate rivers.
		PlanetMap riverSourceMap;
		// A map to compute the distance from the equator.
		PlanetMap equatorDistMap;

		// Set the properties of each generator.

		this.cloudFreqMapGenerator.setWidth(this.width);
		this.cloudFreqMapGenerator.setHeight(this.height);

		this.riverSourceModiferMap.setWidth(this.width);
		this.riverSourceModiferMap.setHeight(this.height);

		this.equatorMapGenerator.setWidth(this.width);
		this.equatorMapGenerator.setHeight(this.height);

		// Set hydrosphere properties.

		cloudFreqMap = this.cloudFreqMapGenerator.generateMap();
		equatorDistMap = this.equatorMapGenerator.generateMap();
		riverSourceModMap = this.riverSourceModiferMap.generateMap();

		result.setNumRivers(this.numRivers);

		// Generate the rivers
		riverBuilder.setNumRivers(this.numRivers);
		riverBuilder.setSeaLevel(this.seaLevel);
		riverBuilder.setWidth(this.width);
		riverBuilder.setHeight(this.height);
		riverBuilder.setCloudFrequencyMap(cloudFreqMap);
		riverBuilder.setRiverSourceModiferMap(riverSourceModMap);
		riverBuilder.generateRivers(result, heightMap);

		result.setCloudFreqMap(cloudFreqMap);
		result.setEquatorMap(equatorDistMap);
		result.setModifiedHeightMap(computeModHeightMap(heightMap));

		return result;
	}

	private PlanetMap computeModHeightMap(PlanetMap heightmap) {
		PlanetMap result = new PlanetMap(this.width, this.height);

		float min = -1, max = -1;
		int len = this.width * this.height;
		float val;
		boolean bSet = false;

		for(int i = 0; i < len; ++i) {
			val = heightmap.getData(i);

			if(val < this.seaLevel)
				continue;

			if(bSet == false) {
				min = val;
				max = val;
				bSet = true;
			}

			else {
				if(val < min)
					min = val;
				if(val > max)
					max = val;
			}
		}

		for(int i = 0; i < len; ++i) {
			val = heightmap.getData(i);

			if(min == max || val < 0.37f)
				result.setData(i, 0);
			else
				result.setData(i, (val - min) / (max - min));
		}

		return result;
	}
}
