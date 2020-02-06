package world;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import blocks.GameBlock;
import blocks.resource_blocks.coal.CoalMiningBlock;
import blocks.resource_blocks.coal.CoalRefineryBlock;
import blocks.resource_blocks.uranium.UraniumPowerPlantBlock;
import final_blocks.AirportBlock;
import final_blocks.CommericalBlock;
import final_blocks.DirtBlock;
import final_blocks.ForestBlock;
import final_blocks.IndustrialBlock;
import final_blocks.PoliceBlock;
import final_blocks.PowerLineBlock;
import final_blocks.RailBlock;
import final_blocks.ResidentialBlock;
import final_blocks.RoadBlock;
import input.InterfaceGUI;
import world.GameIDs.Block;
import world.GameIDs.Zone;
import zones.AirportZone;
import zones.CommericalZone;
import zones.GameZone;
import zones.IndustrialZone;
import zones.PoliceZone;
import zones.ResidentialZone;
import zones.resource_zones.coal.CoalRefineryZone;
import zones.resource_zones.uranium.UraniumPowerPlantZone;

public class World implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long lastTime = System.nanoTime();
	
	public final int gridHeight = 200; // y dimension of grid
	public final int gridWidth = 200; // x dimension of grid
	public boolean worldLoaded = false;
	
	public GameBlock[][] gameBlocks = new GameBlock[gridHeight][gridWidth];
	//public Player player = null;
	
	public boolean recalculatePoliceCoverage = false;
	
	public HashMap<Zone, ArrayList<GameZone>> zones = new HashMap<Zone, ArrayList<GameZone>>(); // enum "Zone" used to identify zones
	
	public int[] cityCenter = new int[2]; // [0] = y, [1] = x
	
	public int offsetY = 64; // world offset for when player moves around world
	public int offsetX = 64;
	
	// in a separate section we need to keep track of all money related aspects, such as taxes,
	// resource yields, money spent, resources sold, funding of city aspects
	
	public ResourceStorageUnit storageUnit = new ResourceStorageUnit();
	
	
	
	
	
	
	
	
	
	
	
	public void initializeHashMap() {
		for (int i = 0; i < Zone.values().length; i++) {
			zones.put(Zone.values()[i], new ArrayList<GameZone>());
		}
		cityCenter[0] = 49;
		cityCenter[1] = 49;
	}
	
	public void update(int tickCount, boolean newDay) {
		float deltaTime = (System.nanoTime() - lastTime) / 1000000000.0f;
		lastTime = System.nanoTime();
		if(!worldLoaded) {
			return;
		}
		// only here can the world be given updates via player command
		// otherwise can run into concurrent issues, first we check for
		// inputs can then call functions based on the actions to take
		// then game updates will be called, we call update each tick
		// but change the order so power is not biased against building
		// placement
		
		if(newDay) {
			if((tickCount + 1) % 4 == 0) {
				for(int y = 0; y < gridHeight; y++) {
					for(int x = 0; x < gridWidth; x++) {
						gameBlocks[y][x].update(deltaTime, tickCount);
						if(recalculatePoliceCoverage) {
							gameBlocks[y][x].determinePoliceCoverage();
						}
					}
				}
			}
			else if((tickCount + 1) % 4 == 1) {
				for(int x = gridWidth - 1; x > 0; x--) {
					for(int y = 0; y < gridHeight; y++) {
						gameBlocks[y][x].update(deltaTime, tickCount);
						if(recalculatePoliceCoverage) {
							gameBlocks[y][x].determinePoliceCoverage();
						}
					}
				}
			}
			else if((tickCount + 1) % 4 == 2) {
				for(int y = gridHeight - 1; y > 0; y--) {
					for(int x = gridWidth - 1; x > 0; x--) {
						gameBlocks[y][x].update(deltaTime, tickCount);
						if(recalculatePoliceCoverage) {
							gameBlocks[y][x].determinePoliceCoverage();
						}
					}
				}
			}
			else {
				for(int x = 0; x < gridWidth; x++) {
					for(int y = gridHeight - 1; y > 0; y--) {
						gameBlocks[y][x].update(deltaTime, tickCount);
						if(recalculatePoliceCoverage) {
							gameBlocks[y][x].determinePoliceCoverage();
						}
					}
				}
			}
			
			recalculatePoliceCoverage = false;
			
			for(int y = 0; y < gridHeight; y++) {
				for(int x = 0; x < gridWidth; x++) {
					gameBlocks[y][x].determineLandValue();
				}
			}
			
			
			for (ArrayList<GameZone> specificZoneType : zones.values()) {
				for(GameZone specificZone : specificZoneType) {
					specificZone.update(deltaTime, tickCount);
				}
			}
		}
		
		InterfaceGUI.update(deltaTime, tickCount);
	}
	
	public GameZone getZone(int tileNumberY, int tileNumberX) {
		for (ArrayList<GameZone> specificZoneType : zones.values()) {
			for(GameZone specificZone : specificZoneType) {
				if(specificZone.tileNumberY == tileNumberY && specificZone.tileNumberX == tileNumberX) {
					return specificZone;
				}
			}
		}
		return null;
	}
	
	public GameZone getZone(int tileNumberY, int tileNumberX, Zone zone) {
		for(GameZone specificZone : zones.get(zone)) {
			if(specificZone.tileNumberY == tileNumberY && specificZone.tileNumberX == tileNumberX) {
				return specificZone;
			}
		}
		return null;
	}
	
	public void render(Graphics g, int tickCount) {
		for(int i = 0; i < gridHeight; i++) {
			for(int j = 0; j < gridWidth; j++) {
				gameBlocks[i][j].render(g, tickCount, 0);
				//gameBlocks[i][j].renderCoverage(g, tickCount, 0);
			}
		}
		InterfaceGUI.render(g, tickCount);
		//player.render(g, tickCount, 0);
	}
	
	public boolean ZoneAddable(int gridY, int gridX, int dimensions) {
		// this function can also be called by bulldoze to see if bulldozable
		// this function will check if zone is allowed to be added
		// later will use an id to see specific zone conditions
		// we must first check if with boarders (zone will fit)
		// then check it and the 8 surrounding blocks if they are
		// bulldozable i.e. belong to another zone, water, rail/road,
		// etc. only dirt and forest can be constructed on top of
		//int px = getGridCoords(posX);
		//int py = getGridCoords(posY);
		int edgeLT = (dimensions - 1)/2; // 3 = 1, 4 = 1, 5 = 2, 6 = 2, 7 = 3
		int edgeRB = Math.round((dimensions - 1)/2f); // 3 = 1, 4 = 2, 5 = 2, 6 = 3, 7 = 3
		
		
		if(gridX - edgeLT < 0 || gridX + edgeRB > gridWidth - 1 || gridY - edgeLT < 0 || gridY + edgeRB > gridHeight - 1) { // check if within boarders for 3x3 build
			return false;
		}
		
		
		for(int y = -edgeLT; y < edgeRB + 1; y++) {
			for(int x = -edgeLT; x < edgeRB + 1; x++) {
				if(!gameBlocks[gridY + y][gridX + x].bullDozable) {
					return false;
				}
			}
		}
		
		// thus far the zone checks out, further tests may be needed
		return true;
	}
	
	public void addZone(Zone zoneType, int tileNumberY, int tileNumberX) {
		// first we must convert these blocks to residence blocks and
		// store them in an array and pass it off to a residence zone
		// since after game initialization only the player can add zones
		// this function will only ever be called after everything in
		// the grid is updated i.e. no concurrency issues
		
		
		
		GameBlock[] blocks = null;
		
		if(zoneType == Zone.RESIDENTIAL) {
			blocks = new ResidentialBlock[9];
			for(int y = -1, coord = 0; y < 2; y++) {
				for(int x = -1; x < 2; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new ResidentialBlock(tileNumberY + y, tileNumberX + x, 0, coord, this);
					blocks[coord] = gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.RESIDENTIAL).add(new ResidentialZone(tileNumberY, tileNumberX, (ResidentialBlock[])blocks, this));
		}
		else if(zoneType == Zone.COMMERCIAL) {
			blocks = new CommericalBlock[9];
			for(int y = -1, coord = 0; y < 2; y++) {
				for(int x = -1; x < 2; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new CommericalBlock(tileNumberY + y, tileNumberX + x, 0, coord, this);
					blocks[coord] = gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.COMMERCIAL).add(new CommericalZone(tileNumberY, tileNumberX, (CommericalBlock[])blocks, this));
		}
		else if(zoneType == Zone.INDUSTRIAL) {
			blocks = new IndustrialBlock[9];
			for(int y = -1, coord = 0; y < 2; y++) {
				for(int x = -1; x < 2; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new IndustrialBlock(tileNumberY + y, tileNumberX + x, 0, coord, this);
					blocks[coord] = gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.INDUSTRIAL).add(new IndustrialZone(tileNumberY, tileNumberX, (IndustrialBlock[])blocks, this));
		}
		else if(zoneType == Zone.POLICE) {
			blocks = new PoliceBlock[9];
			for(int y = -1, coord = 0; y < 2; y++) {
				for(int x = -1; x < 2; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new PoliceBlock(tileNumberY + y, tileNumberX + x, coord, this);
					blocks[coord] = (PoliceBlock)gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.POLICE).add(new PoliceZone(tileNumberY, tileNumberX, (PoliceBlock[])blocks, this));
			recalculatePoliceCoverage = true;
		}
		else if(zoneType == Zone.COALREFINERY) {
			blocks = new CoalRefineryBlock[9];
			for(int y = -1, coord = 0; y < 2; y++) {
				for(int x = -1; x < 2; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new CoalRefineryBlock(tileNumberY + y, tileNumberX + x, coord, this);
					blocks[coord] = (CoalRefineryBlock)gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.COALREFINERY).add(new CoalRefineryZone(tileNumberY, tileNumberX, (CoalRefineryBlock[])blocks, this));
		}
		else if(zoneType == Zone.NUCLEAR) {
			blocks = new UraniumPowerPlantBlock[16];
			for(int y = -1, coord = 0; y < 3; y++) {
				for(int x = -1; x < 3; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new UraniumPowerPlantBlock(tileNumberY + y, tileNumberX + x, coord, this);
					blocks[coord] = (UraniumPowerPlantBlock)gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.NUCLEAR).add(new UraniumPowerPlantZone(tileNumberY, tileNumberX, (UraniumPowerPlantBlock[])blocks, this));
		}
		else if(zoneType == Zone.AIRPORT) {
			blocks = new AirportBlock[36];
			for(int y = -2, coord = 0; y < 4; y++) {
				for(int x = -2; x < 4; x++) {
					gameBlocks[tileNumberY + y][tileNumberX + x] = new AirportBlock(tileNumberY + y, tileNumberX + x, coord, this);
					blocks[coord] = (AirportBlock)gameBlocks[tileNumberY + y][tileNumberX + x];
					coord++;
				}
			}
			zones.get(Zone.AIRPORT).add(new AirportZone(tileNumberY, tileNumberX, (AirportBlock[])blocks, this));
		}
		else {
			System.out.println("error: addZone(), World.java " + zoneType);
		}
		
	}
	

	public void addBlock(Block blockType, int tileNumberY, int tileNumberX) {
		
		if(blockType == Block.DIRT) {
			gameBlocks[tileNumberY][tileNumberX] = new DirtBlock(tileNumberY, tileNumberX, this);
		}
		else if(blockType == Block.FOREST) {
			gameBlocks[tileNumberY][tileNumberX] = new ForestBlock(tileNumberY, tileNumberX, this);
		}
		else if(blockType == Block.POWERLINE) {
			gameBlocks[tileNumberY][tileNumberX] = new PowerLineBlock(tileNumberY, tileNumberX, this);
		}
		else if(blockType == Block.RAIL) {
			gameBlocks[tileNumberY][tileNumberX] = new RailBlock(tileNumberY, tileNumberX, this);
		}
		else if(blockType == Block.ROAD) {
			gameBlocks[tileNumberY][tileNumberX] = new RoadBlock(tileNumberY, tileNumberX, this);
		}
		else if(blockType == Block.COALMINER) {
			gameBlocks[tileNumberY][tileNumberX] = new CoalMiningBlock(tileNumberY, tileNumberX, this);
		}
		else {
			System.out.println("error: addBlock(), World.java " + blockType);
		}
	}
	
	public void deletionAt(int tileNumberY, int tileNumberX) {
		// this function will first test whether the location is a zone
		// or a single block, then either calls for block deletion
		// or further checks if zone is delete-able(location is centered)
		// if zone is centered then delete zone is called
		Zone id = gameBlocks[tileNumberY][tileNumberX].ID;
		if(id != null) {
			// a zone is detected
			for (GameZone specificZone: zones.get(id)) {
				if(specificZone.tileNumberY == tileNumberY && specificZone.tileNumberX == tileNumberX) {
					// zone is delete-able we can now delete the zone
					if(specificZone instanceof PoliceZone) {
						recalculatePoliceCoverage = true;
					}
					zones.get(id).remove(specificZone);
					
					if(specificZone instanceof UraniumPowerPlantZone) {
						for(int y = -1; y < 3; y++) {
							for(int x = -1; x < 3; x++) {
								gameBlocks[tileNumberY + y][tileNumberX + x] = new DirtBlock(tileNumberY + y, tileNumberX + x, this);
							}
						}
					}
					else if(specificZone instanceof AirportZone) {
						for(int y = -2; y < 4; y++) {
							for(int x = -2; x < 4; x++) {
								gameBlocks[tileNumberY + y][tileNumberX + x] = new DirtBlock(tileNumberY + y, tileNumberX + x, this);
							}
						}
					}
					else {
						for(int y = -1; y < 2; y++) {
							for(int x = -1; x < 2; x++) {
								gameBlocks[tileNumberY + y][tileNumberX + x] = new DirtBlock(tileNumberY + y, tileNumberX + x, this);
							}
						}
					}
					break;
				}
				else {
					// do nothing as we are on a zone but not on its center
					// later play a sound effect
				}
			}
		}
		else {
			// single block detected
			gameBlocks[tileNumberY][tileNumberX] = new DirtBlock(tileNumberY, tileNumberX, this);
		}
	}
	
	public void loadWorld() {
		
	}
}
