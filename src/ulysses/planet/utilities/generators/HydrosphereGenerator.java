// Liam Wynn, 2/1/2018, Ulysses

/*
	The HydrosphereGenerator manages the entire process of
	Hydrosphere creation. It does so by following this algorithm:

	1. Get the heightmap from the Lithosphere
	2. Create a cloud frequency map
	3. Use the height map and cloud frequency map to determine
	   where rivers will likely form (river source map)
	4. Use the river source map to choose where our rivers will
	   begin
	5. Create rivers
	6. Use rivers + height map to compute the distance from water
	   source map.
	7. Use water source map + cloud map to create a precipitation
	   map.

	In this system, we assume that the amount of precipitation in
	a given area is determined by the presence of clouds and proximity
	to sources of water. Thus, we use that to make a precipitation map.
*/

package ulysses.planet.utilities.generators;

// Used in generateHydrosphere method.
import ulysses.planet.Hydrosphere;
import ulysses.planet.River;
// Used to store the height map.
import ulysses.planet.utilities.PlanetMap;
// Used in generating rivers.
import java.awt.Point;
// Used to choose the origins of Rivers.
import java.awt.geom.Point2D;
// Used in river generation.
import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;
// Used to shuffle the neighbors that we check. This way
// we encourage more random rivers.
import java.util.Random;

public class HydrosphereGenerator
{
	// The dimensions of the resulting hydrosphere.
	// Used to keep all maps consistent in size.
	private int width, height;

	// Used to compute the river source map. Which
	// Tells us the most likely places rivers will form.
	private PlanetMap heightMap;

	// Used to generate the cloud frequency map.
	private MapGenerator cloudFreqMapGenerator;

	// The number of rivers we want in our map.
	private int numRivers;

	// Used to shuffle the order we check neighbors in getNeighbors
	private Random rand;

