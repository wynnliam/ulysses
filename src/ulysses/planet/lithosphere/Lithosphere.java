// Liam Wynn, 12/21/2017, Ulysses

/*
	The Lithosphere describes the rough geologic features
	of a Planet. It stores three maps: Tectonics, Thickness,
	and Orogenic.

	The Tectonics map describes the amount of tectonic activity
	happening at each location. In this case, 1.0 describes
	high tectonic activity, and 0.0 would be low tectonic
	activity. When I say 'tectonic activity', I mean a combination
	of various plate tectonic data. For example, this would
	include the age of crust for each plate, where collisions
	are happening, the density of the plate crust, etc. We combine
	all of these features into one map.

	The Thickness map tells us the thickness of the crust
	at specific locations.

	Finally, the Orogenic map is just a general-purpose
	map to describe geologic features of the world. If you
	were to try to find the real-world scientific basis of
	this map, the closest aproximation would be geologic
	provinces. Realistically, a map like this would be
	a function of several other maps. This would be like
	the tectonics map insofar as its a single map to
	represent multiple real-world phenomena.

	We combine each of these maps to create a final height
	map.
*/

package ulysses.planet;

// For storing our planet maps.
import ulysses.planet.utilities.PlanetMap;
// For sorting our normalized crust values
import java.awt.geom.Point2D;
// For choosing heights in generating the heightmap.
import java.util.Random;

public class Lithosphere
{
	// The dimensions of the world.
	private int width, height;

	// A value from 0 to 1 that is the percentage
	// of land the planet will have. Note that the
	// percent ocean is 1 - (%land + %mountains)
	private float percentLand;
	// A value from 0 to 1 that is the percentage of
	// mountains.
	private float percentMountains;

	// Describes the tectonics features of each map. This
	// would be essentially a combination of age, tectonic activity,
	// etc.
	private PlanetMap tectonicsMap;
	// Describes the thickness of the crust at each point.
	private PlanetMap thicknessMap;
	// Primarily for adding extra details to the overall lithosphere.
	private PlanetMap orogenicMap;

	public Lithosphere(int width, int height)
	{
		setWidth(width);
		setHeight(height);

		this.tectonicsMap = null;
		this.thicknessMap = null;
		this.orogenicMap = null;

		this.percentLand = 0.3f;
		this.percentMountains = 0.058f;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int val)
	{
		// Use some default value if the
		// input is bad.
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
		// Use some default value if the
		// input is bad.
		if(val <= 0)
			val = 128;

		this.height = val;
	}

	public float getPercentLand()
	{
		return this.percentLand;
	}

	public void setPercentLand(float val)
	{
		if(val < 0.0f || val > 1.0f)
			val = 0.3f;

		this.percentLand = val;
	}

	public float getPercentMountains()
	{
		return this.percentMountains;
	}

	public void setPercentMountains(float val)
	{
		if(val < 0.0f || val > 1.0f)
			val = 0.058f;

		this.percentMountains = val;
	}

	public PlanetMap getTectonicsMap()
	{
		return this.tectonicsMap;
	}

	public void setTectonicsMap(PlanetMap val)
	{
		this.tectonicsMap = val;
	}

	public PlanetMap getThicknessMap()
	{
		return this.thicknessMap;
	}

	public void setThicknessMap(PlanetMap val)
	{
		this.thicknessMap = val;
	}

	public PlanetMap getOrogenicMap()
	{
		return this.orogenicMap;
	}

	public void setOrogenicMap(PlanetMap val)
	{
		this.orogenicMap = val;
	}

	/*
		Uses the tectonics, thickness, and orogenics maps to produce a heightmap.
		We do so by essentially treating every value in each map as a component in a
		vector. We take this vector and compute its magnitude. For example,
		given point point (x, y), we get the vector (t, h, o), where t is the
		tectonics value at (x, y); h is the thickness value at (x, y); and o is
		the orogenics value at (x, y). The height at (x, y) thus is the magnitude
		of (t, h, o).

		Next, we normalize every data point. Then, we sort every point based on its height
		from lowest to heighest. The first set of these points is set to submerged oceanic heights.
		The next set are non-mountainous land. The final set are mountainous land.

		ARGUMENTS:
			minMtn - the minimum value (between 0 and 1) that constitutes mountainous land.
			minLnd - the minimum value (between 0 and 1) that constitutes land.
	*/
	public PlanetMap getHeightMap(float minMtn, float minLnd)
	{
		if(this.tectonicsMap == null || this.thicknessMap == null || this.orogenicMap == null)
			return null;

		// What we will return.
		PlanetMap height;
		// Used to normalize the data.
		float min, max;
		int len = this.width * this.height;
		// Sort these to compute the mountain, land, and ocean values.
		Point2D.Float[] temp;
		int lndLimit, seaLimit;
		// The maximum elevation that is still considered submerged.
		// Think of this as the minimum depth of the sea.
		float maxSea = minLnd - 0.001f;
		// The percentage of the world that is sea
		float percentSea = 1.0f - (this.percentMountains + this.percentLand);

		height = this.tectonicsMap.combineWith(new PlanetMap[] { this.thicknessMap, this.orogenicMap });
		height.sqrt();

		// Normalize the data.
		height.normalize();

		temp = height.getSortedPoints();

		// If it is negative, make it 0.
		maxSea = maxSea < 0.0f ? 0.0f : maxSea;

		seaLimit = (int)(len * percentSea);
		lndLimit = (int)(len * this.percentLand);

		// Clamp all points based on what terrain they are supposed to be.
		// This way, our heightmap has the correct percentages of everything.

		clampHeight(height, temp, 0, seaLimit, 0.0f, maxSea);
		// Classify the next number of points land.
		clampHeight(height, temp, seaLimit, seaLimit + lndLimit, minLnd, minMtn - 0.001f);
		// Classify the remaining points mountainous.
		clampHeight(height, temp, seaLimit + lndLimit, len, minMtn, 1.0f);

		return height;
	}

	/*
		Iterates through a portion of the height map and clamps the values according
		to a specified bound. The way we traverse the height map is through a list
		of 2D points called temp. Temp sorts the height map from smallest to largest.
		It is a set of 2D points because we can store the index of each point, as
		well as its value. We use this method to simplify setting values in the
		final height map.

		ARGUMENTS:
			height - stores the height values
			temp - stores the height values sorted by height.
			start - where we want to start clamping the temp array.
			end - where we want to stop clamping. We do not clamp the value at end.
			min - the lower bound to clamp by.
			max - the upper bound to clamp by.
	*/
	private void clampHeight(PlanetMap height, Point2D.Float[] temp, int start, int end, float min, float max)
	{
		int currIndex;
		float currVal;

		for(int i = start; i < end; ++i)
		{
			currIndex = (int)temp[i].getX();
			currVal = (float)temp[i].getY();

			currVal = currVal > max ? max : currVal;
			currVal = currVal < min ? min : currVal;

			height.setData(currIndex, currVal);
		}
	}
}
