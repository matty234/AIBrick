import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Brick {
	static BTConnection connection;
	static DataOutputStream dataOutputStream;
	static DataInputStream dataInputStream;
	
	public static void main(String[] args) throws IOException {
		Sound.beep();
		System.out.println("Mac Address: "+Bluetooth.getLocalAddress());
		btConnect();
		while(!Button.ESCAPE.isDown()) {
			btRead();
		}
		btClose();
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
	
	private static void btClose() throws IOException {
		dataInputStream.close();
		dataOutputStream.close();
		connection.close();

	}
}
