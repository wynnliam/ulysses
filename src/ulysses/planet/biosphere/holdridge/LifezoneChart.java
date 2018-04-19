// Liam Wynn, 4/19/2018, Ulysses

/*
 * The lifezone chart in the Holdridge System is how we classify biotemperatures
 * and precipitation values. In essence, we represent our chart as an array
 * of hexagons. When we want to find the specific life zone given a biotemp
 * and precipitation, we scan through this array and find the "closest" lifezone.
 * Since each lifezone is a hexagon, we can take the 6 points and calculate
 * the centroid of the hexagon. From there, we simply calculate the distance.
 */
public class LifezoneChart {
    // Tells us the number of hexagons used in the chart.
    public static final int CHART_SIZE = 30;
    // The number of points in each hexagon.
    public static final int NUM_POINTS = 6;
    
    private final Lifezone[] lifezones;
    
    public LifezoneChart() {
        this.lifezones = new Lifezone[CHART_SIZE];
        
        for(int i = 0; i < CHART_SIZE; ++i)
            this.lifezones[i] = new Lifezone();
        
        //Subpolar
        //Dry Tundra
        lifezones[0].points[0].biotemp = 2.121160699;
        lifezones[0].points[0].precip = 62.5;
        lifezones[0].points[1].biotemp = 3.0;
        lifezones[0].points[1].precip = 88.395;
        lifezones[0].points[2].biotemp = 3.0;
        lifezones[0].points[2].precip = 125.0;
        lifezones[0].points[3].biotemp = 2.121160699;
        lifezones[0].points[3].precip = 125.0;
        lifezones[0].points[4].biotemp = 1.5;
        lifezones[0].points[4].precip = 88.395;
        lifezones[0].points[5].biotemp = 1.5;
        lifezones[0].points[5].precip = 62.5;

        //Moist Tundra
        lifezones[1].points[0].biotemp = 2.121160699;
        lifezones[1].points[0].precip = 125.0;
        lifezones[1].points[1].biotemp = 3.0;
        lifezones[1].points[1].precip = 176.69;
        lifezones[1].points[2].biotemp = 3.0;
        lifezones[1].points[2].precip = 250.0;
        lifezones[1].points[3].biotemp = 2.121160699;
        lifezones[1].points[3].precip = 250.0;
        lifezones[1].points[4].biotemp = 1.5;
        lifezones[1].points[4].precip = 176.79;
        lifezones[1].points[5].biotemp = 1.5;
        lifezones[1].points[5].precip = 125.0;

        //Wet Tundra
        lifezones[2].points[0].biotemp = 2.121160699;
        lifezones[2].points[0].precip = 250.0;
        lifezones[2].points[1].biotemp = 3.0;
        lifezones[2].points[1].precip = 353.58;
        lifezones[2].points[2].biotemp = 3.0;
        lifezones[2].points[2].precip = 500.0;
        lifezones[2].points[3].biotemp = 2.121160699;
        lifezones[2].points[3].precip = 500.0;
        lifezones[2].points[4].biotemp = 1.5;
        lifezones[2].points[4].precip = 353.58;
        lifezones[2].points[5].biotemp = 1.5;
        lifezones[2].points[5].precip = 250.0;

        //Rain Tundra
        lifezones[3].points[0].biotemp = 2.121160699;
        lifezones[3].points[0].precip = 500.0;
        lifezones[3].points[1].biotemp = 3.0;
        lifezones[3].points[1].precip = 707.16;
        lifezones[3].points[2].biotemp = 3.0;
        lifezones[3].points[2].precip = 1000.0;
        lifezones[3].points[3].biotemp = 2.121160699;
        lifezones[3].points[3].precip = 1000.0;
        lifezones[3].points[4].biotemp = 1.5;
        lifezones[3].points[4].precip =  707.16;
        lifezones[3].points[5].biotemp = 1.5;
        lifezones[3].points[5].precip = 500.0;

        //Boreal
        //Desert
        lifezones[4].points[0].biotemp = 4.242321398;
        lifezones[4].points[0].precip = 62.5;
        lifezones[4].points[1].biotemp = 6.0;
        lifezones[4].points[1].precip = 88.395;
        lifezones[4].points[2].biotemp = 6.0;
        lifezones[4].points[2].precip = 125.0;
        lifezones[4].points[3].biotemp = 4.242321398;
        lifezones[4].points[3].precip = 125.0;
        lifezones[4].points[4].biotemp = 3.0;
        lifezones[4].points[4].precip =  88.395;
        lifezones[4].points[5].biotemp = 3.0;
        lifezones[4].points[5].precip = 62.5;

        //Dry Scrub
        lifezones[5].points[0].biotemp = 4.242321398;
        lifezones[5].points[0].precip = 125.0;
        lifezones[5].points[1].biotemp = 6.0;
        lifezones[5].points[1].precip = 176.79;
        lifezones[5].points[2].biotemp = 6.0;
        lifezones[5].points[2].precip = 250.0;
        lifezones[5].points[3].biotemp = 4.242321398;
        lifezones[5].points[3].precip = 250.0;
        lifezones[5].points[4].biotemp = 3.0;
        lifezones[5].points[4].precip =  176.79;
        lifezones[5].points[5].biotemp = 3.0;
        lifezones[5].points[5].precip = 125.0;

        //Moist Forest
        lifezones[6].points[0].biotemp = 4.242321398;
        lifezones[6].points[0].precip = 250.0;
        lifezones[6].points[1].biotemp = 6.0;
        lifezones[6].points[1].precip = 353.58;
        lifezones[6].points[2].biotemp = 6.0;
        lifezones[6].points[2].precip = 500.0;
        lifezones[6].points[3].biotemp = 4.242321398;
        lifezones[6].points[3].precip = 500.0;
        lifezones[6].points[4].biotemp = 3.0;
        lifezones[6].points[4].precip =  353.58;
        lifezones[6].points[5].biotemp = 3.0;
        lifezones[6].points[5].precip = 250.0;

        //Wet Forest
        lifezones[7].points[0].biotemp = 4.242321398;
        lifezones[7].points[0].precip = 500.0;
        lifezones[7].points[1].biotemp = 6.0;
        lifezones[7].points[1].precip = 707.16;
        lifezones[7].points[2].biotemp = 6.0;
        lifezones[7].points[2].precip = 1000.0;
        lifezones[7].points[3].biotemp = 4.242321398;
        lifezones[7].points[3].precip = 1000.0;
        lifezones[7].points[4].biotemp = 3.0;
        lifezones[7].points[4].precip =  707.16;
        lifezones[7].points[5].biotemp = 3.0;
        lifezones[7].points[5].precip = 500.0;

        //Rain Forest
        lifezones[8].points[0].biotemp = 4.242321398;
        lifezones[8].points[0].precip = 1000.0;
        lifezones[8].points[1].biotemp = 6.0;
        lifezones[8].points[1].precip = 1414.32;
        lifezones[8].points[2].biotemp = 6.0;
        lifezones[8].points[2].precip = 2000.0;
        lifezones[8].points[3].biotemp = 4.242321398;
        lifezones[8].points[3].precip = 2000.0;
        lifezones[8].points[4].biotemp = 3.0;
        lifezones[8].points[4].precip =  1414.32;
        lifezones[8].points[5].biotemp = 3.0;
        lifezones[8].points[5].precip = 1000.0;

        //Cool Temperate
        //Desert
        lifezones[9].points[0].biotemp = 8.484642797;
        lifezones[9].points[0].precip = 62.5;
        lifezones[9].points[1].biotemp = 12.0;
        lifezones[9].points[1].precip = 88.395;
        lifezones[9].points[2].biotemp = 12.0;
        lifezones[9].points[2].precip = 125.0;
        lifezones[9].points[3].biotemp = 8.484642797;
        lifezones[9].points[3].precip = 125.0;
        lifezones[9].points[4].biotemp = 6.0;
        lifezones[9].points[4].precip =  88.395;
        lifezones[9].points[5].biotemp = 6.0;
        lifezones[9].points[5].precip = 62.5;

        //Desert Scrub
        lifezones[10].points[0].biotemp = 8.484642797;
        lifezones[10].points[0].precip = 125.0;
        lifezones[10].points[1].biotemp = 12.0;
        lifezones[10].points[1].precip = 176.79;
        lifezones[10].points[2].biotemp = 12.0;
        lifezones[10].points[2].precip = 250.0;
        lifezones[10].points[3].biotemp = 8.484642797;
        lifezones[10].points[3].precip = 250.0;
        lifezones[10].points[4].biotemp = 6.0;
        lifezones[10].points[4].precip =  176.79;
        lifezones[10].points[5].biotemp = 6.0;
        lifezones[10].points[5].precip = 125.0;

        //Steppe
        lifezones[11].points[0].biotemp = 8.484642797;
        lifezones[11].points[0].precip = 250.0;
        lifezones[11].points[1].biotemp = 12.0;
        lifezones[11].points[1].precip = 353.58;
        lifezones[11].points[2].biotemp = 12.0;
        lifezones[11].points[2].precip = 500.0;
        lifezones[11].points[3].biotemp = 8.484642797;
        lifezones[11].points[3].precip = 500.0;
        lifezones[11].points[4].biotemp = 6.0;
        lifezones[11].points[4].precip =  353.58;
        lifezones[11].points[5].biotemp = 6.0;
        lifezones[11].points[5].precip = 250.0;

        //Moist Forest
        lifezones[12].points[0].biotemp = 8.484642797;
        lifezones[12].points[0].precip = 500.0;
        lifezones[12].points[1].biotemp = 12.0;
        lifezones[12].points[1].precip = 707.16;
        lifezones[12].points[2].biotemp = 12.0;
        lifezones[12].points[2].precip = 1000.0;
        lifezones[12].points[3].biotemp = 8.484642797;
        lifezones[12].points[3].precip = 1000.0;
        lifezones[12].points[4].biotemp = 6.0;
        lifezones[12].points[4].precip =  707.16;
        lifezones[12].points[5].biotemp = 6.0;
        lifezones[12].points[5].precip = 500.0;

        //Wet Forest
        lifezones[13].points[0].biotemp = 8.484642797;
        lifezones[13].points[0].precip = 1000.0;
        lifezones[13].points[1].biotemp = 12.0;
        lifezones[13].points[1].precip = 1414.32;
        lifezones[13].points[2].biotemp = 12.0;
        lifezones[13].points[2].precip = 2000.0;
        lifezones[13].points[3].biotemp = 8.484642797;
        lifezones[13].points[3].precip = 2000.0;
        lifezones[13].points[4].biotemp = 6.0;
        lifezones[13].points[4].precip =  1414.32;
        lifezones[13].points[5].biotemp = 6.0;
        lifezones[13].points[5].precip = 1000.0;

        //Rain Forest
        lifezones[14].points[0].biotemp = 8.484642797;
        lifezones[14].points[0].precip = 2000.0;
        lifezones[14].points[1].biotemp = 12.0;
        lifezones[14].points[1].precip = 2828.64;
        lifezones[14].points[2].biotemp = 12.0;
        lifezones[14].points[2].precip = 4000.0;
        lifezones[14].points[3].biotemp = 8.484642797;
        lifezones[14].points[3].precip = 4000.0;
        lifezones[14].points[4].biotemp = 6.0;
        lifezones[14].points[4].precip =  2828.64;
        lifezones[14].points[5].biotemp = 6.0;
        lifezones[14].points[5].precip = 2000.0;

        //Warm Temperate/Subtropical
        //Desert
        lifezones[15].points[0].biotemp = 16.96938559;
        lifezones[15].points[0].precip = 62.5;
        lifezones[15].points[1].biotemp = 24.0;
        lifezones[15].points[1].precip = 88.395;
        lifezones[15].points[2].biotemp = 24.0;
        lifezones[15].points[2].precip = 125.0;
        lifezones[15].points[3].biotemp = 16.96938559;
        lifezones[15].points[3].precip = 125.0;
        lifezones[15].points[4].biotemp = 12.0;
        lifezones[15].points[4].precip =  88.395;
        lifezones[15].points[5].biotemp = 12.0;
        lifezones[15].points[5].precip = 62.5;

        //Desert Scrub
        lifezones[16].points[0].biotemp = 16.96938559;
        lifezones[16].points[0].precip = 125.0;
        lifezones[16].points[1].biotemp = 24.0;
        lifezones[16].points[1].precip = 176.79;
        lifezones[16].points[2].biotemp = 24.0;
        lifezones[16].points[2].precip = 250.0;
        lifezones[16].points[3].biotemp = 16.96938559;
        lifezones[16].points[3].precip = 250.0;
        lifezones[16].points[4].biotemp = 12.0;
        lifezones[16].points[4].precip =  176.79;
        lifezones[16].points[5].biotemp = 12.0;
        lifezones[16].points[5].precip = 125.0;

        //Thorn Steppe
        lifezones[17].points[0].biotemp = 16.96938559;
        lifezones[17].points[0].precip = 250.0;
        lifezones[17].points[1].biotemp = 24.0;
        lifezones[17].points[1].precip = 353.58;
        lifezones[17].points[2].biotemp = 24.0;
        lifezones[17].points[2].precip = 500.0;
        lifezones[17].points[3].biotemp = 16.96938559;
        lifezones[17].points[3].precip = 500.0;
        lifezones[17].points[4].biotemp = 12.0;
        lifezones[17].points[4].precip =  353.58;
        lifezones[17].points[5].biotemp = 12.0;
        lifezones[17].points[5].precip = 250.0;

        //Dry Forest
        lifezones[18].points[0].biotemp = 16.96938559;
        lifezones[18].points[0].precip = 500.0;
        lifezones[18].points[1].biotemp = 24.0;
        lifezones[18].points[1].precip = 707.16;
        lifezones[18].points[2].biotemp = 24.0;
        lifezones[18].points[2].precip = 1000.0;
        lifezones[18].points[3].biotemp = 16.96938559;
        lifezones[18].points[3].precip = 1000.0;
        lifezones[18].points[4].biotemp = 12.0;
        lifezones[18].points[4].precip =  707.16;
        lifezones[18].points[5].biotemp = 12.0;
        lifezones[18].points[5].precip = 500.0;

        //Moist Forest
        lifezones[19].points[0].biotemp = 16.96938559;
        lifezones[19].points[0].precip = 1000.0;
        lifezones[19].points[1].biotemp = 24.0;
        lifezones[19].points[1].precip = 1414.32;
        lifezones[19].points[2].biotemp = 24.0;
        lifezones[19].points[2].precip = 2000.0;
        lifezones[19].points[3].biotemp = 16.96938559;
        lifezones[19].points[3].precip = 2000.0;
        lifezones[19].points[4].biotemp = 12.0;
        lifezones[19].points[4].precip =  1414.32;
        lifezones[19].points[5].biotemp = 12.0;
        lifezones[19].points[5].precip = 1000.0;

        //Wet Forest
        lifezones[20].points[0].biotemp = 16.96938559;
        lifezones[20].points[0].precip = 2000.0;
        lifezones[20].points[1].biotemp = 24.0;
        lifezones[20].points[1].precip = 2828.64;
        lifezones[20].points[2].biotemp = 24.0;
        lifezones[20].points[2].precip = 4000.0;
        lifezones[20].points[3].biotemp = 16.96938559;
        lifezones[20].points[3].precip = 4000.0;
        lifezones[20].points[4].biotemp = 12.0;
        lifezones[20].points[4].precip =  2828.64;
        lifezones[20].points[5].biotemp = 12.0;
        lifezones[20].points[5].precip = 2000.0;

        //Rain Forest
        lifezones[21].points[0].biotemp = 16.96938559;
        lifezones[21].points[0].precip = 4000.0;
        lifezones[21].points[1].biotemp = 24.0;
        lifezones[21].points[1].precip = 5657.28;
        lifezones[21].points[2].biotemp = 24.0;
        lifezones[21].points[2].precip = 8000.0;
        lifezones[21].points[3].biotemp = 16.96938559;
        lifezones[21].points[3].precip = 8000.0;
        lifezones[21].points[4].biotemp = 12.0;
        lifezones[21].points[4].precip =  5657.28;
        lifezones[21].points[5].biotemp = 12.0;
        lifezones[21].points[5].precip = 4000.0;

        //Tropical
        //Desert
        lifezones[22].points[0].biotemp = 33.93857118;
        lifezones[22].points[0].precip = 62.5;
        lifezones[22].points[1].biotemp = 48.0;
        lifezones[22].points[1].precip = 88.395;
        lifezones[22].points[2].biotemp = 48.0;
        lifezones[22].points[2].precip = 125.0;
        lifezones[22].points[3].biotemp = 33.93857118;
        lifezones[22].points[3].precip = 125.0;
        lifezones[22].points[4].biotemp = 24.0;
        lifezones[22].points[4].precip =  88.395;
        lifezones[22].points[5].biotemp = 24.0;
        lifezones[22].points[5].precip = 62.5;

        //Desert Scrub
        lifezones[23].points[0].biotemp = 33.93857118;
        lifezones[23].points[0].precip = 125.0;
        lifezones[23].points[1].biotemp = 48.0;
        lifezones[23].points[1].precip = 176.79;
        lifezones[23].points[2].biotemp = 48.0;
        lifezones[23].points[2].precip = 250.0;
        lifezones[23].points[3].biotemp = 33.93857118;
        lifezones[23].points[3].precip = 250.0;
        lifezones[23].points[4].biotemp = 24.0;
        lifezones[23].points[4].precip =  176.79;
        lifezones[23].points[5].biotemp = 24.0;
        lifezones[23].points[5].precip = 125.0;

        //Thorn Woodland
        lifezones[24].points[0].biotemp = 33.93857118;
        lifezones[24].points[0].precip = 250.0;
        lifezones[24].points[1].biotemp = 48.0;
        lifezones[24].points[1].precip = 353.58;
        lifezones[24].points[2].biotemp = 48.0;
        lifezones[24].points[2].precip = 500.0;
        lifezones[24].points[3].biotemp = 33.93857118;
        lifezones[24].points[3].precip = 500.0;
        lifezones[24].points[4].biotemp = 24.0;
        lifezones[24].points[4].precip =  353.58;
        lifezones[24].points[5].biotemp = 24.0;
        lifezones[24].points[5].precip = 250.0;

        //Very Dry Forest
        lifezones[25].points[0].biotemp = 33.93857118;
        lifezones[25].points[0].precip = 500;
        lifezones[25].points[1].biotemp = 48.0;
        lifezones[25].points[1].precip = 707.16;
        lifezones[25].points[2].biotemp = 48.0;
        lifezones[25].points[2].precip = 1000.0;
        lifezones[25].points[3].biotemp = 33.93857118;
        lifezones[25].points[3].precip = 1000.0;
        lifezones[25].points[4].biotemp = 24.0;
        lifezones[25].points[4].precip =  707.16;
        lifezones[25].points[5].biotemp = 24.0;
        lifezones[25].points[5].precip = 500.0;

        //Dry Forest
        lifezones[26].points[0].biotemp = 33.93857118;
        lifezones[26].points[0].precip = 1000.0;
        lifezones[26].points[1].biotemp = 48.0;
        lifezones[26].points[1].precip = 1414.32;
        lifezones[26].points[2].biotemp = 48.0;
        lifezones[26].points[2].precip = 2000.0;
        lifezones[26].points[3].biotemp = 33.93857118;
        lifezones[26].points[3].precip = 2000.0;
        lifezones[26].points[4].biotemp = 24.0;
        lifezones[26].points[4].precip =  1414.32;
        lifezones[26].points[5].biotemp = 24.0;
        lifezones[26].points[5].precip = 1000.0;

        //Moist Forest
        lifezones[27].points[0].biotemp = 33.93857118;
        lifezones[27].points[0].precip = 2000.0;
        lifezones[27].points[1].biotemp = 48.0;
        lifezones[27].points[1].precip = 2828.64;
        lifezones[27].points[2].biotemp = 48.0;
        lifezones[27].points[2].precip = 4000.0;
        lifezones[27].points[3].biotemp = 33.93857118;
        lifezones[27].points[3].precip = 4000.0;
        lifezones[27].points[4].biotemp = 24.0;
        lifezones[27].points[4].precip =  2828.64;
        lifezones[27].points[5].biotemp = 24.0;
        lifezones[27].points[5].precip = 2000.0;

        //Wet Forest
        lifezones[28].points[0].biotemp = 33.93857118;
        lifezones[28].points[0].precip = 4000.0;
        lifezones[28].points[1].biotemp = 48.0;
        lifezones[28].points[1].precip = 5657.28;
        lifezones[28].points[2].biotemp = 48.0;
        lifezones[28].points[2].precip = 8000.0;
        lifezones[28].points[3].biotemp = 33.93857118;
        lifezones[28].points[3].precip = 8000.0;
        lifezones[28].points[4].biotemp = 24.0;
        lifezones[28].points[4].precip =  5657.28;
        lifezones[28].points[5].biotemp = 24.0;
        lifezones[28].points[5].precip = 4000.0;

        //Rain Forest
        lifezones[29].points[0].biotemp = 33.93857118;
        lifezones[29].points[0].precip = 8000.0;
        lifezones[29].points[1].biotemp = 48.0;
        lifezones[29].points[1].precip = 11314.56;
        lifezones[29].points[2].biotemp = 48.0;
        lifezones[29].points[2].precip = 16000.0;
        lifezones[29].points[3].biotemp = 33.93857118;
        lifezones[29].points[3].precip = 16000.0;
        lifezones[29].points[4].biotemp = 24.0;
        lifezones[29].points[4].precip =  11314.56;
        lifezones[29].points[5].biotemp = 24.0;
        lifezones[29].points[5].precip = 8000.0;
    }
    
