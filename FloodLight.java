package brick.behavior;

import lejos.nxt.LightSensor;

public class FloodLight {
	static public boolean lighttooLow;
	static public boolean shouldTakeControl = false;
	static LightSensor Ls;

	public void action() {
		if (lighttooLow) {
			Ls.setFloodlight(true);
		} else {
			Ls.setFloodlight(false);
		}
	}

	public void suppress() {
		shouldTakeControl = false;
	}

	public boolean takeControl() {
		return shouldTakeControl;
	}

}
