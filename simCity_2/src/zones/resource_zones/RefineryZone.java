package zones.resource_zones;

import world.World;
import zones.GameZone;

public class RefineryZone extends GameZone{
	
	private static final long serialVersionUID = 1L;
	
	public int resourceUnits = 0;
	public int refinedUnits = 0;
	public int refinedUnitStorageCapacity = 10;
	public int resourceUnitStorageCapacity = 10;
	
	public int refiningCapacity = 1;

	public RefineryZone(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
	}
	
	public void refineResources() {
		// this when called will refine a certain number of resource units
		// and then add some pollution units to the city total
		if(resourceUnits >= refiningCapacity) {
			resourceUnits -= refiningCapacity;
			refinedUnits += refiningCapacity;
		}
		else {
			refinedUnits += resourceUnits;
			resourceUnits = 0;
		}
	}
	
	public void acceptResourceUnit(int amount) {
		resourceUnits += amount;
		if(resourceUnits > resourceUnitStorageCapacity) {
			// send off remaining to city storage
		}
	}

}