    /*
	 Determines the lifezone closest to a given biotemp and precipitation
     value. Conceptually, we are guessing what the most likely lifezone
     type for that biotemp and precipitation value.

	 ARGUMENTS:
     	biotemp - the biotemperature.
     	precip - the precipitation.

	 RETURNS:
     	a lifezone.
     */
    public int getLifezone(double biotemp, double precip) {
        int result = 0;
        double maxDist = 0;
        double currDist;
        BiotempPrecipPoint point = new BiotempPrecipPoint(biotemp, precip);

		// TODO: Handle polar desert!

        for(int i = 0; i < CHART_SIZE; ++i) {
            currDist = this.lifezones[i].getDistance(point);
            if(currDist > maxDist) {
                result = i;
                maxDist = currDist;
            }
        }
        
        return result;
    }
    
    // Defines a single lifezone hexagon
    private class Lifezone {
        private final BiotempPrecipPoint[] points;
        
        public Lifezone() {
            this.points = new BiotempPrecipPoint[NUM_POINTS];
            for(int i = 0; i < this.points.length; ++i)
                this.points[i] = new BiotempPrecipPoint(0, 0);
        }
        
        /*
			Gets a the point at a given index.

			ARGUMENTS:
				index a value from 0 to 6, since this is a hexagon.

			RETURNS:
				null if index is invalid, or the BiotempPrecipPoint at the index.
         */
        public BiotempPrecipPoint getPoint(int index) {
            if(index < 0 || index >= 6)
                return null;
            
            return this.points[index];
        }
        
