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

		double seaLevelBio, pet;
		LatitudeBelt latitudeBelt;
		AltitudeBelt altitudeBelt;
		HumidityProvince humidityProvince;

		result.setBiotemperature(biotemp);
		result.setPrecipitation(precip);
		result.setAltitude(altitude);

		seaLevelBio = getSeaLevelBiotemp(biotemp, altitude);
		pet = getPET(biotemp, precip);

		latitudeBelt = getLatitudeBelt(seaLevelBio);
		altitudeBelt = getAltitudeBelt(biotemp);
		humidityProvince = getHumidityProvince(pet);

		result.setSeaLevelBiotemperature(seaLevelBio);
		result.setPotentialEvapotranspiration(pet);
		result.setLatitudeBelt(latitudeBelt);
		result.setAltitudeBelt(altitudeBelt);
		result.setHumidityProvince(humidityProvince);
		result.setLifezone(getLifezoneColorType(CHART.getLifezone(biotemp, precip)));

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

    /*
		Calculates the potential evapotranspiration from a biotemperature
		and precipitation. The formula for this 58.93 * biotemp / precip,
		according to the Holdridge System. Note that if the precipitation
		is 0, then we will assume the driest possible outcome, which is a
		PET of 64.00

		ARGUMENTS:
			biotemp - the biotemperature (from 0 to 48 degrees ceslius)
			precip - the precipitation (from 0 to 23000 mm)

		RETURNS:
			the potential evapotranspiration.
     */
    public static double getPET(double biotemp, double precip) {
        double result;
        
        if(precip == 0.0)
            result = 0.0;
        else
            result = 58.93 * biotemp / precip;
        
        return result;
    }

    /**
     * Calculates the latitude belt from a sea level biotemperature. It
     * essentially boils down to mapping the sea level biotemp to
     * a latitude belt based on what range of values the biotemp
     * falls under. Note how seaLevelBiotemp is a biotemperature without
     * influence from altitude. Thus, the latitude belt is biotemperature
     * behavior based solely on latitude. We apply a bit of fuzzyness to
     * the bounds of each range to make it more realistic.
     * @param seaLevelBiotemp the sea level biotemperature.
     * @return a latitude belt.
     */
    public static LatitudeBelt getLatitudeBelt(double seaLevelBiotemp)
    {
        LatitudeBelt result;
        
        if(0.0 <= seaLevelBiotemp && seaLevelBiotemp <= 1.68)
            result = LatitudeBelt.POLAR;
	else if(1.68 < seaLevelBiotemp && seaLevelBiotemp <= 3.36)
            result = LatitudeBelt.SUBPOLAR;
	else if(3.36 < seaLevelBiotemp && seaLevelBiotemp <= 6.72)
            result = LatitudeBelt.BOREAL;
	else if(6.72 < seaLevelBiotemp && seaLevelBiotemp <= 13.44)
            result = LatitudeBelt.COOL_TEMPERATE;
	else if(13.44 < seaLevelBiotemp && seaLevelBiotemp <= 26.89)
            result = LatitudeBelt.WARM_TEMPERATE;
	else
            result = LatitudeBelt.TROPICAL;
        
        return result;
    }
    
    /**
     * Determines the altitude belt from a biotemp. Like getLatitudeBelt,
     * we classify the biotemp as an altitude belt based on what range of
     * valies biotemp falls under. Note how biotemp is value that takes
     * that is somewhat based on altitude. Thus, we use it to predict the
     * altitude behavior.
     * @param biotemp the biotemperature.
     * @return an altitude belt
     */
    public static AltitudeBelt getAltitudeBelt(double biotemp)
    {
        AltitudeBelt result;
        
	if(0.0 <= biotemp && biotemp <= 1.5)
            result = AltitudeBelt.NIVAL;
	else if(1.5 < biotemp && biotemp <= 3.0)
            result = AltitudeBelt.ALPINE;
	else if(3.0 < biotemp && biotemp <= 6.0)
            result = AltitudeBelt.SUBALPINE;
	else if(6.0 < biotemp && biotemp <= 12.0)
            result = AltitudeBelt.MONTANE;
	else if(12.0 < biotemp && biotemp <= 24.0)
            result = AltitudeBelt.LOWER_MONTANE;
	else
            result = AltitudeBelt.BASAL;
        
        return result;
    }
    
    /**
     * Classifies the potential evapotranspiration into a humidity province.
     * @param pet the potential evapotranspiration.
     * @return a humidity province.
     */
    public static HumidityProvince getHumidityProvince(double pet)
    {
        HumidityProvince result;
        
        if(pet <= 0.03125)
            result = HumidityProvince.SATURATED;
	else if(pet <= 0.0625)
            result = HumidityProvince.SUBSATURATED;
	else if(pet <= 0.125)
            result = HumidityProvince.SEMISATURATED;
	else if(pet <= 0.25)
            result = HumidityProvince.SUPERHUMID;
	else if(pet <= 0.50)
            result = HumidityProvince.PERHUMID;
	else if(pet <= 1.00)
            result = HumidityProvince.HUMID;
	else if(pet <= 2.00)
            result = HumidityProvince.SUBHUMID;
	else if(pet <= 4.00)
            result = HumidityProvince.SEMIARID;
	else if(pet <= 8.00)
            result = HumidityProvince.ARID;
	else if(pet <= 16.00)
            result = HumidityProvince.PERARID;
	else if(pet <= 32.00)
            result = HumidityProvince.SUPERARID;
	else
            result = HumidityProvince.SEMIPARCHED;
        
        return result;
    }

	public static int getLifezoneColorType(int lifezoneID) {
		/*
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
		*/
        switch(lifezoneID)
        {
            case 0:
				return 1;
            case 1:
				return 2;
            case 2:
				return 3;
            case 3:
				return 4;
            case 4:
				return 0;
            case 5:
				return 5;
            case 6:
				return 6;
            case 7:
				return 7;
            case 8:
				return 8;
            case 9:
				return 0;
            case 10:
				return 9;
            case 11:
				return 11;
            case 12:
				return 6;
            case 13:
				return 7;
            case 14:
				return 8;
            case 15:
				return 0;
            case 16:
				return 9;
            case 17:
				return 12;
            case 18:
				return 10;
            case 19:
				return 6;
            case 20:
				return 7;
            case 21:
				return 8;
            case 22:
				return 0;
            case 23:
				return 9;
            case 24:
				return 12;
            case 25:
				return 13;
            case 26:
				return 10;
            case 27:
				return 6;
            case 28:
				return 7;
            case 29:
				return 8;
            default:
				return 0;
        }
	}
    
    /**
     * Using the index of a lifezone hexagon (see LifezoneChart), we can
     * deduce the type of lifezone. These are set according to the labels
     * from the Holdridge System itself.
     * @param lifezoneID the index of a specific hexagon in the LifezoneChart
     * @return the lifezone type.
     */
    public static LifezoneType getLifezoneType(int lifezoneID)
    {
        LifezoneType result;
        
        switch(lifezoneID)
        {
            case 0:
                result = LifezoneType.DRY_TUNDRA;
                break;
            case 1:
                result = LifezoneType.MOIST_TUNDRA;
                break;
            case 2:
                result = LifezoneType.WET_TUNDRA;
                break;
            case 3:
                result = LifezoneType.RAIN_TUNDRA;
                break;
            case 4:
                result = LifezoneType.DESERT;
                break;
            case 5:
                result = LifezoneType.DRY_SCRUB;
                break;
            case 6:
                result = LifezoneType.MOIST_FOREST;
                break;
            case 7:
                result = LifezoneType.WET_FOREST;
                break;
            case 8:
                result = LifezoneType.RAIN_FOREST;
                break;
            case 9:
                result = LifezoneType.DESERT;
                break;
            case 10:
                result = LifezoneType.DESERT_SCRUB;
                break;
            case 11:
                result = LifezoneType.STEPPE;
                break;
            case 12:
                result = LifezoneType.MOIST_FOREST;
                break;
            case 13:
                result = LifezoneType.WET_FOREST;
                break;
            case 14:
                result = LifezoneType.RAIN_FOREST;
                break;
            case 15:
                result = LifezoneType.DESERT;
                break;
            case 16:
                result = LifezoneType.DESERT_SCRUB;
                break;
            case 17:
                result = LifezoneType.WOODLAND;
                break;
            case 18:
                result = LifezoneType.DRY_FOREST;
                break;
            case 19:
                result = LifezoneType.MOIST_FOREST;
                break;
            case 20:
                result = LifezoneType.WET_FOREST;
                break;
            case 21:
                result = LifezoneType.RAIN_FOREST;
                break;
            case 22:
                result = LifezoneType.DESERT;
                break;
            case 23:
                result = LifezoneType.DESERT_SCRUB;
                break;
            case 24:
                result = LifezoneType.WOODLAND;
                break;
            case 25:
                result = LifezoneType.VERY_DRY_FOREST;
                break;
            case 26:
                result = LifezoneType.DRY_FOREST;
                break;
            case 27:
                result = LifezoneType.MOIST_FOREST;
                break;
            case 28:
                result = LifezoneType.WET_FOREST;
                break;
            case 29:
                result = LifezoneType.RAIN_FOREST;
                break;
            default:
                result = LifezoneType.DESERT;
                break;
        }
        
        return result;
    }
}
