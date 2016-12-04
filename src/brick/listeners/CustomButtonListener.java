package brick.listeners;

import brick.Brick;
import brick.behavior.FreeRoam;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class CustomButtonListener implements ButtonListener{

	@Override
	public void buttonPressed(Button b) {
		switch (b.getId()) {
		case Button.ID_ENTER:
			FreeRoam.SHOULD_TAKE_CONTROL = !FreeRoam.SHOULD_TAKE_CONTROL;
			break;
		case Button.ID_ESCAPE:
			System.out.println("Force stop...");
			Brick.connection.close();
			System.exit(0);
		default:
			break;
		}
		
	}

	@Override
	public void buttonReleased(Button b) {
		// TODO Auto-generated method stub
		
	}

}
