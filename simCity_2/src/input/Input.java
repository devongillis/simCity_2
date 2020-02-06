package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
	
	private static boolean[] currentKeys =  new boolean[128]; // these keys reflect whether they are pressed or not
	private static boolean[] currentKeysTyped = new boolean[128];
	
	
	
	
	public static void flushInputs() {
		for(int i = 0; i < 128; i++) {
			currentKeys[i] = false;
			currentKeysTyped[i] = false;
		}
	}
	
	public static boolean getKey(int keyCode) {
		return currentKeys[keyCode];
	}
	
	public static boolean getKeyTyped(int keyCode) {
		if(currentKeysTyped[keyCode] == true) {
			currentKeysTyped[keyCode] = false;
			return true;
		}
		return false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		currentKeys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(currentKeys[e.getKeyCode()] == true) {
			// the key has been previously typed
			currentKeysTyped[e.getKeyCode()] = true;
		}
		currentKeys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	// need a better system for determining when a key is typed
	
}
