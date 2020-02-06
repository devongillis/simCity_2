package zones;

import java.io.Serializable;

import world.World;

public class GameZone implements Serializable{
	// this is the base class of all zones
	// residence and commercial 0-6
	// industrial 0-4, residential 0 - then houses 1-8 represent
	// 1 and then jump to 2
	
	private static final long serialVersionUID = 1L;
	
	public int tileNumberX = -1;
	public int tileNumberY = -1;
	
	public int crimeRate = 0; // this is updated each frame and updates its own zone blocks
	public final int populationIncrement = 160;
	
	public World world = null;
	
	public GameZone(int tileNumberY, int tileNumberX, World world) {
		this.tileNumberY = tileNumberY;
		this.tileNumberX = tileNumberX;
		this.world = world;
	}
	
	public void update(float deltaTime, int tickCount) {
		
	}
}
