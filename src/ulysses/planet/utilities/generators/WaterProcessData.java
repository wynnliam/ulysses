// Liam Wynn, 2/23/2018, Ulysses

/*
	When we estimate the distance to a water source (ocean or river)
	for each point, we need to know which points are actually water
	sources, and which are not. This way, as we process points, we start
	from river sources and grow out to points furthest away from water.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.Hydrosphere;
import ulysses.planet.utilities.PlanetMap;
import java.util.ArrayList;
import java.awt.Point;

public class WaterProcessData
{
	public PlanetMap heightMap;
	public Hydrosphere hydro;
	public ArrayList<Point> toProcess;
	public PlanetMap distToWater;
}
