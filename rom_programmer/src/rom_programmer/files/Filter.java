package rom_programmer.files;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class Filter extends FileFilter{

	public boolean accept(File file) {
		if(file.isDirectory())
			return true;
		
		String fileName = file.getName().toLowerCase();
		return fileName.endsWith("txt");
	}

	@Override
	public String getDescription() {
		return ".txt";
	}

}
