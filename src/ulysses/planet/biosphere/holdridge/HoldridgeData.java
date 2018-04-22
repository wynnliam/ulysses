// Liam Wynn, 4/14/2018, Ulysses

/*
	Stores all Holdridge climate data for a given point.
	Please see HoldridgeSystem for explanation of all components.

	TODO: Make belts, humidity, lifezone members of enumerators.
*/

package ulysses.planet.holdridge;

import ulysses.planet.holdridge.HoldridgeSystem.*;

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
	private LatitudeBelt latitudeBelt;
	// The altitude behavior of this climate.
	private AltitudeBelt altitudeBelt;
	// The humidity province of this climate.
	private HumidityProvince humidityProvince;

	// The actual lifezone of this climate is apart of.
	private int lifezone;

	public HoldridgeData() {
		this.biotemp = 0.0;
		this.precip = 0.0;
		this.altitude = 0.0;

		this.seaLevelBiotemp = 0.0;
		this.pet = 0.0;

		this.latitudeBelt = LatitudeBelt.POLAR;
		this.altitudeBelt = AltitudeBelt.NIVAL;
		this.humidityProvince = HumidityProvince.SATURATED;

		this.lifezone = 0;
	}

	public double getBiotemperature() {
		return this.biotemp;
	}

	public void setBiotemperature(double val) {
		if(val < 0.0 || val > 48.0)
			val = 0.0;

		this.biotemp = val;
	}

	public double getPrecipitation() {
		return this.precip;
	}

	public void setPrecipitation(double val) {
		if(val < 62.5 || val > 22629.12)
			val = 62.5;

		this.precip = val;
	}

	public double getAltitude() {
		return this.altitude;
	}

	public void setAltitude(double val) {
		if(val < 0 || val > 4000)
			val = 0;

		this.altitude = val;
	}

	public double getSeaLevelBiotemperature() {
		return this.seaLevelBiotemp;
	}

	public void setSeaLevelBiotemperature(double val) {
		if(val < 0 || val > 48)
			val = 0;

		this.seaLevelBiotemp = val;
	}

	public double getPotentialEvapotranspiration() {
		return this.pet;
	}

	public void setPotentialEvapotranspiration(double val) {
		this.pet = val;
	}

	public LatitudeBelt getLatitudeBelt() {
		return this.latitudeBelt;
	}

	public void setLatitudeBelt(LatitudeBelt val) {
		this.latitudeBelt = val;
	}

	public AltitudeBelt getAltitudeBelt() {
		return this.altitudeBelt;
	}

	public void setAltitudeBelt(AltitudeBelt val) {
		this.altitudeBelt = val;
	}

	public HumidityProvince getHumidityProvince() {
		return this.humidityProvince;
	}

	public void setHumidityProvince(HumidityProvince val) {
		this.humidityProvince = val;
	}

	public int getLifezone() {
		return this.lifezone;
	}

	public void setLifezone(int val) {
		this.lifezone = val;
	}
}
