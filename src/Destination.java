import lejos.robotics.navigation.Waypoint;

public class Destination { // maybe extend it to Waypoint
	private String name;
	private Waypoint coord;
	
	public Destination(String n, Waypoint wp){
		name = n;
		coord = wp;
	}
	
	public Destination(String n){
		name = n;
	}
	
	public void setWaypoint(Waypoint wp){
		coord = wp;
	}
	
	public double getX(){
		return coord.getX();
	}
	
	public double getY(){
		return coord.getY();
	}
	
	public Waypoint getWaypoint(){
		return coord;
	}
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String toString(){
		return "Name: " + getName() + " | ("+getX()+","+getY()+")";
	}
}
