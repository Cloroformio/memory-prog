package rom_programmer.commands;

import rom_programmer.commands.list.*;

public enum Commands {

	// Command list
	BYE(0, "Close the program", new Bye()),
	CLOSE(1, "Same as bye command", new Close()),
	HELP(2, "Get help", new Help()),
	CREDITS(3, "", new Credits()),
	LOAD(5, "", new Load()),
	SERIAL(4, "serial communication", new Serial());

	private Commands(int code, String desc, Command cmd) {
		this.id = code;
		this.description = desc;
		this.command = cmd;
	}

	private int id;
	private String description = null;
	private Command command = null;

	public String getDescription() {
		return this.description;
	}

	public Command getCommand() {
		return command;
	}

	public int getId() {
		return this.id;
	}

}