	public HydrosphereGenerator(long shuffleSeed)
	{
		this.width = 256;
		this.height = 128;

		this.heightMap = null;
		this.cloudFreqMapGenerator = null;
		this.numRivers = 0;

		this.rand = new Random(shuffleSeed);
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int val)
	{
		if(val <= 0)
			val = 256;

		this.width = val;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void setHeight(int val)
	{
		if(val <= 0)
			val = 128;

		this.height = val;
	}

	public PlanetMap getHeightMap()
	{
		return this.heightMap;
	}

	public void setHeightMap(PlanetMap val)
	{
		this.heightMap = val;
	}

	public MapGenerator getCloudFreqMapGenerator()
	{
		return this.cloudFreqMapGenerator;
	}

	public void setCloudFreqMapGenerator(MapGenerator val)
	{
		this.cloudFreqMapGenerator = val;
	}

	public int getNumRivers()
	{
		return this.numRivers;
	}

	public void setNumRivers(int val)
	{
		if(val < 0)
			val = 0;

		this.numRivers = val;
	}

	public Hydrosphere generateHydrosphere()
	{
		if(this.heightMap == null ||
		   this.cloudFreqMapGenerator == null)
		{
			return null;
		}

		// What we will return.
		Hydrosphere result = new Hydrosphere(this.width, this.height);
		// Use this to generate the river source map.
		PlanetMap cloudFreqMap;
		// Use this to generate rivers.
		PlanetMap riverSourceMap;
		// Used to compute final precipitation map.
		PlanetMap distToWaterMap;

		// Set the properties of each generator.

		this.cloudFreqMapGenerator.setWidth(this.width);
		this.cloudFreqMapGenerator.setHeight(this.height);

		// Set hydrosphere properties.

		cloudFreqMap = this.cloudFreqMapGenerator.generateMap();
		riverSourceMap = computeRiverSourceMap(cloudFreqMap);

		result.setNumRivers(this.numRivers);
		generateRivers(result, heightMap, riverSourceMap);

		distToWaterMap = computeDistToWaterMap(heightMap, result);

		result.setCloudFreqMap(cloudFreqMap);

		return result;
	}

	private PlanetMap computeRiverSourceMap(PlanetMap cloudFreqMap)
	{
		PlanetMap[] p = new PlanetMap[] { cloudFreqMap };
		PlanetMap riverSourceMap = this.heightMap.combineWith(p);

		for(int i = 0; i < this.width * this.height; ++i)
		{
			if(this.heightMap.getData(i) <= 0.37f)
				riverSourceMap.setData(i, 0.0f);
		}

		riverSourceMap.sqrt();
		riverSourceMap.normalize();

		return riverSourceMap;
	}

	/*
		Generates the rivers for the hydrosphere. This operation will fail if at least
		of the following is true:

		1. hydro, heightmap, or riverSourceMap are null.
		2. Number of rivers is 0.

		ARGUMENTS:
			hydro - where to store resulting rivers.
			heightmap - used to choose points for building rivers.
			riverSourceMap - used to choose the initial points for the river.
	*/
	private void generateRivers(Hydrosphere hydro, PlanetMap heightmap, PlanetMap riverSourceMap)
	{
		if(hydro == null || heightmap == null || riverSourceMap == null)
			return;
		if(numRivers < 1)
			return;

		// Easy access to the source points for rivers.
		Point2D.Float[] sources = riverSourceMap.getSortedPoints();
		// The next river to add.
		River river;
		// Easy access sources array.
		int index;
		// Used to conveniently access our source point.
		Point source;

		for(int i = 0; i < this.numRivers; ++i)
		{
			index = (sources.length - 1) - i;
			// A 2D point to 1D is i = y * width + x.
			// The opposite is x = i % width, y = i / width.
			source = new Point((int)sources[index].getX() % this.width,
							   (int)sources[index].getX() / this.width);

			river = new River();
			buildRiver(river, source, heightmap, hydro);

			hydro.setRiver(i, river);
		}
	}

	/*
		Builds a single river. We store the result in river. The river begins at
		source. We use heightmap to choose points in the river. We use a depth first
		search algorithm that goes until ocean is found. TODO: Finish documentation
	*/
	private void buildRiver(River river, Point source, PlanetMap heightmap, Hydrosphere hydro)
	{
		// Stores unvisited points.
		Stack<Point> stack = new Stack<Point>();
		// Stores parent relationship. Used to build rivers.
		HashMap<Point, Point> parent = new HashMap<Point, Point>();	
		// Marks points as visited.
		boolean[] visited = new boolean[this.width * this.height];
		// Manages our current point.
		Point curr;
		// The point to visit next.
		Point next;
		// When building the river, use this to examine the point
		// before curr
		Point prev;
		// Use these for errosion
		float currVal, nextVal;
		// Manages the neighbors of curr.
		ArrayList<Point> neighbors;

		stack.push(source);
		visited[(int)source.getY() * this.width + (int)source.getX()] = true;

		while(!stack.empty())
		{
			curr = stack.pop();
			currVal = heightmap.getData((int)curr.getX(), (int)curr.getY());

			// Found water!
			if(currVal <= 0.37f || hydro.getRiverOf(curr) != -1)
			{
				while(!curr.equals(source))
				{
					river.insertPoint(curr);
					prev = (Point)parent.get(curr);
					//prevVal = heightmap.getData((int)prev.getX(), (int)prev.getY());

					// 'Erode' point to be the level of the parent.
					//if(currVal > prevVal)
						//heightmap.setData((int)curr.getX(), (int)curr.getY(), prevVal);

					curr = prev;
				}

				return;
			}

			neighbors = getNeighbors(curr, visited);
			if(neighbors.isEmpty())
				continue;
			else
			{
				next = chooseNeighbor(neighbors, heightmap, hydro);
				stack.push(next);
				visited[(int)next.getY() * this.width + (int)next.getX()] = true;
				parent.put(next, curr);

				// Erode next if needed
				nextVal = heightmap.getData((int)next.getX(), (int)next.getY());
				if(nextVal > currVal)
					heightmap.setData((int)next.getX(), (int)next.getY(), currVal);
			}
		}
	}

	private ArrayList<Point> getNeighbors(Point curr, boolean[] visited)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		int x, y;
		int l, r, u, d;

		x = (int)curr.getX();
		y = (int)curr.getY();

		l = (x - 1 % this.width + this.width) % this.width;
		r = (x + 1 % this.width + this.width) % this.width;
		u = (y - 1 % this.height + this.height) % this.height;
		d = (y + 1 % this.height + this.height) % this.height;

		if(!visited[y * this.width + l])
			result.add(new Point(l, y));
		if(!visited[y * this.width + r])
			result.add(new Point(r, y));
		if(!visited[u * this.width + x])
			result.add(new Point(x, u));
		if(!visited[d * this.width + x])
			result.add(new Point(x, d));

		//shuffleNeighbors(result);

		return result;
	}

