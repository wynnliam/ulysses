// Liam Wynn, 1/6/2017, Ulysses

/*
	In Ulysses, a Planet uses many different PlanetMaps to represent various
	features. For example, we use a PlanetMap to describe the elevation of every
	point on the Planet. In addition, we use a PlanetMap to describe the thickness
	of every point on a Planet. There are countless examples of these.

	So what exactly is a PlanetMap? A PlanetMap is just a data structure that holds
	a data value for every point on a 2D surface. Naturally, we represent
	this with an array.
For memory efficieny's sake, we use a 1D array. To compute an index
	from a 2D point, we use the formula y * w + x, where (x, y) is the
	point, and w is the width of the PlanetMap. A PlanetMap maintains both a width
	and height dimension.
*/

package ulysses.planet.utilities;

public class PlanetMap
{
	// The width and height of the map.
	private final int width, height;
	// The data we want to store.
	private final float[] data;

	public PlanetMap(int width, int height)
	{
		if(width <= 0)
			width = 256;
		if(height <= 0)
			height = 128;

		this.width = width;
		this.height = height;

		this.data = new float[this.width * this.height];
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	/*
		Returns the data at a specified index.
		WARNING: THIS OPERATION ASSUMES INDEX IS VALID.

		ARGUMENTS:
			index - where we access the data at.

		RETURNS:
			the data value at index.
	*/
	public float getData(int index)
	{
		return this.data[index];
	}

	/*
		Returns the data at a specified point.
		We do so by converting the point into a unique index
		with the formula y * width + x.

		ARGUMENTS:
			(x, y): The point to index at.

		RETURNS:
			The data at the given point.
	*/
	public float getData(int x, int y)
	{
		return getData(y * this.width + x);
	}

	/*
		Sets the data at a specified index.
		WARNING: THIS OPERATION ASSUMES INDEX IS VALID.

		ARGUMENTS:
			index - where we set the data at.
			val - the value to set the data to.
	*/
	public void setData(int index, float val)
	{
		this.data[index] = val;
	}

	/*
		Sets the data at a specified point.
		We use the point to get an index with the formula
		y * width + x.

		ARGUMENTS:
			x, y - the point to access.
			val - the value to set our data to.
	*/
	public void setData(int x, int y, float val)
	{
		setData(y * this.width + x, val);
	}

	/*
		Normalizes the data in this map. It does so by scanning the
		list to find the maximum and minimum values of the map. Next
		it scans every point again, but sets the data value d to
		(d - min) / (max - min). Note that if max == min, every value
		becomes 0.0f.

		After calling this, every value will be between 0 and 1.
	*/
	public void normalize()
	{
		// Used to normalize every point.
		float max, min;
		// A value we divide every point by.
		float maxDist;

		// Assume the first point is both the max and min.
		// This way, we can compare against every other point.
		max = this.data[0];
		min = this.data[0];

		// First find the max and the min.
		for(int i = 0; i < this.data.length; ++i)
		{
			if(this.data[i] > max)
				max = this.data[i];
			if(this.data[i] < min)
				min = this.data[i];
		}

		maxDist = max - min;

		for(int i = 0; i < this.data.length; ++i)
		{
			if(maxDist == 0.0f)
				this.data[i] = 0.0f;
			else
				this.data[i] = (this.data[i] - min) / maxDist;
		}
	}

	/*
		Computes a map r where each point in r is the
		corresponding point value in this map squared plus
		the corresponding point value in each p squared. That is,
		for every point i and every j where 0 <= j < p.length:

		r[i] = this[i] ^ 2 + sum(p[j][i] ^ 2).

		ARGUMENTS:
			p - the maps to combine this one with

		RETURNS:
			null if p is null.
			Otherwise, it returns a new map that uses the combine
			formula specified above.
	*/
	public PlanetMap combineWith(PlanetMap[] p)
	{
		if(p == null || p[0].getWidth() != this.width || p[0].getHeight() != this.height)
			return null;

		PlanetMap r = new PlanetMap(this.width, this.height);

		for(int i = 0; i < this.data.length; ++i)
		{
			r.data[i] = this.data[i] * this.data[i];
			for(int j = 0; j < p.length; ++j)
				r.data[i] += p[j].data[i] * p[j].data[i];
		}

		return r;
	}

	/*
		Performs an element-wise square root operation.
	*/
	public void sqrt()
	{
		for(int i = 0; i < this.data.length; ++i)
			this.data[i] = (float)Math.sqrt(this.data[i]);
	}
}