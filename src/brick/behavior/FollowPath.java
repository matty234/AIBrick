package brick.behavior;

import java.util.ArrayList;

import brick.Brick;
import brick.RCCommand;
import brick.ShortestMultipointPathFinder;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

public class FollowPath implements Behavior, RCCommand {
	Path path;
	Navigator navigator;
	PoseProvider poseProvider = new OdometryPoseProvider(Brick.differentialPilot);

	ShortestMultipointPathFinder finder = new ShortestMultipointPathFinder(LINEMAP);
	public static boolean SHOULD_TAKE_CONTROL;
	ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

	{
		navigator = new Navigator(Brick.differentialPilot);
		SHOULD_TAKE_CONTROL = false;
	}

	public FollowPath(Waypoint[] waypoints) {
		for (int i = 0; i < waypoints.length; i++) {
			this.waypoints.add(waypoints[i]);
		}
	}

	public FollowPath() {
	}

	@Override
	public boolean takeControl() {
		return (path != null) && SHOULD_TAKE_CONTROL;
	}

	@Override
	public void action() {
		if (!navigator.isMoving()) {
			navigator.followPath();
		} else {
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
		}

	}

	@Override
	public void suppress() {
		navigator.stop();
		SHOULD_TAKE_CONTROL = false;
	}

	public void addWayPoint(Waypoint waypoint) {
		this.waypoints.add(waypoint);
	}

	public void addAllWayPoints(ArrayList<Waypoint> waypoints) {
		this.waypoints.addAll(waypoints);
	}

}