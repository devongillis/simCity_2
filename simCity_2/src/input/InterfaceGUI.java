package input;

import java.awt.Graphics;

import blocks.resource_blocks.MiningBlock;
import game.Game;
import input.popup.Advice_PopupWindowGUI;
import input.popup.Disasters_PopupWindowGUI;
import input.popup.GameSpeed_PopupWindowGUI;
import input.popup.ResourceDestination_PopupWindowGUI;
import input.popup.SaveLoad_PopupWindowGUI;
import input.popup.SetUp_PopupWindowGUI;
import input.popup.Statistics_PopupWindowGUI;
import player.Coordinates;
import player.Player;
import zones.GameZone;
import zones.resource_zones.RefineryZone;

public class InterfaceGUI {
	// this class will contain the entire interface of the GUI whenever 
	// a player does an action outside of movement the GUI will first
	// test whether the GUI is to be involved and then promptly take action
	// mostly button clicks which will switch GUI states and construction
	// states
	
	public static MiningBlock block = null; // a reference for when a player is selecting a new destination
	
	public static State state = State.CONSTRUCTION;
	public static PopupState popup = PopupState.RESOURCE_DESTINATION_MENU;// default doesn't matter
	
	public static ResourceDestination_PopupWindowGUI resourceDestination_PopupWindowGUI = new ResourceDestination_PopupWindowGUI();
	
	public static GameSpeed_PopupWindowGUI gameSpeed_PopupWindowGUI = new GameSpeed_PopupWindowGUI();
	public static SetUp_PopupWindowGUI setUp_PopupWindowGUI = new SetUp_PopupWindowGUI();
	public static Disasters_PopupWindowGUI disasters_PopupWindowGUI = new Disasters_PopupWindowGUI();
	public static Statistics_PopupWindowGUI statistics_PopupWindowGUI = new Statistics_PopupWindowGUI();
	public static SaveLoad_PopupWindowGUI saveLoad_PopupWindowGUI = new SaveLoad_PopupWindowGUI();
	public static Advice_PopupWindowGUI advice_PopupWindowGUI = new Advice_PopupWindowGUI();
	
	public static StandardWindowGUI standardWindowGUI = new StandardWindowGUI();
	public static TopWindowGUI topWindowGUI = new TopWindowGUI();
	public static Player player = new Player();
	
	
	public static void buildGUIs() {
		resourceDestination_PopupWindowGUI.assignButtons();
		
		gameSpeed_PopupWindowGUI.assignButtons();
		setUp_PopupWindowGUI.assignButtons();
		disasters_PopupWindowGUI.assignButtons();
		statistics_PopupWindowGUI.assignButtons();
		saveLoad_PopupWindowGUI.assignButtons();
		advice_PopupWindowGUI.assignButtons();
		
		standardWindowGUI.assignButtons();
		topWindowGUI.assignButtons();
	}
	
