package brick.behavior;

import brick.Brick;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

public class FollowPath implements Behavior {
	Path path;
	Navigator navigator;
	

	public static boolean SHOULD_TAKE_CONTROL;

	{
		navigator = new Navigator(Brick.differentialPilot);
		SHOULD_TAKE_CONTROL = false;
	}
	
	
	public FollowPath(Path path) {
		this.path = path;
	}
	
	public FollowPath() {}
	
	@Override
	public boolean takeControl() {
		return (path != null) && SHOULD_TAKE_CONTROL;
	}

	@Override
	public void action() {
		if(navigator.pathCompleted()) {
			navigator.setPath(path);
			navigator.followPath();
		} else if(!navigator.isMoving()) {
			navigator.followPath();
		} 
	}

	@Override
	public void suppress() {
		navigator.stop();
		SHOULD_TAKE_CONTROL = false;
	}
	
	public Path getPath() {
		return path;
	}

	public Navigator getNavigator() {
		return navigator;
	}
	
	/**
	 * Sets the current path for the {@link Navigator} and allows the behaviour to take control.
	 */
	public void setPath(Path path) {
		SHOULD_TAKE_CONTROL = true;
		this.path = path;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
	

}