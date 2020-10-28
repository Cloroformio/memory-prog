package rom_programmer.commands.list;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import rom_programmer.Console;
import rom_programmer.Main;
import rom_programmer.ProgramBuffer;
import rom_programmer.SerialHandler;
import rom_programmer.commands.Command;
import rom_programmer.files.Filter;

public class Load extends JPanel implements Command {

	private static JFileChooser file;
	private static File asm_code;
	private static int result;
	private static ProgramBuffer buffer;
	private static FileReader fr;
	private static BufferedReader reader;
	private static int length = 0;
	public void execute(String[] args) {

		/*if (Main.getMode() != 1) {
			System.out.println("Please, select program mode first!");
			return;
		}*/
		
		/*if(!args[0].equals("")) {
			System.out.println("Invalid Syntax");
			return;
		}*/

		// open the file selection window
		openFileChooser();

		if (result == JFileChooser.CANCEL_OPTION) {
			return;
		}
		if (result == JFileChooser.APPROVE_OPTION) {
			asm_code = file.getSelectedFile();
		}

		// load all bytes into an array
		loadBytes();
		if(length == 0)
		{
			System.out.println("The file is empty!");
			return;
		}
		else System.out.println("File succefully loaded! Total bytes: " + length + " bytes");
		
		SerialHandler.write(0x10); 									  // checks if VPP voltage is right
		while (SerialHandler.getSelectedPort().bytesAvailable() < 1); // waits for the feedback
		if (SerialHandler.read() == 0x22) {
			System.out.println("Please, apply a minimum of 11.5V to a maximum of 12.5V to the VPP pin first!");
			buffer = null;
			return;
		}

		// if it's all right starts the programming sequence
		System.out.println("All right! Push the 'execute' button to start. WARNING: this operation will be irreversible!");
		while (SerialHandler.getSelectedPort().bytesAvailable() < 1);
		if (SerialHandler.read() != 0x56)
			return;

		int packets = 0;
		int remainingBytes = 0;
		if (length <= 32)
			packets = 1;
		if (length > 32) {
			for (int i = 0; i < length; i++) {
				if (i % 32 == 0) {
					packets++;
				}
			}
			packets--;
			remainingBytes = length - (packets * 32);
		}

		//---for debug only---
		System.out.println("Length: " + length);
		System.out.println("Packets to send: " + packets);
		System.out.print("Sending bytes");
		//--------------------

		int counter = 0;
		int response;
		int bytesToSend = 0, addOffset = 0;
		int currentByte;

		//sends the offset
		SerialHandler.write((byte) Main.getOffset() & 0xFF);
		SerialHandler.write((byte) (Main.getOffset() >> 8) & 0xFF);
		Main.delay(50);
		
		System.currentTimeMillis();
		while (true) {
			if (counter > packets) {
				SerialHandler.write(0x55);
				break;
			}
			if (counter == packets) {
				if (remainingBytes == 0)
					SerialHandler.write(0x55);
				else {
					SerialHandler.write(0x56);
					Main.delay(100);
					SerialHandler.write((byte) remainingBytes & 0xFF);
					bytesToSend = remainingBytes;
					addOffset = 0;
				}
			} 
			else
			{
				SerialHandler.write(32);
				bytesToSend = 32;
			}

			while (SerialHandler.getSelectedPort().bytesAvailable() < 1);
			response = SerialHandler.read();
			if (response == 0x21) {
				System.out.println("The packet is too large!");
				buffer = null;
				return;
			}
			if (response == 0x56)
				continue;
			
			Main.delay(5);
			// send the packet
			for (int i = 0; i < bytesToSend; i++) {
				currentByte = buffer.getBuffer()[i + addOffset];
				SerialHandler.write(currentByte);
			}
			counter++;
			addOffset += 32;

			while (SerialHandler.getSelectedPort().bytesAvailable() < 1);
			if (SerialHandler.read() != 0x56)
				return;
		}
		
		System.out.println();  //new line
		while (SerialHandler.getSelectedPort().bytesAvailable() < 1);
		if(SerialHandler.read() != 0x56)
		{
			 System.out.println("Unexpected Error!");
			 return;
		}
		
		System.out.println("EEPROM succefully programmed!");
		return;
	}

	public void openFileChooser() {
		file = new JFileChooser();
		file.setFileFilter(new Filter());
		result = file.showOpenDialog(Load.this);
	}

	private void loadBytes() {

		int lastValue;
		int counter = 0;
		String line = null;

		try {
			fr = new FileReader(asm_code);
			reader = new BufferedReader(fr);

			while (reader.readLine() != null)
				length++; // counts how many opcode there are
			if (length > 2047) {
				System.out.println("The program exceeds the memory capacity!");
				return;
			}
			buffer = new ProgramBuffer(length); // creates a new program buffer

			reader.close();
			fr.close();

			fr = new FileReader(asm_code);
			reader = new BufferedReader(fr);
			while ((line = reader.readLine()) != null) // checks if the assembly file is properly written
			{
				lastValue = Integer.decode("0x" + line);
				if (lastValue > 255) {
					System.out.println("There's something wrong with the hex list.");
					buffer = null;
					return;
				}
			}
			reader.close();
			fr.close();

			fr = new FileReader(asm_code);
			reader = new BufferedReader(fr);
			while ((line = reader.readLine()) != null) // reads all opcode and add them to the program buffer
			{
				lastValue = Integer.decode("0x" + line);
				buffer.getBuffer()[counter] = lastValue;
				counter++;
			}

			// some cleanup
			reader.close();
			fr.close();

			Console.setProgParameters();

			// start the programming sequence

		} catch (FileNotFoundException e) {
			System.out.println("Error while opening the file you selected.");
		} catch (IOException e) {
			System.out.println("Error while reading the file you selected.");
		}
	}
}
