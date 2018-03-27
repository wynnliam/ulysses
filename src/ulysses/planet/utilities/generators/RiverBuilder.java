// Liam Wynn, 3/24/2018, Ulysses

/*
	This class exists as an attempt to refactor the monster
	that is the HydrosphereGenerator. Essentially, we take the
	logic of building rivers and move it here.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.Hydrosphere;
import ulysses.planet.River;
import ulysses.planet.utilities.PlanetMap;
import java.util.Random;
import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.geom.Point2D;

public class RiverBuilder {

	// When choosing neighbors of a given point,
	// we want the behavior to be random. We use these
	// to facilitate that.
	private final long shuffleSeed;
	private Random rand;

	// The dimensions of the world we are working with.
	private int width, height;

	// The number of rivers we wish to generate.
	private int numRivers;

	// We compute where rivers originate with these
	// maps.
	// Represents annual cloud presence at every point.
	private PlanetMap cloudFreq;
	// A modifier to randomize the behavior of river selection.
	private PlanetMap riverSourceModifier;

	// Used to check when the river building algorithm has found
	// ocean to thus end the process.
	private float seaLevel;

	public RiverBuilder(long shuffleSeed) {
		this.shuffleSeed = shuffleSeed;
		this.rand = new Random(this.shuffleSeed);

		this.width = 128;
		this.height = 64;

		this.numRivers = 0;

		this.cloudFreq = null;
		this.riverSourceModifier = null;

		this.seaLevel = 0.37f;
	}

	public long getShuffleSeed() {
		return this.shuffleSeed;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int val) {
		if(val <= 0)
			val = 128;

		this.width = val;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int val) {
		if(val <= 0)
			val = 64;

		this.height = val;
	}

	public int getNumRivers() {
		return this.numRivers;
	}

	public void setNumRivers(int val) {
		if(val < 0)
			val = 0;

		this.numRivers = val;
	}

	public PlanetMap getCloudFrequencyMap() {
		return this.cloudFreq;
	}

	public void setCloudFrequencyMap(PlanetMap val) {
		this.cloudFreq = val;
	}

	public PlanetMap getRiverSourceModifierMap() {
		return this.riverSourceModifier;
	}

	public void setRiverSourceModiferMap(PlanetMap val) {
		this.riverSourceModifier = val;
	}

	public float getSeaLevel() {
		return this.seaLevel;
	}

	public void setSeaLevel(float val) {
		this.seaLevel = val;
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
	public void generateRivers(Hydrosphere hydro, PlanetMap heightmap) {
		if(hydro == null || heightmap == null)
			return;
		if(this.numRivers < 1)
			return;

		// A map of the most likely spawn points for rivers.
		PlanetMap riverSourceMap = computeRiverSourceMap(heightmap);

		// Easy access to the source points for rivers.
		Point2D.Float[] sources = riverSourceMap.getSortedPoints();
		// The next river to add.
		River river;
		// Easy access sources array.
		int index;
		// Used to conveniently access our source point.
		Point source;

		for(int i = 0; i < this.numRivers; ++i) {
			// Grab the next available point from the bottom of the list.
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
		Combines the heightmap, cloudFreq map, and river source modifier map
		to produce a map that tells us how well each point fits the conditions
		for producing a river.

		ARGUMENTS:
			heightMap - Greater heights imply more likely a river originates there.

		RETURNS:
			the "river source map" which is assigns a score for each point based on
			how fit a river is to spawn at each point.
	*/
	private PlanetMap computeRiverSourceMap(PlanetMap heightMap) {
		PlanetMap[] p = new PlanetMap[] { this.cloudFreq, this.riverSourceModifier };
		PlanetMap riverSourceMap = heightMap.combineWith(p);

		for(int i = 0; i < this.width * this.height; ++i) {
			if(heightMap.getData(i) <= this.seaLevel)
				riverSourceMap.setData(i, 0.0f);
		}

		riverSourceMap.sqrt();
		riverSourceMap.normalize();

		return riverSourceMap;
	}

	/*
		Builds a single river. We store the result in river. The river begins at
		source. We use heightmap to choose points in the river. We use a depth first
		search algorithm that goes until ocean is found. To select the next point to
		visit, we gather all of the unvisited neighbors of the current point. Then, we
		select the "best" neighbor. This involves examining its height, is it river/ocean,
		etc.

		ARGUMENTS:
			river - Where we store the resulting river.
			heightmap - Used to examine the height of every point
			hydro - used to check for points that are apart of other rivers.

		RETURNS:
			A single, randomly generated river.
	*/
	private void buildRiver(River river, Point source, PlanetMap heightmap, Hydrosphere hydro) {
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

		while(!stack.empty()) {
			curr = stack.pop();
			currVal = heightmap.getData((int)curr.getX(), (int)curr.getY());

			// Found water!
			if(currVal <= this.seaLevel || hydro.getRiverOf(curr) != -1) {
				while(!curr.equals(source)) {
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
			else {
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

	/*
		Gets the set of all unvisited neighbors to expand our river to.

		ARGUMENTS:
			curr - the point to find the neighbors of.
			visited - tells us if our river building algorithm has already
			examined the point.

		RETURNS:
			A list containing every unvisited neighbor of curr.
	*/
	private ArrayList<Point> getNeighbors(Point curr, boolean[] visited) {
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

	/*
		Selects the "best" neighbor to build our river with. We prioritize points with lower
		height values and/or ones that are already apart of a river.

		ARGUMENTS:
			neighbors - The pool of neighbors to select from.
			heightmap - Used to access the height value of each neighbor point.
			hydro - Tells us which points are apart of a river.

		RETURNS:
			the best neighbor given our criteria from the neighbors.
	*/
	private Point chooseNeighbor(ArrayList<Point> neighbors, PlanetMap heightmap, Hydrosphere hydro) {
		int result = 0;
		Point p = (Point)neighbors.get(0);
		float val = heightmap.getData((int)p.getX(), (int)p.getY());
		float bestVal = val;
		ArrayList<Point> bestNeighbors = new ArrayList<Point>();

		// Prioritize points already apart of a river
		for(int i = 0; i < neighbors.size(); ++i) {
			if(hydro.getRiverOf((Point)neighbors.get(i)) != -1)
				return (Point)neighbors.get(i);
		}

		// Scan the neighbors to find the best value.
		for(int i = 0; i < neighbors.size(); ++i) {
			p = (Point)neighbors.get(i);
			val = heightmap.getData((int)p.getX(), (int)p.getY());
			if(val < bestVal) {
				bestVal = val;
				result = i;
			}
		}

		// If multiple neighbors match the best valued neighbor, add them to a list and
		// pick a random one.
		for(int i = 0; i < neighbors.size(); ++i) {
			p = (Point)neighbors.get(i);
			val = heightmap.getData((int)p.getX(), (int)p.getY());
			if(val == bestVal)
				bestNeighbors.add(p);
		}

		return (Point)bestNeighbors.get(this.rand.nextInt(bestNeighbors.size()));
	}

	/*
		Shuffles the list of neighbors. If this list is < 2 then there is no need to
		shuffle this list, and we return it as is.

		ARGUMENTS:
			neighbors - the unsorted list of neighbors.
	*/
	private void shuffleNeighbors(ArrayList<Point> neighbors) {
		if(neighbors.size() < 2)
			return;

		int i, j;
		int n = neighbors.size();
		Point temp;

		for(int k = 0; k < 100; ++k) {
			i = this.rand.nextInt(n);
			j = this.rand.nextInt(n);
			// Replaces the point at j with the point at i. Stores
			// point j in temp
			temp = (Point)neighbors.set(j, (Point)neighbors.get(i));
			neighbors.set(i, temp);
		}
	}
}
