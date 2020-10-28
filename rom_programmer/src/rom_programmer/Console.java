package rom_programmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rom_programmer.commands.Command;
import rom_programmer.commands.Commands;

public abstract class Console {
	private static BufferedReader buffer = null;
	private static String input = null;
	private static String[] args = null;
	private static boolean isRunning = true;
	private static InputStreamReader inputStreamReader = null;

	public static void start() throws IOException {
		while (isRunning) {
			System.out.print("> ");
			if (inputStreamReader == null) {
				inputStreamReader = new InputStreamReader(System.in);
			}
			if (buffer == null) {
				buffer = new BufferedReader(inputStreamReader);
			}

			// Get the input from the user and split it into args
			// buffer = new BufferedReader(inputStreamReader);
			input = buffer.readLine().toLowerCase();
			args = input.split(" ");

			// call the command execute request
			executeCommand(args[0]);
		}
	}

	private static void executeCommand(String cmd) throws IOException {
		if (!checkCmd(cmd)) {
			System.out.println("Please, enter a valid command!");
			return;
		}

		// pass the control to the requested command class
		call(Commands.valueOf(cmd.toUpperCase()).getCommand(), args);
		return;
	}

	private static boolean checkCmd(String cmd) {
		for (Commands c : Commands.values()) {
			if (c.name().equalsIgnoreCase(cmd))
				return true;
		}
		return false;
	}

	private static void call(Command cmd, String[] args) {
		cmd.execute(args);
		return;
	}

	public static void setRunning(boolean i) {
		isRunning = i;
	}

	public static void setProgParameters() {
		int offset;
		while (true) {
			try {
				System.out.print("Enter the address offset: ");
				if (inputStreamReader == null) {
					inputStreamReader = new InputStreamReader(System.in);
				}
				if (buffer == null) {
					buffer = new BufferedReader(inputStreamReader);
				}
				offset = Integer.parseInt(buffer.readLine());
				if (offset > 0 & offset < 2047) {
					break;
				}
				System.out.println("Please, enter an offset between 0 and 2047");
				if(Main.getOffset() != 0) Main.setOffset(0);

			} catch (NumberFormatException e) {
				System.out.println("Please, enter a valid offset.");
				Main.setOffset(0);
			} catch (IOException e) {
				System.out.println("Unexpected error!");
				return;
			}
		}
		Main.setOffset(offset);
	}
}
