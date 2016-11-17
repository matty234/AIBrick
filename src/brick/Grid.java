
package brick;

import java.util.ArrayList;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Waypoint;

public class Grid {
	private int size;
	private Line[] lines;
	private ArrayList<Waypoint> waypoints;
	private LineMap map;
	private final Waypoint HOME = new Waypoint(0.0,0.0);
	
	public Grid(int s){
		size = s;
		waypoints = new ArrayList<Waypoint>();
		waypoints.add(HOME);
	}
	
	public Grid(int s, ArrayList<Waypoint> wp){
		size = s;
		waypoints = wp;
		constructGrid();
	}
	
	public LineMap getMap(){
		return map;
	}
	
	public void addDestination(Waypoint p){
		lines = new Line[waypoints.size()];
		waypoints.add(p);
		
		Line[] newLines = new Line[lines.length+1];
		for(int i = 0; i<lines.length; i++)
			newLines[i] = lines[i];
		
		lines = newLines;
		constructGrid();
	}
	
	
	public void constructGrid(){
		for(int i = 0; i<waypoints.size() && !waypoints.isEmpty(); i++){
			for(int j = 1; j<waypoints.size(); j++){
				lines[i] = new Line((float) waypoints.get(i).getX(), (float) waypoints.get(i).getY(), (float) waypoints.get(j).getX(), (float) waypoints.get(j).getY());
			}
		}
		
		map = new LineMap(lines, new Rectangle(0,0, size,size));
	}
	
	public void printwaypoints(){
		for(Waypoint w : waypoints)
			System.out.println(w);
	}
		
}
