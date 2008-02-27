package pkg;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;

import javax.swing.WindowConstants;


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
public class FileChooser extends javax.swing.JFrame {
	public JFileChooser filechoose;
	private App app=null;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
	}
	
	public FileChooser() {
		super();
		initGUI();
		
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				filechoose = new JFileChooser();
				getContentPane().add(filechoose, BorderLayout.CENTER);
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
