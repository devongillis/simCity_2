package input.popup;

import input.Input;
import input.PopupButton;
import world.GameIDs.Image;

public class Statistics_PopupWindowGUI extends PopupWindowGUI{
	// this class is going to open up a large gui with many buttons
	// and some of these buttons will open other guis with their own
	// charts and buttons as well
	public Statistics_PopupWindowGUI() {
		width = 32 * 3 + 32;
		height = 32 * 2 + 32;
		posY = 0;
		posX = 0;
		image = Image.BACKGROUND_GREY;
	}
	
	@Override
	public void openWindow() {
		Input.flushInputs();
		pointer = buttons[0];
	}
	
	@Override
	public void assignButtons() {
		buttons = new PopupButton[6];
		int buttonSize = 32;
		buttons[0] = new PopupButton(posY + 16, posX + buttonSize * 0 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[1] = new PopupButton(posY + 16, posX + buttonSize * 1 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[2] = new PopupButton(posY + 16, posX + buttonSize * 2 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[3] = new PopupButton(posY + buttonSize + 16, posX + buttonSize * 0 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[4] = new PopupButton(posY + buttonSize + 16, posX + buttonSize * 1 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[5] = new PopupButton(posY + buttonSize + 16, posX + buttonSize * 2 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[0].assignPaths(null, buttons[1], buttons[3], null);
		buttons[1].assignPaths(null, buttons[2], buttons[4], buttons[0]);
		buttons[2].assignPaths(null, null, buttons[5], buttons[1]);
		
		buttons[3].assignPaths(buttons[0], buttons[4], null, null);
		buttons[4].assignPaths(buttons[1], buttons[5], null, buttons[3]);
		buttons[5].assignPaths(buttons[2], null, null, buttons[4]);
	}
}
