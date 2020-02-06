package blocks.resource_blocks;

import java.awt.Graphics;

import blocks.PowerBlock;
import resources.Resources;
import world.World;

public class PowerPlantBlock extends PowerBlock{
	
	private static final long serialVersionUID = 1L;
	
	// a power source block has its own type of fuel for creating power

	public PowerPlantBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		// do nothing as we do not want to check for power, we are a source
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image,  coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
		}
	}

}
