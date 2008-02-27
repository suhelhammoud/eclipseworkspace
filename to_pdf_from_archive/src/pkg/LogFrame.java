package pkg;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

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
public class LogFrame extends javax.swing.JFrame {
	private JScrollPane scrl;
	private JTextArea txt;
	private JButton btnClear;
	private JToolBar bar;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		LogFrame inst = new LogFrame();
		inst.setVisible(true);
	}

	public LogFrame() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				bar = new JToolBar();
				getContentPane().add(bar, BorderLayout.NORTH);
				{
					btnClear = new JButton();
					bar.add(btnClear);
					BorderLayout btnClearLayout = new BorderLayout();
					btnClear.setText("Clear Log");
					btnClear.setLayout(null);
					btnClear.setToolTipText("Clear the log tip");
					btnClear.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnClearActionPerformed(evt);
						}
					});
				}
			}
			{
				scrl = new JScrollPane();
				getContentPane().add(scrl, BorderLayout.CENTER);
				{
					txt = new JTextArea();
					scrl.setViewportView(txt);
					txt.setText("log");
				}
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void btnClearActionPerformed(ActionEvent evt) {
		txt.setText("");
	}
	public void log(String msg){
		txt.append("\n"+msg);
	}

}
