package input;

import java.awt.Graphics;

import game.Game;
import player.Coordinates;
import resources.Resources;
import world.GameIDs.Cursor;
import world.GameIDs.Image;

public class StandardButton {
	public int posY;
	public int posX;
	
	public int size;
	
	public Image image;
	public int imageNumber;
	public int numberOfButtons;
	
	public boolean clicked = false;
	
	public Cursor id = null;
	
	public StandardButton(int posY, int posX, int size, int numberOfButtons, int imageNumber, Cursor id, Image image) {
		this.posY = posY;
		this.posX = posX;
		this.size = size;
		this.image = image;
		this.numberOfButtons = numberOfButtons;
		this.imageNumber = imageNumber;
		this.id = id;
	}
	
	public void render(Graphics g, int tickCount, int season) {
		if(clicked) {
			g.drawImage(Resources.getImage(image, imageNumber + numberOfButtons), posX, posY, size, size, null);
		}
		else {
			g.drawImage(Resources.getImage(image, imageNumber), posX, posY, size, size, null);
		}
	}
	
	public void click(int posY, int posX) {
		// this function is overridden to make calls to the game to do certain actions
	}
	
	public int getGridCoordsY(int posY) {
		return (posY - Game.world.offsetY)/16;
	}
	
	public int getGridCoordsX(int posX) {
		return (posX - Game.world.offsetX)/16;
	}
	
	public boolean withinRange(Coordinates position) {
		// this function will test if the coords are within the range this
		// button covers to be considered clicked
		if(imageNumber == 0) {
			//System.out.println(position.posX + " " + posX + " " + position.posY + " " + posY);
		}
		if(position.posX < posX + 16 && position.posX >= posX - 16 && position.posY < posY + 16 && position.posY >= posY - 16) {
			//System.out.println("fucker" + imageNumber);
			return true;
		}
		if((posX - position.posX)/size == 0 && (posY - position.posY)/size == 0) {
			// the gap between the position and the button is less than the button width
			// therefore we are inside the buttons range
			//return true;
		}
		return false;
	}
}
