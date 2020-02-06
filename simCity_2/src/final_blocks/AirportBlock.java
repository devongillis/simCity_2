package final_blocks;

import java.awt.Graphics;

import blocks.PowerBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Image;
import world.GameIDs.Value;
import world.GameIDs.Zone;

public class AirportBlock extends PowerBlock{
	private static final long serialVersionUID = 1L;
	
	// an airport will be needed for any city above a population threshold
	// it can be upgraded to allow better shipping costs which will help your city

	public AirportBlock(int tileNumberY, int tileNumberX, int coord, World world) {
		super(tileNumberY, tileNumberX, world);
		this.coord = coord;
		image = Image.AIRPORT;
		ID = Zone.AIRPORT;
		type = Value.MAJORNEGATIVE;
		centerBlock = 14;
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			super.render(g, tickCount, season);
		}
	}

}
