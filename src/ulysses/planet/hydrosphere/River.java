// Liam Wynn, 2/1/2018, Ulysses

/*
	Stores all information about a particular River. In this case,
	it is simply a list of points.
*/

package ulysses.planet;

// The actual data we store
import java.awt.Point;
// The data structure we use to represent a river.
import java.util.ArrayList;

public class River {
	// Stores the points in our river.
	private ArrayList<Point> points;

	public River() {
		this.points = new ArrayList<>();
	}

	/*
		Inserts a point into the river.

		ARGUMENTS:
			toAdd - the point to add.

		RETURNS:
			true - The point was added to the river.
			false - toAdd was null, or it was already apart
			of the river.
	*/
	public boolean insertPoint(Point toAdd) {
		if(toAdd == null || containsPoint(toAdd))
			return false;

		return this.points.add(toAdd);
	}

	/*
		Removes a point from the river.

		ARGUMENTS:
			toRem - the point to remove.

		RETURNS:
			true - the point was removed from the river.
			false - the point was not apart of the river, or was null.
	*/
	public boolean removePoint(Point toRem) {
		if(toRem == null)
			return false;

		return this.points.remove(toRem);
	}

	/*
		Determines if a point is apart of this River.

		ARGUMENTS:
			toFind - the point to look for in the river.

		RETURNS
			true - toFind is apart of this river.
			false - toFind is not apart of this river, or
			toFind is null.
	*/
	public boolean containsPoint(Point toFind) {
		if(toFind == null)
			return false;

		return this.points.contains(toFind);
	}

	/*
		Returns if the river has points or not.
	*/
	public boolean isEmpty() {
		return this.points.isEmpty();
	}
}
