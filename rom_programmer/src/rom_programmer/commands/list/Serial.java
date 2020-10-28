package rom_programmer.commands.list;

import rom_programmer.Main;
import rom_programmer.SerialHandler;
import rom_programmer.commands.Command;

public class Serial implements Command {

	private static int feedback = 0xFF;
	private static int[] asm = {0x01, 0x02, 0x56, 0x1B, 0x5F, 0x0C, 0xDF, 0x1A};

	public void execute(String[] args) {
		try {
			switch (args[1]) {
			case "-p":
				SerialHandler.getAvailablePorts();
				SerialHandler.selectPort(args[2]);
				return;
			case "-c":
				if (args[2].equals("all")) {
					SerialHandler.getAvailablePorts();
					SerialHandler.showAvailablePorts();
					return;
				}
				System.out.println("Invalid synthax!");
				return;

			case "--getport":
				System.out.println(SerialHandler.getSelectedPortName());
				return;

			case "--setmode":
				if (args[2].equals("program") | args[2].equals("p")) {
					SerialHandler.write(0x01);
					while(SerialHandler.getSelectedPort().bytesAvailable() < 1);
					feedback = SerialHandler.read();
					if (feedback == 0x20) {
						System.out.println("Mode set to program.");
						return;
					}
					System.out.println("Error while setting mode to program, please check the board.");
					return;
				}
				if (args[2].equals("erase") | args[2].equals("e")) {
					SerialHandler.write(0x02);
					while(SerialHandler.getSelectedPort().bytesAvailable() < 1);
					feedback = SerialHandler.read();
					if (feedback == 0x20) {
						System.out.println("Mode set to erase.");
						return;
					}
					System.out.println("Error while setting mode to erase, please check the board.");
					return;
				}

			case "--test":
				if (args[2].equals("read")) {
					SerialHandler.write(0x04);
					while(SerialHandler.getSelectedPort().bytesAvailable() < 1);
					System.out.println(SerialHandler.read());
					return;
				}
				if (args[2].equals("transfer")) {
					SerialHandler.write(0x56);
					System.out.println("Sent the synchro byte!");
					while(SerialHandler.getSelectedPort().bytesAvailable() < 1);
					System.out.println("Received the synchro byte!");
					if(SerialHandler.read() == 0x56)
					{
						Main.delay(50);
						for(int i = 0; i < 8; i++)
						{
							SerialHandler.write(asm[i]);
							System.out.println("Sent " + i + " bytes to the arduino");
						}
						while(SerialHandler.getSelectedPort().bytesAvailable() < 1);
						if(SerialHandler.read() == 0x56)
						{
							System.out.println("Done!");
						}
						while(SerialHandler.getSelectedPort().bytesAvailable() < 8);
						for(int i = 0; i < 8; i++)
						{
							System.out.println(SerialHandler.read());
						}
						return;
					}
					return;
				}
				System.out.println("Invalid synthax!");
				return;

			case "--getmode":
				SerialHandler.write(0x04);
				while (SerialHandler.getSelectedPort().bytesAvailable() < 1);
				feedback = SerialHandler.read();
				System.out.print("Current mode: ");
				if (feedback == 1) {
					System.out.println("program");
					return;
				} else if (feedback == 2) {
					System.out.println("erase");
					return;
				} else {
					System.out.println("Unrecognized mode, please check the board.");
					return;
				}

			default:
				System.out.println("Please, enter a valid command.");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid synthax!");
			return;
		}
		return;
	}
}
