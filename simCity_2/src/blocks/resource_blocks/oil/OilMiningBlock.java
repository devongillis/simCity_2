package blocks.resource_blocks.oil;

import java.awt.Graphics;

import blocks.resource_blocks.MiningBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Image;
import world.GameIDs.Value;

public class OilMiningBlock extends MiningBlock{
	
	private static final long serialVersionUID = 1L;

	public OilMiningBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.OILMINER;
		ID = null;
		type = Value.MINORNEGATIVE;
		
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			super.render(g, tickCount, season);
		}
	}
	
	
	@Override
	public void extractUnit() {
		if(destination != null) {
			destination.acceptResourceUnit(miningCapacity);
		}
		else {
			// send off to the city storage center
			world.storageUnit.acceptOilUnits(miningCapacity);
		}
	}
}
