package brick;

import java.io.IOException;

import brick.behavior.Collision;
import brick.behavior.FollowPath;
import brick.behavior.FreeRoam;
import brick.handler.PathHandler;
import brick.listeners.BluetoothListener;
import brick.listeners.CustomButtonListener;
import brick.listeners.CustomSensorPortListener;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SoundSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Brick implements RCCommand {
	public static BTConnection connection;

	private static DifferentialPilot differentialPilot = new DifferentialPilot(57, 120, Motor.A, Motor.B);
	private static Navigator navigator = new Navigator(differentialPilot);
	private static PoseProvider poseProvider = new OdometryPoseProvider(differentialPilot);


	public static SensorPort lightSensorPort = SensorPort.S1, soundSensorPort = SensorPort.S4, ultrasonicSensorPort = SensorPort.S3;

	public static LightSensor lightSensor = new LightSensor(lightSensorPort);
	public static UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(ultrasonicSensorPort);
	public static SoundSensor soundSensor = new SoundSensor(soundSensorPort);


	private static CustomButtonListener buttonListener = new CustomButtonListener();

	private static FollowPath followPathBehavior = new FollowPath(navigator, poseProvider);
	private static FreeRoam freeRoamBehavior = new FreeRoam(navigator, poseProvider);
	private static Collision collisionBehavior = new Collision(navigator);
	private static BluetoothListener bluetoothListener;

	private static CustomSensorPortListener customSensorPortListener = new CustomSensorPortListener(ultrasonicSensor);
	public static void main(String[] args) throws IOException, DestinationUnreachableException, InterruptedException {
		Sound.beep();
		setup();
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


	
	private static void setup() {
		System.out.println("Mac: " + Bluetooth.getLocalAddress());
		System.out.println("\n1) Calibration");
		System.out.println("  Place robot at (0, 0)");
		Button.ENTER.waitForPress();
		LCD.clear();
		System.out.println("2) Connection");
		System.out.println("  Connect to the Robot (TaxiBot)");
		btConnect();
		LCD.clear();
		addListeners();
		System.out.println("Setup complete...");		
	}
	
	private static void addListeners() {
		
		ultrasonicSensorPort.addSensorPortListener(customSensorPortListener);
		lightSensorPort.addSensorPortListener(customSensorPortListener);
		soundSensorPort.addSensorPortListener(customSensorPortListener);
		
		
		Button.ESCAPE.addButtonListener(buttonListener);
		Button.ENTER.addButtonListener(buttonListener);
		bluetoothListener = new BluetoothListener(connection);
		setPathHandler(new PathHandler() {
			@Override
			public void onFinishedPathSegment() {}
			
			@Override
			public void onEndOfPath(int fare) {
				try {
					bluetoothListener.writeRobotPacket(new RobotPacket(RCCommand.Modes.FARE, (byte) fare));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		new Thread(bluetoothListener).start();

	}
	
	public static void setPathHandler(PathHandler endOfPathHandler) {
		followPathBehavior.addOnEndOfPathHandler(endOfPathHandler);
	}
}
