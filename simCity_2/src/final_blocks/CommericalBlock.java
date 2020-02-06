package final_blocks;

import java.awt.Graphics;

import blocks.PopulationBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Value;
import world.GameIDs.Image;
import world.GameIDs.Zone;

//a single commercial block represents 1/9 of an entire commercial zone
public class CommericalBlock extends PopulationBlock{
	private static final long serialVersionUID = 1L;
	
	public boolean formedTop = false;
	public char topOrientation = 'L'; // assume default position Left
	
	public CommericalBlock(int tileNumberY, int tileNumberX, int zone, int coord, World world) {
		super(tileNumberY, tileNumberX, zone, coord, world);
		ID = Zone.COMMERCIAL;
		type = Value.NEUTRAL;
		image = Image.COMMERCIAL;
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		// some code to get info on zones current state:
		// power, population, grade
		// then use this to find image
		if(renderRange()) {
			if(!population) {
				g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
			else if(formedTop && coord >= 3 && coord <= 5) {
				// formed a top now render accordingly
				// all this will be updated when we get better images
				// middle rows
				if(topOrientation == 'L') {
					g.drawImage(Resources.getImage(image, coord - 3 + 9 * (zoneLevel + 1)), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
				}
				if(topOrientation == 'R') {
					g.drawImage(Resources.getImage(image, coord + 9 * (zoneLevel + 1)), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
				}
				if(topOrientation == 'T') {
					g.drawImage(Resources.getImage(image, coord + 3 + 9 * (zoneLevel + 1)), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
				}
				if(topOrientation == 'B') {
					g.drawImage(Resources.getImage(image, coord + 6 + 9 * (zoneLevel + 1)), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
				}
			}
			else {
				g.drawImage(Resources.getImage(image, coord + 9 * zoneLevel), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			}
			
			// consider having a separate power off image for each power zone,
			// so to keep the number of draw image calls at one
			super.render(g, tickCount, season);
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		super.update(deltaTime, tickCount);
	}
}