	private Point chooseNeighbor(ArrayList<Point> neighbors, PlanetMap heightmap, Hydrosphere hydro)
	{
		int result = 0;
		Point p = (Point)neighbors.get(0);
		float val = heightmap.getData((int)p.getX(), (int)p.getY());
		float bestVal = val;
		ArrayList<Point> bestNeighbors = new ArrayList<Point>();

		// Prioritize points already apart of a river
		for(int i = 0; i < neighbors.size(); ++i)
		{
			if(hydro.getRiverOf((Point)neighbors.get(i)) != -1)
				return (Point)neighbors.get(i);
		}

		// Scan the neighbors to find the best value.
		for(int i = 0; i < neighbors.size(); ++i)
		{
			p = (Point)neighbors.get(i);
			val = heightmap.getData((int)p.getX(), (int)p.getY());
			if(val < bestVal)
			{
				bestVal = val;
				result = i;
			}
		}

		// If multiple neighbors match the best valued neighbor, add them to a list and
		// pick a random one.
		for(int i = 0; i < neighbors.size(); ++i)
		{
			p = (Point)neighbors.get(i);
			val = heightmap.getData((int)p.getX(), (int)p.getY());
			if(val == bestVal)
				bestNeighbors.add(p);
		}

		return (Point)bestNeighbors.get(this.rand.nextInt(bestNeighbors.size()));
	}

	private void shuffleNeighbors(ArrayList<Point> neighbors)
	{
		if(neighbors.size() < 2)
			return;

		// n allows us to use size without accessing it.
		// j is a randomly selected index.
		/*int j, n;
		// For swapping.
		Point temp;

		n = neighbors.size();
		for(int i = n - 1; i > 0; --i)
		{
			j = this.rand.nextInt(i);
			// Replaces the point at j with the point at i. Stores
			// point j in temp
			temp = (Point)neighbors.set(j, (Point)neighbors.get(i));
			neighbors.set(i, temp);
		}*/

		int i, j;
		int n = neighbors.size();
		Point temp;

		for(int k = 0; k < 100; ++k)
		{
			i = this.rand.nextInt(n);
			j = this.rand.nextInt(n);
			// Replaces the point at j with the point at i. Stores
			// point j in temp
			temp = (Point)neighbors.set(j, (Point)neighbors.get(i));
			neighbors.set(i, temp);
		}
	}

	/*
		Computes the distance to water source map for a hydrosphere. The heightmap can
		tell us where oceans are implicitly, and the hydrosphere tells us if a point
		is apart of a river or not.

		ARGUMENTS:
			heightMap - Tells us what points are ocean.
			hydro - Used to tell us what points are apart of rivers.

		RETURNS:
			null if heightMap or hydrosphere were null, and thus we cannot compute the
			map. Otherwise, returns a normalized map that estimates the distance to a water
			source a point is.
	*/
	private PlanetMap computeDistToWaterMap(PlanetMap heightMap, Hydrosphere hydro) {
		if(heightMap == null || hydro == null) {
			return null;
		}

		// The list of points that are going to be processed immediately.
		// We continue to process points and compute their estimated distance
		// from water as long as this list remains not empty.
		ArrayList<Point> toProcess = new ArrayList<Point>();
		PlanetMap distToWaterMap = new PlanetMap(this.width, this.height);
		// Use to store arguments for addWaterToProcessList procedure.
		WaterProcessData waterProcessData = new WaterProcessData();

		// Initialize points by marking them as unvisited and add all water points
		// to the toProcess list:
		initializeDistToWaterMap(distToWaterMap);

		waterProcessData.heightMap = heightMap;
		waterProcessData.hydro = hydro;
		waterProcessData.toProcess = toProcess;
		waterProcessData.distToWater = distToWaterMap;
		addWaterToProcessList(waterProcessData);

		// TODO: Process every point in toProcess.

		return distToWaterMap;
	}

	/*
		Marks every point in the distToWaterMap as 2. Marking it as 2 implies
		it is unvisited. By default, every point is unvisited.
	*/
	private void initializeDistToWaterMap(PlanetMap distToWaterMap) {
		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				distToWaterMap.setData(x, y, 2);
			}
		}
	}

	/*
		For each point, if it is apart of a river or is ocean, we mark it as a 1
		in the distToWaterMap and add it to the toProcessList. After doing this, we
		can begin the process of estimating every point's distance to a source of water.
	*/
	private void addWaterToProcessList(WaterProcessData waterProcessData) {
		Point p;

		for(int x = 0; x < this.width; ++x) {
			for(int y = 0; y < this.height; ++y) {
				p = new Point(x, y);

				if(waterProcessData.heightMap.getData(x, y) <= 0.37f ||
				   waterProcessData.hydro.getRiverOf(p) != -1)
				{
					// 1 denotes a source of water.
					waterProcessData.distToWater.setData(x, y, 1);
					waterProcessData.toProcess.add(p);
				}
			}
		}
	}
}
