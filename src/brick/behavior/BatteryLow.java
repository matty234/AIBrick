package brick.behavior;

import lejos.nxt.Battery;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class BatteryLow implements Behavior {
	private Navigator navigator;
	
	public BatteryLow(Navigator navigator) {
		this.navigator = navigator;
	}


	private final float LOW_BATTERY_THRESHOLD = (float) 0.2;
	@Override
	public boolean takeControl() {
		return Battery.getVoltage() < LOW_BATTERY_THRESHOLD;
	}

	@Override
	public void action() {
		navigator.stop();
		navigator.clearPath();
		System.out.println("Battery low");
		while(true);
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
