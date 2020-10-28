package rom_programmer;

import java.io.IOException;
import java.util.Scanner;

public class Main 
{	
	private static long lastTime;
	private static Scanner enter;
	private static int offset = 0;
	private static int currentMode;
	private static long time;
	
	public static void main(String[] args) throws IOException
	{
		splash();
		
		//Start the console
		Console.start();
	}
	
	static private void splash() throws IOException
	{
		//Show splash screen
		System.out.println("EEPROM programmer board software");
		
		//wait 1 seconds before continuing...completely useless but cool
		delay(1000);
		
		System.out.println("Hi! type help for the commands list");
		System.out.println();
		return;
	}
	
	public static void pressEnterToContinue()
	{
		System.out.println("Press enter to confirm...");
		enter = new Scanner(System.in);
		enter.nextLine();
	}
	
	public static void setOffset(int offset)
	{
		Main.offset = offset;
	}
	public static int getOffset()
	{
		return offset;
	}
	public static void getModeFromBoard()
	{
		//retrieve the mode from the board
		SerialHandler.write(0x04);
		while (SerialHandler.getSelectedPort().bytesAvailable() < 1);
		currentMode = SerialHandler.read();
	}
	
	public static int getMode()
	{
		return currentMode;
	}
	public static void delay(int t)
	{
		//wait
		time = System.currentTimeMillis();
		while(System.currentTimeMillis() - time < t);
	}
}
