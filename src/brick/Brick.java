package brick;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class Brick implements RCCommand {
	static BTConnection connection;
	static DataOutputStream dataOutputStream;
	static DataInputStream dataInputStream;
	public static DifferentialPilot differentialPilot;
	public static void main(String[] args) throws IOException, DestinationUnreachableException, InterruptedException  {
		Sound.beep();
		System.out.println("Mac Address: "+Bluetooth.getLocalAddress());
		btConnect();				
		
		differentialPilot = new DifferentialPilot(57, 120, Motor.A, Motor.B);
		Navigator navigator = new Navigator(differentialPilot);
		PoseProvider poseProvider = new OdometryPoseProvider(differentialPilot);
		
		ShortestMultipointPathFinder  finder = new ShortestMultipointPathFinder(LINEMAP);
		
		while(true) {
			RobotPacket packet = readRobotPacket();
			if(packet.getMode() == Modes.NAVIGATE) {
				navigator.followPath();
				Waypoint[] waypoints = getLocations(packet.commands);
				Path[] paths = finder.findPaths(poseProvider.getPose(), waypoints); // Could throw dest unreachable
				for (int i = 0; i < paths.length; i++) {
					navigator.followPath(paths[i]);
					navigator.waitForStop();
					Thread.sleep(500); // Stopped @ path end
				}
			}
		}
		//btClose();
	}

	private static Waypoint[] getLocations(byte[] commands) { //only for testing
		ArrayList<Waypoint> waypoints = new  ArrayList<>();
		for (int i = 0; i < commands.length; i++) {
			if(commands[i] == HOME) waypoints.add(HOMEPOINT);
			else if (commands[i] == OFFICE) waypoints.add(OFFICEPOINT);
			else if (commands[i] == PARK) waypoints.add(PARKPOINT);
			else if (commands[i] == SHOP) waypoints.add(SHOPPOINT);
			else;
		}
		return waypoints.toArray(new Waypoint[waypoints.size()]);
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
			System.out.println("Finishing because IO error occurred");
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
			System.out.println("Finishing because IO error occurred");
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
}
