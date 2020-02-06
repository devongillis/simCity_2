package input;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import blocks.resource_blocks.MiningBlock;
import game.Game;
import input.InterfaceGUI.State;
import player.Coordinates;
import resources.Resources;
import world.GameIDs.Block;
import world.GameIDs.Cursor;
import world.GameIDs.Image;
import world.GameIDs.TopButton;
import world.GameIDs.Zone;

public class TopWindowGUI {
	
	public HashMap<TopButton, StandardButton> buttons = null;
	
	public int posY = 0;
	public int posX = 0;
	
	public int size = 32;
	
	public Image image = Image.TOPBUTTONS;
	
	
	//boolean buttonsAssigned = false;
	
	//public static int verticalOffset = 64; // units down the gui is
	//public static int horizontalOffset = 0; // units right the gui is
	//public static int buttonSize = 32;
	
	public Coordinates position = new Coordinates(0, 0); // the position of the cursor
	public int speed = 2;
	
	public int left = posX;
	public int right = posX + 32 * 8;
	public int top = posY;
	public int bottom = 64;
	
	public void render(Graphics g, int tickCount, int season) {
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(TopButton.values()[i]).render(g, tickCount, season);
		}
		if(InterfaceGUI.state == State.TOP_MENU) {
			// render cursor
			int realX = (int) position.posX;
			int realY = (int) position.posY;
			g.drawImage(Resources.getImage(Image.POINTER, 0), realX, realY, 32, 32, null);
		}
	}
	
	public State move() {
		// this function should move the cursor across the buttons based off input
		// it then checks the boundaries and switches states if appropriate
		// it then checks for button clicks
		if(Input.getKey(KeyEvent.VK_D)) {
			position.posX += speed;
		}
		else if(Input.getKey(KeyEvent.VK_A)) {
			position.posX -= speed;
		}
		
		if(Input.getKey(KeyEvent.VK_S)) {
			position.posY += speed;
		}
		else if(Input.getKey(KeyEvent.VK_W)) {
			position.posY -= speed;
		}
		
		if(!withinBounds(position)) {
			return InterfaceGUI.RecalculateState(position);
		}
		else {
			if(Input.getKey(KeyEvent.VK_J)) {
				click(position);
			}
			
			return InterfaceGUI.state;
		}
	}
	
	public boolean withinBounds(Coordinates p) {
		// we need to send coords because this function can be called
		// by both this class with its own coords or the interface with another set of coords
		
		// this function will return true if the current cursor position is within the
		// bounds of the gui, if not then return false
		if(p.posX >= left && p.posX <= right - 32/2) {
			if(p.posY >= top && p.posY <= bottom - 32/2) {
				return true;
			}
		}
		return false;
	}
	
	private void click(Coordinates position) {
		// this function is called when the player has clicked a button
		// it then proceeds to activate a popup window
		
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(TopButton.values()[i]).withinRange(position)) {
				buttons.get(TopButton.values()[i]).clicked = true;
				buttons.get(TopButton.values()[i]).click(position.posY, position.posX);
			}
			else {
				buttons.get(TopButton.values()[i]).clicked = false;
			}
		}
	}
	// need to fix these 
	public void assignButtons() {
		
		buttons = new HashMap<TopButton, StandardButton>();
		buttons.put(TopButton.GAMESPEED, new StandardButton(posY, posX + 0 * size, size, TopButton.values().length, 0, null, image) {
			@Override
			public void click(int posY, int posX) {
				InterfaceGUI.SwitchToSelectGameSpeedWindow();
			};
		});
		buttons.put(TopButton.SETUP, new StandardButton(posY, posX + 1 * size, size, TopButton.values().length, 1, null, image) {
			@Override
			public void click(int posY, int posX) {
				InterfaceGUI.SwitchToSetUpWindow();
			};
		});
		buttons.put(TopButton.DISASTERS, new StandardButton(posY, posX + 2 * size, size, TopButton.values().length, 2, null, image) {
			@Override
			public void click(int posY, int posX) {
				InterfaceGUI.SwitchToDisastersWindow();
			};
		});
		buttons.put(TopButton.STATISTICS, new StandardButton(posY, posX + 3 * size, size, TopButton.values().length, 3, null, image) {
			@Override
			public void click(int posY, int posX) {
				
			};
		});
		buttons.put(TopButton.SAVELOAD, new StandardButton(posY, posX + 4 * size, size, TopButton.values().length, 4, null, image) {
			@Override
			public void click(int posY, int posX) {
				
			};
		});
		buttons.put(TopButton.ADVICE, new StandardButton(posY, posX + 5 * size, size, TopButton.values().length, 5, null, image) {
			@Override
			public void click(int posY, int posX) {
				
			};
		});
		buttons.put(TopButton.MAGNIFY, new StandardButton(posY, posX + 6 * size, size, TopButton.values().length, 6, null, image) {
			@Override
			public void click(int posY, int posX) {
				
			};
		});
		buttons.put(TopButton.HELICOPTER, new StandardButton(posY, posX + 7 * size, size, TopButton.values().length, 7, null, image) {
			@Override
			public void click(int posY, int posX) {
				
			};
		});
	}
	
	public void takeBlockClickAction(int posY, int posX) {
		// this function is called when in resource select mood and player has clicked on the grid
		// check to see if resource block
		if(Game.world.gameBlocks[getGridCoordsY(posY)][getGridCoordsX(posX)] instanceof MiningBlock) {
			// we open the gui and set block to the found mining block
			InterfaceGUI.SwitchToResourceDestinationWindow((MiningBlock) Game.world.gameBlocks[getGridCoordsY(posY)][getGridCoordsX(posX)]);
		}
	}
	
	public int getGridCoordsY(int posY) {
		return (posY - Game.world.offsetY)/16;
	}
	
	public int getGridCoordsX(int posX) {
		return (posX - Game.world.offsetX)/16;
	}
	
}
