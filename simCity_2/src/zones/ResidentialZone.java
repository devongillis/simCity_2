package zones;

import java.util.Random;

import final_blocks.ResidentialBlock;
import world.World;

public class ResidentialZone extends GameZone {
	// a residence zone holds reference to 3x3 or 9 residence blocks
	// each residence block does its own update independently for things
	// like power, but the entire zone is updated here, including
	// population, zone value, toping, etc.
	
	// posX and posY refer to the center residence block of the entire
	// zone (rendering), as well as tileNumbers (coords)
	// residence zone does not have a render function as it only contains
	// references to other residence blocks and their status
	
	private static final long serialVersionUID = 1L;
	
	public ResidentialBlock[] blocks = null; // blocks that make up the zone
	public int apartment = 0; // 0 - 6, apartment level
	public int population = 0; // zone population
	
	public ResidentialZone(int tileNumberY, int tileNumberX, ResidentialBlock[] blocks, World world) {
		super(tileNumberY, tileNumberX, world);
		this.blocks = blocks;
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		// a lot of code will be added here from population updates
		// involving taxes, crime, land value, economy, supply, demand,
		// randomness, job opportunity
		
		// a single block can have a max value of 36 land value thus a residential 3x3 can return
		// a maximum of 4*(5*4 + 4*2) + 4*(3*4 + 6*2) + 9*2 = 4*(20+8) + 4*(12+12) + 18 = 4*28+4*24+18
		// = 112+96+18 = 226, but will likely return 4*(8) + 4*(12) + 18 = 32+48+18 = 98, as most zones
		// will not have valuable land near by
		//System.out.println(tileNumberY + " " + tileNumberX);
		
		// later lets use a random number to decide if zone should call an update or not (1/30)
		// use a default minimum delay and then an added random delay before next update
		int total = landValuePoints() + cityCenterPoints() + jobDistancePoints() + CrimeRatePoints();
		setPopulation(total);
		
	}
	
	public boolean hasTransportation() {
		// if this returns false then maximum zone level of 2 can be reached
		// later should return true if touching road/rail, false if not
		return true;
	}
	
	public int landValuePoints() {
		// return 0 - 250 points
		int landValue = 0;
		for(int i = 0; i < blocks.length; i++) {
			landValue += blocks[i].landValue; // land value range for residential is 98 - 226
		}
		// 226 - 98 = 128, 250/128 = 1.954
		int points = (int) ((landValue - 98) * 1.954f);
		if(points < 0) {
			return 0; // for first few frames the blocks landvalues are not up to date so a negative number may be returned
		}
		return points;
	}
	
