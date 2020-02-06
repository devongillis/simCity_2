package blocks;

import java.awt.Graphics;

import resources.Resources;
import world.World;
import world.GameIDs.Image;

public class PowerBlock extends GameBlock{
	
	private static final long serialVersionUID = 1L;
	
	public int powerValue = 0; // numeric value of power being supplied
	public int powerFrameCounter = 0; // the tick counter that increments when power is out
	public final int powerFrameCount = 60 * 5; // number of ticks till the zone responds to loss of power
	public boolean powerOut = false; // status of power supply
	public int centerBlock = 4;
	
	public PowerBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			if(powerValue == 0 && coord == centerBlock && powerFrameCounter == powerFrameCount && tickCount + 1 > 30) {
				g.drawImage(Resources.getImage(Image.POWEROFF, 0), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		
		if(powerOut && powerFrameCounter < powerFrameCount) {
			powerFrameCounter++;
		}
		
		
		int max = 0;
		
		if(tileNumberY > 0) {
			// tile above exists
			GameBlock block = world.gameBlocks[tileNumberY - 1][tileNumberX];
			if (block instanceof PowerBlock) {
				// power is involved check for power value
				PowerBlock p = (PowerBlock)block;
				if(p.powerValue > max){
					max = p.powerValue;
				}
			}
		}
		if(tileNumberX > 0) {
			// tile left exists
			GameBlock block = world.gameBlocks[tileNumberY][tileNumberX - 1];
			if (block instanceof PowerBlock) {
				PowerBlock p = (PowerBlock)block;
				if(p.powerValue > max){
					max = p.powerValue;
				}
			}
		}
		if(tileNumberY < world.gridHeight - 1) {
			// tile below exists
			GameBlock block = world.gameBlocks[tileNumberY + 1][tileNumberX];
			if (block instanceof PowerBlock) {
				PowerBlock p = (PowerBlock)block;
				if(p.powerValue > max){
					max = p.powerValue;
				}
			}
		}
		if(tileNumberX < world.gridWidth - 1) {
			// tile right exists
			GameBlock block = world.gameBlocks[tileNumberY][tileNumberX + 1];
			if (block instanceof PowerBlock) {
				PowerBlock p = (PowerBlock)block;
				if(p.powerValue > max){
					max = p.powerValue;
				}
			}
		}
		
		if(max <= 1 || max < powerValue) {// if max is less than our own than a source is not 
			//connected, if max <= 1 then source is too weak or not connected, no power we shut down
			powerValue = 0;
			powerOut = true;
			return;
		}
		
		// power source founded
		powerOut = false;
		powerFrameCounter = 0;
		powerValue = max - 1;
		return;
	}
	
}
