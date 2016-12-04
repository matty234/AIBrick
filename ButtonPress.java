package brick.behavior;

import lejos.robotics.subsumption.Behavior;


public class ButtonPress implements Behavior {
	public static boolean SHOULD_TAKE_CONTROL = false;
	
	
	public void action() {
		System.exit(0);
	}
	public void suppress() {
		SHOULD_TAKE_CONTROL = false;
	}
	public boolean takeControl() {
		return SHOULD_TAKE_CONTROL;
	}
}