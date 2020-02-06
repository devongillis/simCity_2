package final_blocks;

import blocks.TransportationBlock;
import world.GameIDs.Image;
import world.World;

public class RoadBlock extends TransportationBlock{

	private static final long serialVersionUID = 1L;

	public RoadBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.ROAD;
	}

}
