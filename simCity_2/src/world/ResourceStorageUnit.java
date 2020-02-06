package world;

import java.io.Serializable;

public class ResourceStorageUnit implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// this class is a large storage unit which by default will contain all resources sent to it
	// by other elements, refinery and plant extend this class
	// a plant has its own storage unit for refined resources sent to it if its burning less than
	// its receiving then it builds up and after maxing out it sends it back to the main storage unit
	
	public int coalUnits = 0;
	public int refinedCoalUnits = 0;
	public int oilUnits = 0;
	public int refinedOilUnits = 0;
	public int uraniumUnits = 0;
	public int refinedUraniumUnits = 0;
	public int rareMetalUnits = 0;
	public int refinedRareMetalUnits = 0;

	public ResourceStorageUnit() {
		
	}
	
	public void acceptCoalUnits(int units) {
		coalUnits += units;
		System.out.println(coalUnits);
	}
	public void acceptRefinedCoalUnits(int units) {
		refinedCoalUnits += units;
	}
	public void acceptOilUnits(int units) {
		oilUnits += units;
	}
	public void acceptRefinedOilUnits(int units) {
		refinedOilUnits += units;
	}
	public void acceptUraniumUnits(int units) {
		uraniumUnits += units;
	}
	public void acceptRefinedUraniumUnits(int units) {
		refinedUraniumUnits += units;
	}
	public void acceptRareMetalUnits(int units) {
		rareMetalUnits += units;
	}
	public void acceptRefinedRareMetalUnits(int units) {
		refinedRareMetalUnits += units;
	}

}
