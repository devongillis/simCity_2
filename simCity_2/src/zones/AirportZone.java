package zones;

import final_blocks.AirportBlock;
import world.World;

public class AirportZone extends GameZone{
	
	private static final long serialVersionUID = 1L;
	
	public AirportBlock[] blocks = null;

	public AirportZone(int tileNumberY, int tileNumberX, AirportBlock[] blocks, World world) {
		super(tileNumberY, tileNumberX, world);
		this.blocks = blocks;
	}

}
