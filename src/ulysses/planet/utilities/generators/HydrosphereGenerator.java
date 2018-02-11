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
import ulysses.planet.River;
// Used to store the height map.
import ulysses.planet.utilities.PlanetMap;
// Used in generating rivers.
import java.awt.Point;
// Used to choose the origins of Rivers.
import java.awt.geom.Point2D;

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

	// The number of rivers we want in our map.
	private int numRivers;

	public HydrosphereGenerator()
	{
		this.width = 256;
		this.height = 128;

		this.heightMap = null;

		this.cloudFreqMapGenerator = null;

		this.numRivers = 0;
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

	public int getNumRivers()
	{
		return this.numRivers;
	}

	public void setNumRivers(int val)
	{
		if(val < 0)
			val = 0;

		this.numRivers = val;
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

		result.setNumRivers(this.numRivers);
		generateRivers(result, heightMap, riverSourceMap);

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

	/*
		Generates the rivers for the hydrosphere. This operation will fail if at least
		of the following is true:

		1. hydro, heightmap, or riverSourceMap are null.
		2. Number of rivers is 0.

		ARGUMENTS:
			hydro - where to store resulting rivers.
			heightmap - used to choose points for building rivers.
			riverSourceMap - used to choose the initial points for the river.
	*/
	private void generateRivers(Hydrosphere hydro, PlanetMap heightmap, PlanetMap riverSourceMap)
	{
		if(hydro == null || heightmap == null || riverSourceMap == null)
			return;
		if(numRivers < 1)
			return;

		// Easy access to the source points for rivers.
		Point2D.Float[] sources = riverSourceMap.getSortedPoints();
		// The next river to add.
		River river;
		// Easy access sources array.
		int index;
		// Used to conveniently access our source point.
		Point source;

		for(int i = 0; i < this.numRivers; ++i)
		{
			index = (sources.length - 1) - i;
			// A 2D point to 1D is i = y * width + x.
			// The opposite is x = i % width, y = i / width.
			source = new Point((int)sources[index].getX() % this.width,
							   (int)sources[index].getX() / this.width);

			river = new River();
			// TODO: Use river generating algorithm.
			river.insertPoint(source);

			hydro.setRiver(i, river);
		}
	}
}
