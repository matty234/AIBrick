package brick.behavior;
import brick.Brick;
import brick.RCCommand;
import lejos.robotics.subsumption.Behavior;

public class Obstacle implements Behavior {

    final static int MIN_DIST = 30;
    static long time = 0;
  
    public void action() {

    	FollowPath.navigator.stop();
    	
    	
  /*  Brick.differentialPilot.stop(); // sees object and stops
	RCCommand.PATHFINDER.segementBlocked(start node???,waypoints); //tell the Dijkstra algor that the path is blocked  


	//goes to last waypint location but might think that the destination is the starting waypoint

	Brick.differentialPilot.steer(-50,180,true);	
	Brick.differentialPilot.travel
	(-(Brick.differentialPilot.getMovementIncrement())); // got to last waypoint  
	
	
	FollowPath.navigator.stop();
	FollowPath.navigator.followPath();
	
	Brick.navigator.followpath(SHOULD_TAKE_OUT);
	Brick.navigator.followPath(Brick.finder.findPaths(poseProvider.getPose(), waypoints));
		//	RCCommand.PATHFINDER.startpathfinding(Brick.poseProvider.getPose(),Brick.waypoints)); // finds the new shortest path.*/
    }

    public void suppress() {
    	FollowPath.navigator.followPath();

    }

    public synchronized boolean takeControl() {

        long currentTime = System.currentTimeMillis();

        if (currentTime < Obstacle.time + 200) {

            return false;

        }

        Obstacle.time = currentTime;

        return (Brick.us.getDistance() < MIN_DIST);

    }

}