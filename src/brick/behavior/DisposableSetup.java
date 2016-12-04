package brick.behavior;

import brick.Brick;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;

public class DisposableSetup {
	Navigator navigator = new Navigator(Brick.differentialPilot);
	public DisposableSetup() {
		navigator.goTo(0, 0, 0);
	}
}
