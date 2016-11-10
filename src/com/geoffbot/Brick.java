package com.geoffbot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import com.geoffbot.behaviours.FollowPath;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Brick implements RCCommand {
	static BTConnection connection;
	static DataOutputStream dataOutputStream;
	static DataInputStream dataInputStream;
	public static DifferentialPilot differentialPilot = new DifferentialPilot(65, 140, Motor.A, Motor.B);
	
	// Behaviours
	static FollowPath followPath = new FollowPath(null);
	
	public static void main(String[] args) throws IOException  {
		Sound.beep();
		System.out.println("--> Behaviour branch");
		System.out.println("Mac Address: "+Bluetooth.getLocalAddress());
		btConnect();

		Behavior[] behaviorList = {followPath};
		Arbitrator arb = new Arbitrator(behaviorList);
		arb.start();
		
		
		RobotPacket packet;
		while((packet = readRobotPacket()) != null) {
			if(packet.getMode() == Modes.NAVIGATE) followPath.setPathFromCommands(packet.commands);
			else if(packet.getMode() == Modes.HANDSHAKE) System.out.println("Handshake made");
			else System.out.println("A packet has been received");
		}
		
		btClose();
		
		System.out.println("Bluetooth connection closed. New connection (center)?");
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
			System.out.println("Finishing for IO err (unsafe exit");
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
