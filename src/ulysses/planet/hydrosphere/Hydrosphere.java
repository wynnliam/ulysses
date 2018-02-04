// Liam Wynn, 1/30/2018, Ulysses

/*
	The Hydrosphere is responsible for all things water...
	except Oceans. For Oceans, see Lithosphere. Otherwise,
	this entails two things: Precipitation and Rivers.

	We will start with Rivers. A River is a list of points that
	begins at a land point, and ends at an ocean/lake. To generate
	Rivers, we first choose the points we want for our Rivers
	to spawn. This entails using the Heightmap and a randomly
	generated cloud map to define where the most likely places
	rivers spawn. Once we have our river sources, we must
	generate our rivers. 

	We use both our rivers and oceans to create a map that 
	describes the distance from sources of water. Generally
	speaking, the closer you are to water, the more precipitation
	you will have.

	Finally, we combine this with our cloud map to produce a
	final precipitation map.
*/

package ulysses.planet;

// Used to store specific hydrosphere data
// and to compute the Precipitation Map.
import ulysses.planet.utilities.PlanetMap;

public class Hydrosphere
{
	// The dimensions of the world.
	private int width, height;

	// Store the frequency of clouds
	private PlanetMap cloudFreqMap;

	public Hydrosphere(int width, int height)
	{
		setWidth(width);
		setHeight(height);

		this.cloudFreqMap = null;
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

	public PlanetMap getCloudFreqMap()
	{
		return this.cloudFreqMap;
	}

	public void setCloudFreqMap(PlanetMap val)
	{
		this.cloudFreqMap = val;
	}

	public PlanetMap getPrecipitationMap()
	{
		return this.cloudFreqMap;
	}
}
