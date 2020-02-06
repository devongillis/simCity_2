package blocks.resource_blocks.uranium;

import blocks.resource_blocks.PowerPlantBlock;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;
import world.GameIDs.Zone;

public class UraniumPowerPlantBlock extends PowerPlantBlock{
	
	private static final long serialVersionUID = 1L;

	public UraniumPowerPlantBlock(int tileNumberY, int tileNumberX, int coord, World world) {
		super(tileNumberY, tileNumberX, world);
		this.coord = coord;
		image = Image.NUCLEAR;
		powerValue = 300;
		ID = Zone.NUCLEAR;
		type = Value.NEUTRAL;
	}

}
