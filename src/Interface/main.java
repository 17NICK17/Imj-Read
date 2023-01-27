
package Interface;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utilities.Picture;
import utilities.Jobs;
import utilities.Utilities;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.JSlider;


/*****
 * 
 * @author Niccolò Mazzi
 * In questa classe sono presentate delle funzioni generate da WindowsBuilder per la gestione dell'interfaccia grafica
 *
 */
public class main {

	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField txtBaseFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
	}

	
	// Creo l'array condiviso in cui verranno poi caricate le immagini
	public static void createEmptyImageArrayList(List<Picture> immagini, String dir) {
		for (int i = 0; i < Utilities.countPics(dir); i++) {
			immagini.add(new Picture());
		}

	}



	// WindowsBuilder inizializza l'interfaccia

	private void initialize() {

		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(98, 207, 213));
		frame.setBounds(100, 100, 608, 449);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

		txtBaseFolder = new JTextField();
		txtBaseFolder.setEditable(false);
		txtBaseFolder.setBounds(172, 79, 181, 20);
		frame.getContentPane().add(txtBaseFolder);
		txtBaseFolder.setColumns(10);

		JButton btnLoadImages = new JButton("Load");
		btnLoadImages.setBounds(246, 243, 107, 20);
		frame.getContentPane().add(btnLoadImages);
		btnLoadImages.setEnabled(false);

		DefaultTableModel model = new DefaultTableModel();
		scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 282, 537, 97);
		frame.getContentPane().add(scrollPane);
		table = new JTable(model);
		scrollPane.setViewportView(table);

		model.addColumn("Path");

		JButton btnSelectDir = new JButton("Select Directory");
		btnSelectDir.setBounds(382, 79, 126, 20);
		frame.getContentPane().add(btnSelectDir);


		JLabel lblNewLabel = new JLabel("Choose the folder containing the pics you want to open");
		lblNewLabel.setBounds(173, 54, 281, 14);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Image Visualizer");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(225, 11, 191, 28);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblCoresNumber = new JLabel("Select Numer of cores");
		lblCoresNumber.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCoresNumber.setBounds(38, 168, 156, 20);
		frame.getContentPane().add(lblCoresNumber);
		
		JSlider slider = new JSlider();
		slider.setForeground(new Color(0, 0, 0));
		slider.setBackground(new Color(98, 207, 213));
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(1);
		slider.setMaximum(12);
		slider.setMinimum(1);
		slider.setBounds(204, 156, 304, 45);
		frame.getContentPane().add(slider);
		
		JLabel lblNewLabel_2 = new JLabel("written by Niccolò Mazzi");
		lblNewLabel_2.setBounds(420, 390, 172, 20);
		frame.getContentPane().add(lblNewLabel_2);

		btnSelectDir.addActionListener(new ActionListener() {


		// Questo metodo servirà a gestire il riempimento della tabella in cui saranno visualizzate le immagini

			public void actionPerformed(ActionEvent arg0) {
				model.setRowCount(0); // Azzero la tabella
				JFileChooser f = new JFileChooser();
				f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				f.showSaveDialog(null);
				txtBaseFolder.setText(f.getSelectedFile().toString());
				btnLoadImages.setEnabled(true);

				File[] images = Utilities.walkDir(f.getSelectedFile().toString()); // percorsi
				int i = 0;

				for (File f1 : images) {
					model.addRow(new String[] {f1.toString() });
					i = i + 1;
				}

			}
		});

		List<Picture> immagini = Collections.synchronizedList(new ArrayList<Picture>());
		btnLoadImages.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent arg0) {
				String dir = txtBaseFolder.getText();
				int coreNum = slider.getValue();
				File[] images = Utilities.walkDir(dir); // percorsi
				createEmptyImageArrayList(immagini, dir);

				Thread runForkJoinWorker = new Thread(() -> {
					Jobs forkJoinWorker = new Jobs(dir, immagini, images);
					forkJoinWorker.setCoreNum(coreNum);
					forkJoinWorker.start();
				});

				runForkJoinWorker.start();

			}
		});

		table.addMouseListener(new MouseAdapter() {
			//cliccando su in immagine nella tabella, essa verrà aperta
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					System.out.println(table.getModel().getValueAt(row, 0));
					Picture chosen = immagini.get(row); // Visualizing the requested image.
					if (!chosen.getPercorso().equals("null")) {
						chosen.draw();
					} else {
						Utilities.err("Image not loaded. Try again later", "E");
					}

				}
			}
		});

	}
}