	public static void render(Graphics g, int tickCount) {
		// we should render each gui and then the player
		standardWindowGUI.render(g, tickCount, 0);
		topWindowGUI.render(g, tickCount, 0);
		if(state == State.POPUP) {
			// render the menu
			if(popup == PopupState.RESOURCE_DESTINATION_MENU) {
				resourceDestination_PopupWindowGUI.render(g, tickCount, 0);
			}
			else if(popup == PopupState.SELECT_GAME_SPEED) {
				gameSpeed_PopupWindowGUI.render(g, tickCount, 0);
			}
			else if(popup == PopupState.SETUP) {
				setUp_PopupWindowGUI.render(g, tickCount, 0);
			}
			else if(popup == PopupState.DISASTERS) {
				disasters_PopupWindowGUI.render(g, tickCount, 0);
			}
			else if(popup == PopupState.STATISTICS) {
				statistics_PopupWindowGUI.render(g, tickCount, 0);
			}
			else if(popup == PopupState.SAVELOAD) {
				saveLoad_PopupWindowGUI.render(g, tickCount, 0);
			}
			else if(popup == PopupState.ADVICE) {
				advice_PopupWindowGUI.render(g, tickCount, 0);
			}
		}
		if(state == State.CONSTRUCTION || state == State.SELECT_DESTINATION) {
			player.render(g, tickCount, 0);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void update(float deltaTime, int tickCount) {
		// this function should be called every frame, and then calls the correct
		// gui based off current state to accept and execute player movements
		if(state == State.CONSTRUCTION) {
			// we are within the grid and simply moving around
			// we call the player class for movement and then
			// decide if the state needs to change
			state = player.update(deltaTime, tickCount); // player will change the state if out of bounds
		}
		else if(state == State.SELECT_DESTINATION) {
			// we are within the grid and choosing a destination
			state = player.destinationUpdate(deltaTime, tickCount);
		}
		else if(state == State.BUILDING_MENU) {
			// we are within the building menu
			state = standardWindowGUI.move();
		}
		else if(state == State.TOP_MENU) {
			// we are within the top menu
			state = topWindowGUI.move();
		}
		else if(state == State.POPUP) {
			// resource menu popup is in effect
			if(popup == PopupState.RESOURCE_DESTINATION_MENU) {
				resourceDestination_PopupWindowGUI.playerMakeInput();
			}
			else if(popup == PopupState.SELECT_GAME_SPEED) {
				gameSpeed_PopupWindowGUI.playerMakeInput();
			}
			else if(popup == PopupState.SETUP) {
				setUp_PopupWindowGUI.playerMakeInput();
			}
			else if(popup == PopupState.DISASTERS) {
				disasters_PopupWindowGUI.playerMakeInput();
			}
			else if(popup == PopupState.STATISTICS) {
				statistics_PopupWindowGUI.playerMakeInput();
			}
			else if(popup == PopupState.SAVELOAD) {
				saveLoad_PopupWindowGUI.playerMakeInput();
			}
			else if(popup == PopupState.ADVICE) {
				advice_PopupWindowGUI.playerMakeInput();
			}
			
			// state does not need to be changed as player make input will change it for us
		}
		//System.out.println(state);
	}
	
	
	
	
	// see if mining block can be assigned before function call
	public static void SwitchToResourceDestinationWindow(MiningBlock miningBlock) {
		block = miningBlock;
		state = State.POPUP;
		popup = PopupState.RESOURCE_DESTINATION_MENU;
		resourceDestination_PopupWindowGUI.openWindow();
		//return position;
	}
	
	public static void SwitchToSelectGameSpeedWindow() {
		state = State.POPUP;
		popup = PopupState.SELECT_GAME_SPEED;
		gameSpeed_PopupWindowGUI.openWindow();
	}
	
	public static void SwitchToSetUpWindow() {
		state = State.POPUP;
		popup = PopupState.SETUP;
		setUp_PopupWindowGUI.openWindow();
	}
	
	public static void SwitchToDisastersWindow() {
		state = State.POPUP;
		popup = PopupState.DISASTERS;
		disasters_PopupWindowGUI.openWindow();
	}
	
	public static void SwitchToStatisticsWindow() {
		state = State.POPUP;
		popup = PopupState.STATISTICS;
		statistics_PopupWindowGUI.openWindow();
	}
	
	public static void SwitchToSaveLoadWindow() {
		state = State.POPUP;
		popup = PopupState.SAVELOAD;
		saveLoad_PopupWindowGUI.openWindow();
	}
	
	public static void SwitchToAdviceWindow() {
		state = State.POPUP;
		popup = PopupState.ADVICE;
		advice_PopupWindowGUI.openWindow();
	}
	
	public static void standardWindowGUIButtonAction(Coordinates position) {
		// this function is called by the player when clicking on the grid
		// we must must execute the current button highlighted
		standardWindowGUI.takeButtonAction(position);
	}
	
	public static void assignResourceToDestination(Coordinates position) {
		// we must find the refinery/plant and assign it the resource block
		GameZone zone = Game.world.getZone(getGridCoordsY(position.posY), getGridCoordsX(position.posX));
		if(zone != null) {
			if(zone instanceof RefineryZone) {
				block.assignDestination((RefineryZone) zone);
			}
		}
	}
	
	public static int getGridCoordsY(int posY) {
		return (posY - Game.world.offsetY)/16;
	}
	
	public static int getGridCoordsX(int posX) {
		return (posX - Game.world.offsetX)/16;
	}
	
	public static State RecalculateState(Coordinates p) {
		// this function is called by any gui that determines the cursor
		// is out of bounds
		if(standardWindowGUI.withinBounds(p)) {
			standardWindowGUI.position = p;
			return State.BUILDING_MENU;
		}
		else if(topWindowGUI.withinBounds(p)) {
			topWindowGUI.position = p;
			return State.TOP_MENU;
		}
		else if(player.withinBounds(p)) {
			player.position = p;
			return State.CONSTRUCTION;
		}
		return State.CONSTRUCTION;
	}
	
	public static boolean withinGUITopMenuCoords(Coordinates position) {
		return false;
	}
	
	public enum State{
		// need separate states so the gui is interacted with properly
		// for popup windows use one state for popup and then a new state
		// class to distinguish them
		CONSTRUCTION,
		BUILDING_MENU,
		TOP_MENU,
		POPUP,
		SELECT_DESTINATION
	}
	
	public enum PopupState{
		RESOURCE_DESTINATION_MENU,
		SELECT_GAME_SPEED,
		SETUP,
		DISASTERS,
		STATISTICS,
		SAVELOAD,
		ADVICE
	}

}