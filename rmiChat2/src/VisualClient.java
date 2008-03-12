import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
public class VisualClient extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JButton btnSend = null;

	private JPanel jpnUser = null;

	private JButton btnUser = null;

	private JLabel lbluser = null;

	private JPanel jpnSend = null;

	private JLabel SendMessage = null;

	private JScrollPane scrol = null;

	ChatClient client;

	private JTextArea txtMessage = null;

	private JTextArea txtUser = null;

	private JTextArea txtChat = null;








	/**
	 * This is the default constructor
	 */
	public VisualClient() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(532, 386);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJpnUser(), null);
			jContentPane.add(getJpnSend(), null);
			jContentPane.add(getScrol(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes btnSend	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton();
			btnSend.setText("Send");
			btnSend.setBounds(new Rectangle(59, 110, 99, 25));
			btnSend.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						client.sendMsg(txtMessage.getText());
						txtChat.append("\n"+client.getClientName()+": "+ txtMessage.getText());
					} catch (Exception ioe) {
						ioe.printStackTrace();
					}
				}
			});
		}
		return btnSend;
	}




	/**
	 * This method initializes jpnUser	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpnUser() {
		if (jpnUser == null) {
			lbluser = new JLabel();
			lbluser.setBounds(new Rectangle(16, 5, 138, 52));
			lbluser.setText("Enter Your User Name");
			jpnUser = new JPanel();
			jpnUser.setLayout(null);
			jpnUser.setBounds(new Rectangle(17, 8, 209, 175));
			jpnUser.add(getBtnUser(), null);
			jpnUser.add(lbluser, null);
			jpnUser.add(getTxtUser(), null);
		}
		return jpnUser;
	}




	/**
	 * This method initializes btnUser	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnUser() {
		if (btnUser == null) {
			btnUser = new JButton();
			btnUser.setText("Enter");
			btnUser.setBounds(new Rectangle(43, 92, 89, 38));
			btnUser.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					client =new ChatClient(txtUser.getText(),txtChat);
					client.start();
				}
			});
		}
		return btnUser;
	}




	/**
	 * This method initializes jpnSend	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpnSend() {
		if (jpnSend == null) {
			SendMessage = new JLabel();
			SendMessage.setBounds(new Rectangle(38, 6, 132, 30));
			SendMessage.setText("Send a Message");
			jpnSend = new JPanel();
			jpnSend.setLayout(null);
			jpnSend.setBounds(new Rectangle(15, 196, 212, 145));
			jpnSend.setName("");
			jpnSend.add(getBtnSend(), null);
			jpnSend.add(SendMessage, null);
			jpnSend.add(getTxtMessage(), null);
		}
		return jpnSend;
	}




	/**
	 * This method initializes txtMessage	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtMessage() {
		if (txtMessage == null) {
			txtMessage = new JTextArea();
			txtMessage.setBounds(new Rectangle(15, 39, 184, 66));
		}
		return txtMessage;
	}




	/**
	 * This method initializes txtUser	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtUser() {
		if (txtUser == null) {
			txtUser = new JTextArea();
			txtUser.setBounds(new Rectangle(20, 60, 131, 27));
			txtUser.setText("Ammar");
		}
		return txtUser;
	}




	/**
	 * This method initializes txtChat	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtChat() {
		if (txtChat == null) {
			txtChat = new JTextArea();
		}
		return txtChat;




	}




	/**
	 * This method initializes scrol	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrol() {
		if (scrol == null) {
			scrol = new JScrollPane();
			scrol.setBounds(new Rectangle(237, 10, 273, 332));
			scrol.setViewportView(getTxtChat());
		}
		return scrol;
	}


	public static void main(String[] args){
		VisualClient vc=new VisualClient();
		vc.setVisible(true);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
