// Liam Wynn, 4/29/2018, Ulysses

/*
	Manages a collection of similar pieces of data.
	These represent the min and max values for altitude,
	temperature, and precipitation used by the Holdridge
	System. We translate values from 0 to 1 to the scale
	of min to max. This serves as a class for keeping
	the generator organized.
*/

package ulysses.planet.utilities.generators;

public class BiosphereGeneratorParams {
	// heights, temperatures, and precipitations are
	// values from 0 to 1. We use these to set the actual
	// values the Holdridge System can use.
	public double minAltitude, maxAltitude;
	public double minTemp, maxTemp;
	public double minPrecip, maxPrecip;

	public BiosphereGeneratorParams() {
		// Default values set according to Holdridge
		// System.
		this.minAltitude = 0.0;
		this.maxAltitude = 4000.0;
		this.minTemp = 0.0;
		this.maxTemp = 48.0;
		this.minPrecip = 62.5;
		this.maxPrecip = 22629.12;
	}
}
