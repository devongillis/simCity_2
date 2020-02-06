package zones;

import final_blocks.CommericalBlock;
import world.World;
import world.GameIDs.Zone;

public class CommericalZone extends GameZone{
	
	private static final long serialVersionUID = 1L;
	// a residence zone holds reference to 3x3 or 9 residence blocks
	// each residence block does its own update independently for things
	// like power, but the entire zone is updated here, including
	// population, zone value, toping, etc.
	
	// posX and posY refer to the center residence block of the entire
	// zone (rendering), as well as tileNumbers (coords)
	// residence zone does not have a render function as it only contains
	// references to other residence blocks and their status
	
	public CommericalBlock[] blocks = null; // blocks that make up the zone
	public int zoneLevel = 0; // 0 - 5, apartment level
	public int population = 0; // zone population
	public boolean topReady = false;
	public boolean topFormed = false;
	public CommericalZone partner = null;
	
	public CommericalZone(int tileNumberY, int tileNumberX, CommericalBlock[] blocks, World world) {
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
		
		// later lets use a random number to decide if zone should call an update or not
		// use a default minimum delay and then an added random delay before next update
		// land value 0-150, city center 0-450, crime rate 0-400
		int total = landValuePoints() + cityCenterPoints() + CrimeRatePoints();
		setPopulation(total);
	}
	
	public boolean hasTransportation() {
		// if this returns false then maximum zone level of 2 can be reached
		// later should return true if touching road/rail, false if not
		return true;
	}
	
	public int landValuePoints() {
		// return 0 - 150 points
		int landValue = 0;
		for(int i = 0; i < blocks.length; i++) {
			landValue += blocks[i].landValue; // land value range for residential is 98 - 226
		}
		// 226 - 98 = 128, 150/128 = 1.172
		int points = (int) ((landValue - 98) * 1.172f);
		if(points < 0) {
			return 0; // for first few frames the blocks land values are not up to date so a negative number may be returned
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
	
	public int cityCenterPoints() {
		// return 0 - 450 points
		// since distance is 100 wide max distance is 50, if city of to side and zone on other side it receives 0
		int distanceY = Math.abs(world.cityCenter[0] - tileNumberY);
		int distanceX = Math.abs(world.cityCenter[1] - tileNumberX);
		float distance = (float) Math.sqrt(distanceY * distanceY + distanceX * distanceX);
		int points = (int) Math.round((50f - distance) * 9f);
		
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
		if(zoneLevel < 6) {
			for(int i = 0; i < blocks.length; i++) {
				blocks[i].zoneLevel--;
				if(zoneLevel == 1) {
					blocks[i].population = false;
				}
			}
			zoneLevel--;
			population = populationIncrement * zoneLevel;
		}
		else {
			loseTop();
		}
	}
	
	public void increasePopulation() {
		if(zoneLevel < 5) {
			for(int i = 0; i < blocks.length; i++) {
				blocks[i].zoneLevel++;
				if(zoneLevel == 0) {
					blocks[i].population = true;
				}
			}
			zoneLevel++;
			population = populationIncrement * zoneLevel;
		}
		else {
			becomeTop();
		}
	}
	
	public void loseTop() {
		// we need to take care of us and the zone we toped with
		for(int i = 0; i < 9; i++) {
			partner.blocks[i].topOrientation = 'L'; // default
			partner.blocks[i].zoneLevel--;
			partner.blocks[i].formedTop = false;
			blocks[i].topOrientation = 'L'; // default
			blocks[i].zoneLevel--;
			blocks[i].formedTop = false;
		}
		partner.zoneLevel--;
		partner.topFormed = false;
		partner.population = populationIncrement * partner.zoneLevel;
		zoneLevel--;
		topFormed = false;
		population = populationIncrement * zoneLevel;
		
		partner.partner = null;
		partner = null;
	}
	
	public void becomeTop() {
		// this function is called once the zone has reached C5 and points allow it to 
		// reach CT, it first scopes the surrounding zones for other C5's wanting to form
		// a top and if it can find one it forms a top, otherwise it marks itself ready and
		// waits
		topReady = true;
		boolean topped = false;
		for(GameZone specificZone: world.zones.get(Zone.COMMERCIAL)){
			CommericalZone c = (CommericalZone)specificZone;
			if(c.tileNumberY == tileNumberY && c.tileNumberX == tileNumberX - 3 && c.topReady && c.topFormed == false) {
				for(int i = 0; i < 9; i++) {
					blocks[i].topOrientation = 'R';
					c.blocks[i].topOrientation = 'L';
				}
				topped = true;
			}
			if(c.tileNumberY == tileNumberY - 3 && c.tileNumberX == tileNumberX && c.topReady && c.topFormed == false) {
				for(int i = 0; i < 9; i++) {
					blocks[i].topOrientation = 'B';
					c.blocks[i].topOrientation = 'T';
				}
				topped = true;
			}
			if(c.tileNumberY == tileNumberY && c.tileNumberX == tileNumberX + 3 && c.topReady && c.topFormed == false) {
				for(int i = 0; i < 9; i++) {
					blocks[i].topOrientation = 'L';
					c.blocks[i].topOrientation = 'R';
				}
				topped = true;
			}
			if(c.tileNumberY == tileNumberY + 3 && c.tileNumberX == tileNumberX && c.topReady && c.topFormed == false) {
				for(int i = 0; i < 9; i++) {
					blocks[i].topOrientation = 'T';
					c.blocks[i].topOrientation = 'B';
				}
				topped = true;
			}
			if(topped) {
				topFormed = true;
				zoneLevel++;
				population = populationIncrement * zoneLevel;
				c.topFormed = true;
				c.zoneLevel++;
				c.population = populationIncrement * c.zoneLevel;
				for(int i = 0; i < 9; i++) {
					blocks[i].formedTop = true;
					blocks[i].zoneLevel++;
					c.blocks[i].formedTop = true;
					c.blocks[i].zoneLevel++;
				}
				partner = c;
				c.partner = this;
				break;
			}
		}
	}
}
