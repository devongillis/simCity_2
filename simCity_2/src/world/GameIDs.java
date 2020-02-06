package world;

public class GameIDs {
	public static enum Zone{
		RESIDENTIAL,
		POLICE,
		FIRE,
		COMMERCIAL,
		INDUSTRIAL,
		NUCLEAR,
		COAL,
		AIRPORT,
		SEAPORT,
		STADIUM,
		GIFT,
		COALREFINERY
	}
	
	public static enum Block{
		DIRT,
		ROAD,
		RAIL,
		POWERLINE,
		FOREST,
		COALMINER,
		OILMINER,
		URANIUMMINER,
		RAREMETALMINER
	}
	
	public static enum Value{
		// every block is one of these for calculating land value, consider using different names as
		// negatives still translate to positive points
		MAJORNEGATIVE, // airport/seaport/coal pollution
		MINORNEGATIVE, // industrial pollution, road pollution, certain gifts
		NEUTRAL,       // population, service, railway, powerline, nuclear
		MINORPOSITIVE, // forest, certain gifts
		MAJORPOSITIVE, // water, forest park, certain gifts
	}
	
	public static enum Image{
		DIRT,
		FOREST,
		ROAD,
		RAIL,
		RESIDENTIAL,
		INDUSTRIAL,
		COMMERCIAL,
		POLICE,
		NUCLEAR,
		POWERLINE,
		POWEROFF,
		BUILDINGSCROLLER,
		BUILDER3,
		RESOURCESELECTOR,
		WATER,
		BUTTONS,
		AIRPORT,
		SEAPORT,
		COALMINER,
		COALREFINER,
		COALPLANT,
		OILMINER,
		OILREFINER,
		OILPLANT,
		URANIUMMINER,
		URANIUMREFINER,
		URANIUMPLANT,
		RAREMETALMINER,
		RAREMETALREFINER,
		RAREMETALPLANT,
		RESOURCEMENU,
		BUTTONGREEN,
		BUTTONRED,
		POINTER,
		CURSOR,
		TOPBUTTONS,
		BACKGROUND_GREY,
		BACKGROUND_LIGHTGREEN,
		BACKGROUND_DARKGREEN
	}
	
	public static enum Cursor{
		BULLDOZE,
		SELECT,
		ONE,
		THREE,
		FOUR,
		SIX
	}
	
	public static enum Button{
		BULLDOZE,
		ROAD,
		RAIL,
		POWERLINE,
		
		FOREST,
		RESIDENTIAL,
		COMMERCIAL,
		INDUSTRIAL,
		
		POLICE,
		FIRE,
		STADIUM,
		SEAPORT,
		
		COAL,
		NUCLEAR,
		AIRPORT,
		GIFT,
		
		BLOCKCLICK,
		UNDEFINED
	}
	
	public enum TopButton{
		GAMESPEED,
		SETUP,
		DISASTERS,
		STATISTICS,
		SAVELOAD,
		ADVICE,
		MAGNIFY,
		HELICOPTER
	}
}
