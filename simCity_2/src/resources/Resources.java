package resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import graphics.Renderer;
import world.GameIDs.Image;

public class Resources {
	
	public static boolean allLoaded = false;
	private static HashMap<Image, BufferedImage[]> images = new HashMap<Image, BufferedImage[]>();
	
	
	public static void loadImages() {
		images.put(Image.FOREST, createBufferedImages(4, 16, "swaying_tree.png"));
		images.put(Image.RESIDENTIAL, createBufferedImages(9, 16, "residential_zones.png"));
		images.put(Image.INDUSTRIAL, createBufferedImages(9, 16, "industrial_zones.png"));
		images.put(Image.COMMERCIAL, createBufferedImages(9, 16, "commercial_zones.png"));
		images.put(Image.POLICE, createBufferedImages(3, 16, "police_department.png"));
		images.put(Image.NUCLEAR, createBufferedImages(4, 16, "nuclear.png"));
		images.put(Image.POWERLINE, createBufferedImages(1, 16, "power_line.png"));
		images.put(Image.POWEROFF, createBufferedImages(1, 16, "power_out.png"));
		images.put(Image.DIRT, createBufferedImages(1, 8, "dirt.png"));
		images.put(Image.BUILDINGSCROLLER, createBufferedImages(1, 32, "building_scroller.png"));
		images.put(Image.BUILDER3, createBufferedImages(1, 64, "builder_3.png"));
		images.put(Image.RESOURCESELECTOR, createBufferedImages(1, 32, "resource_selector.png"));
		images.put(Image.WATER, createBufferedImages(4, 8, "water_tiles.png"));
		images.put(Image.BUTTONS, createBufferedImages(6, 32, "buttons_updated.png"));
		images.put(Image.AIRPORT, createBufferedImages(6, 16, "airport.png"));
		images.put(Image.COALMINER, createBufferedImages(1, 16, "coal_mine.png"));
		images.put(Image.COALREFINER, createBufferedImages(3, 16, "coal_refinery.png"));
		images.put(Image.RESOURCEMENU, createBufferedImages(1, 48, "resource_destination_menu.png"));
		images.put(Image.BUTTONRED, createBufferedImages(1, 16, "button_red.png"));
		images.put(Image.BUTTONGREEN, createBufferedImages(1, 16, "button_green.png"));
		images.put(Image.POINTER, createBufferedImages(1, 32, "pointer.png"));
		images.put(Image.CURSOR, createBufferedImages(1, 16, "button_cursor.png"));
		images.put(Image.TOPBUTTONS, createBufferedImages(4, 16, "top_buttons.png"));
		images.put(Image.BACKGROUND_GREY, createBufferedImages(1, 32, "background_grey.png"));
		images.put(Image.BACKGROUND_LIGHTGREEN, createBufferedImages(1, 32, "background_lightgreen.png"));
		images.put(Image.BACKGROUND_DARKGREEN, createBufferedImages(1, 32, "background_darkgreen.png"));
		
		allLoaded = true;
	}
	
	public static BufferedImage getImage(Image ID, int index) {
		return images.get(ID)[index];
	}
	
	private static BufferedImage loadBufferedImage(String resource, int size, int newSize) {
		BufferedImage sheet = null;
		try {
			sheet = ImageIO.read(Resources.class.getResource("/resources/" + resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		float mf = (float)newSize/size;
		int w = (int) (sheet.getWidth() * mf);
		int h = (int) (sheet.getHeight() * mf);
		// size here is being used to determine the in game pixel dimensions to use
		// if game is 1000 pixels and 16 is used then each block is 16/1000 the width
		// of the game screen
		BufferedImage image = Renderer.canvas.getGraphicsConfiguration().createCompatibleImage(w, h, sheet.getTransparency());
		// size here is used to determine how the image will fill the 16x16 square created
		image.getGraphics().drawImage(sheet, 0, 0, w, h, null);
		return image;
	}
	
	private static BufferedImage loadBufferedImage(String resource) {
		BufferedImage sheet = null;
		try {
			sheet = ImageIO.read(Resources.class.getResource("/resources/" + resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage image = Renderer.canvas.getGraphicsConfiguration().createCompatibleImage(sheet.getWidth(), sheet.getHeight(), sheet.getTransparency());
		image.getGraphics().drawImage(sheet, 0, 0, sheet.getWidth(), sheet.getHeight(), null);
		return image;
	}
	
	private static BufferedImage[] createBufferedImages(int sheetDimensions, int individualSquareSize, String resource) {
		int size = sheetDimensions * sheetDimensions;
		BufferedImage sheet = null;
		BufferedImage[] spriteSheet = new BufferedImage[size];
		try {
			sheet = ImageIO.read(Resources.class.getResource("/resources/" + resource));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < size; i++) {
			int x = (i % sheetDimensions) * individualSquareSize;
			int y = (i / sheetDimensions) * individualSquareSize;
			BufferedImage raw = sheet.getSubimage(x, y, individualSquareSize, individualSquareSize);
			spriteSheet[i] = Renderer.canvas.getGraphicsConfiguration().createCompatibleImage(raw.getWidth(), raw.getHeight(), raw.getTransparency());
			spriteSheet[i].getGraphics().drawImage(raw, 0, 0, raw.getWidth(), raw.getHeight(), null);	
		}
		return spriteSheet;
	}
	
}
