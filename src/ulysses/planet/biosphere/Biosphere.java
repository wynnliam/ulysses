// Liam Wynn, 4/14/2018, Ulysses

/*
	The Biosphere manages the biome data for every point.
	We store this as an array of HoldridgeData, which
	goes more into detail about the Holdridge climate system.
*/

package ulysses.planet;

public class Biosphere {
	// The dimensions of the world.
	private int width, height;

	public Biosphere(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int val) {
		if(val <= 0)
			val = 256;

		this.width = val;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int val) {
		if(val <= 0)
			val = 128;

		this.height = val;
	}
}
