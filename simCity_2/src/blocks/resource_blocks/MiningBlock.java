package blocks.resource_blocks;

import blocks.PowerBlock;
import world.World;
import zones.resource_zones.RefineryZone;

public class MiningBlock extends PowerBlock{
	// a mining block is its own entity it is not part of a zone, it only sends its resources to the bank
	// or to a refinery
	
	// a mining block is a block that extracts resources from the ground, coal, oil, rare metal, uranium
	// each resource can be either be refined or sold, if sold its of minimal value and no pollution
	// if refined there's pollution but more value and can either be sold or used, if used it provides
	// power, since green energy is expensive to start up, causes pollution to refine and provides
	// little energy, it is not viable to use to power a city
	
	private static final long serialVersionUID = 1L;
	
	public RefineryZone destination = null;
	
	public int miningCapacity = 1;
	
	public MiningBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		super.update(deltaTime, tickCount);
	}
	
	public void extractUnit() {
		// this function when called will produce a single unit of its assigned resource
	}
	
	public void assignDestination(RefineryZone storage) {
		// call this function to assign the destination the resources produced here will be sent to
		destination = storage;
	}

}
