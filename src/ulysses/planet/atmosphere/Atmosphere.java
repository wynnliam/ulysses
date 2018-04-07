// Liam Wynn, 4/7/2018, Ulysses

/*
	The Atmosphere is responsible for a Planet's
	temperature data. The temperature data is represented
	in the form of the Temperature Map - which is the
	temperatures of every land point. The Temperature map
	is a combination of several submaps as follows:

	Altitude - The height of each point.
	Equator - The distance each point is from a specified
	equator.
	Wind - Essentially a smooth noise map that represents wind
	patterns and their effects on the temperature.
	Distance to sea - TODO: Finish me!

	We choose these because scientifically these factors relate
	to the temperature behavior in a climate system. For altitude,
	the temperature decreases as altitude increases. For distance from
	the equator, the temperature decreases as the distance increases.
	Wind patterns affect temperature in numerous and complicated ways.
	As such, we simply use smooth noise to represent those factors.

	TODO: Finish me!
*/

package ulysses.planet;

import ulysses.planet.utilities.PlanetMap;

public class Atmosphere {
	// Dimensions of the world.
	private int width, height;

	// higher altitudes (starting with sea level) yield cooler
	// temperatures.
	private PlanetMap heightMap;
	// Use this to make a modified heightMap (see getTemperatureMap).
	private float seaLevel;

	public Atmosphere(int width, int height) {
		setWidth(width);
		setHeight(height);

		this.heightMap = null;
		this.seaLevel = 0;
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
}
