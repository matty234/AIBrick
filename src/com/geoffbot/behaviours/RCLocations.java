package com.geoffbot.behaviours;

import lejos.robotics.navigation.Waypoint;

public interface RCLocations {

	public final byte // PILOT
		HOME = 0x01, 
		SHOP = 0x02, 
		PARK = 0x03, 
		OFFICE = 0x04;

	public final Waypoint // PILOT
		HOMEPOINT = new Waypoint(0, 0), 
		SHOPPOINT = new Waypoint(300, 900), 
		PARKPOINT = new Waypoint(350, 400),
		OFFICEPOINT = new Waypoint(800, 500);

}
