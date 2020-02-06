package final_blocks;

import java.awt.Graphics;

import blocks.GameBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;

public class WaterBlock extends GameBlock{
	
	private static final long serialVersionUID = 1L;

	// water blocks are non bulldozable blocks set to major positive
	public WaterBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.WATER;
		type = Value.MAJORPOSITIVE;
		bullDozable = false;
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, findImage()), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
		}
	}
	
	public int findImage() {
		int total = 0;
		
		if(tileExists(tileNumberY, tileNumberX - 1)) {
			if(world.gameBlocks[tileNumberY][tileNumberX - 1].getClass() != WaterBlock.class) {
				total+=1;
			}
		}
		if(tileExists(tileNumberY - 1, tileNumberX)) {
			if(world.gameBlocks[tileNumberY - 1][tileNumberX].getClass() != WaterBlock.class) {
				total+=2;
			}
		}
		if(tileExists(tileNumberY, tileNumberX + 1)) {
			if(world.gameBlocks[tileNumberY][tileNumberX + 1].getClass() != WaterBlock.class) {
				total+=4;
			}
		}
		if(tileExists(tileNumberY + 1, tileNumberX)) {
			if(world.gameBlocks[tileNumberY + 1][tileNumberX].getClass() != WaterBlock.class) {
				total+=8;
			}
		}
		return total;
	}

}