	public int CrimeRatePoints() {
		// return 0 - 400 points
		
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
		int points = (int) (400 - ((population/960f) * 10 + (pollution/64f) * 15 - policeCoveragePoints) * 16);
		points = Math.min(points, 400);
		// crime rate is 0 - 10, so 400 - points / 400 * 10 = crime rate
		crimeRate = (int) (((400 - points)/400f) * 10);
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].crimeRate = crimeRate;
		}
		
		return points;
	}
	
	public int jobDistancePoints() {
		// return 0 - 50 points
		return 50;
	}
	
	public int cityCenterPoints() {
		// return 0 - 300 points
		// since distance is 100 wide max distance is 50, if city of to side and zone on other side it receives 0
		int distanceY = Math.abs(world.cityCenter[0] - tileNumberY);
		int distanceX = Math.abs(world.cityCenter[1] - tileNumberX);
		float distance = (float) Math.sqrt(distanceY * distanceY + distanceX * distanceX);
		int points = (int) Math.round((50f - distance) * 6f);
		
		if(points < 0) {
			return 0;
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
		
		// convert points into a points ranking (0, 1-8, 20, 30, 40, 50, 60)
		int pointsRanking = points/160;
		if(pointsRanking < 2) {
			// if points is 319 or less, convert this to 1-8
			pointsRanking = Math.min(points/20, 8);
		}
		else {
			pointsRanking *= 10;
		}
		// now points ranking is 0, 1-8, 20, 30, 40, 50, 60
		// do the same to population
		int populationRanking = population/160;
		if(populationRanking < 2) {
			populationRanking = Math.min(population/20, 8);
		}
		else {
			populationRanking *= 10;
		}
		
		
		// now compare them, if points ranking is higher than we need to upgrade
		if(pointsRanking > populationRanking) {
			// we need to upgrade
			increasePopulation(populationRanking);
		}
		else if(pointsRanking < populationRanking) {
			if(population - points > populationIncrement/4 || populationRanking < 20) {
				decreasePopulation();
			}
		}
	}
	
	
	
	public void decreasePopulation() {
		if(apartment > 2) {
			downGradeApartment();
		}
		else if(apartment == 2) {
			downGradeFromApartment();
		}
		else {
			subtractHouse();
		}
	}
	
	public void increasePopulation(int populationRank) {
		// we have confirmed that an upgrade is needed since points rank > population rank
		// we need to determine whether to add a house, upgrade to or upgrade apartment
		if(populationRank < 8) {
			// add house
			addHouse();
		}
		else if(populationRank == 8){
			// upgrade to apartment
			upGradeToApartment();
		}
		else {
			// upgrade apartment
			upGradeApartment();
		}
	}
	
	public void upGradeApartment() { // switch this out to setting values directly rather than by increments
		// we move up a level within the apartment
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].zoneLevel++;
		}
		apartment++;
		population = populationIncrement * apartment;
	}
	
	public void downGradeApartment() {
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].zoneLevel--;
		}
		apartment--;
		population = populationIncrement * apartment;
	}
	
	public void upGradeToApartment() {
		// we now render all 9 as one unit
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].zoneLevel = 2; // we have now entered the first apartment level which is marked as level 2
		}
		// middle block is not used for population but its still checked for rendering
		// set to true so proper image is used
		blocks[4].population = true;
		apartment = 2;
		population = populationIncrement * apartment; // population is now set to apartment level 1 population
	}
	
	public void downGradeFromApartment() {
		for(int i = 0; i < blocks.length; i++) {
			blocks[i].zoneLevel = 1;
		}
		blocks[4].population = false;
		apartment = 1;
		population = populationIncrement * apartment;
	}
	
	public void addHouse() {
		// this function is called to add a house to a 3x3 grid
		int total = 0;
		int[] blocksEmpty = new int[8];
		for(int i = 0; i < blocks.length; i++) {
			if(blocks[i].population == false && i != 4) {
				blocksEmpty[total] = i;
				total++;
			}
		}
		if(total == 0) {
			return;
		}
		Random random = new Random();
		int value = random.nextInt(total);
		blocks[blocksEmpty[value]].population = true; // add the population
		blocks[blocksEmpty[value]].zoneLevel = 1;
		blocks[blocksEmpty[value]].zonegrade = getZoneValue();
		blocks[blocksEmpty[value]].random = random.nextInt(3); // random house design
		population = (populationIncrement/8) * ((8 - total) + 1); 
	}
	
	public void subtractHouse() {
		// this function is called to subtract a house from a 3x3 grid
		int total = 0;
		int[] blocksFull = new int[9];
		for(int i = 0; i < blocks.length; i++) {
			if(blocks[i].population && i != 4) { // i at 4 population should only be true when zone is level 2 or higher, might not need to check 
				blocksFull[total] = i;
				total++;
			}
		}
		if(total == 0) {
			return;
		}
		Random random = new Random();
		int value = random.nextInt(total);
		blocks[blocksFull[value]].population = false; // subtract the population
		blocks[blocksFull[value]].zoneLevel = 0;
		population = (populationIncrement/8) * (total - 1);
	}
	
	public int getZoneValue() {
		// this function should inspect the pros and cons of the land
		// to determine the rank of house to be put down
		// ultimately this should let the player see how the land is
		// doing and understand what kind of zone rank the residence
		// may end up becoming
		Random r = new Random();
		return r.nextInt(3);
	}
	
	
	
	
}
