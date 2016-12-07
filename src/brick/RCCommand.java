package brick;

import lejos.geom.Rectangle;
import lejos.geom.Line;
import lejos.geom.Point;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.DijkstraPathFinder;

public interface RCCommand {
	public static interface Modes {
		 public final byte  // Send Modes
		  	HANDSHAKE = 0x01,
		  	REPORT = 0x02,
		  	MANUAL = 0x03,
		  	NAVIGATE = 0x04,
		  	FARE = 0x05,
		  	EXIT = 0x06;
		 
		 public final byte  // Active Modes
		  	MOVING = 0x01,
		  	STOPPED = 0x02;
	}
	 
	  
	  public final byte  // REPORT
	  	MACADDR = 0x01,
	  	BATTERY = 0x02;
	  
	  public final byte  // MANUAL
	  	STOP = 0x01,
	  	FORWARD = 0x02,
	  	REVERSE = 0x03,
	  	TURN = 0x04;
	  	
	  public final byte  // PILOT
	  	LOCATION = 0x01;
	  	
	  public final byte
		HOME = 0x01,
		SHOP = 0x02,
		PARK = 0x03,
		OFFICE = 0x04,
		CHURCH = 0x05,
		HOSPITAL = 0x06,
		SUPERMARKET = 0x07,
		STATION = 0x08;

	public final static Waypoint
		HOMEPOINT = new Waypoint(140, 535),
		CHURCHPOINT = new Waypoint(600, 330),
		HOSPITALPOINT = new Waypoint(745, 330),
		SUPERMARKETPOINT = new Waypoint(745, 200),
		OFFICEPOINT = new Waypoint(745, 90),
		PARKPOINT = new Waypoint(450, 465),
		STATIONPOINT = new Waypoint(235, 90),
		SHOPPOINT = new Waypoint(255, 333);
	  
	  public final static Waypoint[] WAYPOINTS = {HOMEPOINT, CHURCHPOINT, HOSPITALPOINT, SUPERMARKETPOINT, OFFICEPOINT, PARKPOINT, STATIONPOINT, SHOPPOINT};
	  
	  public final Line[] ROADS = {
			  new Line(0, 0, 0, 100),
			  new Line(0, 90, 830, 90),
			  new Line(450, 20, 450, 550),
			  new Line(210, 80, 210, 550),
			  new Line(30, 540, 460, 540),
			  new Line(670, 80, 670, 550),
			  new Line(660, 200, 830, 200),
			  new Line(430, 460, 520, 460),
			  new Line(200, 330, 830, 330)
	  };
	  
	  public final Rectangle MAPAREA = new Rectangle(0, 0, 841, 594);
	  public final LineMap LINEMAP = new LineMap(ROADS, MAPAREA);
	  

	  
	  public DijkstraPathFinder PATHFINDER = new DijkstraPathFinder(LINEMAP);
	  public final static String IOERR = "Finishing for IO err";

}
