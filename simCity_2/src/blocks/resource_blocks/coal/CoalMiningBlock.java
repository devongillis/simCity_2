package blocks.resource_blocks.coal;

import java.awt.Graphics;
import java.util.Random;

import blocks.resource_blocks.MiningBlock;
import resources.Resources;
import world.World;
import world.GameIDs.Image;
import world.GameIDs.Value;

public class CoalMiningBlock extends MiningBlock{
	
	private static final long serialVersionUID = 1L;
	
	public int counter = 0;
	public final int extractTime = 60 * 5;

	public CoalMiningBlock(int tileNumberY, int tileNumberX, World world) {
		super(tileNumberY, tileNumberX, world);
		image = Image.COALMINER;
		ID = null;
		type = Value.MINORNEGATIVE;
		Random r = new Random();
		counter = r.nextInt(60 * 5);
	}
	
	@Override
	public void render(Graphics g, int tickCount, int season) {
		if(renderRange()) {
			g.drawImage(Resources.getImage(image, coord), realX + world.offsetX, realY + world.offsetY, blockSize, blockSize, null);
			super.render(g, tickCount, season);
		}
	}
	
	@Override
	public void update(float deltaTime, int tickCount) {
		counter++;
		if(counter >= extractTime) {
			counter = 0;
			extractUnit();
		}
	}
	
	
	@Override
	public void extractUnit() {
		if(destination != null) {
			destination.acceptResourceUnit(miningCapacity);
		}
		else {
			// send off to the city storage center
			world.storageUnit.acceptCoalUnits(miningCapacity);
		}
	}

}
