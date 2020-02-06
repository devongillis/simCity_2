package blocks.resource_blocks.coal;

import java.awt.Graphics;

import blocks.resource_blocks.RefineryBlock;
import resources.Resources;
import world.GameIDs.Image;
import world.World;

public class CoalRefineryBlock extends RefineryBlock{
	
	private static final long serialVersionUID = 1L;

	public CoalRefineryBlock(int tileNumberY, int tileNumberX, int coord, World world) {
		super(tileNumberY, tileNumberX, world);
		this.coord = coord;
		image = Image.COALREFINER;
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			super.render(g, tickCount, season);
		}
	}

}
