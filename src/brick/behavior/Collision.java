package brick.behavior;

import brick.Brick;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;

public  class Collision implements Behavior{
	public static boolean SHOULD_TAKE_CONTROL = false;
	
	public void hasObstacle() {
		SHOULD_TAKE_CONTROL = true;
	}
	@Override
	public boolean takeControl() {
		return SHOULD_TAKE_CONTROL;
	}

	@Override
	public void action() {
		Brick.differentialPilot.quickStop();
		Sound.playNote(Sound.PIANO, 1000, 3000);
	}

	@Override
	public void suppress() {
		
	}


	
}
