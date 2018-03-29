// Liam Wynn, 3/28/2018, Ulysses

/*
	Used when computing the approximate distance to water
	map for the hydrosphere. Essentially it stores the average
	location of water for each cell of the world. Basically, we have
	some number of N x N cells that the points of the world are
	placed in.
*/

package ulysses.planet.utilities.generators;

import java.awt.Point;

public class WaterSourcesContainer {
	// For each cell of points, this stores the average position of water.
	public Point[] waterSources = null;
	// For each cell of points, stores the number of points used to compute
	// the average water source.
	public int[] pointCount = null;

	public void initialize(int len) {
		this.waterSources = new Point[len];
		this.pointCount = new int[len];

		for(int i = 0; i < len; ++i) {
			this.waterSources[i] = new Point(-1, -1);
			this.pointCount[i] = 0;
		}
	}

	/*
		Adds a given point to the waterSource at index i.
		We use this to compute the average point of water
		for each cel of points.

		ARGUMENTS:
			x, y - the point to add.
			i - the index of the waterSource to add to.

		RETURNS:
			true - the operation was done successfully.
			false - i is an invalid index, or the points were not initialized.
	*/
	public boolean addPoint(int x, int y, int i) {
		if(this.waterSources == null || this.pointCount == null || 
		   i < 0 || i >= this.waterSources.length)
		{
			return false;
		}

		this.waterSources[i].setLocation(this.waterSources[i].getX() + x,
										 this.waterSources[i].getY() + y);
		this.pointCount[i] += 1;

		return true;
	}

	/*
		Averages out each waterSource by dividing the x and y locations by
		the waterSource's corresponding point count. If the point count is 0,
		we do nothing.

		RETURNS:
			true - operation done successfully.
			false - waterSources was not initialized.
	*/
	public boolean average() {
		if(this.waterSources == null || this.pointCount == null)
			return false;

		double x, y;

		for(int i = 0; i < this.waterSources.length; ++i) {
			if(this.pointCount[i] == 0)
				continue;

			x = this.waterSources[i].getX();
			y = this.waterSources[i].getY();

			x /= this.pointCount[i];
			y /= this.pointCount[i];

			this.waterSources[i].setLocation(x, y);
		}

		return true;
	}

	public float getDistToWater(int x, int y) {
		int closestPoint = getFirstNonzeroCount();
		float minDist, currDist;

		if(closestPoint == -1)
			return 0.0f;

		minDist = (float)getDistFromWaterSource(x, y, this.waterSources[closestPoint]);

		for(int i = closestPoint; i < this.waterSources.length; ++i) {
			if(this.waterSources[i].getX() != -1 && this.waterSources[i].getY() != -1) {
				currDist = (float)getDistFromWaterSource(x, y, this.waterSources[i]);

				if(currDist < minDist) {
					minDist = currDist;
					closestPoint = i;
				}
			}
		}

		return minDist;
	}

	/*
		Used to start the process of finding the closest water source.
		If this method returns -1, there are no sources of water on the map.
		If this method returns a value p == 0, then there is at least one source
		of water on the map.
		If this method returns a value p > 0, then every point before p was not
		water, and this one is. For this case and the previous one, there could be
		one more, but we need to scan for it.
	*/
	private int getFirstNonzeroCount() {
		for(int i = 0; i < this.pointCount.length; ++i) {
			if(i > 0)
				return i;
		}

		return -1;
	}

	private double getDistFromWaterSource(int x, int y, Point waterSource) {
		double xDiff, yDiff;

		xDiff = (double)x - waterSource.getX();
		yDiff = (double)y - waterSource.getY();

		xDiff *= xDiff;
		yDiff *= yDiff;

		return Math.sqrt(xDiff + yDiff);
	}
}
