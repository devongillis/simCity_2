package zones;

import final_blocks.IndustrialBlock;
import world.World;

public class IndustrialZone extends GameZone{
	// a residence zone holds reference to 3x3 or 9 residence blocks
	// each residence block does its own update independently for things
	// like power, but the entire zone is updated here, including
	// population, zone value, toping, etc.
	
	// posX and posY refer to the center residence block of the entire
	// zone (rendering), as well as tileNumbers (coords)
	// residence zone does not have a render function as it only contains
	// references to other residence blocks and their status
	
	private static final long serialVersionUID = 1L;
	
	public IndustrialBlock[] blocks = null; // blocks that make up the zone
	public int zoneLevel = 0; // 0 - 5, apartment level
	public int population = 0; // zone population
	
	
	public IndustrialZone(int tileNumberY, int tileNumberX, IndustrialBlock[] blocks, World world) {
		super(tileNumberY, tileNumberX, world);
		this.blocks = blocks;
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		// a lot of code will be added here from population updates
		// involving taxes, crime, land value, economy, supply, demand,
		// randomness, job opportunity
		
		// industrial does not care at all about land value or location, and is
		// less sensitive to crime but also drags more crime in
		
		// later lets use a random number to decide if zone should call an update or not
		// use a default minimum delay and then an added random delay before next update
		// industrial only goes up to 800 so set maximum to 840
		// 640 + crime rate 0-200
		int total = 640 + CrimeRatePoints();
		setPopulation(total);
	}
	
	public boolean hasTransportation() {
		// if this returns false then maximum zone level of 2 can be reached
		// later should return true if touching road/rail, false if not
		return true;
	}
	
	public int CrimeRatePoints() {
		// return 0 - 200 points
		
		// later crime rate should reflect population, police coverage, and pollution
		float policeCoverage = 0;
		for(int i = 0; i < blocks.length; i++) {
			policeCoverage += blocks[i].policeCoverage;
		}
		policeCoverage = policeCoverage/blocks.length; // now represents the average police coverage
		
		// pollution is represented by major and minor negative
		int pollution = 0;
		for(int i = 0; i < blocks.length; i++) {
			pollution += (18 - blocks[i].landValue); // each block uses a 3x3 to calculate land value and a standard score without any value blocks around is 18
		}
		// max value for pollution is (18 * 9) - 98 = 64
		if(pollution < 0) {
			pollution = 0;
		}
		
		int policeCoveragePoints = (int) Math.min(10, policeCoverage * 5f/3);
		int points = (int) (200 - ((population/960f) * 10 + (pollution/64f) * 15 - policeCoveragePoints) * 8);
		
		points = Math.min(points, 200);
		// crime rate is 0 - 10, so 400 - points / 400 * 10 = crime rate
		crimeRate = (int) (((200 - points)/200f) * 10);
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].crimeRate = crimeRate;
		}
		
		return points;
	}
	
	public void setPopulation(int points) {
		// this function will set up the population and the images to reflect it
		// but only adjust by a single increment each function call (1 every 30 frames)
		// first determine if to add or subtract from current population
		
		// max 960, min 0, r0 = 0, r1 = 20-160, r2 = 320
		// =   0, set r0,   0 = r0
		//20-160, set r1, 319 = r1
		// > 320, min r2, 479 = r2
		// > 480, min r3, 639 = r3
		// > 640, min r4, 799 = r4
		// > 800, min r5, 959 = r5
		// > 960, set r6, 960+= r6
		
		if(points - population >= populationIncrement) {
			// we need to upgrade
			increasePopulation();
		}
		else if(population - points > populationIncrement/4){
			// we need to down grade
			decreasePopulation();
		}
	}
	
	public void decreasePopulation() {
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].zoneLevel--;
			if(zoneLevel == 1) {
				blocks[i].population = false;
			}
		}
		zoneLevel--;
		population = populationIncrement * zoneLevel;
	}
	
	public void increasePopulation() {
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].zoneLevel++;
			if(zoneLevel == 0) {
				blocks[i].population = true;
			}
		}
		zoneLevel++;
		population = populationIncrement * zoneLevel;
	}
}
