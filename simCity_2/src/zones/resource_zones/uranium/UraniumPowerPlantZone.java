package zones.resource_zones.uranium;

import blocks.resource_blocks.uranium.UraniumPowerPlantBlock;
import world.World;
import zones.resource_zones.PowerPlantZone;

public class UraniumPowerPlantZone extends PowerPlantZone{
	
	private static final long serialVersionUID = 1L;
	
	public UraniumPowerPlantBlock[] blocks = null;

	public UraniumPowerPlantZone(int tileNumberY, int tileNumberX, UraniumPowerPlantBlock[] blocks, World world) {
		super(tileNumberY, tileNumberX, world);
		this.blocks = blocks;
	}

}
