package rom_programmer.commands.list;

import rom_programmer.commands.Command;

public class Help implements Command {

	public void execute(String[] args) {
		System.out.println("---------------------------COMMAND LIST---------------------------");
		if (args.length == 1) {
			serialHelp();
			assemblyLoad();
			getSpace();
			System.out.println(
					"If you need more help please check my github repository below: https://github.com/Cloroformio/eeprom-programmer");
		}

		System.out.println(); // final space
		return;
	}

	private void serialHelp() {
		getSpace();
		System.out.println("------------------------Serial communication------------------------");
		System.out.println("-> serial -p <port>			   'selects the input port for the communication");
		System.out.println("-> serial -c <port>			   'checks if the input serial port is open");
		System.out.println("-> serial -c all			   'lists all available serial ports");
		System.out.println("-> serial --setmode program    'sets the board mode to program mode'");
		System.out.println("-> serial --setmode erase      'sets the board mode to erase mode'");
		System.out.println("-> serial --getmode            'gets the current mode inserted'");
		System.out.println("-> serial --getport   		   'gets the current opened serial port'");
		System.out.println("-> serial --test               'test if serial communication works correctly'");

	}

	private void assemblyLoad() {
		getSpace();
		System.out.println("------------------------Assembly code loading------------------------");
		System.out.println("-> load                        'loads the assembly code (only .bin files)'");
	}

	private void getSpace() {
		System.out.print("\n\n");
	}
}
