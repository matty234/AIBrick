package brick.listeners;

import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class LightListener implements SensorPortListener {
	public LightListener() {

	}
	
	private boolean isNight() {
		return true;
	}

	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		// TODO Auto-generated method stub
		
	}
	
}	
