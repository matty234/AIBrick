package brick.behavior;

import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public  class Collision implements Behavior{
	public static boolean SHOULD_TAKE_CONTROL = false;
	private Navigator navigator;
	
	public Collision(Navigator navigator) {
		this.navigator = navigator;
	}
	

	
	@Override
	public boolean takeControl() {
		return SHOULD_TAKE_CONTROL;
	}

	@Override
	public void action() {
		navigator.stop();
	}

	@Override
	public void suppress() {
		
	}


	
}
