import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

public class WaitForClap implements Behavior {

	private SoundSensor ss;
	private Navigator navigator;
	private static PoseProvider pp;
	private boolean passControl = false;
	
	public WaitForClap(SensorPort sp, DifferentialPilot dp){
		ss = new SoundSensor(sp);
		navigator = new Navigator(dp);
		pp = new OdometryPoseProvider(dp);
	}
	
	@Override
	public boolean takeControl() {
		return passControl;
	}

	@Override
	public void action() {
		Path[] paths;
		
		while(!passControl && Brick.packet.getMode() == RCCommand.Modes.NAVIGATE){
			paths = getPaths(Brick.packet.commands);
			
			for(Path p : paths){
				navigator.followPath(p);
				navigator.waitForStop();
				while(ss.readValue() < 55);
			}
			
			passControl = Button.ENTER.isDown();
		}
	}
	
	private static Path[] getPaths(byte[] commands) {
		Point currentPose = pp.getPose().getLocation();
		ArrayList<Waypoint> waypoints = new  ArrayList<>();
		for (int i = 0; i < commands.length; i++) {
			if(commands[i] == RCCommand.HOME && !RCCommand.HOMEPOINT.equals(currentPose))
				waypoints.add(RCCommand.HOMEPOINT);
			else if(commands[i] == RCCommand.OFFICE && !RCCommand.OFFICEPOINT.equals(currentPose))
				waypoints.add(RCCommand.OFFICEPOINT);
			else if(commands[i] == RCCommand.PARK && !RCCommand.PARKPOINT.equals(currentPose))
				waypoints.add(RCCommand.PARKPOINT);
			else if(commands[i] == RCCommand.SHOP && !RCCommand.SHOPPOINT.equals(currentPose))
				waypoints.add(RCCommand.SHOPPOINT);
		}
		
		return (Path[]) waypoints.toArray();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
