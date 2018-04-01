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

	We then use an equator map, which describes each point's
	normalized distance from the equator. Generally speaking,
	the further you are from the equator, the drier the point
	becomes.

	Next, we include a map that describe's each points approximate
	distance to water sources.

	Finally, we combine this with our cloud map to produce a
	final precipitation map.
*/

package ulysses.planet;

// Used to store specific hydrosphere data
// and to compute the Precipitation Map.
import ulysses.planet.utilities.PlanetMap;
// Used to find the river of associated points.
import java.awt.Point;

public class Hydrosphere {
	// The dimensions of the world.
	private int width, height;

	// Store the frequency of clouds
	private PlanetMap cloudFreqMap;
	// Store the distance to the equator for each point.
	private PlanetMap equatorMap;
	// Stores the approximate distance to water for each point.
	private PlanetMap approxWaterDist;

	// Stores the rivers of the world.
	private River[] rivers;

	public Hydrosphere(int width, int height) {
		setWidth(width);
		setHeight(height);

		this.cloudFreqMap = null;
		this.equatorMap = null;
		this.approxWaterDist = null;
		this.rivers = null;
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

	public PlanetMap getCloudFreqMap() {
		return this.cloudFreqMap;
	}

	public void setCloudFreqMap(PlanetMap val) {
		this.cloudFreqMap = val;
	}

	public PlanetMap getEquatorMap() {
		return this.equatorMap;
	}

	public void setEquatorMap(PlanetMap val) {
		this.equatorMap = val;
	}

	public PlanetMap getApproxDistToWaterMap() {
		return this.approxWaterDist;
	}

	public void setApproxDistToWaterMap(PlanetMap val) {
		this.approxWaterDist = val;
	}

	public int getNumRivers() {
		if(this.rivers == null)
			return 0;
		else
			return this.rivers.length;
	}

	public void setNumRivers(int val) {
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
	public River getRiver(int index) {
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
	public void setRiver(int index, River val) {
		if(this.rivers == null || val == null || index < 0 || index >= this.rivers.length)
			return;

		this.rivers[index] = val;
	}

	/*
		Returns the index of the river that contains a given point.
		This operation will return -1 (denoting failure) if no river
		has said point, said point is null, or the river array is null.

		ARGUMENTS:
			point - the point we want the river of.

		RETURNS:
			the index of the river that contains point OR -1 if point
			or the rivers array is null.
	*/
	public int getRiverOf(Point point) {
		if(point == null || this.rivers == null)
			return -1;

		// What we will return. Assume the point
		// is not associated with any river.
		int result = -1;

		for(int i = 0; i < this.rivers.length; ++i) {
			// Found the river. Update result and terminate the loop.
			if(this.rivers[i] != null && this.rivers[i].containsPoint(point)) {
				result = i;
				break;
			}
		}

		return result;
	}

	/*
		Returns a 2D map representation of every river. For each point, we use 1 to denote
		'apart of a river', and 0 to denote 'not apart of a river'

		RETURNS:
			A map where each point that is 0 is not apart of a river, and 1 denotes the
			point is apart of a river.
	*/
	public PlanetMap getRiverMap() {
		PlanetMap result = new PlanetMap(this.width, this.height);

		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				if(getRiverOf(new Point(x, y)) != -1) {
					result.setData(x, y, 1);
				}
				else
					result.setData(x, y, 0);
			}
		}

		return result;
	}

	public PlanetMap getPrecipitationMap() {
		//return this.approxWaterDist;

		PlanetMap[] maps = new PlanetMap[3];
		// So we can scale the cloud map.
		PlanetMap cloud = this.cloudFreqMap.getCopy();
		PlanetMap precip;

		maps[0] = this.equatorMap.getCopy();
		maps[1] = getRiverMap().getCopy();
		maps[2] = this.approxWaterDist.getCopy();

		cloud.scaleBy(1.1f);
		maps[0].scaleBy(0.65f);
		maps[1].scaleBy(0.75f);
		maps[2].scaleBy(0.2f);

		precip = cloud.combineWith(maps);
		precip.sqrt();
		precip.blurr(5);
		precip.normalize();

		return precip;
	}
}
