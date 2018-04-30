// Liam Wynn, 4/14/2018, Ulysses

/*
	The Biosphere manages the biome data for every point.
	We store this as an array of HoldridgeData, which
	goes more into detail about the Holdridge climate system.
*/

package ulysses.planet;

import ulysses.planet.holdridge.HoldridgeData;

public class Biosphere {
	// The dimensions of the world.
	private int width, height;

	// Stores the biome data of each point.
	private HoldridgeData[] biomeData;

	public Biosphere(int width, int height) {
		setWidth(width);
		setHeight(height);

		this.biomeData = new HoldridgeData[this.width * this.height];
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

	public HoldridgeData getBiomeData(int x, int y) {
		if(x < 0 || x >= this.width || y < 0 || y >= this.height)
			return null;

		return this.biomeData[y * this.width + x];
	}

	public void setHoldridgeData(int x, int y, HoldridgeData val) {
		if(x < 0 || x >= this.width || y < 0 || y >= this.height)
			return;

		this.biomeData[y * this.width + x] = val;
	}
}
