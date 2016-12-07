package brick.listeners;

import brick.Brick;
import brick.RCCommand;
import brick.behavior.Collision;
import brick.behavior.FollowPath;
import brick.behavior.FreeRoam;
import brick.handler.EndOfPathHandler;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.UltrasonicSensor;

public class CustomSensorPortListener implements SensorPortListener {
	private final int LIGHT_THRESHOLD = 750;
	private final int COLLISION_THRESHOLD = 65;
	private final int CLAP_THRESHOLD = 30;
	private final UltrasonicSensor sensor;
	private long NEXT_SOUND_CHANGE = System.currentTimeMillis() + 2000l;

	public CustomSensorPortListener(UltrasonicSensor sensor) {
		this.sensor = sensor;
	}
	
	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		if(aSource == Brick.lightSensorPort) {
			if(aNewValue > LIGHT_THRESHOLD) {
				System.out.println("Going home!");
				FollowPath.addWayPoint(RCCommand.HOMEPOINT);
				FollowPath.SHOULD_TAKE_CONTROL = true;
				FreeRoam.SHOULD_TAKE_CONTROL = false;
				Brick.setEndOfPathHandler(new EndOfPathHandler() {
					@Override
					public void onEndOfPath(int fare) {
						LCD.clear();
						System.out.println("It's pretty dark so I'm going home.");
						System.exit(0);
					}
				});
			}
		} else if(aSource == Brick.soundSensorPort){
			if(aNewValue - aOldValue >= CLAP_THRESHOLD && System.currentTimeMillis() > NEXT_SOUND_CHANGE) {
				NEXT_SOUND_CHANGE = System.currentTimeMillis() + 2000l;
				System.out.println("Registered Clap");
			}
		} else if(aSource == Brick.ultrasonicSensorPort) {
			
			/*if(sensor.getDistance() <= COLLISION_THRESHOLD) Collision.SHOULD_TAKE_CONTROL = true;
			if(Collision.SHOULD_TAKE_CONTROL && sensor.getDistance() > COLLISION_THRESHOLD) Collision.SHOULD_TAKE_CONTROL = false;*/
		} else {
			
		}		
	}
}
