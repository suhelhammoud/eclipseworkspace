import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

import javax.swing.JTextArea;

public class ChatClient extends Thread{
	final String seperator="-:-";
	int index_last_message=0;
	String clientName;
	ChatInterface clientInt;
	JTextArea txt;

	public ChatClient(String clientName, JTextArea txt){
		try {
			this.clientName=clientName;
			this.txt=txt;
			clientInt = (ChatInterface)Naming.lookup("//localhost/Chat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int sendMsg(String msg){
		try {
			index_last_message=clientInt.sendMsg(clientName+seperator+ msg);
			return index_last_message;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getClientName(){
		return clientName;
	}
	//Get new messages in the server every 0.3 second.
	@Override
	public void run() {

		while(true){
			try {
				Thread.sleep(300);
				String[] message_part=clientInt.getNewMessages(index_last_message).split(seperator);

				if (message_part[0].equals("no_new_messages") || message_part[0].equals(clientName))
					continue;
				index_last_message++;
				if (txt==null ){
					System.out.println(message_part[0]+ ": "+ message_part[1]);
				}else
					txt.append("\n"+message_part[0]+ ": "+ message_part[1]);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * To Run the Application type
	 * java ChatClient clientName
	 */
	public static void main (String[] argv) {
		ChatClient hc=new ChatClient(argv[0],null);
		hc.start();
		//Keep reading the user input and send it to the server.
		while(true){
			try {
				System.out.println(hc.clientName+ ": ");
				String msg=new BufferedReader(new InputStreamReader(System.in)).readLine();
				hc.sendMsg(msg);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

