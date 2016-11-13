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
	  

	  public final Waypoint
	  	HOMEPOINT = new Waypoint(0, 0),
	  	SHOPPOINT = new Waypoint(300, 900),
	  	PARKPOINT = new Waypoint(350,400),
	  	OFFICEPOINT = new Waypoint(800, 500);
	  
	  
	  public final Line[] ROADS = {
			  new Line(0, 0, 300, 900),
			  new Line(0, 0, 350, 400),
			  new Line(350, 0, 350, 400)

	  };
	  
	  public final Rectangle MAPAREA = new Rectangle(0, 0, 1000, 1000);
	  public final LineMap LINEMAP = new LineMap(ROADS, MAPAREA);
	  
	  
	  
	  public DijkstraPathFinder PATHFINDER = new DijkstraPathFinder(LINEMAP);
}
