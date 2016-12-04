package brick.behavior;

import java.util.Random;

import brick.Brick;
import brick.RCCommand;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;

public class FreeRoam implements Behavior, RCCommand {
	
	public static  boolean SHOULD_TAKE_CONTROL = false;
	ShortestPathFinder finder = new ShortestPathFinder(LINEMAP);
	PoseProvider poseProvider;
	Navigator navigator;

	
	public FreeRoam(Navigator navigator, PoseProvider poseProvider){
		this.navigator = navigator;
		this.poseProvider = poseProvider;
	}
	
	
	@Override
	public boolean takeControl() {
		return SHOULD_TAKE_CONTROL;
	}

	@Override
	public void action() {
		Path path;
		try {
			path = finder.findRoute(poseProvider.getPose(), getRandomPoint());
			navigator.followPath(path);
			navigator.waitForStop();
		} catch (DestinationUnreachableException e) {
			SHOULD_TAKE_CONTROL = false;
			System.out.println("Could not reach the destination");
			return;
		}
	}
	
	private static Waypoint getRandomPoint() {
		Random random = new Random();
		Waypoint waypoint = WAYPOINTS[random.nextInt(WAYPOINTS.length)];
		return waypoint;
	}

	@Override
	public void suppress() {
		navigator.stop();
	}
	
	
}