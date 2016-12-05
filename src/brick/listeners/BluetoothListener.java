package brick.listeners;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import brick.Brick;
import brick.RCCommand;
import brick.RobotPacket;
import brick.RCCommand.Modes;
import brick.behavior.FollowPath;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.Waypoint;

public class BluetoothListener implements Runnable, RCCommand {
	private static DataOutputStream dataOutputStream;
	private static DataInputStream dataInputStream;
	
	public BluetoothListener(BTConnection btConnection) {
		dataOutputStream = btConnection.openDataOutputStream();
		dataInputStream = btConnection.openDataInputStream();
	}

	@Override
	public void run() {
		RobotPacket packet;
		while((packet = readRobotPacket()) != null) {
			if(packet.getMode() == Modes.NAVIGATE) {
					ArrayList<Waypoint> waypoints = getWaypoints(packet.commands);
					FollowPath.addAllWayPoints(waypoints);
					FollowPath.SHOULD_TAKE_CONTROL = true;
					FollowPath.SHOULD_BREAK = true;
				}
			else if(packet.getMode() == Modes.HANDSHAKE) System.out.println("Handshake made");
			else System.out.println("Packet received?");
		}
	}
	
	
	public void writeRobotPacket(RobotPacket packet) throws IOException {
		try{
			btWrite(packet.getMode());
			btWrite((byte) packet.getLength());
			for (int i = 0; i < packet.getLength(); i++) {
				btWrite(packet.getCommands()[i]);
			}
		} catch (IOException e) {
			System.out.println(Brick.IOERR+" (U/S Exit)");
			Button.waitForAnyPress();
		}
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
			System.out.println(Brick.IOERR+" (U/S Exit)");
			Button.waitForAnyPress();
			System.exit(0);
		}
		return null; 
	}
	
	private static void btWrite(byte b) throws IOException{
		dataOutputStream.write(b);
		dataOutputStream.flush();
	}
	
	private static ArrayList<Waypoint> getWaypoints(byte[] commands) {
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
}
