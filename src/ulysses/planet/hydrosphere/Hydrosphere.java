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

	// Stores the rivers of the world.
	private River[] rivers;

	public Hydrosphere(int width, int height)
	{
		setWidth(width);
		setHeight(height);

		this.cloudFreqMap = null;
		this.rivers = null;
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

	public int getNumRivers()
	{
		if(this.rivers == null)
			return 0;
		else
			return this.rivers.length;
	}

	public void setNumRivers(int val)
	{
		if(val < 1)
			return;

		this.rivers = new River[val];
	}

	/*
		Returns the river at the given index for the rivers array.

		ARGUMENTS:
			index: the index into the rivers array.

		RETURNS:
			the river at index, or null if the index is invalid OR
			the rivers array is null.
			
	*/
	public River getRiver(int index)
	{
		if(this.rivers == null || index < 0 || index >= this.rivers.length)
			return null;
		else
			return this.rivers[index];
	}

	/*
		Set the river at the specified index to the given river.
		This operation can fail if index is invalid, val is null, or
		the rivers array is null.

		ARGUMENTS:
			index: index into the rivers array.
			val: the river to set to in the rivers array.
	*/
	public void setRiver(int index, River val)
	{
		if(this.rivers == null || val == null || index < 0 || index >= this.rivers.length)
			return;

		this.rivers[index] = val;
	}

	public PlanetMap getPrecipitationMap()
	{
		return this.cloudFreqMap;
	}
}
