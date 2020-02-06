package blocks;

import world.World;
import world.GameIDs.Value;

public class PopulationBlock extends PowerBlock{
	
	private static final long serialVersionUID = 1L;
	public int zonegrade = 0; // the quality of the zone, 0-2, where 0 = low grade
	public boolean population = false; // population here is either true or false (0 or 20)
	public int zoneLevel = 0; // the level of the zone 0-T (T = 6)

	public PopulationBlock(int tileNumberY, int tileNumberX, int zone, int coord, World world) {
		super(tileNumberY, tileNumberX, world);
		this.coord = coord;
		zonegrade = zone;
		type = Value.MAJORNEGATIVE;
	}

}
