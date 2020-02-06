package input.popup;

import input.Input;
import input.PopupButton;
import world.GameIDs.Image;

public class SetUp_PopupWindowGUI extends PopupWindowGUI{
	public SetUp_PopupWindowGUI() {
		width = 32 * 4 + 32;
		height = 64;
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
		buttons = new PopupButton[4];
		int buttonSize = 32;
		buttons[0] = new PopupButton(posY + (64 - buttonSize)/2, posX + buttonSize * 0 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[1] = new PopupButton(posY + (64 - buttonSize)/2, posX + buttonSize * 1 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[2] = new PopupButton(posY + (64 - buttonSize)/2, posX + buttonSize * 2 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[3] = new PopupButton(posY + (64 - buttonSize)/2, posX + buttonSize * 3 + 16, buttonSize, Image.BACKGROUND_LIGHTGREEN, Image.CURSOR) {
			@Override
			public void click() {
				this.image = Image.BACKGROUND_DARKGREEN;
			}
		};
		buttons[0].assignPaths(null, buttons[1], null, null);
		buttons[1].assignPaths(null, buttons[2], null, buttons[0]);
		buttons[2].assignPaths(null, buttons[3], null, buttons[1]);
		buttons[3].assignPaths(null, null, null, buttons[2]);
	}
}
