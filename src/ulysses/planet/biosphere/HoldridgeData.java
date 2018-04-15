// Liam Wynn, 4/14/2018, Ulysses

/*
	TODO: Finish documentation!

	//A value from 0 degrees celsius to 48 degrees celsius.
	double biotemp;
	//The value that represents the precipitation. This is
	//from 62.5 to 22629.12
	double precip;
	//A large value from 0 to about 4000
	double altitude;
	//A number bounded from 0 to 48 degrees celsius.
	double seaLevelBio;
	//The potential evapotranspiration.
	double pet;
	//Tells us what latitudal behavior this climate would have.
	//Note that this is NOT indicitive of the latitude itself.
	int latitudeBelt;
	//Tells us the altitudinal belt behavior this climate would
	//have. Note that this is NOT indicitive of the altitude itself.
	int altitudeBelt;
	//The humidity province.
	int humidityProvince;
	//The lifezone this climate is apart of.
	int lifezone;
*/

package ulysses.planet;

public class HoldridgeData {
	// A value from 0 degrees celsius to 48 degrees celsius.
	private double biotemp;
	// The precipitation. A value from 62.5 to 22629.12.
	private double precip;
	// The altitude. A number from 0 to 4000.
	private double altitude;

	// The biotemperature adjusted for height. A value from 0 to
	// 48 degrees celsius.
	private double seaLevelBiotemp;
	// The potential evapotranspiration.
	private double pet;

	// The latitude behavior of this climate.
	private int latitudeBelt;
	// The altitude behavior of this climate.
	private int altitudeBelt;
	// The humidity province of this climate.
	private int humidityProvince;

	// The actual lifezone of this climate is apart of.
	private int lifezone;

	public HoldridgeData() {
		this.biotemp = 0.0;
		this.precip = 0.0;
		this.altitude = 0.0;

		this.seaLevelBiotemp = 0.0;
		this.pet = 0.0;

		this.latitudeBelt = 0;
		this.altitudeBelt = 0;
		this.humidityProvince = 0;

		this.lifezone = 0;
	}
}
