package brick;

import java.io.File;
import java.io.IOException;

import brick.behavior.Collision;
import brick.behavior.DisposableSetup;
import brick.behavior.FollowPath;
import brick.behavior.FreeRoam;
import brick.listeners.BluetoothListener;
import brick.listeners.CustomButtonListener;
import brick.listeners.UltrasonicListener;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SoundSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Brick implements RCCommand {
	public static BTConnection connection;

	public static DifferentialPilot differentialPilot = new DifferentialPilot(57, 120, Motor.A, Motor.B);
	public final static String IOERR = "Finishing for IO err";

	private static SensorPort lightSensorPort = SensorPort.S1, soundSensorPort = SensorPort.S4, ultrasonicSensorPort = SensorPort.S3;

	public static LightSensor lightSensor = new LightSensor(lightSensorPort);
	public static UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(ultrasonicSensorPort);
	public static SoundSensor soundSensor = new SoundSensor(soundSensorPort);

	private static UltrasonicListener ultrasonicListener = new UltrasonicListener(ultrasonicSensor);
	private static CustomButtonListener buttonListener = new CustomButtonListener();

	private static FollowPath followPathBehavior = new FollowPath();
	private static FreeRoam freeRoamBehavior = new FreeRoam();
	private static Collision collisionBehavior = new Collision();
	private static BluetoothListener bluetoothListener;

	public static void main(String[] args) throws IOException, DestinationUnreachableException, InterruptedException {
		Sound.beep();
		System.out.println("Mac: " + Bluetooth.getLocalAddress());
		btConnect();

		addListeners();
		Behavior[] behaviorList = { freeRoamBehavior, followPathBehavior, collisionBehavior };

		Arbitrator arb = new Arbitrator(behaviorList);
		arb.start();
		Button.waitForAnyPress();

	}

	private static void btConnect() {
		System.out.println("Waiting for connection");
		connection = Bluetooth.waitForConnection();
		Sound.twoBeeps();
		System.out.println("Connection made");
	}

	private static void addListeners() {
		ultrasonicSensorPort.addSensorPortListener(ultrasonicListener);
		Button.ESCAPE.addButtonListener(buttonListener);
		Button.ENTER.addButtonListener(buttonListener);
		bluetoothListener = new BluetoothListener(connection);
		new Thread(bluetoothListener).start();

	}
}
