// Liam Wynn, 12/22/2017, Ulysses

/*
	The TectonicsMapGenerator is responsible for generating the Plates of
	a Lithosphere. We begin with a seed and a number of plates. First,
	we choose the centers for each Plate. These also act as the first
	crust point as well. We add this to a list called the "border". The
	border is all crust on the edge of the plate. We remove a random
	point from this list, and add that points unassigned neighbors to the
	plate. The process stops when we can no longer add any points to any plate.

	This is a simplification for computing a series maps that would describe
	plate tectonic features. For example, we could create a tectonics map
	by combining a plate age map, density map, and a collision map that describes
	the collisions of plates. For simplicity, we just choose random values
	to represent these properties.
*/

package ulysses.planet.utilities.generators;

import ulysses.planet.utilities.PlanetMap;

// For keeping track of points and centers of plates.
import java.awt.Point;
// For random color picking.
import java.awt.Color;
// For maintaining border lists for each plate.
import java.util.ArrayList;
// For choosing points.
import java.util.Random;

public class TectonicsMapGenerator extends MapGenerator {
	// Used to generate the correct number of plates.
	private int numPlates;

	public TectonicsMapGenerator(Random rand) {
		super(rand);

		this.numPlates = 20;
	}

	public int getNumPlates() {
		return this.numPlates;
	}

	public void setNumPlates(int val) {
		if(val <= 0)
			val = 20;

		this.numPlates = val;
	}

	/*
		The main driver for generating the tectonics map
	*/
	public PlanetMap generateMap() {
		// What we will return.
		PlanetMap result = new PlanetMap(this.width, this.height);
		// The center of each plate. Used in computing the age.
		Point[] center = new Point[this.numPlates];
		// Stores which plates own which crust.
		int[] crust = new int[this.width * this.height];

		// Initialize the crust map
		for(int i = 0; i < crust.length; ++i)
			crust[i] = -1; // -1 indicates no plate owns it.

		choosePlateCenters(center, crust);
		computeTectonicData(result, center, crust);

		return result;
	}

	/*
		Initializes the plate growing process by choosing the center of
		each plate. We do so by continuously selecting a random point until
		it is not apart of any plate. We mark it as part of a plate by setting
		the point in crust to the id of that plate.

		ARGUMENTS:
			centers - Where we store the plate centers.
			crust - Used to look up marked and unmarked crust.
	*/
	private void choosePlateCenters(Point[] centers, int[] crust) {
		int x, y;

		for(int i = 0; i < this.numPlates; ++i) {
			do {
				x = rand.nextInt(this.width);
				y = rand.nextInt(this.height);
			} while(crust[y * this.width + x] != -1);

			crust[y * this.width + x] = i;
			centers[i] = new Point(x, y);
		}
	}

	/*
		Computes the tectonic data of the crust by 'growing' each plate. We maintain
		a list of the border points of each plate. That is, we keep a list
		of every point at the current edge of the plate. This implies each
		point in this list is adjacent to unmarked crust. We pick a random point
		in this list, remove it, and add its unmarked neighbors to the list.
		We repeat this process until no more plates can grow.

		In a more scientifically acurate model, we would compute tectonic
		data using age data, plate collision data, plate density, etc. For
		simplicity's sake, we just pick a random value between 0 and 1 for
		each plate and say that every crust point of the plate has that
		value.

		ARGUMENTS:
			tectonicsMap - where we store the tectonic data.
			center - the center point of each plate.
			crust - tells us the owning plate of a particular point of crust,
			or if it is unowned.
	*/
	private void computeTectonicData(PlanetMap tectonicsMap, Point[] center, int[] crust) {
		// Keeps track of the crust points on the edge of each plate.
		ArrayList<Point>[] border = new ArrayList[this.numPlates];
		// If >= 1, Means we have at least one edge crust with points we
		// have not checked the neighbors of.
		int maxBorderSize;
		// Use this in place of calling count() repeadetly
		int currBorderSize;
		// Used when adding points to border.
		Point currPoint;
		// Used to simplify some of the calculations in checking
		// neighbors
		int x, y;
		int l, r, u, d;

		// Initialize the border list with the centers.
		for(int i = 0; i < this.numPlates; ++i) {
			tectonicsMap.setData((int)center[i].getX(), (int)center[i].getY(), 0);
			border[i] = new ArrayList<>();
			border[i].add(new Point(center[i]));
		}

		// All borders now have one point in them.
		maxBorderSize = 1;
		while(maxBorderSize > 0) {
			// Assume it is 0 for now. If all plates have a border size of 0, then
			// we cannot add anymore crust to any plate so we are done. Otherwise,
			// we can still add crust to at least one plate.
			maxBorderSize = 0;

			for(int i = 0; i < this.numPlates; ++i) {
				if(border[i].isEmpty())
					continue;

				currBorderSize = border[i].size();
				maxBorderSize = currBorderSize > maxBorderSize ? currBorderSize : maxBorderSize;

				currPoint = new Point((Point)border[i].remove(rand.nextInt(currBorderSize)));

				x = (int)currPoint.getX();
				y = (int)currPoint.getY();

				l = ((x - 1) % this.width + this.width) % this.width;
				r = ((x + 1) % this.width + this.width) % this.width;
				u = ((y - 1) % this.height + this.height) % this.height;
				d = ((y + 1) % this.height + this.height) % this.height;

				addCrustToPlate(new Point(l, y), i, border, crust);
				addCrustToPlate(new Point(r, y), i, border, crust);
				addCrustToPlate(new Point(x, u), i, border, crust);
				addCrustToPlate(new Point(x, d), i, border, crust);
			}
		}

		generateTectonicValues(tectonicsMap, crust);
	}

	/*
		Marks a point as being apart of a plate. We do so by marking its point in the crust
		map, then adding it to the border list for the plate.

		ARGUMENTS:
			toAdd - the point to add.
			plate - the plate to add toAdd to.
			border - the border list for the plate.
			crust - the crust map.
	*/
	private void addCrustToPlate(Point toAdd, int plate, ArrayList<Point>[] border, int[] crust) {
		int indexToAdd;

		indexToAdd = (int)toAdd.getY() * this.width + (int)toAdd.getX();

	    // Left neighbor.
		if(crust[indexToAdd] == -1) {
			// Mark as apart of this plate
			crust[indexToAdd] = plate;
			border[plate].add(toAdd);
		}
	}

	/*
		Determines the value of every point in the map based on the plate
		its apart of. We choose a random value between 0 and 1 for each
		plate. Next, we scan every point, find its plate, and set the point's
		value to the value associated with the plate.

		ARGUMENTS:
			tectonicsMap - where we store the final noise values.
			crust - maps points to plates. 
	*/
	private void generateTectonicValues(PlanetMap tectonicsMap, int[] crust) {
		// The values for each plate.
		float[] tectonics = new float[this.numPlates];

		for(int i = 0; i < this.numPlates; ++i)
			tectonics[i] = (float)rand.nextDouble();

		for(int i = 0; i < crust.length; ++i)
			tectonicsMap.setData(i, tectonics[crust[i]]);
		
	}
}
