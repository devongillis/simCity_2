package final_blocks;

import blocks.GameBlock;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;

public class DirtBlock extends GameBlock{
	
	private static final long serialVersionUID = 1L;

	public DirtBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		bullDozable = true;
		image = Image.DIRT;
		ID = null;
		type = Value.NEUTRAL;
	}

}
