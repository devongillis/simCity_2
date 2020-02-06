package final_blocks;

import blocks.TransportationBlock;
import world.World;
import world.GameIDs.Image;

public class RailBlock extends TransportationBlock {
	
	private static final long serialVersionUID = 1L;

	public RailBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.RAIL;
	}
	
	@Override
	public void incrementTraffic() {
		// do nothing at traffic is to remain 0
	}

}
