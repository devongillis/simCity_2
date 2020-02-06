package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import final_blocks.DirtBlock;
import final_blocks.ForestBlock;
import final_blocks.PowerLineBlock;
import final_blocks.WaterBlock;
import graphics.Renderer;
import input.InterfaceGUI;
import player.Player;
import resources.Resources;
import world.World;
import world.GameIDs.Block;
import world.GameIDs.Zone;

public class Game {
	
	public static World world = null;

	public static void main(String[] args) {
		boolean test = false;
		
		if(test) {
			try {
				loadWorld();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Renderer.init();
			Renderer.world = world;
			Resources.loadImages();
		}
		else {
			world = new World();
			Renderer.init();
			Renderer.world = world;
			Resources.loadImages();
			
			
			for(int i = 0; i < world.gridHeight; i++) {// y
				for(int j = 0; j < world.gridWidth; j++) {// x
					world.gameBlocks[i][j] = new PowerLineBlock(i, j, world);
				}
			}
			//world.player = new Player(world);
			/*
			for(int i = 0; i < world.gridHeight; i++) {// y
				for(int j = 0; j < world.gridWidth; j++) {// x
					world.gameBlocks[i][j] = new ForestBlock(i, j, world);
				}
			}
			
			for(int x1 = 1, x2 = 100, y = 98; x1 < x2; x1+=2, x2-=2, y-=2) {
				for(int x = x1; x < x2; x++) {
					world.gameBlocks[y][x] = new ForestBlock(y, x, world);
				}
			}
			for(int x1 = 1, x2 = 99, y = 1; x1 < x2; x1+=2, x2-=2, y+=2) {
				for(int x = x1; x < x2; x++) {
					world.gameBlocks[y][x] = new ForestBlock(y, x, world);
				}
			}
			for(int y1 = 1, y2 = 98, x = 1; y1 < y2; y1+=2, y2-=2, x+=2) {
				for(int y = y1; y < y2; y++) {
					world.gameBlocks[y][x] = new ForestBlock(y, x, world);
				}
			}
			for(int y1 = 1, y2 = 97, x = 98; y1 < y2; y1+=2, y2-=2, x-=2) {
				for(int y = y1; y < y2; y++) {
					world.gameBlocks[y][x] = new ForestBlock(y, x, world);
				}
			}
			
			world.gameBlocks[98][99] = new ForestBlock(98,  99, world);
			world.gameBlocks[97][99] = new ForestBlock(97,  99, world);
			world.gameBlocks[96][99] = new ForestBlock(96,  99, world);
			*/
			world.initializeHashMap();
			
			for(int y = 1; y < world.gridHeight; y+=3) {
				for(int x = 1; x < world.gridWidth; x+=3) {
					if(world.ZoneAddable(y, x, 3)) {
						world.addZone(Zone.RESIDENTIAL, y, x);
					}
				}
			}
			
			for(int x = 1; x < 100; x+=3) {
				world.deletionAt(97, x);
				//world.addZone(Zone.INDUSTRIAL, 97, x);
			}
			
			// the first step to deleting anything from the grid is to first
			// inspect the zone type of the block, if null then it is not a zone
			// type but rather a single block (dirt, forest, power line, etc.)
			// in which case we can call a simple delete function
			// if it is a zone type then we check with the list to see if it is
			// the center of the zone (delete-able blocks), if not then do nothing
			// if so then we call zone delete function
			/*
			for(int i = 1 + 12; i < 100; i+=12) {
				for(int j = 1 + 12; j < 100; j+=12) {
					World.deletionAt(i, j);
					World.addZone(Zone.POLICE, i, j);
				}
			}
			*/
			/*
			for(int i = 1 + 12; i < 100; i+=12) {
				for(int j = 1 + 12 + 12; j < 100; j+=24) {
					World.deletionAt(i, j);
					World.addZone(Zone.POLICE, i, j);
				}
			}
			
			for(int i = 1 + 6; i < 100; i+=12) {
				for(int j = 1 + 12; j < 100; j+=24) {
					World.deletionAt(i, j);
					World.addZone(Zone.POLICE, i, j);
				}
			}
			*/
			//World.deletionAt(13, 13);
			
			world.deletionAt(1, 1);
			if(world.ZoneAddable(1, 1, 1)) {
				world.addBlock(Block.COALMINER, 1, 1);
			}
			
			world.deletionAt(1, 4);
			world.addZone(Zone.COALREFINERY, 1, 4);
			
			world.deletionAt(49, 49);
			world.addZone(Zone.POLICE, 49, 49);
			
			world.deletionAt(52, 49);
			world.addZone(Zone.COMMERCIAL, 52, 49);
			
			world.deletionAt(55, 49);
			world.addZone(Zone.COMMERCIAL, 55, 49);
			/*
			world.deletionAt(40, 40);
			//world.addZone(Zone.POLICE, 40, 40);
			*/
			world.deletionAt(97, 97);
			world.deletionAt(97, 100);
			world.deletionAt(100, 97);
			world.deletionAt(100, 100);
			if(world.ZoneAddable(97,  97, 4)) {
				world.addZone(Zone.NUCLEAR, 97, 97);
			}
			/*
			world.deletionAt(37, 37);
			
			world.gameBlocks[36][36] = new WaterBlock(36, 36, world);
			//world.gameBlocks[36][37] = new WaterBlock(36, 37, world);
			world.gameBlocks[36][38] = new WaterBlock(36, 38, world);
			
			world.gameBlocks[37][36] = new WaterBlock(37, 36, world);
			world.gameBlocks[37][37] = new WaterBlock(37, 37, world);
			//world.gameBlocks[37][38] = new WaterBlock(37, 38, world);
			
			world.gameBlocks[38][36] = new WaterBlock(38, 36, world);
			world.gameBlocks[38][37] = new WaterBlock(38, 37, world);
			world.gameBlocks[38][38] = new WaterBlock(38, 38, world);
			
			
			
			
			
			
			
			//world.addZone(Zone.INDUSTRIAL, 37, 37);
			
			world.deletionAt(1, 1);
			world.addZone(Zone.INDUSTRIAL, 1,  1);
			
			world.deletionAt(25, 25);
			world.addZone(Zone.COMMERCIAL, 25, 25);
			
			
			world.deletionAt(13, 10);
			//world.addZone(Zone.INDUSTRIAL, 13,  10);
			world.deletionAt(13, 13);
			//world.addZone(Zone.INDUSTRIAL, 13,  13);
			world.deletionAt(13, 16);
			//world.addZone(Zone.INDUSTRIAL, 13,  16);
			world.deletionAt(16, 10);
			//world.addZone(Zone.INDUSTRIAL, 16,  10);
			world.deletionAt(16, 13);
			world.addZone(Zone.RESIDENTIAL, 16,  13);
			world.deletionAt(16, 16);
			//world.addZone(Zone.INDUSTRIAL, 16,  16);
			world.deletionAt(19, 10);
			//world.addZone(Zone.INDUSTRIAL, 19,  10);
			world.deletionAt(19, 13);
			world.addZone(Zone.POLICE, 19,  13);
			world.deletionAt(19, 16);
			//world.addZone(Zone.INDUSTRIAL, 19,  16);
			
			*/
			
			
			
			// only after everything is loaded can the game begin doing world updates
			InterfaceGUI.buildGUIs();
			world.worldLoaded = true;
		}
		
	}
	
	public static void saveWorld() throws IOException {
		File f = new File("World.txt");
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(world);
		oos.close();
	}
	
	public static void loadWorld() throws IOException, ClassNotFoundException {
		File f = new File("World.txt");
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		world = (World)ois.readObject();
		ois.close();
	}
	
	public static void quit() {
		System.exit(0);
	}
	
}
