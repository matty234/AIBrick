package brick;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import brick.behavior.FollowPath;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Brick implements RCCommand, ButtonListener {
	static BTConnection connection;
	static DataOutputStream dataOutputStream;
	static DataInputStream dataInputStream;
	public static DifferentialPilot differentialPilot;
	private final static String IOERR = "Finishing for IO err";
	
	public static void main(String[] args) throws IOException, DestinationUnreachableException, InterruptedException  {
		Sound.beep();
		System.out.println("Mac Address: "+Bluetooth.getLocalAddress());
		btConnect();				
		
		
		Button.RIGHT.addButtonListener(new Brick()); 

		differentialPilot = new DifferentialPilot(57, 120, Motor.A, Motor.B);
		FollowPath followPath = new FollowPath();
		Behavior[] behaviorList = {followPath};
		Arbitrator arb = new Arbitrator(behaviorList);
		arb.start();
		
		
		RobotPacket packet;
		while((packet = readRobotPacket()) != null) {
			if(packet.getMode() == Modes.NAVIGATE) {
				ArrayList<Waypoint> waypoints = getWaypoints(packet.commands);
				followPath.addAllWayPoints(waypoints);
				FollowPath.SHOULD_TAKE_CONTROL = true;
			}
			else if(packet.getMode() == Modes.HANDSHAKE) System.out.println("Handshake made");
			else System.out.println("Packet received?");
		}
		btClose();

		System.out.println("Closed. New connection (center)?");
		int pressedButton = 0;
		while ((pressedButton = Button.waitForAnyPress(10000)) != 0){
			switch (pressedButton) {
			case Button.ID_ESCAPE:
				break;
			case Button.ID_ENTER:
				main(args);
			default:
				break;
			}
		}
		System.out.println("Good Bye!");
		
	}

	private static ArrayList<Waypoint> getWaypoints(byte[] commands) { //only for testing
		ArrayList<Waypoint> waypoints = new  ArrayList<>();
		for (int i = 0; i < commands.length; i++) {
			if(commands[i] == HOME) waypoints.add(HOMEPOINT);
			else if (commands[i] == OFFICE) waypoints.add(OFFICEPOINT);
			else if (commands[i] == PARK) waypoints.add(PARKPOINT);
			else if (commands[i] == SHOP) waypoints.add(SHOPPOINT);
			else;
		}
		return waypoints;
	}

	private static void btConnect() {
		System.out.println("Waiting for connection");
		connection = Bluetooth.waitForConnection();
		Sound.twoBeeps();
		System.out.println("Connection made");
		dataOutputStream = connection.openDataOutputStream();
		dataInputStream = connection.openDataInputStream();
	}
	
	private static void btRead() {
		int result = -1;
		try {
			result = dataInputStream.readInt();
			dataOutputStream.writeInt(result);
			dataOutputStream.flush();
		} catch (IOException e) {
			System.out.println(IOERR);
			Button.waitForAnyPress();
			System.exit(0);
		}
		System.out.println(result);
	}
	
	private static RobotPacket readRobotPacket() {
		byte result = 0x00;
		try {
			result = dataInputStream.readByte();
			if(result == Modes.HANDSHAKE) {
				btWrite(Modes.HANDSHAKE);
				return new RobotPacket(Modes.HANDSHAKE, null);
			} else if(result == Modes.EXIT) {
				return null;
			} else {
				int paramsLength = dataInputStream.readByte();
				byte[] params = new byte[paramsLength];
				for (int i = 0; i < paramsLength; i++) {
					byte readByte = dataInputStream.readByte();
					params[i] = readByte;
				}
				return new RobotPacket(result, params);
			} 
		} catch (IOException e) {
			System.out.println(IOERR+"(U/S Exit)");
			Button.waitForAnyPress();
			System.exit(0);
		}
		return null; // Assumed never reached
	}
	
	
	private static void btWrite(byte b) throws IOException{
		dataOutputStream.write(b);
		dataOutputStream.flush();
	}
	private static void btClose() throws IOException {
		dataInputStream.close();
		dataOutputStream.close();
		connection.close();
	}

	@Override
	public void buttonPressed(Button b) {
		FollowPath.SHOULD_TAKE_CONTROL = !FollowPath.SHOULD_TAKE_CONTROL;
	}

	@Override
	public void buttonReleased(Button b) {}
}
