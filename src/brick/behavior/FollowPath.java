package brick.behavior;

import java.util.ArrayList;

import brick.Brick;
import brick.RCCommand;
import brick.ShortestMultipointPathFinder;
import lejos.nxt.Sound;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

public class FollowPath implements Behavior, RCCommand {
	//Path path;
	Navigator navigator = new Navigator(Brick.differentialPilot);
	PoseProvider poseProvider = new OdometryPoseProvider(Brick.differentialPilot);

	ShortestMultipointPathFinder finder = new ShortestMultipointPathFinder(LINEMAP);
	public static boolean SHOULD_TAKE_CONTROL = false;;
	static ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();


	public FollowPath(Waypoint[] waypoints) {
		for (int i = 0; i < waypoints.length; i++) {
			this.waypoints.add(waypoints[i]);
		}
	}

	public FollowPath() {
	}

	@Override
	public boolean takeControl() {
		return  SHOULD_TAKE_CONTROL; //(this.waypoints.size() > 0) &&
	}

	@Override
	public void action() {
			Path[] paths;
			try {
				paths = finder.findPaths(poseProvider.getPose(), waypoints);
			} catch (DestinationUnreachableException e) {
				SHOULD_TAKE_CONTROL = false;
				System.out.println("Could not reach the destination");
				return;
			}
			for (int i = 0; i < paths.length; i++) {
				navigator.followPath(paths[i]);
				navigator.waitForStop();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
			System.out.println("Route Finished!");
			//this.waypoints.clear();
			Sound.systemSound(false, 3);
			SHOULD_TAKE_CONTROL = false;

	}

	@Override
	public void suppress() {
		System.out.println("Supressed");
		navigator.stop();
		SHOULD_TAKE_CONTROL = false;

	}

	
	public static void addWayPoint(Waypoint waypoint) {
		waypoints.add(waypoint);
	}

	public static void addAllWayPoints(ArrayList<Waypoint> wp) {
		waypoints.clear();
		waypoints.addAll(wp);
	}

}