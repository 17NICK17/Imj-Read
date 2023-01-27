package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;






public class Jobs {	
	private static String dir;
	private static List<Picture> immagini;
	private static File[] images;
	private static int numCore = Utilities.getProcessorNum();
	
	// Metodo    : ParallelProcesses
	// Obiettivo : Semplice costruttore della classe
	public Jobs(String dir, List<Picture> immagini, File[] images) {
		Jobs.dir = dir;
		Jobs.immagini = immagini;
		Jobs.images = images;
	}
	
	// Metodo    : start
	// Obiettivo : Metodo che suddivide il lavoro in più task. Si suddivide in metodo sequenziale (se un solo core
	//             viene utilizzato) o in caso parallelo se vengono utilizzati più cores.
	public void start() {		
		if (numCore == 1) {
			loadPics sequentialTask = new loadPics(0, immagini, images, 0, Utilities.countPics(dir));
			long startTime = System.currentTimeMillis();
			sequentialTask.fork();
			sequentialTask.join();
			long endTime = System.currentTimeMillis();
			Utilities.info("Every image has been loaded in " + (endTime - startTime), "Completed.");
			return;
		}
		
		// si valuta su quante immagini dovrà lavorare ogni processore
		int totale = Utilities.countPics(dir);
		int chunk = totale / numCore; 															
		int resto = totale % numCore; 															

		ArrayList<ForkJoinTask<Boolean>> C = new ArrayList<ForkJoinTask<Boolean>>(numCore);		
	
		// Si suddivide effettivamente il lavoro
		int threadID = 0;
		for (int i = 0; i < numCore; i++) {
			// For core 1...N-1 i assign a chunk.
			int start = i * chunk;
			int end = (i + 1) * chunk;
			if (i == numCore - 1) {													
				C.add(new loadPics(threadID, immagini, images, start, end + resto));		
				threadID++;
			}
			else {
				//if N-1 then work + reminder.
				C.add(new loadPics(threadID, immagini, images, start, end));			
				threadID++;				
			}
		}
		
		//inizio il task
		long startTime = System.currentTimeMillis();	    
		for (ForkJoinTask<Boolean> task : C) {
			task.fork();
		}
		
		//aspetto la fine del task
		for (ForkJoinTask<Boolean> task : C) {				
			task.join();
		}

		long endTime = System.currentTimeMillis();			// <- timing.		
		Utilities.info("Every image has been loaded in " + 	// <- result.
					  (endTime - startTime),
					  "Completed.");	 
	}
	
	
	//Seleziono il numero di core
	public void setCoreNum(int coresNum) {
		Jobs.numCore = coresNum;
	}

	


	



}
