package rom_programmer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fazecast.jSerialComm.SerialPort;

public abstract class SerialHandler {
	private static SerialPort[] ports = null;
	private static SerialPort openPort = null;

	private static OutputStream out = null;
	private static InputStream in = null;

	private static int readBuffer;

	public static void getAvailablePorts() {
		// get all available ports
		ports = SerialPort.getCommPorts();
		return;
	}

	public static void selectPort(String port) {
		if (openPort != null)
			openPort.closePort(); // close previous open port
		for (SerialPort p : ports) {
			if (p.getSystemPortName().toLowerCase().equals(port.toLowerCase())) {
				if (!p.openPort()) {
					System.out.println("Error in opening the port.");
					openPort = null;
					return;
				}
				openPort = p;
				openPort.openPort();
				openPort.setComPortParameters(9600, 8, 1, 0); // set default parameters
				openPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2000, 0);
				break;
			}
		}
		if (openPort == null) {
			System.out.println("Cannot find the serial port you entered.");
			return;
		}
		
		System.out.println("DONE! Port succefully selected.");
		
		//retrieve some useful information from the board
		Main.delay(500);
		Main.getModeFromBoard();
		return;
	}

	public static String getSelectedPortName() {
		if (openPort != null) {
			return openPort.getSystemPortName().toString();
		}
		return "No port selected.";
	}
	
	public static SerialPort getSelectedPort() {
		if (openPort != null) {
			return openPort;
		}
		return null;
	}

	public static void showAvailablePorts() {
		for (SerialPort p : ports) {
			System.out.println(p.getSystemPortName().toString());
		}
		return;
	}

	public static void write(int i) {
		try {
			out = openPort.getOutputStream();
			out.write(i);
			out.flush();
			if (out != null)
				out.close();
		} catch (NullPointerException e) {
			System.out.println("Please, select a serial port.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void closePort() {
		if (openPort != null) {
			openPort.closePort();
		}
	}

	public static int read() {
		try {
			in = openPort.getInputStream();
			readBuffer = in.read();
			in.close();
		} catch (IOException e) {
			System.out.println("Please, connect the board.");
			return 0xFF;
		}
		catch (NullPointerException e)
		{
			System.out.println("Please, connect the board.");
			return 0xFF;
		}
		return readBuffer;
	}
}
