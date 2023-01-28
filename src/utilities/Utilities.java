package utilities;

import java.io.File;


import javax.swing.JOptionPane;


// Classe    : Utilities
// Obiettivo   : In questa classe vengono implementati alcuni metodi volti ad effettuare print 
//'--------------------------------------------------------------------------------------- 
public class Utilities {


	public static int countPics(String dir) {
		File[] list = walkDir(dir);
		return list.length;
	}

	public static void info(String message, String titleBar) {
		JOptionPane.showMessageDialog(null, message, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static int getProcessorNum() {
		return Runtime.getRuntime().availableProcessors();
	}
	
	
	public static void err(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.ERROR_MESSAGE);
	}	
	
	public static File[] walkDir(String dir){
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
		
	}
}
