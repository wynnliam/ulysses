// Liam Wynn, 4/19/2018, Ulysses

/*
	This manages the entire process of classifying a point
	using the Holdridge Lifezone system. Since the overall
	process is somewhat involved, we can keep method for it
	here.

	TODO: Explain the entire process here.
*/

package ulysses.planet.holdridge;

public class HoldridgeSystem {
	// Stores every defined hexagon in the chart.
	public static final LifezoneChart CHART = new LifezoneChart();

	/*
		Describes the latitude biological behavior of a given
		climate. This is not the same as the latitude of a climate
		itself, but describes the biological and temperature behavior.
	*/
	public static enum LatitudeBelt {
		POLAR,
		SUBPOLAR,
		BOREAL,
		COOL_TEMPERATE,
		WARM_TEMPERATE,
		TROPICAL
	};

	/*
		Describes the altitude behavior of the biology for a given
		climate/region. This is not the same as the altitude of the
		climate/region itself.
	*/
	public static enum AltitudeBelt {
		NIVAL,
		ALPINE,
		SUBALPINE,
		MONTANE,
		LOWER_MONTANE,
		BASAL
	};

	// A qualitative description of the humidity of a climate/region.
    public static enum HumidityProvince {
		SATURATED,
		SUBSATURATED,
		SEMISATURATED,
		SUPERHUMID,
		PERHUMID,
		HUMID,
		SUBHUMID,
		SEMIARID,
		ARID,
		PERARID,
		SUPERARID,
		SEMIPARCHED
    };
    
	// A qualitative description for the overall lifezone.
    public static enum LifezoneType {
		DESERT,
		DRY_TUNDRA,
		MOIST_TUNDRA,
		WET_TUNDRA,
		RAIN_TUNDRA,
		DRY_SCRUB,
		MOIST_FOREST,
		WET_FOREST,
		RAIN_FOREST,
		DESERT_SCRUB,
		DRY_FOREST,
		STEPPE,
		WOODLAND,
		VERY_DRY_FOREST
    };

	/*
		Computes all climate data given a biotemperature, precipitation, and altitude value.
		TODO: Finish me!

		ARGUMENTS:
			biotemp - the temperature in celsius.
			precip - annual precipitation in milimeters
			altitude - height of a position in meters.

		RETURNS:
			a HoldridgeData object describing a given life zone.
	*/
	public static HoldridgeData computeData(double biotemp, double precip, double altitude) {
		HoldridgeData result = new HoldridgeData();

		double seaLevelBio;

		result.setBiotemperature(biotemp);
		result.setPrecipitation(precip);
		result.setAltitude(altitude);

		seaLevelBio = getSeaLevelBiotemp(biotemp, altitude);

		result.setSeaLevelBiotemperature(seaLevelBio);

		return result;
	}

    /*
		Calculates the sea-level biotemperature from a given biotemp and
		precip. According to the Holdridge System, the sea level biotemp
		is taking the regular biotemp and adding 6 degrees celsius for
		every 1000 meters in altitude.

		ARGUMENTS:
			biotemp - a biotemp from 0 to 48 degrees celsius.
			altitude - an altitude from 0 to 4000 meters

		RETURNS:
			a sea level biotemperature
     */
    public static double getSeaLevelBiotemp(double biotemp, double altitude) {
        double result;
        
        result = biotemp + (6.0 * altitude / 1000);
        
        result = result >= 0.0 ? result : 0.0;
        result = result <= 48.0 ? result : 48.0;
        
        return result;
    }
}
