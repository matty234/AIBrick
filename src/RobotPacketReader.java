import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class RobotPacketReader {
	static BTConnection connection;
	static DataOutputStream dataOutputStream;
	static DataInputStream dataInputStream;
	
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
