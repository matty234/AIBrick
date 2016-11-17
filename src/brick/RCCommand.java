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
		  	NAVIGATE = 0x04;
		 
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
	  	
	  public final byte  // PILOT
	  	HOME = 0x01,
	  	SHOP = 0x02,
	  	PARK = 0x03,
	  	OFFICE = 0x04;
	  

	  public final static Waypoint
	  	HOMEPOINT = new Waypoint(100, 100),
	  	SHOPPOINT = new Waypoint(600, 100),
	  	PARKPOINT = new Waypoint(100, 600),
	  	OFFICEPOINT = new Waypoint(600, 600);
	  
	  
	  public final Line[] ROADS = {
			  new Line(0, 0, (int) HOMEPOINT.getX(), (int) HOMEPOINT.getY()),
			  new Line((int) HOMEPOINT.getX(), (int) HOMEPOINT.getY(), (int) SHOPPOINT.getX(), (int) SHOPPOINT.getY()),
			  new Line((int) SHOPPOINT.getX(), (int) SHOPPOINT.getY(), (int) OFFICEPOINT.getX(), (int) OFFICEPOINT.getY()),
			  new Line((int) SHOPPOINT.getX(), (int) SHOPPOINT.getY(), (int) PARKPOINT.getX(), (int) PARKPOINT.getY()),
			  new Line((int) PARKPOINT.getX(), (int) PARKPOINT.getY(), (int) OFFICEPOINT.getX(), (int) OFFICEPOINT.getY()),
	  };
	  
	  public final Rectangle MAPAREA = new Rectangle(0, 0, 965, 650);
	  public final LineMap LINEMAP = new LineMap(ROADS, MAPAREA);
	  
	  
	  
	  public DijkstraPathFinder PATHFINDER = new DijkstraPathFinder(LINEMAP);
}
