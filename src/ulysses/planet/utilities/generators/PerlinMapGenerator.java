// Liam Wynn, 12/28/2017, Ulysses

/*
	Generates a general purpose map of perlin noise.

	The algorithm used for noise generation is found here:
	devmag.org.za/2009/04/25/perlin-noise/

	Note that this is not actually a perlin noise algorithm.
	As the original author notes, he mistakenly described it
	as perlin noise.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;
// For choosing random noise values.
import java.util.Random;

public class PerlinMapGenerator extends MapGenerator
{
	// Describe noise generating fields.
	// The number of smooth noise maps to combine
	private int octaveCount;
	// Used in blending the smooth noise maps together.
	private float persistence;

	public PerlinMapGenerator(Random rand)
	{
		super(rand);

		this.octaveCount = 8;
		this.persistence = 0.75f;
	}

	public int getOctaveCount()
	{
		return this.octaveCount;
	}

	public void setOctaveCount(int val)
	{
		if(val <= 0)
			val = 1;

		this.octaveCount = val;
	}

	public float getPersistence()
	{
		return this.persistence;
	}

	public void setPersistence(float val)
	{
		if(val <= 0)
			val = 0.5f;
		if(val > 1.0f)
			val = 1.0f;

		this.persistence = val;
	}

	/*
		This is the main driver for the map generation procedure.
		First, it generates random noise. Next, it uses that noise
		to produce a series of smooth noise maps. Finally, it combines
		these maps to make a final noise map.

		RETURNS:
			A randomly generated noise map.
	*/
	public PlanetMap generateMap()
	{
		// What we will return.
		PlanetMap result = new PlanetMap(this.width, this.height);
		// Use this to produce final, smooth noise.
		float[][] baseNoise = generateWhiteNoise();
		float[][] smoothNoise = getSmoothNoise(baseNoise);
		// Use this to index into result.
		int index;

		// Final step, add noise to result.
		for(int x = 0; x < this.width; ++x)
		{
			for(int y = 0; y < this.height; ++y)
				result.setData(x, y, smoothNoise[x][y]);
		}

		result.normalize();

		return result;
	}

	/*
		Computes the smooth noise from some base noise. First, we compute smooth noise
		maps for each octave. Then we combine them into our final map.

		ARGUMENTS:
			base - what we calculate the smooth noise from.

		RETURNS:
			the smooth noise.
	*/
	private float[][] getSmoothNoise(float[][] base)
	{
		float[][] result = getEmptyArray();
		float[][][] smoothMaps = new float[this.octaveCount][][];
		float amplitude = 1.0f;

		// Get the smooth noise for each octave
		for(int i = 0; i < this.octaveCount; ++i)
			smoothMaps[i] = getSmoothNoise(base, i);

		// Blend the noise together.
		for(int o = this.octaveCount - 1; o >= 0; --o)
		{
			amplitude *= this.persistence;

			for(int x = 0; x < this.width; ++x)
			{
				for(int y = 0; y < this.height; ++y)
					result[x][y] += smoothMaps[o][x][y] * amplitude;
			}
		}

		return result;
	}

	/*
		Computes the smooth noise from base noise at a given octave. We do
		so by going through each point (x*2^k, y*2^k) for every (x, y) and
		interpolating the points.

		ARGUMENTS:
			baseNoise - the noise to smooth.
			octave - computes our period and frequency, which are then
			used to interpolate our points.

		RETURNS:
			Smooth noise.
	*/
	private float[][] getSmoothNoise(float[][] baseNoise, int octave)
	{
		// What we will return.
		float[][] result = getEmptyArray();

		// Stores 2^k. k is the currenct octave.
		int period = 1 << octave;
		float freq = 1.0f / period;

		// Stores values for blending.
		int sampX0, sampX1;
		int sampY0, sampY1;
		float hBlend, vBlend;
		float top, bot;

		for(int x = 0; x < this.width; ++x)
		{
			sampX0 = (x / period) * period;
			sampX1 = (sampX0 + period) % this.width;
			hBlend = (x - sampX0) * freq;

			for(int y = 0; y < this.height; ++y)
			{
				sampY0 = (y / period) * period;
				sampY1 = (sampY0 + period) % height;
				vBlend = (y - sampY0) * freq;

				top = interp(baseNoise[sampX0][sampY0],
							 baseNoise[sampX1][sampY0],
							 hBlend);
				bot = interp(baseNoise[sampX0][sampY1],
							 baseNoise[sampX1][sampY1],
							 hBlend);

				result[x][y] = interp(top, bot, vBlend);
			}
		}

		return result;
	}

	/*
		Performs linear interpolation on two values.
	*/
	private float interp(float x0, float x1, float alpha)
	{
		return x0 * (1 - alpha) + alpha * x1;
	}

	/*
		Generates the base noise from which our smooth noise arrays will come from.
		We create width by height noise map, and use a random number generator to get
		values from 0 to 1.

		RETURNS:
			a 2D map of random floats between 0 and 1.
	*/
	private float[][] generateWhiteNoise()
	{
		// What we will return.
		float[][] result = new float[this.width][];

		for(int x = 0; x < this.width; ++x)
		{
			result[x] = new float[this.height];
			for(int y = 0; y < this.height; ++y)
				result[x][y] = (float)rand.nextDouble();
		}

		return result;
	}

	/*
		Creates a width x height empty array (array of zeroes).
	*/
	private float[][] getEmptyArray()
	{
		float[][] result = new float[this.width][];

		for(int x = 0; x < this.width; ++x)
			result[x] = new float[this.height];

		return result;
	}
}
