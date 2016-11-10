package com.geoffbot;
import lejos.robotics.navigation.Waypoint;

public interface RCCommand {
	public static interface Modes {
		 public final byte  // Send Modes
		  	HANDSHAKE = 0x01,
		  	REPORT = 0x02,
		  	MANUAL = 0x03,
		  	NAVIGATE = 0x04,
		  	EXIT = 0x05;
		 
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
	  	
	  
}
