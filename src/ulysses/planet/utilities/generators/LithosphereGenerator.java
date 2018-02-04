// Liam Wynn, 12/21/2017, Ulysses

/*
	Responsible for generating a planet's Lithosphere.
	We do this by generating three maps: a tectonics map,
	a crust thickness map, and an orogenics map. I describe
	what these mean in the Lithosphere class. We take these
	maps and add them to a Lithosphere which we then return
	to the user.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;
import ulysses.planet.*;

import java.util.Random;

public class LithosphereGenerator
{
	// The dimensions of the resulting map.
	private int width, height;
	// A value from 0 to 1 that is the percentage
	// of land the planet will have. Note that the
	// percent ocean is 1 - (%land + %mountains)
	private float percentLand;
	// A value from 0 to 1 that is the percentage of
	// mountains.
	private float percentMountains;

	// The generators for each relevant map
	private MapGenerator tectonicsMapGenerator;
	private MapGenerator thicknessMapGenerator;
	private MapGenerator orogenicsMapGenerator;

	public LithosphereGenerator()
	{
		this.width = 256;
		this.height = 128;

		this.percentLand = 0.3f;
		this.percentMountains = 0.058f;

		this.tectonicsMapGenerator = null;
		this.thicknessMapGenerator = null;
		this.orogenicsMapGenerator = null;
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

	public MapGenerator getTectonicsMapGenerator()
	{
		return this.tectonicsMapGenerator;
	}

	public void setTectonicsMapGenerator(MapGenerator val)
	{
		this.tectonicsMapGenerator = val;
	}

	public MapGenerator getThicknessMapGenerator()
	{
		return this.thicknessMapGenerator;
	}

	public void setThicknessMapGenerator(MapGenerator val)
	{
		this.thicknessMapGenerator = val;
	}

	public MapGenerator getOrogenicsMapGenerator()
	{
		return this.orogenicsMapGenerator;
	}

	public void setOrogenicsMapGenerator(MapGenerator val)
	{
		this.orogenicsMapGenerator = val;
	}

	/*
		Generates a random lithosphere. It does so by generating the tectonics,
		thickness, and orogenics maps for a lithosphere, and then returns the result.
		This operation will return null if any of the generators are null.

		Note that the width and height of the individual generators is overloaded to
		the values set in this class. This way, all of the maps are of the same size.

		RETURNS:
			A new lithosphere, or null if any of the generators are null.
	*/
	public Lithosphere generateLithosphere()
	{
		if(this.tectonicsMapGenerator == null ||
		   this.thicknessMapGenerator == null ||
		   this.orogenicsMapGenerator == null)
		{
			return null;
		}

		Lithosphere result = new Lithosphere(this.width, this.height);

		// Set the properties for each map generator

		this.tectonicsMapGenerator.setWidth(this.width);
		this.tectonicsMapGenerator.setHeight(this.height);

		this.thicknessMapGenerator.setWidth(this.width);
		this.thicknessMapGenerator.setHeight(this.height);

		this.orogenicsMapGenerator.setWidth(this.width);
		this.orogenicsMapGenerator.setHeight(this.height);

		// Set our data.
		result.setTectonicsMap(this.tectonicsMapGenerator.generateMap());
		result.setThicknessMap(this.thicknessMapGenerator.generateMap());
		result.setOrogenicMap(this.orogenicsMapGenerator.generateMap());

		result.setPercentLand(this.percentLand);
		result.setPercentMountains(this.percentMountains);

		return result;
	}
}
