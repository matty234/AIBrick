package brick.listeners;

import brick.NotifyWait;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;

public class SoundListener implements SensorPortListener {
	private long NEXT_LIGHT_CHANGE = 0;
	private final int CLAP_THRESHOLD = 30;
	private NotifyWait notifyWait;
	public SoundListener(NotifyWait notifyWait) {
		this.notifyWait = notifyWait;
	}
	
	@Override
	public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		
		if(aNewValue - aOldValue >= CLAP_THRESHOLD && System.currentTimeMillis() > NEXT_LIGHT_CHANGE) {
			NEXT_LIGHT_CHANGE = System.currentTimeMillis() + 2000l;
			hasClapped();
		}
	}

	private void hasClapped() {
		notifyWait.pushNotify();
	}
}
