package zones;

import final_blocks.PoliceBlock;
import world.World;

public class PoliceZone extends GameZone{
	
	private static final long serialVersionUID = 1L;
	
	public PoliceBlock[] blocks = null;
	public int policeStrength = 20;
	
	public PoliceZone(int tileNumberY, int tileNumberX, PoliceBlock[] blocks, World world) {
		super(tileNumberY, tileNumberX, world);
		this.blocks = blocks;
	}
}
