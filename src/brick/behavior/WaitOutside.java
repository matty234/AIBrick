package brick.behavior;

import java.io.File;

import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public class WaitOutside implements Behavior {
	public static boolean SHOULD_TAKE_CONTROL = false;;

	@Override
	public boolean takeControl() {
		return SHOULD_TAKE_CONTROL;
	}

	@Override
	public void action() {
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
