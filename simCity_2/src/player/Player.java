package player;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;

import game.Game;
import input.Input;
import input.InterfaceGUI;
import input.InterfaceGUI.State;
import resources.Resources;
import world.GameIDs.Cursor;
import world.GameIDs.Image;

public class Player {
	// player is just a movable object to show where you are building next
	
	// the players actions should be broken down into several states each with different
	// ways for the player to move based of current state, gui is the biggest factor
	
	// multiple cursors are to be used for the player, if in the gui it should be a finger
	// if in construction zone it should reflect the gui button action, bulldoze, construct,
	// select resource
	
	//private static final long serialVersionUID = 1L;
	
	private final int speed = 2;
	private final int worldScrollSpeed = 4;
	private int directionX = 0;
	private int directionY = 0;
	
	// position refers to game coordinates, they get truncated to match
	// the world grid coordinates upon any interaction with the grid
	public Coordinates position = new Coordinates(0, 0);
	public int playerSize = 32;
	public int playerOffset = 8;
	
	Image image = null;
	Image builder3 = null;
	
	// the boundaries of the player movement
	public int left = 64;
	public int right = 480;
	public int top = 64;
	public int bottom = 432;
	
	
	
	
	
	
	public Player() {
		image = Image.BUILDINGSCROLLER;
		builder3 = Image.BUILDER3;
	}
	
	
	public void render(Graphics g, int tickCount, int season) {
		// some code to get info on zones current state:
		// power, population, grade
		// then use this to find image
		
		int realX = position.posX - playerOffset;
		int realY = position.posY - playerOffset;
				
		if(InterfaceGUI.standardWindowGUI.pointer.id == Cursor.BULLDOZE) {
			g.drawImage(Resources.getImage(image, 0), realX, realY, playerSize, playerSize, null);
		}
		else if(InterfaceGUI.standardWindowGUI.pointer.id == Cursor.SELECT) {
			g.drawImage(Resources.getImage(Image.RESOURCESELECTOR, 0), realX, realY, playerSize, playerSize, null);
		}
		else if(InterfaceGUI.standardWindowGUI.pointer.id == Cursor.THREE) {
			g.drawImage(Resources.getImage(builder3, 0), realX - 16, realY - 16, 64, 64, null);
		}
	}
	
	public State destinationUpdate(float deltaTime, int tickCount) {
		// this function is called each frame when we are looking for a resource destination
		// move like we would regularly but if a button is clicked we must check for destination
		// assignment if so then assign, if not then switch to regular update
		moveX();
		moveY();
		moveX_world();
		moveY_world();
		if(!withinBounds(position)) {
			return InterfaceGUI.RecalculateState(position);
		}
		else {
		
			if(Input.getKey(KeyEvent.VK_J)) {
				// use current button ID to make action
				InterfaceGUI.assignResourceToDestination(position);
				return State.CONSTRUCTION;
			}
			
			
		
			return State.SELECT_DESTINATION;
		}
	}
	
	public State update(float deltaTime, int tickCount) {
		// we move the player from current cursor position, if this results in out of bounds
		// we return the appropriate state to switch to, and line up world if switching states
		
		// these should be combined as we should only be able to move in one or the other at any time
		moveX();
		moveY();
		moveX_world();
		moveY_world();
		
		if(!withinBounds(position)) {
			return InterfaceGUI.RecalculateState(position);
		}
		else {
		
			if(Input.getKey(KeyEvent.VK_J)) {
				// use current button ID to make action
				InterfaceGUI.standardWindowGUIButtonAction(position); // when called it finds the current button and calls its action
				if(InterfaceGUI.state == State.POPUP) {
					// the state got switched as we have activated a popup
					return State.POPUP;
				}
			}
			
			if(Input.getKey(KeyEvent.VK_P)) {
				try {
					Game.saveWorld();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
			return State.CONSTRUCTION;
		}
	}
	
	public boolean withinBounds(Coordinates p) {
		if(p.posX >= left && p.posX <= right) {
			if(p.posY >= top && p.posY <= bottom) {
				return true;
			}
		}
		return false;
	}
	
	private void moveX() {
		if(Input.getKey(KeyEvent.VK_D)) {
			position.posX += speed;
			directionX = 1;
		}
		else if(Input.getKey(KeyEvent.VK_A)) {
			position.posX -= speed;
			directionX = -1;
		}
		else if(Math.abs(position.posX) % 16 > 0){
			lineUpX();
		}
	}
	
	private void lineUpX() {
		// we are not perfectly lined up with the grid, use direction to line up
		if(directionX == -1) {
			// was moving backwards
			position.posX -= (position.posX % 16);
		}
		else if(directionX == 1) {
			position.posX += (16 - (position.posX % 16));
		}
		else {
			System.out.println("player lineUpX error");
		}
	}
	
	private void moveY() {
		if(Input.getKey(KeyEvent.VK_S)) {
			position.posY += speed;
			directionY = 1;
		}
		else if(Input.getKey(KeyEvent.VK_W)) {
			position.posY -= speed;
			directionY = -1;
		}
		else if(Math.abs(position.posY) % 16 > 0){
			lineUpY();
		}
	}
	
	private void lineUpY() {
		// we are not perfectly lined up with the grid, use direction to line up
		if(directionY == -1) {
			// was moving backwards
			position.posY -= (position.posY % 16);
		}
		else if(directionY == 1) {
			position.posY += (16 - (position.posY % 16));
		}
		else {
			System.out.println("error");
		}
	}
	
	private void moveX_world() {
		// we still want to be able to move the world in block increments
		// only player screen movement has changed
		if(Input.getKey(KeyEvent.VK_RIGHT)) {
			Game.world.offsetX -= worldScrollSpeed;
			directionX = -1;
		}
		else if(Input.getKey(KeyEvent.VK_LEFT)) {
			Game.world.offsetX += worldScrollSpeed;
			directionX = 1;
		}
		else if(Math.abs(Game.world.offsetX) % 16 > 0){
			lineUpX_world();
		}
	}
	
	private void lineUpX_world() {
		// we are not perfectly lined up with the grid, use direction to line up
		if(directionX == -1) {
			// was moving backwards
			// we adjust the world offset
			Game.world.offsetX -= (Game.world.offsetX % 16);
		}
		else if(directionX == 1) {
			// we adjust the world offset
			Game.world.offsetX -= (16 + (Game.world.offsetX % 16));
		}
		else {
			System.out.println("error");
		}
	}
	
	private void moveY_world() {
		if(Input.getKey(KeyEvent.VK_UP)) {
			Game.world.offsetY += worldScrollSpeed;
			directionY = -1;
		}
		else if(Input.getKey(KeyEvent.VK_DOWN)) {
			Game.world.offsetY -= worldScrollSpeed;
			directionY = 1;
		}
		else if(Math.abs(Game.world.offsetY) % 16 > 0){
			lineUpY_world();
		}
	}
	
	private void lineUpY_world() {
		// we are not perfectly lined up with the grid, use direction to line up
		if(directionY == -1) {
			// was moving backwards
			// we adjust the world offset
			Game.world.offsetY -= (Game.world.offsetY % 16);
		}
		else if(directionY == 1) {
			// we adjust the world offset
			Game.world.offsetY -= (16 + (Game.world.offsetY % 16));
		}
		else {
			System.out.println("error");
		}
	}
	
}