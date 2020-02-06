package blocks;

import java.awt.Graphics;
import java.io.Serializable;

import resources.Resources;
import world.World;
import world.GameIDs.Image;
import world.GameIDs.Value;
import world.GameIDs.Zone;
import zones.PoliceZone;

// game block is a single square that represents either part of or the whole
// entity, it has an update function which is overridden
// per subset class block

public class GameBlock implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public World world = null;
	
	public Zone ID = null; // the zone type which this block belongs to, null if N/A
	public Value type = null; // the block type this block is set to, major negative to major positive
	public boolean pollution = false; // states whether block is of pollution type status, police coverage affected by this
	
	protected final int blockSize = 16; // use these for in world coordinates
	protected final int blockOffset = 8; // offset to center the images
	protected int realX = 0; // the real in game coordinates for x
	protected int realY = 0; // the real in game coordinates for y
	
	protected int tileNumberY = -1; // tile coordinates for y
	protected int tileNumberX = -1; // tile coordinates for x
	
	public boolean bullDozable = false; // if true we can immediately bulldoze this blocks without checking for zone
	
	public int policeCoverage = 0; // range 0 - 10 for coverage, determined by individual block
	public int crimeRate = 0; // range 0 - 10 for crime, determined by zone not by individual block
	
	public int landValue = 0; // land value is calculated based on block type of adjacent blocks
	// range is 0 to 36, land value should not reflect power as parks should contain value without
	// power and power only determines if a zone is allowed to grow at all, shrink if power outage
	
	public Image image = null; // the image used for rendering
	public int coord = 0; // refers to 0-8/0-15/0-24/0-35 of the entire zone and the image index
	
	public GameBlock(int tileNumberY, int tileNumberX, World world) {
		this.world = world;
		this.realX = tileNumberX * blockSize;
		this.realY = tileNumberY * blockSize;
		this.tileNumberX = tileNumberX;
		this.tileNumberY = tileNumberY;
	}
	
	public int getTileNX() {
		return tileNumberX;
	}
	
	public int getTileNY() {
		return tileNumberY;
	}
	
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
		}
	}
	
	public void update(float deltaTime, int tickCount) {
		// this will be overridden by all subclasses
	}
	
	public boolean renderRange() {
		if(realX + world.offsetX >= 0 - 16 && realX + world.offsetX <= 480 + 16 && realY + world.offsetY >= 0 - 16 && realY + world.offsetY <= 480 + 16) {
			return true;
		}
		return false;
	}
	
	public void determineLandValue() {
		// a single block can have a max value of 36 land value thus a residential 3x3 can return
		// a maximum of 4*(5*4 + 4*2) + 4*(3*4 + 6*2) + 9*2 = 4*(20+8) + 4*(12+12) + 18 = 4*28+4*24+18
		// = 112+96+18 = 226, but will likely return 4*(8) + 4*(12) + 18 = 32+48+18 = 98, as most zones
		// will not have valuable land near by
		
		int points = 0;
		for(int y = -1; y < 2; y++) {
			for(int x = -1; x < 2; x++) {
				// we want the entire 3x3 grid that forms around the block including our own
				if(tileExists(y, x)) {
					// tile exists now get its type
					if(world.gameBlocks[tileNumberY+y][tileNumberX+x].type == Value.MAJORPOSITIVE) {
						points+=4;
					}
					else if(world.gameBlocks[tileNumberY+y][tileNumberX+x].type == Value.MINORPOSITIVE) {
						points+=3;
					}
					else if(world.gameBlocks[tileNumberY+y][tileNumberX+x].type == Value.NEUTRAL) {
						points+=2;
					}
					else if(world.gameBlocks[tileNumberY+y][tileNumberX+x].type == Value.MINORNEGATIVE) {
						points+=1;
					}
					else {
						// major negative is zero points
					}
				}
			}
		}
		landValue = points;
	}
	
	public boolean tileExists(int y, int x) {
		if(tileNumberY + y >= 0 && tileNumberY + y < world.gridHeight && tileNumberX + x >= 0 && tileNumberX + x < world.gridWidth) {
			return true;
		}
		return false;
	}
	
	// this is a very expensive function when police numbers are large
	public void determinePoliceCoverage() {
		// should also reflect zone id of the other blocks, ex: industrial brings crime
		// coverage set to 0.93^(blocks that bring crime), since a max of 9 blocks can be
		// industrial, 0.93^9 = max = 0.52, so coverage can drop to half if bad blocks are
		// around
		int coverage = 0;
		for(int i = 0; i < world.zones.get(Zone.POLICE).size(); i++) {
			coverage += getPoliceCoverage(i);
		}
		
		if(coverage > 0) {
			int count = 0;
			for(int y = -1; y < 2; y++) {
				for(int x = -1; x < 2; x++) {
					if(tileExists(y, x)) {
						if(world.gameBlocks[tileNumberY + y][tileNumberX + x].pollution) {
							count++;
						}
					}
				}
			}
			coverage *= coverageFactor(count);
		}
		
		if(coverage < 10) {
			policeCoverage = coverage;
		}
		else {
			policeCoverage = 10;
		}
	}
	
	public int getPoliceCoverage(int i) {
		PoliceZone p = (PoliceZone) world.zones.get(Zone.POLICE).get(i);
		int totalDistance = Math.abs(tileNumberY - p.tileNumberY) + Math.abs(tileNumberX - p.tileNumberX);
		
		int coverage = p.policeStrength - totalDistance;
		if(coverage > 0) {
			return coverage;
		}
		return 0;
	}
	
	public float coverageFactor(int count) {
		if(count == 0) {
			return 1.0f;
		}
		else if(count == 1) {
			return 0.94f;
		}
		else if(count == 2) {
			return 0.89f;
		}
		else if(count == 3) {
			return 0.83f;	
		}
		else if(count == 4) {
			return 0.78f;
		}
		else if(count == 5) {
			return 0.72f;
		}
		else if(count == 6) {
			return 0.67f;
		}
		else if(count == 7) {
			return 0.61f;
		}
		else if(count == 8) {
			return 0.56f;
		}
		else {
			return 0.5f;
		}
	}
}
