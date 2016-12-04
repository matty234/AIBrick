package brick.listeners;

import brick.behavior.Collision;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.UltrasonicSensor;

public class UltrasonicListener implements SensorPortListener {
	private final int COLLISION_THRESHOLD = 65;
	private final UltrasonicSensor sensor;
	public UltrasonicListener(UltrasonicSensor sensor) {
		this.sensor = sensor;
	}
	
	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		System.out.println(sensor.getDistance());
		if(sensor.getDistance() <= COLLISION_THRESHOLD) hasCollision();
	}
	
	private void hasCollision() {
		Collision.SHOULD_TAKE_CONTROL = true;
	}
}
