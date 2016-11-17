package brick.behavior;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class ClapperListener implements SensorPortListener {

	final static int TRAVEL_SPEED = 300;

	final static int SLOW_TRAVEL_SPEED = 30;

	final static int LIGHT_THRESHOLD = 30;

	final static SensorPort SOUND_SENSOR_PORT = SensorPort.S1;

	final static SensorPort LIGHT_SENSOR_PORT = SensorPort.S2;

	final static LightSensor LIGHT_SENSOR = new LightSensor(LIGHT_SENSOR_PORT);

	static long NEXT_LIGHT_CHANGE = 0;

	static DifferentialPilot PILOT;

	
	
	public ClapperListener() {

		SOUND_SENSOR_PORT.addSensorPortListener(new ClapperListener());
		LIGHT_SENSOR_PORT.addSensorPortListener(new ClapperListener());
		
	}
	
	
	

	@Override

	public void stateChanged(SensorPort sensorPort, int oldValue, int newValue) {
		if (sensorPort == LIGHT_SENSOR_PORT) {
			if (newValue <= LIGHT_THRESHOLD) {
				LIGHT_SENSOR.setFloodlight(true);
				PILOT.setTravelSpeed(SLOW_TRAVEL_SPEED);
			} else {
				LIGHT_SENSOR.setFloodlight(false);
				PILOT.setTravelSpeed(TRAVEL_SPEED);
			}

		} else { // Presume the change is from the sound sensor

			if (newValue - oldValue >= 30 && System.currentTimeMillis() > NEXT_LIGHT_CHANGE)

			{

				NEXT_LIGHT_CHANGE = System.currentTimeMillis() + 2000l;

				if (Motor.A.isMoving()) {

					PILOT.setTravelSpeed(0);

				} else {

					PILOT.setTravelSpeed(TRAVEL_SPEED);

				}

			}

		}

	}



}