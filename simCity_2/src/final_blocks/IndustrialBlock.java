package final_blocks;

import java.awt.Graphics;

import blocks.PopulationBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;
import world.GameIDs.Zone;

public class IndustrialBlock extends PopulationBlock{
	
	private static final long serialVersionUID = 1L;

	public IndustrialBlock(int tileNumberY, int tileNumberX, int zone, int coord, World world) {
		super(tileNumberY, tileNumberX, zone, coord, world);
		image = Image.INDUSTRIAL;
		ID = Zone.INDUSTRIAL;
		type = Value.MAJORNEGATIVE;
		pollution = true;
	}
	
	@Override
	public void determineLandValue() {
		// do nothing as land value is not used for industrial calculations
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			if(!population) {
				g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
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
