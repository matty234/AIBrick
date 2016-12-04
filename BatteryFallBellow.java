package brick.behavior;

import lejos.nxt.Battery;
import lejos.robotics.subsumption.Behavior;

public class BatteryFallBellow implements Behavior {
	
	public boolean takeControl() {
		return (Battery.getVoltage() < 1);
	}

	
	public void action() {
		System.out.println("Battery Too Low");		
		System.exit(0);
	}


	public void suppress() {
	}

}