package brick.behavior;
import java.util.ArrayList;

import brick.Grid;
import brick.RobotPacket;
import brick.RobotPacketReader;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.subsumption.Behavior;

public class FreeRoam implements Behavior {
	private TouchSensor ts;
	private Navigator navigator;
	private PoseProvider pp;
	private boolean passControl = false;
	private Grid grid;
//	private ShortestMultipointPathFinder finder;
	
	public FreeRoam(SensorPort sp, DifferentialPilot p, ArrayList<Waypoint> wp){
		ts = new TouchSensor(sp);
		navigator = new Navigator(p);
		pp = new OdometryPoseProvider(p);
		grid = new Grid(1000, wp);
	}
	
	
	
	@Override
	public boolean takeControl() {
		return !passControl;
	}

	@Override
	public void action() {
		while(!passControl){
			RobotPacket packet = RobotPacketReader.readRobotPacket();
			
			if(packet.getMode() == 0x04){
				
			}
			
		}
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	
	

}
