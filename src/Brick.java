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
	
	public static void main(String[] args) throws IOException, DestinationUnreachableException, InterruptedException  {
		Sound.beep();
		System.out.println("Mac Address: "+Bluetooth.getLocalAddress());
		btConnect();
		String str = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sit amet purus ornare, euismod odio a, consequat erat. Suspendisse orci magna, porta in fringilla ut, efficitur eu nulla. Aliquam auctor nibh id vestibulum sodales. In nisl nibh, pellentesque sit amet libero vitae, rhoncus ultricies diam. Quisque condimentum tincidunt tempor. Aenean egestas elit tortor, sed congue massa congue sed. Proin a nulla eu erat varius egestas at convallis risus. Etiam hendrerit id quam nec eleifend. Cras ultricies quis nisl non pulvinar. \r\n" + 
				"Phasellus tempor aliquam sapien nec lobortis. Mauris pharetra metus in massa tristique pharetra. Praesent nec elit eu erat bibendum cursus vel a mauris. Duis ut vestibulum ex. Aenean iaculis rutrum condimentum. Pellentesque eu sem porta, ullamcorper justo malesuada, tempor velit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam feugiat nunc at erat suscipit, pellentesque malesuada nisl faucibus. Pellentesque tristique lacus nisi, sed dapibus justo tincidunt vestibulum. Curabitur fermentum sapien vel est faucibus lacinia. Phasellus egestas libero felis, sed rutrum erat gravida eget. Aliquam imperdiet quam ac erat pharetra euismod. \r\n" + 
				"Duis pretium, justo quis lacinia convallis, neque lorem accumsan dui, quis scelerisque justo neque eget felis. Integer nec tortor eget nisi pulvinar consequat. Vivamus tristique suscipit pellentesque. Cras at leo ut lacus elementum consequat. Aenean efficitur urna eget eros tincidunt lobortis. Donec nec consequat metus. Mauris vitae luctus elit. Sed pharetra rutrum tortor. Sed interdum euismod orci eget malesuada. Pellentesque quis erat at lacus pharetra tempor vel at lorem. \r\n" + 
				"Pellentesque at poro ut diam imperdiet convallis. Nam ligula tortor, lacinia ac elit non, tempor imperdiet enim. Nullam dapibus ullamcorper tellus pretium lobortis. Phasellus imperdiet lorem et nulla tincidunt, nec consequat ligula dictum. Phasellus justo neque, placerat pharetra ornare in, condimentum ac purus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nunc iaculis massa magna, sodales aliquam odio dignissim quis. Donec sit amet fermentum augue, nec mollis magna. Suspendisse tincidunt ultricies diam eget faucibus. Integer vitae pharetra ligula. ";
				
		
		DifferentialPilot differentialPilot = new DifferentialPilot(57, 120, Motor.A, Motor.B);
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
