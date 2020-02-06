package final_blocks;

import java.awt.Graphics;

import blocks.GameBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;

public class ForestBlock extends GameBlock{
	
	private static final long serialVersionUID = 1L;
	
	public int imageUpdate = 15;// 60/4 = 15, want to update 4 times per 60 frames
	
	public ForestBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.FOREST;
		bullDozable = true;
		ID = null;
		type = Value.MINORPOSITIVE;
	}
	
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			int a = (int) Math.floor((float)tickCount/imageUpdate);
			g.drawImage(Resources.getImage(image, season * 4 + a), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		
	}
}
