package world;

import java.io.Serializable;

public class Budget implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// this class handles the entire financial aspect of the city, everything resource and
	// money related goes through here
	
	public int money = 0; // the entire city's money
	
	public Budget() {
		
	}
	
	public void collectTaxes() {
		// this function will assess the entire city and collect taxes based of populations,
		// land values, tax rates, etc.
	}

}
