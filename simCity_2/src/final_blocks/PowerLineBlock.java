package final_blocks;

import java.awt.Graphics;

import blocks.PowerBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;

public class PowerLineBlock extends PowerBlock{
	
	private static final long serialVersionUID = 1L;
	
	public PowerLineBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.POWERLINE;
		bullDozable = true;
		ID = null; // not a zone type block
		type = Value.NEUTRAL;
	}
	
	@Override
	public void render(Graphics g, int frameCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			if(powerValue == 0 && frameCount + 1 > 30 && powerFrameCounter == powerFrameCount) {
				g.drawImage(Resources.getImage(Image.POWEROFF,  0), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		super.update(deltaTime, tickCount);
	}
	
}
