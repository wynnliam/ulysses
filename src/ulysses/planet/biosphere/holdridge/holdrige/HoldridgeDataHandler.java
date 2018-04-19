package holdrige;

import holdrige.HoldridgeSystem.*;
import javax.swing.JTextArea;

/**
 * This is a simple utility class that stores all of the Holdridge Data for
 * a land Province. The goal is to refactor this code into a seperate class.
 * 
 * @author Liam Wynn
 */
public class HoldridgeDataHandler
{
    // The average temperature of the region in celsius. A value from 0 to 48.0;
    private double biotemp;
    // The average prepitation in millimeters. A value from 0 to 23000
    private double precip;
    // The altitude of the province in meters. This is a value from
    // 0 to 4000;
    private double altitude;
    
    // The sea level bio temeperature. This is what the temperature would be
    // if we had no altitude.
    private double seaLevelBiotemp;
    // The potential evapotranspiration.
    private double pet;
    
    // Tells us what latitudinal behavior this province has.
    private LatitudeBelt latitudeBelt;
    // Tells us the altitudinal behavior of the province.
    private AltitudeBelt altitudeBelt;
    // Tells us the humidity province.
    private HumidityProvince humidityProvince;
    // Tells us the lifezone this is apart of
    private LifezoneType lifezoneType;
    
    public HoldridgeDataHandler()
    {
        this.biotemp = 0.0;
        this.precip = 0.0;
        this.altitude = 0.0;
        
        this.seaLevelBiotemp = 0.0;
        this.pet = 0.0;
        
        this.latitudeBelt = HoldridgeSystem.LatitudeBelt.BOREAL;
        this.altitudeBelt = HoldridgeSystem.AltitudeBelt.BASAL;
        this.humidityProvince = HoldridgeSystem.HumidityProvince.ARID;
        this.lifezoneType = HoldridgeSystem.LifezoneType.DESERT;
    }
    
    /**
     * @return the biotemperature
     */
    public double getBiotemperature()
    {
        return this.biotemp;
    }

    /**
     * @param biotemp the biotemp to set. Note that biotemp must
     * be from 0 to 48 degrees cesius.
     */
    public void setBiotemperature(double biotemp)
    {
        this.biotemp = biotemp;
        
        this.biotemp = this.biotemp >= 0.0 ? this.biotemp : 0.0;
        this.biotemp = this.biotemp <= 48.0 ? this.biotemp : 48.0;
    }

    /**
     * @return the precip
     */
    public double getPrecipitation()
    {
        return this.precip;
    }

    /**
     * @param precip the precip to set. Note that this must be a value from
     * 0 to 23000.
     */
    public void setPrecipitation(double precip)
    {
        this.precip = precip;
        
        this.precip = this.precip >= 0.0 ? this.precip : 0.0;
        this.precip = this.precip <= 23000.0 ? this.precip : 23000.0;
    }

    /**
     * @return the altitude
     */
    public double getAltitude()
    {
        return this.altitude;
    }

    /**
     * @param altitude the altitude to set. Note that this must be a value
     * from 0 to 4000 meters.
     */
    public void setAltitude(double altitude)
    {
        this.altitude = altitude;
        
        this.altitude = this.altitude >= 0.0 ? this.altitude : 0.0;
        this.altitude = this.altitude <= 4000 ? this.altitude : 4000.0;
    }

    /**
     * @return the seaLevelBiotemp
     */
    public double getSeaLevelBiotemperature()
    {
        return this.seaLevelBiotemp;
    }
    
    /**
     * @return the pet
     */
    public double getPotentialEvapotranspiration()
    {
        return this.pet;
    }

    /**
     * @return the latitudeBelt
     */
    public LatitudeBelt getLatitudeBelt()
    {
        return this.latitudeBelt;
    }

    /**
     * @return the altitudeBelt
     */
    public AltitudeBelt getAltitudeBelt()
    {
        return this.altitudeBelt;
    }

    /**
     * @return the humidityProvince
     */
    public HumidityProvince getHumidityProvince()
    {
        return this.humidityProvince;
    }
    
    /**
     * 
     * @return the lifezoneType.
     */
    public LifezoneType getLifezoneType()
    {
        return this.lifezoneType;
    }
    
    /**
     * Acts as the main driver for calculating all Holdridge climate data.
     * This includes everything that is not the biotemp, precipitation, or
     * altitude.
     */
    public void recalculateData()
    {
        int lifezoneHexagon;
        
        this.seaLevelBiotemp = HoldridgeSystem.getSeaLevelBiotemp(this.biotemp, this.altitude);
        this.pet = HoldridgeSystem.getPET(this.biotemp, this.precip);
        
        this.latitudeBelt = HoldridgeSystem.getLatitudeBelt(this.seaLevelBiotemp);
        this.altitudeBelt = HoldridgeSystem.getAltitudeBelt(this.biotemp);
        this.humidityProvince = HoldridgeSystem.getHumidityProvince(this.pet);
        
        lifezoneHexagon = HoldridgeSystem.CHART.getLifezone(this.biotemp, this.precip);
        this.lifezoneType = HoldridgeSystem.getLifezoneType(lifezoneHexagon);
    }
    
    /**
     * Prints all of the data to the console.
     * @param printTo the text area we will print to.
     */
    public void display(JTextArea printTo)
    {
        printTo.append("Biotemperature: " + this.biotemp + '\n');
        printTo.append("Annual precipitation: " + this.precip + '\n');
        printTo.append("Altitude: " + this.altitude + '\n');
        
        printTo.append("Sea Level Biotemp: " + this.seaLevelBiotemp + '\n');
        printTo.append("Potential evapotranspiration: " + this.pet + '\n');
        
        printTo.append("Latitude Belt: " + this.latitudeBelt.toString() + '\n');
        printTo.append("Altitude Belt: " + this.altitudeBelt.toString() + '\n');
        printTo.append("Humidity Province: " + this.humidityProvince.toString() + '\n');
        
        printTo.append("Lifezone: " + this.lifezoneType.toString() + '\n');
    }
}