        /*
			Calculates the distance of a point from the centroid of this lifezone

			ARGUMENTS:
				point the point we want the distance from.

			RETURNS:
				the distance between point and the centroid of the lifezone.
         */
        public double getDistance(BiotempPrecipPoint point) {
            double centroidBiotemp, centroidPrecip;
            BiotempPrecipPoint centroid;
            
            centroidBiotemp = 0;
            centroidPrecip = 0;
            
            for(int i = 0; i < NUM_POINTS; ++i) {
                centroidBiotemp += this.points[i].biotemp;
                centroidPrecip += this.points[i].precip;
            }
            
            centroidBiotemp /= (double)NUM_POINTS;
            centroidPrecip /= (double)NUM_POINTS;
            
            centroid = new BiotempPrecipPoint(centroidBiotemp, centroidPrecip);
            
            return centroid.getDistance(point);
        }
    }
    
     // Use this to keep the Lifezone hexagons organized.
    private class BiotempPrecipPoint {
        private double biotemp;
        private double precip;
        
        public BiotempPrecipPoint(double biotemp, double precip) {
            this.biotemp = biotemp;
            this.precip = precip;
        }
        
        /*
			Calculates the distance between this point and another point

			ARGUMENTS:
				point: the given point.

			RETURNS:
				the distance from this point to point.
         */
        public double getDistance(BiotempPrecipPoint point) {
            if(point == null)
                return 0.0;
            
            double biotempDiff, precipDiff;
            
            biotempDiff = this.biotemp - point.biotemp;
            precipDiff = this.precip - point.precip;
            
            biotempDiff *= biotempDiff;
            precipDiff *= precipDiff;
            
            return Math.sqrt(biotempDiff + precipDiff);
        }

        public double getBiotemp() {
            return biotemp;
        }

        public void setBiotemp(double biotemp) {
            this.biotemp = biotemp;
        }

        public double getPrecip() {
            return precip;
        }

        public void setPrecip(double precip) {
            this.precip = precip;
        }
    }
}
