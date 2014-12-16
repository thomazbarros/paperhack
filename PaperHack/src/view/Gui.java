package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import org.apache.pdfbox.util.ExtensionFileFilter;

import controller.PaperHack;

public class Gui extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// Starts everything
	public static JFrame window = new JFrame(Configs.NAME);

	// Document Icons
	public static JLabel documentIconLabel1 = new JLabel();
	public static JLabel fileLabel1 = new JLabel();

	// Buttons for open files
	public static JButton file1Button = new JButton();
	public static JButton btn;

	// Scroll Panel and result text area
	public static JLabel resultLabel = new JLabel();
	public static JScrollPane resultScrollPanel = new JScrollPane();
	public static JTextArea resultTextArea = new JTextArea();

	// Similarity button
	public static JButton similarityButton = new JButton();

	// Creates a file chooser dialog for opening the input files
	public static JFileChooser fileChooser = new JFileChooser();
	// File filters
	public static FileFilter fileFilter = new ExtensionFileFilter(new String[] {
			"HTM", "HTML", "XML", "TXT", "PDF" },
			".htm, .html, .xml, .txt, .pdf");
	public JPopupMenu popup;

	// Our Graphic User Interface
	public Gui() {
		// Set native OS Look and feel
		String native_look_and_feel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(native_look_and_feel);
		} catch (InstantiationException e) {
			System.out.println("Error installing Look and Feel"
					+ e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Error installing Look and Feel"
					+ e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println("Error installing Look and Feel"
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("Error installing Look and Feel"
					+ e.getMessage());
		}

		// Document icon
		fileLabel1.setBounds(40, 20, 256, 256);

		fileLabel1.setBackground(Color.red);
		fileLabel1.setBackground(Color.red);
		BufferedImage document_icon_1 = null;
		try {
			document_icon_1 = ImageIO
					.read(new File(Configs.DOCUMENT_ICON_PATH));

		} catch (IOException e) {
			e.printStackTrace();
		}
		documentIconLabel1.setIcon(new ImageIcon(document_icon_1));

		// Add the document icons
		documentIconLabel1.setBounds(0, 20, 256, 256);

		// Add file filter to open file dialog window
		fileChooser.setFileFilter(fileFilter);

		// Sets the button 1 for opening files
		file1Button.setEnabled(true);
		file1Button.addActionListener(this);
		file1Button.setText(Configs.OPEN_FILE_1_TEXT);
		file1Button.setToolTipText(Configs.OPEN_FILE_TIP);
		file1Button.setBounds(30, 520, 90, 30);

		// Sets the result text field
		resultLabel.setBounds(30, 290, 540, 25);
		resultLabel.setText(Configs.RESULT_LABEL);
		resultScrollPanel.setBounds(30, 315, 540, 145);
		resultScrollPanel
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultScrollPanel
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultTextArea.setEditable(false);
		resultScrollPanel.setViewportView(resultTextArea);

		// Sets the button for similarity
		similarityButton.setEnabled(false);
		similarityButton.addActionListener(this);
		similarityButton.setText(Configs.SIMILARITY_BUTTON_TEXT);
		similarityButton.setToolTipText(Configs.SIMILARITY_BUTTON_TIP);
		similarityButton.setBounds(250, 520, 120, 30);

		// Adds things to the window
		window.add(fileLabel1);
		window.add(documentIconLabel1);
		window.add(file1Button);
		window.add(resultLabel);
		window.add(resultScrollPanel);
		window.add(similarityButton);
		window.add(this);

		// Wraps everything and fires up!
		window.setPreferredSize(new Dimension(Configs.WIDTH_SIZE,
				Configs.HEIGHT));
		window.setFocusTraversalKeysEnabled(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	@Override
	// Button actions selector
	public void actionPerformed(ActionEvent action) {
		if (action.getSource().equals(similarityButton)) {
			similarityAction();
		} else if (action.getSource().equals(file1Button)) {
			button1Action();
		}
	}

	// Action for similarity button
	private void similarityAction() {
		// Disables similarity button
		// similarityButton.setEnabled(true);
		// Re-enables file 1 button
		// file1Button.setEnabled(true);

		final JDialog d = new JDialog();
		JPanel p1 = new JPanel(new GridBagLayout());
		p1.add(new JLabel("Please Wait..."), new GridBagConstraints());
		d.getContentPane().add(p1);
		d.setSize(100, 100);
		d.setLocationRelativeTo(window);
		d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		d.setModal(true);

		Thread t = new Thread() {
			public void run() {
				for (int x = 0; x <= 100; x += 10) {
					SwingUtilities.invokeLater(new Runnable() {// do
																// swing
																// work
																// on
																// EDT
								public void run() {

								}
							});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				SwingUtilities.invokeLater(new Runnable() {// do swing
															// work on
															// EDT
							public void run() {
								d.dispose();
								PaperHack.doSearch();
							}
						});
			}
		};
		t.start();
		d.setVisible(true);

	}

	// Action for button 1
	private void button1Action() {
		// Open file_chooser dialog window
		int returnVal = fileChooser.showOpenDialog(Gui.this);
		// If user selects a file
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// Get file selected
			PaperHack.set_language_action(1);
			File file = fileChooser.getSelectedFile();
			String filePath = file.getPath();

			// Main.addFirstFile(filePath);
			PaperHack.addFile(filePath, true);
			similarityButton.setEnabled(true);

			resultTextArea.setText("");

		}
	}

	// Update the file labels
	public static void updateFileInfo(String text, int file_number) {
		if (file_number == 1) {
			fileLabel1.setText("<html>" + text + "</html>");
		}
	}

	// Update the text area
	public static void updateResults(String text) {
		resultTextArea.append(text + "\n");
	}

}
