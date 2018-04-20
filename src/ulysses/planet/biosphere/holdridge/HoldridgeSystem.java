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
}
