// Liam Wynn, 12/21/2017, Ulysses

package ulysses;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;

// TODO: Test code!
import ulysses.planet.*;
import ulysses.planet.utilities.generators.*;
import ulysses.planet.utilities.PlanetMap;
import java.util.Random;
import java.awt.Color;
import java.awt.image.BufferedImage;

import java.io.File;
import javax.imageio.ImageIO;

import java.awt.Point;

class Ulysses {
	public static void main(String[] args) {
		UlyssesRunnable ulyssesDriver = new UlyssesRunnable();
		SwingUtilities.invokeLater(ulyssesDriver);
	}
}

class UlyssesRunnable implements Runnable {
	public void run() {
		createGUI();
	}

	private void createGUI() {
		// Creates the window.
		JFrame frame = new JFrame("Ulysses");
		frame.setSize(800, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Panel to add stuff to. We use a gridbag layout.
		// We need to use this when using the Grid Bag Constraints.
		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel, BorderLayout.WEST);

		// Controls how we add components to the panel.
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		// Create and add the display screen.
		UlyssesGraphicsPanel screen = new UlyssesGraphicsPanel();
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(screen, gbc);

		//long seed = System.nanoTime();
		long seed = "Ponyri".hashCode();
		Random rooseBolton = new Random(seed);
		int w = 1024;
		int h = 512;

		/* LITHOSPHERE */
		LithosphereGenerator lg = new LithosphereGenerator();
		// Generators for lg.
		TectonicsMapGenerator tectonics;
		PerlinMapGenerator thickness;
		PerlinMapGenerator orogenics;

		tectonics = new TectonicsMapGenerator(rooseBolton);
		tectonics.setNumPlates(60);

		thickness = new PerlinMapGenerator(rooseBolton);
		thickness.setOctaveCount(8);
		thickness.setPersistence(0.75f);

		orogenics = new PerlinMapGenerator(rooseBolton);
		orogenics.setOctaveCount(8);
		orogenics.setPersistence(0.75f);

		lg.setWidth(w);
		lg.setHeight(h);
		lg.setPercentLand(0.20f);
		lg.setPercentMountains(0.05f);
		lg.setTectonicsMapGenerator(tectonics);
		lg.setThicknessMapGenerator(thickness);
		lg.setOrogenicsMapGenerator(orogenics);

		Lithosphere litho = lg.generateLithosphere();
		PlanetMap height = litho.getHeightMap(0.63f, 0.37f);

		/* HYDROSPHERE */
		HydrosphereGenerator hg = new HydrosphereGenerator("Squanto".hashCode());
		// Generators for hg;
		PerlinMapGenerator clouds;
		PerlinMapGenerator riverSourceMod;
		EquatorMapGenerator lat;

		clouds = new PerlinMapGenerator(rooseBolton);
		clouds.setOctaveCount(8);
		clouds.setPersistence(0.75f);

		riverSourceMod = new PerlinMapGenerator(rooseBolton);
		riverSourceMod.setOctaveCount(1);
		riverSourceMod.setPersistence(0.95f);

		lat = new EquatorMapGenerator(rooseBolton);
		lat.setWidth(w);
		lat.setHeight(h);
		lat.setEquator(h / 2);

		hg.setWidth(w);
		hg.setHeight(h);
		hg.setHeightMap(height);
		hg.setCloudFreqMapGenerator(clouds);
		hg.setRiverSourceModifierMap(riverSourceMod);
		hg.setEquatorMapGenerator(lat);

		hg.setNumRivers(100);
		hg.setSeaLevel(0.37f);

		Hydrosphere hydro = hg.generateHydrosphere();
		PlanetMap precip = hydro.getPrecipitationMap();
		PlanetMap riverMap = hydro.getRiverMap();

		BufferedImage colorMap;

		try {
			colorMap = ImageIO.read(new File("./content/heightcolormap.png"));
		}

		catch(Exception e) {
			colorMap = null;
		}

		if(colorMap == null) {
			System.out.println("Failed to load image");
			return;
		}

		int index;
		int chan;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Color c;

		for(int x = 0; x < w; ++x) {
			for(int y = 0; y < h; ++y) {
				chan = (int)(255.0f * precip.getData(x, y));

				if(height.getData(x, y) <= 0.37f)
					image.setRGB(x, y, Color.BLUE.getRGB());
				else
					image.setRGB(x, y, new Color(chan, chan, chan).getRGB());

				/*if(height.getData(x, y) >= 0.63f)
					image.setRGB(x, y, Color.WHITE.getRGB());
				else if(height.getData(x, y) > 0.37f)
					image.setRGB(x, y, Color.GREEN.getRGB());
				else
					image.setRGB(x, y, Color.BLUE.getRGB());

				if(riverMap.getData(x, y) == 1)
					image.setRGB(x, y, Color.BLACK.getRGB());*/
			}
		}

		frame.pack();
		screen.setImage(image);
		screen.repaint();
	}
}

class UlyssesGraphicsPanel extends JPanel {
	// The image that we will render.
	private Image image;

	public UlyssesGraphicsPanel() {
		this.image = null;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Dimension getPreferredSize() {
		return new Dimension(1024, 512);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		System.out.println("Redraw!");

		if(this.image == null)
			return;

		g.drawImage(this.image, 0, 0, null);
	}
}
