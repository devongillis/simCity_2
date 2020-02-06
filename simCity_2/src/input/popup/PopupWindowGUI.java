package input.popup;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import input.Input;
import input.InterfaceGUI;
import input.PopupButton;
import input.InterfaceGUI.State;
import resources.Resources;
import world.GameIDs.Image;

public class PopupWindowGUI {
	// this class will act as a popup window which sup class windows will be based
	// off, it haves a queue of buttons each with a list of button tags to indicate
	// where the scroller should go when a player wants to select a new button,
	// example: [A] [B]
	//          [C] [D]
	//              [E]
	// A goes to B and C by moving right or down, while C cannot go down as there is no
	// button below it
	
	public int posY;
	public int posX;
	
	//public int size;
	
	public int width;
	public int height;
	
	public Image image;
	
	PopupButton[] buttons = null;
	
	public PopupButton pointer = null;
	
	boolean buttonsAssigned = false;
	//boolean ready = false;
	
	public int timer = 0;
	public int timeLimit = 15;
	
	public void render(Graphics g, int tickCount, int season) {
		g.drawImage(Resources.getImage(image, 0), posX, posY, width, height, null);
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].render(g, tickCount, season);
			if(pointer == buttons[i]) {
				buttons[i].renderCursor(g, tickCount, season);
			}
		}
	}
	
	public void assignButtons() {
		// this function is overridden with sub classes that know their own button layouts
	}
	
	public void closeWindow() {
		// reset the buttons
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].unClick();
		}
		InterfaceGUI.state = State.CONSTRUCTION;
	}
	
	public void openWindow() {
		// this should set the pointer to a default button
		// and return the player coordinates for that button
		//pointer = buttons[0];
		//return null;
		Input.flushInputs();
		pointer = buttons[0];
	}
	
	public void playerMakeInput() {
		if(timer >= timeLimit) {
			if(Input.getKeyTyped(KeyEvent.VK_W)) {
				// from the current button move down
				if(pointer.popupButtons[0] != null) {
					pointer = pointer.popupButtons[0];
				}
			}
			else if(Input.getKeyTyped(KeyEvent.VK_D)) {
				// from the current button move down
				if(pointer.popupButtons[1] != null) {
					pointer = pointer.popupButtons[1];
				}
			}
			else if(Input.getKeyTyped(KeyEvent.VK_S)) {
				// from the current button move down
				if(pointer.popupButtons[2] != null) {
					pointer = pointer.popupButtons[2];
				}
			}
			else if(Input.getKeyTyped(KeyEvent.VK_A)) {
				// from the current button move down
				if(pointer.popupButtons[3] != null) {
					pointer = pointer.popupButtons[3];
				}
			}
			else if(Input.getKeyTyped(KeyEvent.VK_J)) {
				// button click
				pointer.click();
			}
			else if(Input.getKeyTyped(KeyEvent.VK_K)) {
				closeWindow();
			}
		}
		else {
			timer++;
			Input.flushInputs();
		}
		//return pointer.getCoordinates();
	}
}
