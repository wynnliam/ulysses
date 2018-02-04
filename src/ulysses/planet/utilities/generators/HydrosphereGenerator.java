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
	6. Use rivers + height map to compute the distance from water
	   source map.
	7. Use water source map + cloud map to create a precipitation
	   map.

	In this system, we assume that the amount of precipitation in
	a given area is determined by the presence of clouds and proximity
	to sources of water. Thus, we use that to make a precipitation map.
*/

package ulysses.planet.utilities.generators;

// Used in generateHydrosphere method.
import ulysses.planet.Hydrosphere;
// Used to store the height map.
import ulysses.planet.utilities.PlanetMap;

public class HydrosphereGenerator
{
	// The dimensions of the resulting hydrosphere.
	// Used to keep all maps consistent in size.
	private int width, height;

	// Used to compute the river source map. Which
	// Tells us the most likely places rivers will form.
	private PlanetMap heightMap;

	// Used to generate the cloud frequency map.
	private MapGenerator cloudFreqMapGenerator;

	public HydrosphereGenerator()
	{
		this.width = 256;
		this.height = 128;

		this.heightMap = null;

		this.cloudFreqMapGenerator = null;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int val)
	{
		if(val <= 0)
			val = 256;

		this.width = val;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void setHeight(int val)
	{
		if(val <= 0)
			val = 128;

		this.height = val;
	}

	public PlanetMap getHeightMap()
	{
		return this.heightMap;
	}

	public void setHeightMap(PlanetMap val)
	{
		this.heightMap = val;
	}

	public MapGenerator getCloudFreqMapGenerator()
	{
		return this.cloudFreqMapGenerator;
	}

	public void setCloudFreqMapGenerator(MapGenerator val)
	{
		this.cloudFreqMapGenerator = val;
	}

	public Hydrosphere generateHydrosphere()
	{
		if(this.heightMap == null ||
		   this.cloudFreqMapGenerator == null)
		{
			return null;
		}

		// What we will return.
		Hydrosphere result = new Hydrosphere(this.width, this.height);
		// Use this to generate the river source map.
		PlanetMap cloudFreqMap;
		// Use this to generate rivers.
		PlanetMap riverSourceMap;

		// Set the properties of each generator.

		this.cloudFreqMapGenerator.setWidth(this.width);
		this.cloudFreqMapGenerator.setHeight(this.height);

		// Set hydrosphere properties.

		cloudFreqMap = this.cloudFreqMapGenerator.generateMap();
		riverSourceMap = computeRiverSourceMap(cloudFreqMap);

		// TODO: Use river source map + height map to create rivers.

		result.setCloudFreqMap(cloudFreqMap);

		return result;
	}

	private PlanetMap computeRiverSourceMap(PlanetMap cloudFreqMap)
	{
		PlanetMap[] p = new PlanetMap[] { cloudFreqMap };
		PlanetMap riverSourceMap = this.heightMap.combineWith(p);

		for(int i = 0; i < this.width * this.height; ++i)
		{
			if(this.heightMap.getData(i) <= 0.37f)
				riverSourceMap.setData(i, 0.0f);
		}

		riverSourceMap.sqrt();
		riverSourceMap.normalize();

		return riverSourceMap;
	}
}
