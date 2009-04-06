
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingUtilities;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class GuiPdcConverter extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JTabbedPane tabbedPanel;
	private JPanel pnlDirectories;
	private JButton btnebookFile;
	private JLabel lblEbook;
	private JButton btnStop;
	private JTextArea jTextArea1;
	private JPanel pnlAbout;
	private JButton btnStart;
	private JSpinner spnrDelay;
	private JLabel lblDelay;
	private JSpinner spnrToPage;
	private JLabel lblToPage;
	private JSpinner spnrFromPage;
	private JLabel lblFromPage;
	private JLabel lblOutput;
	private JButton btnOutputFolder;
	private JLabel lblPdcViewer;
	private JButton btnPdcViewer;
	private JPanel pnlmain;

	//private Robot robot;
	private Scanner scanner;
	private Thread scanThread;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuiPdcConverter inst = new GuiPdcConverter();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public GuiPdcConverter() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			getContentPane().setLayout(null);
			this.setTitle("PDC Scanner");
			this.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					stop();
				}
			});
			{
				tabbedPanel = new JTabbedPane();
				getContentPane().add(tabbedPanel);
				tabbedPanel.setBounds(0, 6, 312, 237);
				{
					pnlDirectories = new JPanel();
					tabbedPanel.addTab("Main", null, pnlDirectories, null);
					pnlDirectories.setLayout(null);
					pnlDirectories.setPreferredSize(new java.awt.Dimension(307, 212));
					{
						lblFromPage = new JLabel();
						pnlDirectories.add(lblFromPage);
						lblFromPage.setText("From Page");
						lblFromPage.setBounds(23, 28, 68, 21);
					}
					{
						SpinnerModel model = new SpinnerNumberModel(1, 1, 1000, 1);
						spnrFromPage = new JSpinner(model);
						pnlDirectories.add(spnrFromPage);
						spnrFromPage.setBounds(187, 28, 89, 20);

						JFormattedTextField tf =
							((JSpinner.DefaultEditor)spnrFromPage.getEditor()).getTextField();
						tf.setEditable(false);
						tf.setBackground(Color.white);

						spnrFromPage.addChangeListener(new ChangeListener() {

							public void stateChanged(ChangeEvent evt) {
								checkSpinners();
							}


						});
					}
					{
						lblToPage = new JLabel();
						pnlDirectories.add(lblToPage);
						lblToPage.setText("To Page");
						lblToPage.setBounds(23, 61, 54, 18);
					}
					{
						SpinnerModel model = new SpinnerNumberModel(5, 1, 1000, 1);
						spnrToPage = new JSpinner(model);
						pnlDirectories.add(spnrToPage);
						spnrToPage.setBounds(187, 59, 89, 20);

						JFormattedTextField tf =
							((JSpinner.DefaultEditor)spnrToPage.getEditor()).getTextField();
						tf.setEditable(false);
						tf.setBackground(Color.white);
					}
					{
						lblDelay = new JLabel();

						pnlDirectories.add(lblDelay);
						lblDelay.setText("Time to Scan a Page (1/100 S)");
						lblDelay.setBounds(23, 102, 149, 14);
					}
					{
						SpinnerModel model = new SpinnerNumberModel(200, 50, 1000, 10);
						spnrDelay = new JSpinner(model);
						pnlDirectories.add(spnrDelay);
						spnrDelay.setBounds(187, 96, 89, 20);

						JFormattedTextField tf =
							((JSpinner.DefaultEditor)spnrDelay.getEditor()).getTextField();
						tf.setEditable(false);
						tf.setBackground(Color.white);
					}
					{
						btnStart = new JButton();
						pnlDirectories.add(btnStart);
						btnStart.setText("Start");
						btnStart.setBounds(23, 151, 124, 37);
						btnStart.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								start();
							}


						});
					}
					{
						btnStop = new JButton();
						pnlDirectories.add(btnStop);
						btnStop.setText("Stop");
						btnStop.setEnabled(true);
						btnStop.setBounds(162, 151, 124, 37);
						btnStop.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								btnStopActionPerformed(evt);
							}
						});
					}
				}
				{
					pnlmain = new JPanel();
					tabbedPanel.addTab("Folders", null, pnlmain, null);
					pnlmain.setLayout(null);
					pnlmain.setPreferredSize(new java.awt.Dimension(382, 254));
					{
						btnPdcViewer = new JButton();
						pnlmain.add(btnPdcViewer);
						btnPdcViewer.setText("pdc Viewer");
						btnPdcViewer.setBounds(16, 11, 273, 24);
						btnPdcViewer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								String fileName=choose(ChooseType.PDC_VIEWER_DIR, lblPdcViewer.getText());
								if (fileName!=null)lblPdcViewer.setText(fileName);							}
						});
					}
					{
						lblPdcViewer = new JLabel();
						pnlmain.add(lblPdcViewer);
						lblPdcViewer.setText("C:\\Program Files\\Lizard Safeguard PDF Viewer");
						lblPdcViewer.setBounds(16, 40, 273, 20);
						lblPdcViewer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					}
					{
						btnebookFile = new JButton();
						pnlmain.add(btnebookFile);
						btnebookFile.setText("Choose pdc Ebook ");
						btnebookFile.setBounds(16, 72, 273, 22);
						btnebookFile.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								String fileName=choose(ChooseType.EBOOK, lblEbook.getText());
								if (fileName!=null)lblEbook.setText(fileName);
							}
						});
					}
					{
						lblEbook = new JLabel();
						pnlmain.add(lblEbook);
						lblEbook.setText("C:\\nadyelfikr\\ebook\\ebook.pdc");
						lblEbook.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						lblEbook.setBounds(12, 99, 273, 22);
					}
					{
						btnOutputFolder = new JButton();
						pnlmain.add(btnOutputFolder);
						btnOutputFolder.setText("Output Folder");
						btnOutputFolder.setBounds(12, 140, 273, 22);
						btnOutputFolder.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								String fileName=choose(ChooseType.OUTPUT, lblOutput.getText());
								if (fileName!=null)lblOutput.setText(fileName);
							}
						});
					}
					{
						lblOutput = new JLabel();
						pnlmain.add(lblOutput);
						lblOutput.setText("C:\\nadyelfikr\\images");
						lblOutput.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
						lblOutput.setBounds(12, 173, 272, 23);
					}
				}
				{
					pnlAbout = new JPanel();
					tabbedPanel.addTab("About", null, pnlAbout, null);
					pnlAbout.setLayout(null);
					{
						jTextArea1 = new JTextArea();
						pnlAbout.add(jTextArea1);
						jTextArea1.setText("         \u0642\u0631\u0623\u062a \u0644\u0643\n\n    \u0646\u0627\u062f\u064a \u0627\u0644\u0641\u0643\u0631 \u0627\u0644\u0639\u0631\u0628\u064a\n\n   www.nadyelfikr.com");
						jTextArea1.setOpaque(false);
						jTextArea1.setBounds(56, 45, 191, 113);
						jTextArea1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					}
				}
			}
			pack();
			this.setSize(328, 279);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void checkSpinners() {
		int from=(Integer)spnrFromPage.getValue();
		int to=(Integer)spnrToPage.getValue();
		if(from > to){
			spnrToPage.setValue(new Integer(from));
		}
	}



	/**
	 * 
	 * @return x0,y0,width,height
	 */


	enum ChooseType{EBOOK,OUTPUT,PDC_VIEWER_DIR};
	private String choose(ChooseType type, String currentDir) {
		JFileChooser fileChooser = new JFileChooser(currentDir);
		int result;
		if( type == ChooseType.EBOOK){
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			result =  fileChooser.showOpenDialog(this);
		}else if(type == ChooseType.PDC_VIEWER_DIR){
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			result =  fileChooser.showOpenDialog(this);
		}else{ //IMAGE_DIR
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			result =  fileChooser.showSaveDialog(this);
		}
		if(result == JFileChooser.APPROVE_OPTION){
			String fileName = fileChooser.getSelectedFile().getAbsolutePath();
			System.out.println(fileName);
			return fileName;
		}
		return null;
	}






	private boolean check() {
		if(! new File(lblPdcViewer.getText()).exists()){
			JOptionPane.showMessageDialog(this, "Folder "+ lblPdcViewer.getText()+" does not exist.");
			return false;
		}
		if(! new File(lblEbook.getText()).exists()){
			JOptionPane.showMessageDialog(this, "File "+ lblEbook.getText()+" does not exist.");
			return false;
		}
		if(! new File(lblOutput.getText()).exists()){
			JOptionPane.showMessageDialog(this, "Folder "+ lblOutput.getText()+" does not exist.");
			return false;
		}
		return true;

	}

	private void start() {
		if(! check())return;

		btnStart.setEnabled(false);
		btnStop.setEnabled(true);
		

		scanThread=new Thread(){
			@Override
			public void run() {
				int fromPage=(Integer) spnrFromPage.getValue();
				int toPage=(Integer) spnrToPage.getValue();
				int delay=(Integer) spnrDelay.getValue()*10;

				if(fromPage > toPage){
					JOptionPane.showMessageDialog(null, "End page should be greater than start page.");
					return;
				}
				scanner= new Scanner();
				boolean b=scanner.doWork(lblEbook.getText(),
						lblOutput.getText(), fromPage, toPage, delay);
				if(b)btnStart.setEnabled(true);
			}
		};
		scanThread.start();

	}
	private void stop() {
		// TODO Auto-generated method stub
		try {
			if(scanner !=null){
				scanner.stop();
			};
			while(scanThread.isAlive()){
				btnStart.setEnabled(false);
				Thread.sleep(1000);
			}
			btnStart.setEnabled(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private void btnStopActionPerformed(ActionEvent evt) {
		stop();
	}
}
