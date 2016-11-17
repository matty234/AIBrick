package brick.behavior;

import brick.Brick;
import lejos.robotics.subsumption.Behavior;

public class Clap implements Behavior {
	public static boolean SHOULD_TAKE_CONTROL = false;
	public static boolean NEXT_LOUD_NOISE = false;
	
	public void action() {
		if(NEXT_LOUD_NOISE){			
			Brick.differentialPilot.forward();
			NEXT_LOUD_NOISE = false;
		} else {
			Brick.differentialPilot.stop();

		}
	}
	
	public void suppress() {
		SHOULD_TAKE_CONTROL = false;
	}

	public boolean takeControl() {
		return SHOULD_TAKE_CONTROL;
	}
}