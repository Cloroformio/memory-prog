package rom_programmer.commands.list;

import rom_programmer.Console;
import rom_programmer.SerialHandler;
import rom_programmer.commands.Command;

public class Bye implements Command{

	public void execute(String[] args) 
	{
		Console.setRunning(false);
		SerialHandler.closePort();
		System.out.println("Bye!");
		return;
	}

}
