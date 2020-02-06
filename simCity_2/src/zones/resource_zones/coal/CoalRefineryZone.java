package zones.resource_zones.coal;

import blocks.resource_blocks.RefineryBlock;
import world.World;
import zones.resource_zones.RefineryZone;

public class CoalRefineryZone extends RefineryZone{
	
	private static final long serialVersionUID = 1L;
	
	public RefineryBlock[] blocks = null;

	public CoalRefineryZone(int tileNumberY, int tileNumberX, RefineryBlock[] blocks, World world) {
		super(tileNumberY, tileNumberX, world);
		this.blocks = blocks;
		
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		if(resourceUnits != 0) {
			//System.out.println(resourceUnits);
		}
	}

}
