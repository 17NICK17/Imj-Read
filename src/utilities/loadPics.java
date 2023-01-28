package utilities;

import java.io.File;
import java.util.List;
import java.util.concurrent.RecursiveTask;


// Classe     : loadPics
// Obiettivo  : Definisce un Task, ossia un paragima di tipo fork-join per effettuare 
//              il caricamento delle immagini in memoria




public class loadPics extends RecursiveTask<Boolean> {	
	private static final long serialVersionUID = 1L;
	private int threadID;
	private List<Picture> pictures;
	private int startIndex;
	private int endIndex;
	private File[] baseFolder;
	


	// Method  : loadPics
	// Obiettivo : costruttore della classe loadPics
	public loadPics(int threadID, List<Picture> pictures, File[] baseFolder, int startIndex, int endIndex) {
		this.threadID = threadID;
		this.pictures = pictures;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.baseFolder = baseFolder;

	}
	


	// Method  	 : compute
	// Obiettivo : Metodo utilizzato per caricare le immagini una ad una in memoria (variabile "pictures")
	@Override
	protected Boolean compute() {
		for (int k = startIndex; k < endIndex; k++) {
			this.pictures.set(k, new Picture(baseFolder[k].getAbsolutePath()));
		}
		System.out.println("Thread " + threadID + " Finito");		//Dbg string.
		return true;
	}

}
