package input.popup;

import input.Input;
import input.InterfaceGUI;
import input.PopupButton;
import input.InterfaceGUI.State;
import world.GameIDs.Image;

public class ResourceDestination_PopupWindowGUI extends PopupWindowGUI{

	
	
	public ResourceDestination_PopupWindowGUI() {
		width = 48;
		height = 48;
		posY = (432 - height)/2;
		posX = (480 - width)/2;
		image = Image.RESOURCEMENU;
	}
	
	@Override
	public void openWindow() {
		Input.flushInputs();
		pointer = buttons[0];
		//return pointer.getCoordinates();
	}
	
	
	@Override
	public void closeWindow() {
		InterfaceGUI.state = State.CONSTRUCTION;
	}
	
	@Override
	public void assignButtons() {
		buttons = new PopupButton[2];
		int buttonSize = 16;
		buttons[0] = new PopupButton(posY + (24 - buttonSize), posX - buttonSize/2, buttonSize, Image.BUTTONRED, Image.CURSOR) {
			@Override
			public void click() {
				// this button should set the destination to the city storage
				// and then exit the popup
				InterfaceGUI.block.assignDestination(null);
				closeWindow();
				//ready = false;
			}
		};
		buttons[1] = new PopupButton(posY + (24 - buttonSize), posX + (48 - buttonSize) - buttonSize/2, buttonSize, Image.BUTTONGREEN, Image.CURSOR) {
			@Override
			public void click() {
				// this should put the player in select block mode
				closeWindow();
				//ready = false;
			}
		};
		buttons[0].assignPaths(null, buttons[1], null, null);
		buttons[1].assignPaths(null, null, null, buttons[0]);
	}

}
