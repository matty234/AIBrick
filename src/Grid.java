import java.util.ArrayList;

public class Grid {
	private int size;
	private Line[] lines;
	private ArrayList<Destination> destinations;
	private LineMap map;
	private final Destination HOME = new Destination("HOME", new Waypoint(0.0,0.0));
	
	public Grid(int s){
		size = s;
		destinations = new ArrayList<Destination>;
		destinations.add(HOME);
	}
	
	public Grid(int s, ArrayList<Destination> wp){
		size = s;
		destinations = wp;
		constructGrid();
	}
	
	public LineMap getMap(){
		return map;
	}
	
	public void addDestination(Destination p){
		lines = new Line[destinations.size()];
		destinations.add(p);
		
		Line[] newLines = new Line[lines.size+1];
		for(int i = 0; i<lines.size; i++)
			newLines[i] = lines[i];
		
		lines = newLines;
		constructGrid();
	}
	
	
	public void constructGrid(){
		for(int i = 0; i<destinations.size() && !destinations.isEmpty(); i++){
			for(int j = 1; j<destinations.size(); j++){
				lines[i] = new Line(destinations.get(i).getX(), destinations.get(i).getY(), destinations.get(j).getX(), destinations.get(j).getY());
			}
		}
		
		map = new LineMap(lines, new Rectangle(0,0, size,size));
	}
	
	public void printDestinations(){
		for(Destination d : destinations)
			System.out.println(d);
	}
		
}
