package final_blocks;

import java.awt.Graphics;

import blocks.PowerBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;
import world.GameIDs.Zone;

public class PoliceBlock extends PowerBlock{
	
	private static final long serialVersionUID = 1L;

	public PoliceBlock(int tileNumberY, int tileNumberX, int coord, World world) {
		super(tileNumberY, tileNumberX, world);
		this.coord = coord;
		image = Image.POLICE;
		ID = Zone.POLICE;
		type = Value.NEUTRAL;
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		// some code to get info on zones current state:
		// power, population, grade
		// then use this to find image
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			super.render(g, tickCount, season);
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		super.update(deltaTime, tickCount);
	}
}
