package rom_programmer;

public class ProgramBuffer 
{
	private static int[] buffer;
	
	//Create a new program buffer that contains the opcodes of the program
	//I want to load into the memory
	public ProgramBuffer(long length)
	{
		buffer = new int[(int) length];
	}
	
	public int[] getBuffer()
	{
		return buffer;
	}
}
