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
import world.GameIDs.Button;
import world.GameIDs.Cursor;
import world.GameIDs.Image;
import world.GameIDs.Zone;

public class StandardWindowGUI {
	// this class will contain the standard buttons used for construction
	// it will contain an array of buttons each with a simple function call
	// that will be executed when the player uses it in construction mode
	
	public HashMap<Button, StandardButton> buttons = null;
	
	public int posY = 64;
	public int posX = 0;
	
	public int size = 32;
	
	public Image image = Image.BUTTONS;
	
	public StandardButton pointer = null; // points to the currently highlighted button
	
	public static MiningBlock block = null;
	
	boolean buttonsAssigned = false;
	
	//public static int verticalOffset = 64; // units down the gui is
	//public static int horizontalOffset = 0; // units right the gui is
	//public static int buttonSize = 32;
	
	public Coordinates position = new Coordinates(0, 0); // the position of the cursor
	public int speed = 2;
	
	public int left = posX;
	public int right = posX + 64;
	public int top = posY;
	public int bottom = posY + 9 * 32;
	
	public void render(Graphics g, int tickCount, int season) {
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(Button.values()[i]).render(g, tickCount, season);
		}
		if(InterfaceGUI.state == State.BUILDING_MENU) {
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
				// use current button ID to make action
				click(position);
			}
			return State.BUILDING_MENU;
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
		// it does not take the button action but rather highlights the
		// button selected and sets the pointer to it, for later use
		
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(Button.values()[i]).withinRange(position)) {
				pointer = buttons.get(Button.values()[i]);
				pointer.clicked = true;
			}
			else {
				buttons.get(Button.values()[i]).clicked = false;
			}
		}
	}
	
	public void assignButtons() {
		buttons = new HashMap<Button, StandardButton>();// new StandardButton[Button.values().length];
		buttons.put(Button.BULLDOZE, new StandardButton(posY + 0 * size, posX + 0 * size, size, Button.values().length, 0, Cursor.BULLDOZE, image) {
			@Override
			public void click(int posY, int posX) {
				Game.world.deletionAt(getGridCoordsY(posY), getGridCoordsX(posX));
			};
		});
		pointer = buttons.get(Button.BULLDOZE);
		buttons.put(Button.ROAD, new StandardButton(posY + 0 * size, posX + 1 * size, size, Button.values().length, 1, Cursor.ONE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 1)) {
					Game.world.addBlock(Block.ROAD, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.RAIL, new StandardButton(posY + 1 * size, posX + 0 * size, size, Button.values().length, 2, Cursor.ONE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 1)) {
					Game.world.addBlock(Block.RAIL, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.POWERLINE, new StandardButton(posY + 1 * size, posX + 1 * size, size, Button.values().length, 3, Cursor.ONE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 1)) {
					Game.world.addBlock(Block.POWERLINE, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.FOREST, new StandardButton(posY + 2 * size, posX + 0 * size, size, Button.values().length, 4, Cursor.ONE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 1)) {
					Game.world.addBlock(Block.FOREST, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.RESIDENTIAL, new StandardButton(posY + 2 * size, posX + 1 * size, size, Button.values().length, 5, Cursor.THREE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 3)) {
					Game.world.addZone(Zone.RESIDENTIAL, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.COMMERCIAL, new StandardButton(posY + 3 * size, posX + 0 * size, size, Button.values().length, 6, Cursor.THREE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 3)) {
					Game.world.addZone(Zone.COMMERCIAL, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.INDUSTRIAL, new StandardButton(posY + 3 * size, posX + 1 * size, size, Button.values().length, 7, Cursor.THREE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 3)) {
					Game.world.addZone(Zone.INDUSTRIAL, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.POLICE, new StandardButton(posY + 4 * size, posX + 0 * size, size, Button.values().length, 8, Cursor.THREE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 3)) {
					Game.world.addZone(Zone.POLICE, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.FIRE, new StandardButton(posY + 4 * size, posX + 1 * size, size, Button.values().length, 9, Cursor.THREE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 3)) {
					Game.world.addZone(Zone.FIRE, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.STADIUM, new StandardButton(posY + 5 * size, posX + 0 * size, size, Button.values().length, 10, Cursor.FOUR, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 4)) {
					Game.world.addZone(Zone.STADIUM, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.SEAPORT, new StandardButton(posY + 5 * size, posX + 1 * size, size, Button.values().length, 11, Cursor.FOUR, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 4)) {
					Game.world.addZone(Zone.SEAPORT, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.COAL, new StandardButton(posY + 6 * size, posX + 0 * size, size, Button.values().length, 12, Cursor.FOUR, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 4)) {
					Game.world.addZone(Zone.COAL, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.NUCLEAR, new StandardButton(posY + 6 * size, posX + 1 * size, size, Button.values().length, 13, Cursor.FOUR, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 4)) {
					Game.world.addZone(Zone.NUCLEAR, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.AIRPORT, new StandardButton(posY + 7 * size, posX + 0 * size, size, Button.values().length, 14, Cursor.SIX, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 6)) {
					Game.world.addZone(Zone.AIRPORT, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.GIFT, new StandardButton(posY + 7 * size, posX + 1 * size, size, Button.values().length, 15, Cursor.THREE, image) {
			@Override
			public void click(int posY, int posX) {
				if(Game.world.ZoneAddable(getGridCoordsY(posY), getGridCoordsX(posX), 3)) {
					Game.world.addZone(Zone.GIFT, getGridCoordsY(posY), getGridCoordsX(posX));
				}
			};
		});
		buttons.put(Button.BLOCKCLICK, new StandardButton(posY + 8 * size, posX + 0 * size, size, Button.values().length, 16, Cursor.SELECT, image) {
			@Override
			public void click(int posY, int posX) {
				takeBlockClickAction(posY, posX);
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
	
	public void takeButtonAction(Coordinates position) {
		pointer.click(position.posY, position.posX);
	}
}
