package input;

import java.awt.Graphics;

import player.Coordinates;
import resources.Resources;
import world.GameIDs.Image;

public class PopupButton {
	
	public int posY;
	public int posX;
	
	public int size;
	
	public Image image;
	public Image cursor;
	
	public PopupButton[] popupButtons = null;
	
	public PopupButton(int posY, int posX, int size, Image image, Image cursor) {
		this.posY = posY;
		this.posX = posX;
		this.size = size;
		this.image = image;
		this.cursor = cursor;
		popupButtons = new PopupButton[4];
	}
	
	public void render(Graphics g, int tickCount, int season) {
		g.drawImage(Resources.getImage(image, 0), posX, posY, size, size, null);
	}
	
	public void renderCursor(Graphics g, int tickCount, int season) {
		g.drawImage(Resources.getImage(cursor, 0), posX, posY, size, size, null);
	}
	
	public void assignPaths(PopupButton a, PopupButton b, PopupButton c, PopupButton d) {
		// popupButtons should be length 4, and null if button does not exist
		// 0 = top, 1 = right, 2 = bottom, 3 = left
		if(a != null) {
			this.popupButtons[0] = a;
		}
		if(b != null) {
			this.popupButtons[1] = b;
		}
		if(c != null) {
			this.popupButtons[2] = c;
		}
		if(d != null) {
			this.popupButtons[3] = d;
		}
	}
	
	public void click() {
		// this function is overridden to make calls to the game to do certain actions
	}
	
	public void unClick() {
		this.image = Image.BACKGROUND_LIGHTGREEN;
	}
	
	public Coordinates getCoordinates() {
		return new Coordinates(posY + 8, posX + 8);
	}
}
