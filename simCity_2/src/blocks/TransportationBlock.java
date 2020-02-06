package blocks;

import world.World;

public class TransportationBlock extends GameBlock{
	
	private static final long serialVersionUID = 1L;
	
	// each residence zone picks a nearby commercial zone to go to work
	// it then adds to each road block along the way a single increment
	// these increments total up to represent a traffic value which impact
	// residence zones land value, trains do not have traffic
	// need to create a traffic path finding algorithm
	
	public int traffic = 0;

	public TransportationBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
	}
	
	public void incrementTraffic() {
		traffic++;
		traffic = Math.min(traffic,  10); // maximum value of 10
	}

}
