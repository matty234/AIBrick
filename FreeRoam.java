import java.util.ArrayList;

import lejos.geom.Point;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

public class FreeRoam implements Behavior {
	private TouchSensor ts;
	private Navigator navigator;
	private static PoseProvider pp;
	private boolean passControl = false;
	private RobotPacket rp;
	
	
	public FreeRoam(SensorPort sp, DifferentialPilot p, ArrayList<Waypoint> wp){
		ts = new TouchSensor(sp);
		navigator = new Navigator(p);
		pp = new OdometryPoseProvider(p);
	}
	
	
	
	@Override
	public boolean takeControl() {
		return !passControl;
	}

	@Override
	public void action() {
		Path[] paths;
		while(!passControl){
			RobotPacket packet = RobotPacketReader.readRobotPacket();
			
			if(packet.getMode() == RCCommand.Modes.NAVIGATE){
				paths = getPaths(packet.commands);
				
				for(Path p: paths){
					navigator.followPath(p);
					navigator.waitForStop();
					Thread.sleep(500);
				}
				
				passControl = ts.isPressed();
			}
			
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
		System.out.println("Free Roam ended");
	}
	
	

}
