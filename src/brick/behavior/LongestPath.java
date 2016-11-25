import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

public class LongestPath implements Behavior {

	private Navigator navigator;
	private static PoseProvider pp;
	private boolean passControl = false;

	public LongestPath(DifferentialPilot dp) {
		navigator = new Navigator(dp);
		pp = new OdometryPoseProvider(dp);
	}

	@Override
	public boolean takeControl() {
		return !passControl;
	}

	@Override
	public void action() {
		Path path;
		ShortestMultipointPathFinder  finder = new ShortestMultipointPathFinder(RCCommand.LINEMAP);
		
		while(!passControl && Brick.packet.getMode() == RCCommand.Modes.NAVIGATE){
			try {
				path = getInverse(finder.findPaths(pp.getPose(), getLocations(Brick.packet.commands)));
				navigator.followPath(path);
				passControl = Button.ENTER.isDown();
			} catch (DestinationUnreachableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Waypoint[] getLocations(byte[] commands) { // only for
		// testing
		ArrayList<Waypoint> waypoints = new ArrayList<>();
		for (int i = 0; i < commands.length; i++) {
			if (commands[i] == RCCommand.HOME)
				waypoints.add(RCCommand.HOMEPOINT);
			else if (commands[i] == RCCommand.OFFICE)
				waypoints.add(RCCommand.OFFICEPOINT);
			else if (commands[i] == RCCommand.PARK)
				waypoints.add(RCCommand.PARKPOINT);
			else if (commands[i] == RCCommand.SHOP)
				waypoints.add(RCCommand.SHOPPOINT);
			else
				;
		}
		return waypoints.toArray(new Waypoint[waypoints.size()]);
	}

	private Path getInverse(Path[] paths){
		Waypoint[] wp = getLocations(Brick.packet.commands);
		Waypoint first = wp[0];
		Waypoint last = wp[wp.length-1];
		ArrayList<Waypoint> newWP = new ArrayList<Waypoint>();
		newWP.add(first);
		
		for(int i = 1; i<wp.length-1; i++){
			for(Path p : paths){
				if(!p.contains(wp[i]) && !wp[i].equals(last))
					newWP.add(wp[i]);
			}
		}
		
		newWP.add(last);
		
		return (Path) newWP;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
