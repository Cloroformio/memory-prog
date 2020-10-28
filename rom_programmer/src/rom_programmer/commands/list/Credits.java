package rom_programmer.commands.list;

import rom_programmer.commands.Command;

public class Credits implements Command
{

	public void execute(String[] args) 
	{
		System.out.println("Hardware and software designed by Federico Lo Savio.");
		System.out.println("Github: https://github.com/Cloroformio");
		return;
	}
	
}
