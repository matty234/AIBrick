package brick;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;

public class ShortestMultipointPathFinder extends ShortestPathFinder{

	public ShortestMultipointPathFinder(LineMap map) {
		super(map);
	}

	public Path findRoute(Pose start, Waypoint[] points) throws DestinationUnreachableException {
		Path path = new Path();
		for (int i = 0; i < points.length; i++) {
			Pose nextStartPoint;
			if(i == 0){
				nextStartPoint = start;
			} else {
				nextStartPoint = points[i-1].getPose();
			}
			Waypoint endPoint = new Waypoint(points[i].getX(), points[i].getY(), points[i].getHeading());
			path.addAll(super.findRoute(nextStartPoint, endPoint));
		}
		return path;
	}
		
		public Path[] findPaths(Pose start, Waypoint[] points) throws DestinationUnreachableException {
			Path[] paths = new Path[points.length];
			for (int i = 0; i < points.length; i++) {
				Pose nextStartPoint;
				if(i == 0){
					nextStartPoint = start;
				} else {
					nextStartPoint = points[i-1].getPose();
				}
				Waypoint endPoint = new Waypoint(points[i].getX(), points[i].getY(), points[i].getHeading());
				paths[i] = super.findRoute(nextStartPoint, endPoint);
			}	
		
		return paths;
	}
	

}
