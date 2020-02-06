package final_blocks;

import java.awt.Graphics;

import blocks.PopulationBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;
import world.GameIDs.Zone;

public class ResidentialBlock extends PopulationBlock{
	
	private static final long serialVersionUID = 1L;
	
	public int random = 0; // rendering value for individual houses as some houses look different
	// this is assigned by the residence zone
	
	public ResidentialBlock(int tileNumberY, int tileNumberX, int zone, int coord, World world) {
		super(tileNumberY, tileNumberX, zone, coord, world);
		ID = Zone.RESIDENTIAL;
		type = Value.NEUTRAL;
		image = Image.RESIDENTIAL;
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			if(!population) {
				g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
			else if(zoneLevel == 1){ // house
				g.drawImage(Resources.getImage(image, 9 + zonegrade * 3 + random), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
			else {
				g.drawImage(Resources.getImage(image, coord + 9 * zoneLevel), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
			
			// consider having a separate power off image for each power zone,
			// so to keep the number of draw image calls at one
			super.render(g, tickCount, season);
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		super.update(deltaTime, tickCount);
	}
}
