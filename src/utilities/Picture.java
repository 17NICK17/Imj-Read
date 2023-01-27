package utilities;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;




public class Picture implements Draw {
	private String path;
	private ImageIcon icon;
	
	//Si crea un'immagine vuota (null)
	public Picture() {		
		this.path = "null";
	}
	
	
	//costruttore di un oggetto immagine
	public Picture(String path) {
		this.path = path;
		this.icon = new ImageIcon(path);
	}
	
	
	//Metodo che definisce come l'immagine sarà visualizzata
	@Override
	public void draw() {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setSize(800,600);
		frame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setTitle(path);
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);

	}
	
	
	public String getPercorso() {
		return path;
	}

	
	@Override
	public String toString() {
		return this.path;
	}



}
